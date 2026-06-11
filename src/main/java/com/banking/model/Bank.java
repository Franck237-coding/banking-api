package com.banking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "banks")
@JsonIgnoreProperties({"users", "hibernateLazyInitializer", "handler"})
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la banque est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, unique = true)
    private String nom;

    @NotBlank(message = "Le code de la banque est obligatoire")
    @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
    @Column(nullable = false, unique = true)
    private String code;

    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères")
    private String adresse;

    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    private String telephone;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("bank")
    private List<User> users;

    public Bank() {}

    public Bank(String nom, String code, String adresse, String telephone) {
        this.nom = nom;
        this.code = code;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}