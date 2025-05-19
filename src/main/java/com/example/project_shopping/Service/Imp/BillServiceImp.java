package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Bill.BillDTO;
import com.example.project_shopping.DTO.Bill.UpdateBillDTO;
import com.example.project_shopping.Entity.Bill;
import com.example.project_shopping.Enums.PaymentStatus;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Exception.InvalidTokenException;
import com.example.project_shopping.Mapper.BillMapper;
import com.example.project_shopping.Repository.BillRepository;
import com.example.project_shopping.Service.BillService;
import com.example.project_shopping.Util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class BillServiceImp implements BillService {
    private BillRepository billRepository;
    private BillMapper billMapper;

    @Override
    public List<BillDTO> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        if(bills.isEmpty()){
            return Collections.emptyList();
        }
        return billMapper.toBillDTOList(bills);
    }

    @Override
    public BillDTO getBillById(Integer id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with id: " + id));
        return billMapper.toBillDTO(bill);
    }

    @Override
    public List<BillDTO> getAllUserBills() {
        Integer userID = Auth.getCurrentUserID();
        if (userID == null){
            throw new InvalidTokenException("Please login again!");
        }

        List<Bill> billList = billRepository.findBillsByUserID(userID);
        if(billList.isEmpty()){
            return Collections.emptyList();
        }

        return billMapper.toBillDTOList(billList);
    }

    @Override
    public List<BillDTO> getAllBillsForSeller() {
        Integer sellerID = Auth.getCurrentUserID();
        if (sellerID == null){
            throw new InvalidTokenException("Please login again!");
        }
        List<Bill> billList = billRepository.findBillsBySellerId(sellerID);
        if(billList.isEmpty()){
            return Collections.emptyList();
        }

        return billMapper.toBillDTOList(billList);
    }

    @Override
    public BillDTO updatePaymentStatus(UpdateBillDTO updateBillDTO) {
        Integer sellerID = Auth.getCurrentUserID();
        if (sellerID == null){
            throw new InvalidTokenException("Please login again!");
        }

        Bill bill = billRepository.findById(updateBillDTO.getBillID()).orElseThrow(()->new EntityNotFoundException("No bill with id: "+updateBillDTO.getBillID()));
        bill.setPaymentStatus(updateBillDTO.getPaymentStatus());
        bill.setPaymentTime(LocalDate.now());
        bill = billRepository.save(bill);

        return billMapper.toBillDTO(bill);
    }
}

