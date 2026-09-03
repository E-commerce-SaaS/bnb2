package io.payment.service;


import io.payment.entity.MpesaTransaction;
import io.payment.form.SafaricomStatusQueryRequest;
import io.payment.repository.MpesaTransactionRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class MpesaTransactionReadService {
    protected MpesaTransactionRepository mpesaTransactionRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional(readOnly = true) 
    public List<MpesaTransaction> fetchAllTransactions() {
        return mpesaTransactionRepository.findAll();
    }

    public String pullLiveLedgerFromSafaricom(SafaricomStatusQueryRequest queryPayload, String bearerToken) {
        String safaricomUrl = "https://safaricom.co.ke";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken); 

        HttpEntity<SafaricomStatusQueryRequest> httpRequest = new HttpEntity<>(queryPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(safaricomUrl, httpRequest, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Failed to communicate with Safaricom gateway: " + e.getMessage();
        }
    }

    @Transactional(readOnly = true)
    public MpesaTransaction getTransactionStatus(String transId) {
        return mpesaTransactionRepository.findByTransId(transId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with TransID: " + transId));
    }

    @Autowired
    public void setMpesaTransactionRepository(MpesaTransactionRepository mpesaTransactionRepository){
        this.mpesaTransactionRepository = mpesaTransactionRepository;
    }

}