package com.banking.repository;

import com.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByCompteSourceId(Long compteSourceId);
    
    List<Transaction> findByCompteDestinationId(Long compteDestinationId);
    
    List<Transaction> findByStatut(Transaction.StatutTransaction statut);
    
    List<Transaction> findByTypeTransaction(Transaction.TypeTransaction typeTransaction);
    
    @Query("SELECT t FROM Transaction t WHERE t.dateTransaction BETWEEN ?1 AND ?2")
    List<Transaction> findByDateBetween(LocalDateTime debut, LocalDateTime fin);
    
    @Query("SELECT t FROM Transaction t WHERE t.compteSource.id = ?1 OR t.compteDestination.id = ?1")
    List<Transaction> findByAccountId(Long accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.reference = ?1")
    Transaction findByReference(String reference);
}
