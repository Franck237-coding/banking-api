# API de Gestion des Transactions Bancaires

## Description

API REST complète pour la gestion des transactions bancaires développée avec Spring Boot et PostgreSQL. Ce système permet de gérer les utilisateurs, les comptes bancaires et les transactions de manière sécurisée et efficace.

## Fonctionnalités

### Gestion des Utilisateurs
- Ajout d'utilisateurs dans le système
- Liste de tous les utilisateurs
- Recherche par nom, prénom, email
- Gestion des rôles (USER/ADMIN)

### Gestion des Comptes
- Création de comptes bancaires
- Types de comptes : COURANT, EPARGNE, LIVRET_A
- Dépôts et retraits
- Consultation du solde

### Gestion des Transactions
- Dépôts sur compte
- Retraits avec vérification de solde
- Virements entre comptes
- Historique des transactions
- Filtrage par période, statut, type

## Technologies

- **Java 17**
- **Spring Boot 3.2.5**
- **PostgreSQL**
- **Spring Data JPA**
- **OpenAPI/Swagger**
- **Maven**

## Démarrage Rapide

### Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- PostgreSQL 12+

### Installation

1. **Cloner le projet**
```bash
git clone <repository-url>
cd banking-api
```

2. **Configurer la base de données**
```bash
# Créer la base de données
createdb banking_db

# Exécuter le script de migration
psql -d banking_db -f src/main/resources/db/migration/V1__Create_tables.sql
```

3. **Configurer l'application**
```bash
# Copier le fichier de configuration
cp src/main/resources/application.properties.example src/main/resources/application.properties

# Modifier les paramètres de connexion à la base de données
nano src/main/resources/application.properties
```

4. **Lancer l'application**
```bash
mvn spring-boot:run
```

L'application sera disponible sur `http://localhost:8080`

### Documentation API

Accédez à la documentation Swagger :
- **Swagger UI** : `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON** : `http://localhost:8080/api-docs`

## Endpoints Principaux

### Utilisateurs
- `POST /api/users` - Créer un utilisateur
- `GET /api/users` - Lister tous les utilisateurs
- `GET /api/users/{id}` - Récupérer un utilisateur
- `PUT /api/users/{id}` - Mettre à jour un utilisateur
- `DELETE /api/users/{id}` - Supprimer un utilisateur

### Comptes
- `POST /api/accounts` - Créer un compte
- `GET /api/accounts` - Lister tous les comptes
- `GET /api/accounts/{id}` - Récupérer un compte
- `POST /api/accounts/deposit` - Effectuer un dépôt
- `POST /api/accounts/withdraw` - Effectuer un retrait

### Transactions
- `POST /api/transactions/deposit` - Dépôt
- `POST /api/transactions/withdraw` - Retrait
- `POST /api/transactions/transfer` - Virement
- `GET /api/transactions` - Lister toutes les transactions
- `GET /api/transactions/account/{accountId}` - Transactions d'un compte

## Exemples d'utilisation

### Créer un utilisateur
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@email.com",
    "telephone": "0123456789"
  }'
```

### Effectuer un dépôt
```bash
curl -X POST "http://localhost:8080/api/transactions/deposit?numeroCompte=BANK123456789012&montant=1000&description=Salaire"
```

### Effectuer un virement
```bash
curl -X POST "http://localhost:8080/api/transactions/transfer?compteSource=BANK123456789012&compteDestination=BANK234567890123&montant=500&description=Virement mensuel"
```

## Configuration pour le déploiement

### Render (Production)
1. Créer un compte sur [render.com](https://render.com)
2. Connecter votre repository Git
3. Configurer les variables d'environnement :
   - `DATABASE_URL` : URL de la base de données PostgreSQL
   - `SPRING_PROFILES_ACTIVE` : `neon`
4. Déployer automatiquement

### Neon (Base de données)
1. Créer un compte sur [neon.tech](https://neon.tech)
2. Créer une base de données PostgreSQL
3. Utiliser le fichier `application-neon.properties`
4. Configurer les variables d'environnement

## Tests

### Lancer les tests
```bash
mvn test
```

### Tests avec coverage
```bash
mvn jacoco:report
```

## Architecture

```
src/main/java/com/banking/
|-- controller/     # REST Controllers
|-- service/        # Business Logic
|-- repository/     # Data Access Layer
|-- model/          # JPA Entities
|-- config/         # Configuration Classes
```

## Validation

- Validation des entrées avec Jakarta Validation
- Contraintes sur les champs obligatoires
- Formatage automatique des données

## Monitoring

- Logs structurés
- Métriques Spring Boot Actuator
- Health checks disponibles sur `/actuator/health`

## Licence

MIT License - Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## Support

- Documentation complète disponible
- Issues sur GitHub
- Email : support@banking-api.com

## Contribuer

1. Fork le projet
2. Créer une branche feature
3. Commiter vos changements
4. Pusher vers la branche
5. Créer une Pull Request
# Force Render update
