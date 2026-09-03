package io.payment.controller;

import io.payment.form.MpesaValidationRequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.payment.entity.MpesaTransaction;
import io.payment.form.SafaricomStatusQueryRequest;
import io.payment.service.MpesaPaymentService;
import io.payment.service.MpesaTransactionReadService;
import io.payment.view.C2BpaymentResponceVeiw;

import java.util.List;
import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/payments")
public class MpesaPaymentController {

    private MpesaPaymentService paymentService;
    private MpesaTransactionReadService transactionReadService;

    @PostMapping("/confirm")
    public ResponseEntity<C2BpaymentResponceVeiw> confirmTransaction(
            @RequestBody MpesaValidationRequestForm request) {          // ← changed type

        C2BpaymentResponceVeiw response = paymentService.confirmation(request);
        return ResponseEntity.ok(response);
    }

   
    @GetMapping("/pull")
    public ResponseEntity<List<MpesaTransaction>> getAllTransactions() {
        List<MpesaTransaction> transactions = transactionReadService.fetchAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/pull-from-safaricom")
    public ResponseEntity<?> pullFromSafaricom(
            @RequestBody SafaricomStatusQueryRequest queryRequest,
            @RequestHeader("Authorization") String bearerToken) {
        
        String safaricomResponse = transactionReadService.pullLiveLedgerFromSafaricom(queryRequest, bearerToken);
        return ResponseEntity.ok(safaricomResponse);
    }

    @Autowired
    public void setMpesaPaymentService(MpesaPaymentService paymentService){
        this.paymentService = paymentService;
    }

    @Autowired
    public void setMpesaTransactionReadService (MpesaTransactionReadService transactionReadService){
        this.transactionReadService = transactionReadService ;
    }


}
