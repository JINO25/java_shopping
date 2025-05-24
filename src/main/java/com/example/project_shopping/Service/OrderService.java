package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Order.CartItemeqDTO;
import com.example.project_shopping.DTO.Order.OrderDTO;
import com.example.project_shopping.DTO.Order.OrderDetailReqDTO;
import com.example.project_shopping.Enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDetailReqDTO orderDetailReqDTO);
    OrderDTO createOrderList(List<OrderDetailReqDTO> orderDetailReqDTOList);
    List<OrderDTO> getALlOrders();
    List<OrderDTO> getALlOrdersForSeller();
    List<OrderDTO> getALlOrdersOfUser();
    OrderDTO getOrderByID(Integer orderID);
    OrderDTO updateStatusOrder(Integer orderID, OrderStatus orderStatus);
    void deleteOrder(Integer orderID);
    void cancelOrderByUser(Integer orderID);

    OrderDTO createOrderFromCart(CartItemeqDTO cartItemeqDTO);
}
