# RAPPORT DEVOIR 2 – INF3521 : INTRODUCTION TO SOFTWARE TESTING

## 1. CRUD Complet Utilisateurs

L'API permet maintenant d'effectuer toutes les opérations CRUD sur les utilisateurs :

| Opération | Endpoint | Méthode HTTP |
|----------|----------|-------------|
| Créer | `/api/users` | POST |
| Lire tous | `/api/users` | GET |
| Lire un | `/api/users/{id}` | GET |
| Mettre à jour | `/api/users/{id}` | PUT |
| Supprimer | `/api/users/{id}` | DELETE |

### Fonctionnalités ajoutées :
- **Update (Mise à jour)** : `PUT /api/users/{id}` - Permet de modifier les informations d'un utilisateur existant
- **Delete (Suppression)** : `DELETE /api/users/{id}` - Permet de supprimer un utilisateur du système

## 2. Création de Comptes et Transactions

### Création de Comptes
| Opération | Endpoint | Méthode HTTP |
|----------|----------|-------------|
| Créer | `/api/accounts` | POST |
| Lire tous | `/api/accounts` | GET |
| Lire un | `/api/accounts/{id}` | GET |
| Lire par utilisateur | `/api/accounts/user/{userId}` | GET |
| Mettre à jour | `/api/accounts/{id}` | PUT |
| Supprimer | `/api/accounts/{id}` | DELETE |

### Transactions (Dépôt/Retrait)
| Opération | Endpoint | Méthode HTTP |
|----------|----------|-------------|
| Dépôt | `/api/transactions/deposit` | POST |
| Retrait | `/api/transactions/withdraw` | POST |
| Lister transactions | `/api/transactions` | GET |
| Par ID | `/api/transactions/{id}` | GET |
| Par compte | `/api/transactions/account/{accountId}` | GET |

## 3. Scripts de Tests et Rapport

### Structure des Tests
```
src/test/java/com/banking/
├── service/
│   ├── UserServiceTest.java       (9 tests)
│   ├── AccountServiceTest.java   (13 tests)
│   └── TransactionServiceTest.java (9 tests)
└── controller/
    ├── UserControllerIntegrationTest.java        (8 tests)
    ├── AccountControllerIntegrationTest.java      (8 tests)
    └── TransactionControllerIntegrationTest.java   (8 tests)
```

### Résumé des Tests
- **Total des tests** : 55
- **Réussis** : 55
- **Échecs** : 0
- **Taux de couverture** : 69% (instructions)

### Commandes pour exécuter les tests
```bash
# Compiler et exécuter les tests
mvn clean test

# Générer le rapport de couverture JaCoCo
mvn jacoco:report

# Le rapport est disponible dans target/site/jacoco/index.html
```

## 4. Fonctionnalités Implémentées

### APIs livrées :
1. **User API** (`/api/users`)
   - POST /api/users - Créer un utilisateur
   - GET /api/users - Lister tous les utilisateurs
   - GET /api/users/{id} - Récupérer un utilisateur par ID
   - PUT /api/users/{id} - Mettre à jour un utilisateur
   - DELETE /api/users/{id} - Supprimer un utilisateur

2. **Account API** (`/api/accounts`)
   - POST /api/accounts - Créer un compte
   - GET /api/accounts - Lister tous les comptes
   - GET /api/accounts/{id} - Récupérer un compte par ID
   - GET /api/accounts/user/{userId} - Lister les comptes d'un utilisateur
   - PUT /api/accounts/{id} - Mettre à jour un compte
   - DELETE /api/accounts/{id} - Supprimer un compte

3. **Transaction API** (`/api/transactions`)
   - POST /api/transactions/deposit - Effectuer un dépôt
   - POST /api/transactions/withdraw - Effectuer un retrait
   - GET /api/transactions - Lister toutes les transactions
   - GET /api/transactions/{id} - Récupérer une transaction par ID
   - GET /api/transactions/account/{accountId} - Transactions d'un compte

## 5. Résultats des Tests

| Métrique | Valeur |
|----------|-------|
| Tests exécutés | 55 |
| Tests réussis | 55 |
| Tests échoués | 0 |
| Couverture code | 69% |
| Couverture branches | 63% |

---

**Conclusion** :Toutes les fonctionnalités demandées ont été implémentées et testées avec succès. L'API banking permet maintenant le CRUD complet des utilisateurs, la création de comptes bancaires, et l'effectuation de dépôt et retrait d'argent. Les tests unitaires et d'intégration couvrent les principales fonctionnalités avec un taux de couverture de code de 69%.