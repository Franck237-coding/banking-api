# API BANCAIRE - Banking API

## Description

API REST pour un système bancaire développée avec Spring Boot et PostgreSQL (Neon). Permet la gestion des utilisateurs, comptes bancaires et transactions (dépôt/retrait).

## Fonctionnalités

### Gestion des Utilisateurs (CRUD)
- Créer, Lister, Rechercher, Modifier, Supprimer

### Gestion des Comptes Bancaires (CRUD)
- Créer, Lister, Rechercher, Par utilisateur, Modifier, Supprimer

### Transactions
- Dépôt d'argent
- Retrait d'argent
- Historique des transactions

## Technologies

| Technologie | Rôle |
|-----------|------|
| Java 21 | Langage de programmation |
| Spring Boot 3.2.5 | Framework API REST |
| PostgreSQL (Neon) | Base de données |
| JPA / Hibernate | ORM |
| Swagger (OpenAPI) | Documentation API |
| JUnit 5 + Mockito | Tests unitaires |
| JaCoCo | Couverture de code |

## Déploiement

- **Base de données** : Neon PostgreSQL
- **Serveur** : Render.com
- **API** : https://banking-api-xxxx.onrender.com
- **Swagger** : https://banking-api-xxxx.onrender.com/swagger-ui.html

## Endpoints API

### Utilisateurs
```
POST   /api/users          - Créer utilisateur
GET    /api/users          - Lister utilisateurs
GET    /api/users/{id}     - Rechercher par ID
PUT    /api/users/{id}     - Modifier utilisateur
DELETE /api/users/{id}     - Supprimer utilisateur
```

### Comptes
```
POST   /api/accounts              - Créer compte
GET    /api/accounts              - Lister comptes
GET    /api/accounts/{id}       - Rechercher par ID
GET    /api/accounts/user/{userId} - Comptes utilisateur
PUT    /api/accounts/{id}       - Modifier compte
DELETE /api/accounts/{id}      - Supprimer compte
```

### Transactions
```
POST   /api/transactions/deposit?numeroCompte=X&montant=Y     - Dépôt
POST   /api/transactions/withdraw?numeroCompte=X&montant=Y - Retrait
GET    /api/transactions                              - Historique
GET    /api/transactions/{id}                       - Par ID
GET    /api/transactions/account/{accountId}       - Par compte
```

## Commandes

```bash
# Compiler
mvn compile

#Lancer les tests
mvn test

#Générer rapport couverture
mvn jacoco:report

#Package
mvn package

#Lancer
mvn spring-boot:run
```

## Tests

| Classe | Tests | Type |
|--------|------|------|
| UserServiceTest | 8 | Unit |
| AccountServiceTest | 7 | Unit |
| TransactionServiceTest | 7 | Unit |
| **TOTAL** | **22** | **Unit** |

**Couverture :** 54% instructions, 60% branches

## Structure du Projet

```
src/main/java/com/banking/
├── controller/      # API REST
├── service/         # Logique métier
├── repository/     # Accès données
├── model/           # Entités JPA
├── dto/            # Data Transfer Objects
└── config/         # Configuration

src/test/java/com/banking/
├── UserServiceTest.java
├── AccountServiceTest.java
└── TransactionServiceTest.java
```

## Exemples d'Utilisation

### Créer un utilisateur
```bash
curl -X POST https://banking-api.onrender.com/api/users \
  -H "Content-Type: application/json" \
  -d '{"nom":"Dupont","prenom":"Jean","email":"jean@test.com","telephone":"0612345678"}'
```

### Effectuer un dépôt
```bash
curl -X POST "https://banking-api.onrender.com/api/transactions/deposit?numeroCompte=BANK001&montant=1000&description=Salaire"
```

---

**Développé dans le cadre du cours INF3521 - Introduction to Software Testing**