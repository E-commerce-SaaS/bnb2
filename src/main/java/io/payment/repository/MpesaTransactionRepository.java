package io.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.payment.entity.MpesaTransaction;

import java.util.Optional;

public interface MpesaTransactionRepository extends JpaRepository<MpesaTransaction, Long> {
    
    Optional<MpesaTransaction> findByTransId(String transId);

    // Optional: also useful
    Optional<MpesaTransaction> findByBillRefNumber(String billRefNumber);
}
