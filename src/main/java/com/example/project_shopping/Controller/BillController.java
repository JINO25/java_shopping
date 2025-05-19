package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Bill.BillDTO;
import com.example.project_shopping.DTO.Bill.UpdateBillDTO;
import com.example.project_shopping.Enums.PaymentStatus;
import com.example.project_shopping.Service.BillService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
@AllArgsConstructor
public class BillController {
    private BillService billService;

    @GetMapping
    public ResponseEntity<List<BillDTO>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Integer id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<BillDTO>> getAllUserBills() {
        return ResponseEntity.ok(billService.getAllUserBills());
    }

    @GetMapping("/seller")
    public ResponseEntity<List<BillDTO>> getAllBillsForSeller() {
        return ResponseEntity.ok(billService.getAllBillsForSeller());
    }

    @PutMapping ("/update")
    public ResponseEntity<BillDTO> updateStatus(@RequestBody UpdateBillDTO updateBillDTO) {
        return ResponseEntity.ok(billService.updatePaymentStatus(updateBillDTO));
    }
}

