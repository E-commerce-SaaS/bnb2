package io.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class MpesaTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String transId; 
    private String transactionType; 
    private String transTime;      
    private Double transAmount;    
    private String businessShortCode;
    private String billRefNumber;  
    private Double orgAccountBalance; 
    private String msisdn;          
    private String firstName;       
    private String lastName;       
    
    @Enumerated(EnumType.STRING)
    private MpesaTransactionStatus status = MpesaTransactionStatus.PENDING; 
    private LocalDateTime createdAt = LocalDateTime.now(); 

}