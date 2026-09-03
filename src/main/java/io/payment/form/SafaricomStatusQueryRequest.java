package io.payment.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public
class MpesaValidationRequestForm {
    @JsonProperty("TransactionType") 
    private String transactionType;

    @JsonProperty("TransID") 
    private String transId;

    @JsonProperty("TransTime") 
    private String transTime;

    @JsonProperty("TransAmount") 
    private Double transAmount;

    @JsonProperty("BusinessShortCode")
     private String businessShortCode;

    @JsonProperty("BillRefNumber") 
    private String billRefNumber; 

    @JsonProperty("OrgAccountBalance") 
    private Double orgAccountBalance;

    @JsonProperty("MSISDN")
     private String phoneNumber;

    @JsonProperty("FirstName") 
    private String firstName;

    @JsonProperty("LastName") 
    private String lastName;
}