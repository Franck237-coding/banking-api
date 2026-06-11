package com.banking.service;

import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Account createAccount(Account account) {
        if (accountRepository.existsByNumeroCompte(account.getNumeroCompte())) {
            throw new RuntimeException("Un compte avec ce numéro existe déjà");
        }
        return accountRepository.save(account);
    }
    
    public Account createAccountForUser(Long userId, Account.TypeCompte typeCompte) {
        String numeroCompte = generateAccountNumber();
        Account account = new Account();
        account.setNumeroCompte(numeroCompte);
        account.setTypeCompte(typeCompte);
        account.setSolde(BigDecimal.ZERO);
        return accountRepository.save(account);
    }
    
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }
    
    public Optional<Account> getAccountByNumeroCompte(String numeroCompte) {
        return accountRepository.findByNumeroCompte(numeroCompte);
    }
    
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }
    
    public Account updateAccount(Long id, Account accountDetails) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'ID: " + id));
        
        account.setTypeCompte(accountDetails.getTypeCompte());
        account.setSolde(accountDetails.getSolde());
        account.setBank(accountDetails.getBank());
        
        return accountRepository.save(account);
    }
    
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'ID: " + id));
        accountRepository.delete(account);
    }
    
    public boolean deposit(String numeroCompte, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findByNumeroCompte(numeroCompte);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setSolde(account.getSolde().add(amount));
            accountRepository.save(account);
            return true;
        }
        return false;
    }
    
    public boolean withdraw(String numeroCompte, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findByNumeroCompte(numeroCompte);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            if (account.getSolde().compareTo(amount) >= 0) {
                account.setSolde(account.getSolde().subtract(amount));
                accountRepository.save(account);
                return true;
            }
        }
        return false;
    }
    
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder numero = new StringBuilder("BANK");
        for (int i = 0; i < 12; i++) {
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }
}