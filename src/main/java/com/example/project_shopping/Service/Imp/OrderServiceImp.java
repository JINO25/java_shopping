package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Order.OrderDTO;
import com.example.project_shopping.DTO.Order.OrderDetailDTO;
import com.example.project_shopping.DTO.Order.OrderDetailReqDTO;
import com.example.project_shopping.Entity.*;
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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImp implements OrderService {
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;
    private UserRepository userRepository;
    private ProductVariantRepository productVariantRepository;
    private BillRepository billRepository;
    private OrderMapper orderMapper;

    @Override
    public OrderDTO createOrder(OrderDetailReqDTO orderDetailReqDTO) {
        System.out.println(orderDetailReqDTO.getQuantity()+" "+orderDetailReqDTO.getProductVariantId());
        Integer userID = Auth.getCurrentUserID();
        if(userID == null){
            throw new InvalidTokenException("Please login againt!");
        }

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userID));

        ProductVariant productVariant = productVariantRepository.findById(orderDetailReqDTO.getProductVariantId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + orderDetailReqDTO.getProductVariantId()));

        if (productVariant.getStock() < orderDetailReqDTO.getQuantity()) {
            throw new OutOfStockException("Not enough stock for variant ID: " + productVariant.getId());
        }

        productVariant.setStock(productVariant.getStock() - orderDetailReqDTO.getQuantity());

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(user);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPrice(productVariant.getPrice());
        orderDetail.setQuantity(orderDetailReqDTO.getQuantity());
        orderDetail.setOrder(order);
        orderDetail.setProductVariant(productVariant);

        order.setOrderDetails(Collections.singletonList(orderDetail));

        order = orderRepository.save(order);

        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setBillDate(LocalDate.now());
        bill.setMethod("COD");
        bill.setPaymentStatus(PaymentStatus.UNPAID);
        bill.setTotal(orderDetail.getPrice() * orderDetail.getQuantity());

        billRepository.save(bill);

        return orderMapper.toOrderDTO(order);
    }


    @Override
    public OrderDTO createOrderList(List<OrderDetailReqDTO> orderDetailReqDTOList) {
        Integer userID = Auth.getCurrentUserID();
        if (userID == null) {
            throw new InvalidTokenException("Please login again!");
        }

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userID));

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(user);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderDetailReqDTO reqDTO : orderDetailReqDTOList) {
            ProductVariant variant = productVariantRepository.findById(reqDTO.getProductVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + reqDTO.getProductVariantId()));

            if (variant.getStock() < reqDTO.getQuantity()) {
                throw new OutOfStockException("Not enough stock for variant ID: " + variant.getId());
            }

            variant.setStock(variant.getStock() - reqDTO.getQuantity());

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(reqDTO.getQuantity());
            orderDetail.setPrice(variant.getPrice());
            orderDetail.setProductVariant(variant);
            orderDetail.setOrder(order);

            orderDetails.add(orderDetail);
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
}
