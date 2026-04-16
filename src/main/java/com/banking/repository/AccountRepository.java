package com.banking.repository;

import com.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByNumeroCompte(String numeroCompte);
    
    boolean existsByNumeroCompte(String numeroCompte);
    
    List<Account> findByUserId(Long userId);
    
    @Query("SELECT a FROM Account a WHERE a.user.id = ?1")
    List<Account> findAccountsByUserId(Long userId);
    
    @Query("SELECT a FROM Account a WHERE a.solde > 0")
    List<Account> findAccountsWithPositiveBalance();
}
