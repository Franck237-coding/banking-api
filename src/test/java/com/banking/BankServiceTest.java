package com.banking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.banking.model.Bank;
import com.banking.repository.BankRepository;
import com.banking.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("BankService - Tests Unitaires")
class BankServiceTest {

    @InjectMocks
    private BankService bankService;

    @Mock
    private BankRepository bankRepository;

    private Bank testBank;

    @BeforeEach
    void setUp() {
        testBank = new Bank();
        testBank.setId(1L);
        testBank.setNom("Banque Nationale");
        testBank.setCode("BNK001");
        testBank.setAdresse("123 Rue Principale");
        testBank.setTelephone("0123456789");
    }

    @Test
    @DisplayName("TC-B01: Creer banque valide")
    void testCreerBanqueValide() {
        when(bankRepository.existsByCode("BNK001")).thenReturn(false);
        when(bankRepository.existsByNom("Banque Nationale")).thenReturn(false);
        when(bankRepository.save(any(Bank.class))).thenReturn(testBank);
        Bank result = bankService.createBank(testBank);
        assertNotNull(result);
        assertEquals("Banque Nationale", result.getNom());
    }

    @Test
    @DisplayName("TC-B02: Code deja existe")
    void testCodeDejaExiste() {
        when(bankRepository.existsByCode("BNK001")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> bankService.createBank(testBank));
    }

    @Test
    @DisplayName("TC-B03: Banque trouvee par ID")
    void testBanqueTrouvee() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(testBank));
        Optional<Bank> result = bankService.getBankById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("TC-B04: Banque trouvee par code")
    void testBanqueTrouveeParCode() {
        when(bankRepository.findByCode("BNK001")).thenReturn(Optional.of(testBank));
        Optional<Bank> result = bankService.getBankByCode("BNK001");
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("TC-B05: Modifier banque existante")
    void testModifierBanque() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(testBank));
        when(bankRepository.save(any(Bank.class))).thenReturn(testBank);
        Bank updated = new Bank();
        updated.setNom("Banque Modifiee");
        updated.setCode("BNK001");
        Bank result = bankService.updateBank(1L, updated);
        assertNotNull(result);
    }
}