# Rapport des Tests - DEVOIR 2 INF352

## Résumé des Tests

| Classe de Test | Tests Lancés | Réussis | Échecs | Errors | Temps (s) |
|---------------|-------------|--------|--------|--------|-----------|
| UserServiceTest | 9 | 9 | 0 | 0 | 1.537 |
| AccountServiceTest | 13 | 13 | 0 | 0 | 7.627 |
| TransactionServiceTest | 9 | 9 | 0 | 0 | 1.615 |
| UserControllerIntegrationTest | 8 | 8 | 0 | 0 | 2.892 |
| AccountControllerIntegrationTest | 8 | 8 | 0 | 0 | 1.649 |
| TransactionControllerIntegrationTest | 8 | 8 | 0 | 0 | 75.52 |
| **TOTAL** | **55** | **55** | **0** | **0** | - |

## Couverture de Code (JaCoCo)

| Métrique | Couverture |
|---------|----------|
| Instructions | 69% |
| Branches | 63% |
| Lignes | 70% |
| Méthodes | 75% |
| Classes | 100% |

## Types de Tests Implémentés

### Tests Unitaires (Mockito)
- **UserServiceTest** : Tests des méthodes de UserService
  - testCreateUser_Success
  - testCreateUser_EmailAlreadyExists
  - testGetAllUsers
  - testGetUserById_Found
  - testGetUserById_NotFound
  - testUpdateUser_Success
  - testUpdateUser_NotFound
  - testDeleteUser_Success
  - testDeleteUser_NotFound

- **AccountServiceTest** : Tests des méthodes de AccountService
  - testCreateAccount_Success
  - testCreateAccount_NumeroAlreadyExists
  - testGetAllAccounts
  - testGetAccountById_Found
  - testGetAccountById_NotFound
  - testGetAccountsByUserId
  - testUpdateAccount_Success
  - testDeleteAccount_Success
  - testDeposit_Success
  - testDeposit_AccountNotFound
  - testWithdraw_Success
  - testWithdraw_InsufficientFunds
  - testWithdraw_AccountNotFound

- **TransactionServiceTest** : Tests des méthodes de TransactionService
  - testEffectuerDepot_Success
  - testEffectuerDepot_AccountNotFound
  - testEffectuerRetrait_Success
  - testEffectuerRetrait_InsufficientFunds
  - testEffectuerRetrait_AccountNotFound
  - testGetAllTransactions
  - testGetTransactionById_Found
  - testGetTransactionById_NotFound
  - testGetTransactionsByAccount

### Tests d'Intégration (Spring Boot Test)
- **UserControllerIntegrationTest** : Tests des endpoints REST utilisateurs
- **AccountControllerIntegrationTest** : Tests des endpoints REST comptes
- **TransactionControllerIntegrationTest** : Tests des endpoints REST transactions

## Instructions pour Exécuter les Tests

```bash
# Compiler et exécuter tous les tests
mvn clean test

# Générer le rapport de couverture
mvn jacoco:report

# Voir le rapport de couverture
# Ouvrir target/site/jacoco/index.html dans un navigateur
```

## Résultats

**BUILD SUCCESS** - Tous les 55 tests passent avec succès.