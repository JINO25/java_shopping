package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Order.CartItemeqDTO;
import com.example.project_shopping.DTO.Order.OrderDTO;
import com.example.project_shopping.DTO.Order.OrderDetailReqDTO;
import com.example.project_shopping.Entity.*;
import com.example.project_shopping.Enums.CartStatus;
import com.example.project_shopping.Enums.OrderStatus;
import com.example.project_shopping.Enums.PaymentStatus;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Exception.InvalidTokenException;
import com.example.project_shopping.Exception.OutOfStockException;
import com.example.project_shopping.Exception.PermissionDeniedException;
import com.example.project_shopping.Mapper.OrderMapper;
import com.example.project_shopping.Repository.*;
import com.example.project_shopping.Service.OrderService;
import com.example.project_shopping.Util.Auth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderServiceImp implements OrderService {
    private final AddressRepository addressRepository;
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;
    private UserRepository userRepository;
    private ProductVariantRepository productVariantRepository;
    private BillRepository billRepository;
    private OrderMapper orderMapper;
    private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderDTO createOrder(List<OrderDetailReqDTO> orderDetailReqDTOList) {

        Integer userID = Auth.getCurrentUserID();

        if (userID == null) {
            throw new InvalidTokenException("Please login again!");
        }

        User user = userRepository.findById(userID)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found with id: " + userID));

        if (orderDetailReqDTOList.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one product");
        }

        Integer addressId = orderDetailReqDTOList.get(0).getAddressId();

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Address not found with id: " + addressId));

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setAddress(address);

        List<OrderDetail> orderDetails = new ArrayList<>();

        double total = 0;

        for (OrderDetailReqDTO req : orderDetailReqDTOList) {

            ProductVariant variant = productVariantRepository
                    .findById(req.getProductVariantId())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Product variant not found with id: " + req.getProductVariantId()));

            if (variant.getStock() < req.getQuantity()) {
                throw new OutOfStockException(
                        "Not enough stock for variant ID: " + variant.getId());
            }

            // update stock
            variant.setStock(variant.getStock() - req.getQuantity());
            productVariantRepository.save(variant);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductVariant(variant);
            orderDetail.setQuantity(req.getQuantity());
            orderDetail.setPrice(variant.getPrice());

            total += variant.getPrice() * req.getQuantity();

            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);

        order = orderRepository.save(order);

        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setBillDate(LocalDate.now());
        bill.setMethod("COD");
        bill.setPaymentStatus(PaymentStatus.UNPAID);
        bill.setTotal(total);

        billRepository.save(bill);

        return orderMapper.toOrderDTO(order);
    }

    @Override
    public List<OrderDTO> getALlOrders() {
        List<Order> order = orderRepository.findAll();

        if(order.isEmpty()){
            return Collections.emptyList();
        }

        return orderMapper.toOrderDTOList(order);
    }

    @Override
    public List<OrderDTO> getALlOrdersForSeller() {
        Integer sellerId = Auth.getCurrentUserID();
        if (sellerId == null) {
            throw new InvalidTokenException("Please login again!");
        }

        List<Order> orders = orderRepository.findOrdersBySellerId(sellerId);
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orderMapper.toOrderDTOList(orders);
    }


    @Override
    public List<OrderDTO> getALlOrdersOfUser() {
        Integer userID = Auth.getCurrentUserID();
        User user = userRepository.findUserById(userID);
        if(user == null){
            throw new EntityNotFoundException("Please login!");
        }
        List<Order> orderList = orderRepository.findAllByUser(user);
        if(orderList.isEmpty()){
            return Collections.emptyList();
        }

        return orderMapper.toOrderDTOList(orderList);
    }

    @Override
    public OrderDTO getOrderByID(Integer orderID){
        Order order = orderRepository.findById(orderID).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+orderID));
        return  orderMapper.toOrderDTO(order);
    }

    @Override
    public OrderDTO updateStatusOrder(Integer orderID, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderID).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+orderID));
        order.setOrderStatus(orderStatus);
        order = orderRepository.save(order);
        return orderMapper.toOrderDTO(order);
    }

    @Override
    public void deleteOrder(Integer orderID) {
        Order order = orderRepository.findById(orderID).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+orderID));
        orderRepository.delete(order);
    }

    @Override
    public void cancelOrderByUser(Integer orderID) {
        Integer userID = Auth.getCurrentUserID();
        User user = userRepository.findUserById(userID);
        if(user == null){
            throw new InvalidTokenException("Please login!");
        }

        Order order = orderRepository.findById(orderID).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+orderID));
        if(!order.getUser().getId().equals(userID)){
            throw new PermissionDeniedException("You are not allowed to cancel this order!");
        }
        orderRepository.delete(order);
    }

    @Override
    public OrderDTO createOrderFromCart(CartItemeqDTO cartItemeqDTO) {
        Integer userID = Auth.getCurrentUserID();
        if(userID == null){
            throw new InvalidTokenException("Please login againt!");
        }
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userID));

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(user);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for(Integer i : cartItemeqDTO.getCartItemIds()){
        CartItem cartItem = cartItemRepository.findById(i)
                .orElseThrow(()->new EntityNotFoundException("Cart item not found!"));

        ProductVariant productVariant = productVariantRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + cartItem.getProduct().getId()));

            if (productVariant.getStock() < cartItem.getQuantity()) {
                throw new OutOfStockException("Not enough stock for variant ID: " + productVariant.getId());
            }

            productVariant.setStock(productVariant.getStock() - cartItem.getQuantity());

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setPrice(productVariant.getPrice());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setOrder(order);
            orderDetail.setProductVariant(productVariant);

            orderDetails.add(orderDetail);
            cartItem.setStatus(CartStatus.CHECKD_OUT);
            cartItemRepository.save(cartItem);
        }


        order.setOrderDetails(orderDetails);

        order = orderRepository.save(order);

        Double total = orderDetails.stream()
                .mapToDouble(odr->odr.getPrice()*odr.getQuantity()).sum();

        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setBillDate(LocalDate.now());
        bill.setMethod("COD");
        bill.setPaymentStatus(PaymentStatus.UNPAID);
        bill.setTotal(total);

        billRepository.save(bill);

        return orderMapper.toOrderDTO(order);
    }

    @Override
    public String createCheckoutSession(List<OrderDetailReqDTO> orderList)
            throws StripeException, JsonProcessingException {

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (OrderDetailReqDTO req : orderList) {

            ProductVariant variant = productVariantRepository
                    .findById(req.getProductVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (variant.getStock() < req.getQuantity()) {
                throw new OutOfStockException("Not enough stock");
            }

            long amount = (long) (variant.getPrice() * 100);

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(req.getQuantity().longValue())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("usd")
                                            .setUnitAmount(amount)
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData
                                                            .builder()
                                                            .setName(variant.getProduct().getName())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();

            lineItems.add(lineItem);
        }

        String metadataJson = new ObjectMapper().writeValueAsString(orderList);

        Map<String,String> metadata = new HashMap<>();
        metadata.put("items", metadataJson);

        SessionCreateParams.Builder params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:5173/payment-success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl("http://localhost:5173/payment-cancelled")
                        .putAllMetadata(metadata);

        lineItems.forEach(params::addLineItem);

        Session session = Session.create(params.build());

        return session.getUrl();
    }

    @Override
    @Transactional
    public OrderDTO createOrderWithStripe(List<OrderDetailReqDTO> orderList, String sessionId) {

        Integer userID = Auth.getCurrentUserID();

        if (userID == null) {
            throw new InvalidTokenException("Please login again!");
        }

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (orderList.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one product");
        }

        // lấy address giống COD
        Integer addressId = orderList.get(0).getAddressId();

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        Order order = new Order();
        order.setStripeSessionId(sessionId);
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setAddress(address);

        List<OrderDetail> orderDetails = new ArrayList<>();
        double total = 0;

        for (OrderDetailReqDTO req : orderList) {

            ProductVariant variant = productVariantRepository
                    .findById(req.getProductVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (variant.getStock() < req.getQuantity()) {
                throw new OutOfStockException("Not enough stock");
            }

            variant.setStock(variant.getStock() - req.getQuantity());
            productVariantRepository.save(variant);

            OrderDetail od = new OrderDetail();
            od.setOrder(order);
            od.setProductVariant(variant);
            od.setQuantity(req.getQuantity());
            od.setPrice(variant.getPrice());

            total += variant.getPrice() * req.getQuantity();

            orderDetails.add(od);
        }

        order.setOrderDetails(orderDetails);
        order = orderRepository.save(order);

        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setBillDate(LocalDate.now());
        bill.setMethod("STRIPE");
        bill.setPaymentStatus(PaymentStatus.PAID);
        bill.setPaymentTime(LocalDate.now());
        bill.setTotal(total);

        billRepository.save(bill);

        return orderMapper.toOrderDTO(order);
    }

}
