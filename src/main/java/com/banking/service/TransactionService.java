package com.banking.service;

import com.banking.model.Transaction;
import com.banking.model.Account;
import com.banking.repository.TransactionRepository;
import com.banking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    public Transaction effectuerDepot(String numeroCompte, BigDecimal montant, String description) {
        Optional<Account> accountOpt = accountRepository.findByNumeroCompte(numeroCompte);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Compte non trouvé: " + numeroCompte);
        }
        
        Account account = accountOpt.get();
        account.setSolde(account.getSolde().add(montant));
        accountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setReference(generateReference());
        transaction.setMontant(montant);
        transaction.setTypeTransaction(Transaction.TypeTransaction.DEPOT);
        transaction.setDescription(description);
        transaction.setCompteSource(account);
        transaction.setStatut(Transaction.StatutTransaction.EFFECTUEE);
        
        return transactionRepository.save(transaction);
    }
    
    public Transaction effectuerRetrait(String numeroCompte, BigDecimal montant, String description) {
        Optional<Account> accountOpt = accountRepository.findByNumeroCompte(numeroCompte);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Compte non trouvé: " + numeroCompte);
        }
        
        Account account = accountOpt.get();
        if (account.getSolde().compareTo(montant) < 0) {
            throw new RuntimeException("Solde insuffisant pour le retrait");
        }
        
        account.setSolde(account.getSolde().subtract(montant));
        accountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setReference(generateReference());
        transaction.setMontant(montant);
        transaction.setTypeTransaction(Transaction.TypeTransaction.RETRAIT);
        transaction.setDescription(description);
        transaction.setCompteSource(account);
        transaction.setStatut(Transaction.StatutTransaction.EFFECTUEE);
        
        return transactionRepository.save(transaction);
    }
    
    public Transaction effectuerVirement(String compteSourceNum, String compteDestNum, BigDecimal montant, String description) {
        Optional<Account> sourceOpt = accountRepository.findByNumeroCompte(compteSourceNum);
        Optional<Account> destOpt = accountRepository.findByNumeroCompte(compteDestNum);
        
        if (sourceOpt.isEmpty()) {
            throw new RuntimeException("Compte source non trouvé: " + compteSourceNum);
        }
        if (destOpt.isEmpty()) {
            throw new RuntimeException("Compte destination non trouvé: " + compteDestNum);
        }
        
        Account source = sourceOpt.get();
        Account destination = destOpt.get();
        
        if (source.getSolde().compareTo(montant) < 0) {
            throw new RuntimeException("Solde insuffisant pour le virement");
        }
        
        source.setSolde(source.getSolde().subtract(montant));
        destination.setSolde(destination.getSolde().add(montant));
        
        accountRepository.save(source);
        accountRepository.save(destination);
        
        Transaction transaction = new Transaction();
        transaction.setReference(generateReference());
        transaction.setMontant(montant);
        transaction.setTypeTransaction(Transaction.TypeTransaction.VIREMENT);
        transaction.setDescription(description);
        transaction.setCompteSource(source);
        transaction.setCompteDestination(destination);
        transaction.setStatut(Transaction.StatutTransaction.EFFECTUEE);
        
        return transactionRepository.save(transaction);
    }
    
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
    public List<Transaction> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
    
    public List<Transaction> getTransactionsByDateRange(LocalDateTime debut, LocalDateTime fin) {
        return transactionRepository.findByDateBetween(debut, fin);
    }
    
    public List<Transaction> getTransactionsByStatus(Transaction.StatutTransaction statut) {
        return transactionRepository.findByStatut(statut);
    }
    
    public List<Transaction> getTransactionsByType(Transaction.TypeTransaction type) {
        return transactionRepository.findByTypeTransaction(type);
    }
    
    private String generateReference() {
        Random random = new Random();
        StringBuilder ref = new StringBuilder("TXN");
        for (int i = 0; i < 10; i++) {
            ref.append(random.nextInt(10));
        }
        return ref.toString();
    }
}
