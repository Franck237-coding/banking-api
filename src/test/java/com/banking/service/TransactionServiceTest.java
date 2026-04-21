package com.banking.service;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private TransactionService transactionService;
    
    private Account testAccount;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setNom("Dupont");
        testUser.setPrenom("Jean");
        
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setNumeroCompte("BANK1234567890");
        testAccount.setSolde(new BigDecimal("1000.00"));
        testAccount.setTypeCompte(Account.TypeCompte.COURANT);
        testAccount.setUser(testUser);
    }
    
    @Test
    void testEffectuerDepot_Success() {
        when(accountRepository.findByNumeroCompte("BANK1234567890")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Transaction result = transactionService.effectuerDepot("BANK1234567890", new BigDecimal("500.00"), "Dépôt salaire");
        
        assertNotNull(result);
        assertEquals(Transaction.TypeTransaction.DEPOT, result.getTypeTransaction());
        assertEquals(Transaction.StatutTransaction.EFFECTUEE, result.getStatut());
        assertEquals(new BigDecimal("1500.00"), testAccount.getSolde());
    }
    
    @Test
    void testEffectuerDepot_AccountNotFound() {
        when(accountRepository.findByNumeroCompte("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> 
            transactionService.effectuerDepot("INVALID", new BigDecimal("500.00"), "Test"));
    }
    
    @Test
    void testEffectuerRetrait_Success() {
        when(accountRepository.findByNumeroCompte("BANK1234567890")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Transaction result = transactionService.effectuerRetrait("BANK1234567890", new BigDecimal("300.00"), "Retrait espèces");
        
        assertNotNull(result);
        assertEquals(Transaction.TypeTransaction.RETRAIT, result.getTypeTransaction());
        assertEquals(Transaction.StatutTransaction.EFFECTUEE, result.getStatut());
        assertEquals(new BigDecimal("700.00"), testAccount.getSolde());
    }
    
    @Test
    void testEffectuerRetrait_InsufficientFunds() {
        when(accountRepository.findByNumeroCompte("BANK1234567890")).thenReturn(Optional.of(testAccount));
        
        assertThrows(RuntimeException.class, () -> 
            transactionService.effectuerRetrait("BANK1234567890", new BigDecimal("5000.00"), "Test"));
    }
    
    @Test
    void testEffectuerRetrait_AccountNotFound() {
        when(accountRepository.findByNumeroCompte("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> 
            transactionService.effectuerRetrait("INVALID", new BigDecimal("500.00"), "Test"));
    }
    
    @Test
    void testGetAllTransactions() {
        Transaction txn1 = new Transaction();
        txn1.setReference("TXN1234567890");
        Transaction txn2 = new Transaction();
        txn2.setReference("TXN0987654321");
        
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(txn1, txn2));
        
        List<Transaction> transactions = transactionService.getAllTransactions();
        
        assertEquals(2, transactions.size());
    }
    
    @Test
    void testGetTransactionById_Found() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setReference("TXN1234567890");
        
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        
        Optional<Transaction> result = transactionService.getTransactionById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("TXN1234567890", result.get().getReference());
    }
    
    @Test
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<Transaction> result = transactionService.getTransactionById(999L);
        
        assertFalse(result.isPresent());
    }
    
    @Test
    void testGetTransactionsByAccount() {
        Transaction transaction = new Transaction();
        transaction.setCompteSource(testAccount);
        
        when(transactionRepository.findByAccountId(1L)).thenReturn(Arrays.asList(transaction));
        
        List<Transaction> transactions = transactionService.getTransactionsByAccount(1L);
        
        assertEquals(1, transactions.size());
    }
}