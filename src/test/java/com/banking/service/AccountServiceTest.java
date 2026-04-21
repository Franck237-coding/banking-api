package com.banking.service;

import com.banking.model.Account;
import com.banking.model.User;
import com.banking.repository.AccountRepository;
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
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private AccountService accountService;
    
    private Account testAccount;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setNom("Dupont");
        testUser.setPrenom("Jean");
        testUser.setEmail("jean@example.com");
        testUser.setTelephone("0612345678");
        
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setNumeroCompte("BANK1234567890");
        testAccount.setSolde(new BigDecimal("1000.00"));
        testAccount.setTypeCompte(Account.TypeCompte.COURANT);
        testAccount.setDateCreation(LocalDateTime.now());
        testAccount.setUser(testUser);
    }
    
    @Test
    void testCreateAccount_Success() {
        when(accountRepository.existsByNumeroCompte(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        
        Account result = accountService.createAccount(testAccount);
        
        assertNotNull(result);
        assertEquals("BANK1234567890", result.getNumeroCompte());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void testCreateAccount_NumeroAlreadyExists() {
        when(accountRepository.existsByNumeroCompte(anyString())).thenReturn(true);
        
        assertThrows(RuntimeException.class, () -> accountService.createAccount(testAccount));
        verify(accountRepository, never()).save(any(Account.class));
    }
    
    @Test
    void testGetAllAccounts() {
        Account account2 = new Account();
        account2.setNumeroCompte("BANK0987654321");
        
        when(accountRepository.findAll()).thenReturn(Arrays.asList(testAccount, account2));
        
        List<Account> accounts = accountService.getAllAccounts();
        
        assertEquals(2, accounts.size());
    }
    
    @Test
    void testGetAccountById_Found() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        
        Optional<Account> result = accountService.getAccountById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("BANK1234567890", result.get().getNumeroCompte());
    }
    
    @Test
    void testGetAccountById_NotFound() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<Account> result = accountService.getAccountById(999L);
        
        assertFalse(result.isPresent());
    }
    
    @Test
    void testGetAccountsByUserId() {
        when(accountRepository.findByUserId(1L)).thenReturn(Arrays.asList(testAccount));
        
        List<Account> accounts = accountService.getAccountsByUserId(1L);
        
        assertEquals(1, accounts.size());
    }
    
    @Test
    void testUpdateAccount_Success() {
        Account updatedDetails = new Account();
        updatedDetails.setTypeCompte(Account.TypeCompte.EPARGNE);
        updatedDetails.setSolde(new BigDecimal("2000.00"));
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        
        Account result = accountService.updateAccount(1L, updatedDetails);
        
        assertNotNull(result);
        verify(accountRepository).save(any(Account.class));
    }
    
    @Test
    void testDeleteAccount_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        
        accountService.deleteAccount(1L);
        
        verify(accountRepository, times(1)).delete(testAccount);
    }
    
    @Test
    void testDeposit_Success() {
        when(accountRepository.findByNumeroCompte("BANK1234567890")).thenReturn(Optional.of(testAccount));
        
        boolean result = accountService.deposit("BANK1234567890", new BigDecimal("500.00"));
        
        assertTrue(result);
        verify(accountRepository).save(any(Account.class));
    }
    
    @Test
    void testDeposit_AccountNotFound() {
        when(accountRepository.findByNumeroCompte("INVALID")).thenReturn(Optional.empty());
        
        boolean result = accountService.deposit("INVALID", new BigDecimal("500.00"));
        
        assertFalse(result);
    }
    
    @Test
    void testWithdraw_Success() {
        when(accountRepository.findByNumeroCompte("BANK1234567890")).thenReturn(Optional.of(testAccount));
        
        boolean result = accountService.withdraw("BANK1234567890", new BigDecimal("500.00"));
        
        assertTrue(result);
        verify(accountRepository).save(any(Account.class));
    }
    
    @Test
    void testWithdraw_InsufficientFunds() {
        when(accountRepository.findByNumeroCompte("BANK1234567890")).thenReturn(Optional.of(testAccount));
        
        boolean result = accountService.withdraw("BANK1234567890", new BigDecimal("5000.00"));
        
        assertFalse(result);
    }
    
    @Test
    void testWithdraw_AccountNotFound() {
        when(accountRepository.findByNumeroCompte("INVALID")).thenReturn(Optional.empty());
        
        boolean result = accountService.withdraw("INVALID", new BigDecimal("500.00"));
        
        assertFalse(result);
    }
}