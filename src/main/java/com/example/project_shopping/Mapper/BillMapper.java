package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Bill.BillDTO;
import com.example.project_shopping.Entity.Bill;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BillMapper {
    private ModelMapper modelMapper;
    private OrderMapper orderMapper;

    public BillDTO toBillDTO(Bill bill) {
        BillDTO dto = modelMapper.map(bill, BillDTO.class);
        dto.setOrderDTO(orderMapper.toOrderDTO(bill.getOrder()));
        return dto;
    }

    public List<BillDTO> toBillDTOList(List<Bill> bills) {
        return bills.stream().map(this::toBillDTO).collect(Collectors.toList());
    }
}

