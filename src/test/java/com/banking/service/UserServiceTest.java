package com.banking.service;

import com.banking.model.User;
import com.banking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setNom("Dupont");
        testUser.setPrenom("Jean");
        testUser.setEmail("jean.dupont@example.com");
        testUser.setTelephone("0612345678");
        testUser.setRole(User.Role.USER);
        testUser.setDateCreation(LocalDateTime.now());
    }
    
    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        User result = userService.createUser(testUser);
        
        assertNotNull(result);
        assertEquals("Dupont", result.getNom());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testCreateUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        assertThrows(RuntimeException.class, () -> userService.createUser(testUser));
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void testGetAllUsers() {
        User user2 = new User();
        user2.setNom("Martin");
        user2.setPrenom("Sophie");
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));
        
        List<User> users = userService.getAllUsers();
        
        assertEquals(2, users.size());
    }
    
    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        Optional<User> result = userService.getUserById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("Dupont", result.get().getNom());
    }
    
    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<User> result = userService.getUserById(999L);
        
        assertFalse(result.isPresent());
    }
    
    @Test
    void testUpdateUser_Success() {
        User updatedDetails = new User();
        updatedDetails.setNom("DupontUpdated");
        updatedDetails.setPrenom("Jean");
        updatedDetails.setEmail("jean.dupont@example.com");
        updatedDetails.setTelephone("0699999999");
        updatedDetails.setRole(User.Role.USER);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setNom(updatedDetails.getNom());
            return u;
        });
        
        User result = userService.updateUser(1L, updatedDetails);
        
        assertNotNull(result);
        assertEquals("DupontUpdated", result.getNom());
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> userService.updateUser(999L, testUser));
    }
    
    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        userService.deleteUser(1L);
        
        verify(userRepository, times(1)).delete(testUser);
    }
    
    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
    }
}