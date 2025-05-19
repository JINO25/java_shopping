package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Bill.BillDTO;
import com.example.project_shopping.DTO.Bill.UpdateBillDTO;
import com.example.project_shopping.Enums.PaymentStatus;

import java.util.List;

public interface BillService {
    List<BillDTO> getAllBills();
    BillDTO getBillById(Integer id);

    List<BillDTO> getAllUserBills();
    List<BillDTO> getAllBillsForSeller();
    BillDTO updatePaymentStatus(UpdateBillDTO updateBillDTO);
}
