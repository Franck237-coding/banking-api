package com.banking.controller;

import com.banking.model.User;
import com.banking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Gestion des Utilisateurs", description = "API pour la gestion des utilisateurs du système bancaire")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "Ajouter un nouvel utilisateur", description = "Crée un nouvel utilisateur dans le système")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
    
    @Operation(summary = "Lister tous les utilisateurs", description = "Retourne la liste de tous les utilisateurs du système")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Lister les utilisateurs simples", description = "Retourne uniquement la liste des utilisateurs (non-admins)")
    @GetMapping("/simple")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Récupérer un utilisateur par ID", description = "Retourne les détails d'un utilisateur spécifique")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID de l'utilisateur à récupérer") @PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Récupérer un utilisateur par email", description = "Retourne les détails d'un utilisateur via son email")
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(
            @Parameter(description = "Email de l'utilisateur à récupérer") @PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour les informations d'un utilisateur existant")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID de l'utilisateur à mettre à jour") @PathVariable Long id,
            @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur du système")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer") @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Rechercher par nom", description = "Recherche des utilisateurs par nom")
    @GetMapping("/search/nom/{nom}")
    public ResponseEntity<List<User>> searchByNom(
            @Parameter(description = "Nom à rechercher") @PathVariable String nom) {
        List<User> users = userService.searchUsersByName(nom);
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Rechercher par prénom", description = "Recherche des utilisateurs par prénom")
    @GetMapping("/search/prenom/{prenom}")
    public ResponseEntity<List<User>> searchByPrenom(
            @Parameter(description = "Prénom à rechercher") @PathVariable String prenom) {
        List<User> users = userService.searchUsersByPrenom(prenom);
        return ResponseEntity.ok(users);
    }
}
