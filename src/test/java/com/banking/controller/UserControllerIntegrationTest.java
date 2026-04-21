package com.banking.controller;

import com.banking.model.User;
import com.banking.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setNom("Dupont");
        testUser.setPrenom("Jean");
        testUser.setEmail("jean.dupont@example.com");
        testUser.setTelephone("0612345678");
        testUser.setRole(User.Role.USER);
    }
    
    @Test
    void testCreateUser_Success() throws Exception {
        String userJson = objectMapper.writeValueAsString(testUser);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom", is("Dupont")))
                .andExpect(jsonPath("$.prenom", is("Jean")))
                .andExpect(jsonPath("$.email", is("jean.dupont@example.com")));
    }
    
    @Test
    void testCreateUser_EmailAlreadyExists() throws Exception {
        userRepository.save(testUser);
        
        String userJson = objectMapper.writeValueAsString(testUser);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isConflict());
    }
    
    @Test
    void testGetAllUsers() throws Exception {
        userRepository.save(testUser);
        
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom", is("Dupont")));
    }
    
    @Test
    void testGetUserById_Found() throws Exception {
        User savedUser = userRepository.save(testUser);
        
        mockMvc.perform(get("/api/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("Dupont")));
    }
    
    @Test
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testUpdateUser_Success() throws Exception {
        User savedUser = userRepository.save(testUser);
        
        savedUser.setNom("DupontUpdated");
        String userJson = objectMapper.writeValueAsString(savedUser);
        
        mockMvc.perform(put("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("DupontUpdated")));
    }
    
    @Test
    void testDeleteUser_Success() throws Exception {
        User savedUser = userRepository.save(testUser);
        
        mockMvc.perform(delete("/api/users/" + savedUser.getId()))
                .andExpect(status().isNoContent());
        
        assertTrue(userRepository.findById(savedUser.getId()).isEmpty());
    }
    
    @Test
    void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}