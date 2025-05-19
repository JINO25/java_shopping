package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Order.OrderDTO;
import com.example.project_shopping.DTO.Order.OrderDetailDTO;
import com.example.project_shopping.DTO.Order.ProductVariantOrderDTO;
import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.DTO.Product.ProductVariantDTO;
import com.example.project_shopping.Entity.Order;
import com.example.project_shopping.Entity.OrderDetail;
import com.example.project_shopping.Entity.Product;
import com.example.project_shopping.Entity.ProductVariant;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderDTO toOrderDTO(Order order){
        modelMapper.typeMap(Order.class, OrderDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getName(), OrderDTO::setUserName);
            mapper.map(Order::getOrderDate, OrderDTO::setOrderDate);
        });

        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        dto.setOrderDetailDTOList(toOrderDetailDTOList(order.getOrderDetails()));
        return dto;
    }

    public OrderDetailDTO toOrderDetailDTO(OrderDetail orderDetail) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(orderDetail.getId());
        dto.setQuantity(orderDetail.getQuantity());
        dto.setPrice(orderDetail.getPrice());

        ProductVariant variant = orderDetail.getProductVariant();
        ProductVariantOrderDTO variantDTO = new ProductVariantOrderDTO();
        variantDTO.setId(variant.getId());
        variantDTO.setOption(variant.getOption());
        variantDTO.setColor(variant.getColor());
        variantDTO.setPrice(variant.getPrice());
        variantDTO.setQuantity(orderDetail.getQuantity()); // Gán số lượng user đặt
        variantDTO.setProductName(variant.getProduct().getName());

        dto.setProductVariantOrderDTO(variantDTO);

        return dto;
    }

    public List<OrderDTO> toOrderDTOList(List<Order> orderList){
        return orderList.stream()
                .map(this::toOrderDTO)
                .collect(Collectors.toList());
    }


    public List<OrderDetailDTO> toOrderDetailDTOList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(this::toOrderDetailDTO)
                .collect(Collectors.toList());
    }
}

