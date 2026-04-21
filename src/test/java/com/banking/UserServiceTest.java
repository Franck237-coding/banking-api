package com.banking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.banking.model.User;
import com.banking.repository.UserRepository;
import com.banking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Tests Unitaires")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setNom("Dupont");
        testUser.setPrenom("Jean");
        testUser.setEmail("jean@test.com");
        testUser.setTelephone("0612345678");
    }

    // ─── Tests creation utilisateur (partitionnement classes equivalence) ───

    @Test
    @DisplayName("TC-01: Creer utilisateur valide")
    void testCreerUtilisateurValide() {
        when(userRepository.existsByEmail("jean@test.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        User result = userService.createUser(testUser);
        assertNotNull(result);
        assertEquals("Dupont", result.getNom());
    }

    @Test
    @DisplayName("TC-02: Email deja existe")
    void testEmailDejaExiste() {
        when(userRepository.existsByEmail("jean@test.com")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> userService.createUser(testUser));
    }

    // ─── Tests recherche utilisateur (valeurs limites) ───

    @Test
    @DisplayName("TC-03: Utilisateur trouve par ID")
    void testUtilisateurTrouve() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("TC-04: Utilisateur non trouve (ID invalide)")
    void testUtilisateurNonTrouve() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserById(999L);
        assertFalse(result.isPresent());
    }

    // ─── Tests modification utilisateur ───

    @Test
    @DisplayName("TC-05: Modifier utilisateur existant")
    void testModifierUtilisateur() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        User updated = new User();
        updated.setNom("Martin");
        User result = userService.updateUser(1L, updated);
        assertNotNull(result);
    }

    @Test
    @DisplayName("TC-06: Modifier utilisateur inexistant")
    void testModifierUtilisateurInexistant() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUser(999L, testUser));
    }

    // ─── Tests suppression utilisateur ───

    @Test
    @DisplayName("TC-07: Supprimer utilisateur existant")
    void testSupprimerUtilisateur() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);
        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    @DisplayName("TC-08: Supprimer utilisateur inexistant")
    void testSupprimerUtilisateurInexistant() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
    }
}