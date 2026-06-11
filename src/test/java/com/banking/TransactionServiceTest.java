package com.banking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.banking.model.Account;
import com.banking.model.Bank;
import com.banking.model.Transaction;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.service.TransactionService;
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
@DisplayName("TransactionService - Tests Unitaires")
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

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
    @DisplayName("TC-01: Depot valide")
    void testDepotValide() {
        when(accountRepository.findByNumeroCompte("BANK123456789")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        Transaction result = transactionService.effectuerDepot("BANK123456789", new BigDecimal("500.00"), "Salaire");
        assertNotNull(result);
        assertEquals(Transaction.TypeTransaction.DEPOT, result.getTypeTransaction());
    }

    @Test
    @DisplayName("TC-02: Depot compte inexistant")
    void testDepotCompteInexistant() {
        when(accountRepository.findByNumeroCompte("INVALID")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> 
            transactionService.effectuerDepot("INVALID", new BigDecimal("500.00"), "Test"));
    }

    @Test
    @DisplayName("TC-03: Retrait montant valide")
    void testRetraitMontantValide() {
        when(accountRepository.findByNumeroCompte("BANK123456789")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        Transaction result = transactionService.effectuerRetrait("BANK123456789", new BigDecimal("500.00"), "Retrait");
        assertNotNull(result);
        assertEquals(Transaction.TypeTransaction.RETRAIT, result.getTypeTransaction());
    }

    @Test
    @DisplayName("TC-04: Retrait solde insuffisant")
    void testRetraitSoldeInsuffisant() {
        when(accountRepository.findByNumeroCompte("BANK123456789")).thenReturn(Optional.of(testAccount));
        assertThrows(RuntimeException.class, () -> 
            transactionService.effectuerRetrait("BANK123456789", new BigDecimal("5000.00"), "Test"));
    }

    @Test
    @DisplayName("TC-05: Retrait compte inexistant")
    void testRetraitCompteInexistant() {
        when(accountRepository.findByNumeroCompte("INVALID")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> 
            transactionService.effectuerRetrait("INVALID", new BigDecimal("500.00"), "Test"));
    }

    @Test
    @DisplayName("TC-06: Transaction trouvee par ID")
    void testTransactionTrouvee() {
        Transaction txn = new Transaction();
        txn.setId(1L);
        txn.setReference("TXN001");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(txn));
        Optional<Transaction> result = transactionService.getTransactionById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("TC-07: Transaction non trouvee")
    void testTransactionNonTrouvee() {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Transaction> result = transactionService.getTransactionById(999L);
        assertFalse(result.isPresent());
    }
}