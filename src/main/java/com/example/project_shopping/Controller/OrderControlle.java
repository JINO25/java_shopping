package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Order.OrderDTO;
import com.example.project_shopping.DTO.Order.OrderDetailReqDTO;
import com.example.project_shopping.Enums.OrderStatus;
import com.example.project_shopping.Service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderControlle {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDetailReqDTO orderDetailReqDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e->{
                errorMsg.append(e.getDefaultMessage()).append("\n");
            });
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        OrderDTO createdOrder = orderService.createOrder(orderDetailReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PostMapping("/batch")
    public ResponseEntity<?> createOrderList(@RequestBody @Valid List<OrderDetailReqDTO> orderDetailReqDTOList, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e->{
                errorMsg.append(e.getDefaultMessage()).append("\n");
            });
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
            OrderDTO createdOrder = orderService.createOrderList(orderDetailReqDTOList);
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


}
