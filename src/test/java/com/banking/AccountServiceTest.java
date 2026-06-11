package com.banking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.banking.model.Account;
import com.banking.model.Bank;
import com.banking.repository.AccountRepository;
import com.banking.repository.BankRepository;
import com.banking.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService - Tests Unitaires")
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BankRepository bankRepository;

    private Account testAccount;
    private Bank testBank;

    @BeforeEach
    void setUp() {
        testBank = new Bank();
        testBank.setId(1L);
        testBank.setNom("Banque Nationale");
        testBank.setCode("BNK001");

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setNumeroCompte("BANK123456789");
        testAccount.setSolde(new BigDecimal("1000.00"));
        testAccount.setTypeCompte(Account.TypeCompte.COURANT);
        testAccount.setBank(testBank);
    }

    @Test
    @DisplayName("TC-01: Depot montant valide")
    void testDepotMontantValide() {
        when(accountRepository.findByNumeroCompte("BANK123456789")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        boolean result = accountService.deposit("BANK123456789", new BigDecimal("500.00"));
        assertTrue(result);
    }

    @Test
    @DisplayName("TC-02: Depot compte inexistant")
    void testDepotCompteInexistant() {
        when(accountRepository.findByNumeroCompte("INVALID")).thenReturn(Optional.empty());
        boolean result = accountService.deposit("INVALID", new BigDecimal("500.00"));
        assertFalse(result);
    }

    @Test
    @DisplayName("TC-03: Retrait montant inferieur au solde")
    void testRetraitMontantInferieur() {
        when(accountRepository.findByNumeroCompte("BANK123456789")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        boolean result = accountService.withdraw("BANK123456789", new BigDecimal("500.00"));
        assertTrue(result);
    }

    @Test
    @DisplayName("TC-04: Retrait montant superieur au solde")
    void testRetraitMontantSuperieur() {
        when(accountRepository.findByNumeroCompte("BANK123456789")).thenReturn(Optional.of(testAccount));
        boolean result = accountService.withdraw("BANK123456789", new BigDecimal("5000.00"));
        assertFalse(result);
    }

    @Test
    @DisplayName("TC-05: Retrait compte inexistant")
    void testRetraitCompteInexistant() {
        when(accountRepository.findByNumeroCompte("INVALID")).thenReturn(Optional.empty());
        boolean result = accountService.withdraw("INVALID", new BigDecimal("500.00"));
        assertFalse(result);
    }

    @Test
    @DisplayName("TC-06: Compte trouve par ID")
    void testCompteTrouve() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        Optional<Account> result = accountService.getAccountById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("TC-07: Compte non trouve")
    void testCompteNonTrouve() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Account> result = accountService.getAccountById(999L);
        assertFalse(result.isPresent());
    }
}