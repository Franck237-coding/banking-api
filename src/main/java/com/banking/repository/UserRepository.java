package com.banking.repository;

import com.banking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<User> findByNomContainingIgnoreCase(String nom);
    
    List<User> findByPrenomContainingIgnoreCase(String prenom);
    
    @Query("SELECT u FROM User u WHERE u.role = 'USER'")
    List<User> findUsers();
    
    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
    List<User> findAdmins();
}
