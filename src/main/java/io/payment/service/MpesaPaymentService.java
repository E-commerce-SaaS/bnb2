package io.payment.service;

import org.springframework.beans.factory.annotation.Autowired;

import io.payment.entity.MpesaTransaction;
import io.payment.entity.MpesaTransactionStatus;
import io.payment.repository.MpesaTransactionRepository;
import io.payment.veiw.C2BpaymentResponceVeiw;
import io.payment.form.MpesaValidationRequestForm;
import org.springframework.stereotype.Service;

@Service
public class MpesaPaymentService {
    protected MpesaTransactionRepository repository;

    public C2BpaymentResponceVeiw confirmation (MpesaValidationRequestForm request){
        if (request.getBillRefNumber() == null || request.getBillRefNumber().isBlank()) {
            return new C2BpaymentResponceVeiw("1", "Rejected due to empty reference account.");
        }

        MpesaTransaction transaction = new MpesaTransaction();
        transaction.setTransId(request.getTransId());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setTransTime(request.getTransTime());
        transaction.setTransAmount(request.getTransAmount());
        transaction.setBusinessShortCode(request.getBusinessShortCode());
        transaction.setBillRefNumber(request.getBillRefNumber());
        transaction.setOrgAccountBalance(request.getOrgAccountBalance());
        transaction.setMsisdn(request.getPhoneNumber());
        transaction.setFirstName(request.getFirstName());
        transaction.setLastName(request.getLastName());
        transaction.setStatus(MpesaTransactionStatus.CONFIRMED); 

        repository.save(transaction);

        return new C2BpaymentResponceVeiw("0", "Acknowledgement Received");


    }



    @Autowired 
    public void setRepository(MpesaTransactionRepository repository){
        this.repository = repository;
    }

}
