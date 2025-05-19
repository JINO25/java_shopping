package com.example.project_shopping.DTO.Order;

import com.example.project_shopping.Enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Integer id;
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private String userName;
    List<OrderDetailDTO> orderDetailDTOList;
}
