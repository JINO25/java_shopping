package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Order.CartItemeqDTO;
import com.example.project_shopping.DTO.Order.OrderDTO;
import com.example.project_shopping.DTO.Order.OrderDetailReqDTO;
import com.example.project_shopping.Enums.OrderStatus;
import com.example.project_shopping.Repository.OrderRepository;
import com.example.project_shopping.Service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderControlle {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid List<OrderDetailReqDTO> orderDetailReqDTOList,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e -> {
                errorMsg.append(e.getDefaultMessage()).append("\n");
            });

            return ResponseEntity.badRequest().body(errorMsg.toString());
        }

        OrderDTO createdOrder = orderService.createOrder(orderDetailReqDTOList);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getALlOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/seller")
    public ResponseEntity<List<OrderDTO>> getAllOrdersForSeller() {
        List<OrderDTO> orders = orderService.getALlOrdersForSeller();
        return ResponseEntity.ok(orders);
    }

    // Lấy tất cả đơn hàng của người dùng hiện tại
    @GetMapping("/my")
    public ResponseEntity<List<OrderDTO>> getMyOrders() {
        List<OrderDTO> orders = orderService.getALlOrdersOfUser();
        return ResponseEntity.ok(orders);
    }

    // Lấy đơn hàng theo ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer orderId) {
        OrderDTO order = orderService.getOrderByID(orderId);
        return ResponseEntity.ok(order);
    }

    // Cập nhật trạng thái đơn hàng
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Integer orderId,
                                                      @RequestParam OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateStatusOrder(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // Xoá đơn hàng (admin)
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    // Huỷ đơn hàng bởi user
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId) {
        orderService.cancelOrderByUser(orderId);
        return ResponseEntity.ok(Map.of("message", "Order cancelled successfully"));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> createOrderFromCart(@Valid @RequestBody CartItemeqDTO cartItemeqDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder msg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e->msg.append(e.getDefaultMessage()).append("\n"));
            return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST);
        }
        OrderDTO orderDTO = orderService.createOrderFromCart(cartItemeqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestBody @Valid List<OrderDetailReqDTO> orderList,
            BindingResult bindingResult
    ) throws Exception {

        if (bindingResult.hasErrors()){
            StringBuilder msg = new StringBuilder();
            bindingResult.getAllErrors()
                    .forEach(e -> msg.append(e.getDefaultMessage()).append("\n"));
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        String url = orderService.createCheckoutSession(orderList);

        Map<String, String> response = new HashMap<>();
        response.put("checkoutUrl", url);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payment-success")
    public ResponseEntity<?> paymentSuccess(
            @RequestParam("session_id") String sessionId
    ) throws Exception {

        Session session = Session.retrieve(sessionId);

        if (!"paid".equalsIgnoreCase(session.getPaymentStatus())) {
            return ResponseEntity.badRequest().build();
        }

        if (orderRepository.existsByStripeSessionId(sessionId)) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:5173/"))
                    .build();
        }

        String metadataJson = session.getMetadata().get("items");

        ObjectMapper mapper = new ObjectMapper();
        List<OrderDetailReqDTO> orderList =
                mapper.readValue(metadataJson, new TypeReference<>() {});

        OrderDTO order = orderService.createOrderWithStripe(orderList, sessionId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

}
