# Fiche Technique du Projet : Bank API

**Version**: 1.0.0
**Date**: 2024-05-06

## 1. Vue d'ensemble du Projet

**Bank API** est une application RESTful développée avec Spring Boot qui simule les opérations d'un système bancaire moderne. Elle offre une architecture multi-banques où chaque banque possède ses propres caractéristiques (ex: taux de bénéfice). L'API gère les utilisateurs, les comptes bancaires, et une gamme complète de transactions financières, tout en intégrant des fonctionnalités de sécurité robustes comme l'authentification par token et la vérification des utilisateurs par email.

## 2. Technologies et Outils

| Catégorie           | Technologie/Outil                               | Rôle                                                              |
| ------------------- | ----------------------------------------------- | ----------------------------------------------------------------- |
| **Langage**         | Java 21                                         | Langage de programmation principal.                               |
| **Framework**       | Spring Boot 3.3.4                               | Framework principal pour le développement de l'application.       |
| **Accès aux Données** | Spring Data JPA, Hibernate                      | ORM pour la persistance des données et l'interaction avec la BDD. |
| **Base de Données** | PostgreSQL (via Neon DB)                        | Système de gestion de base de données relationnelle.              |
| **Sécurité**        | Spring Security                                 | Gestion de l'authentification, des autorisations et de la sécurité des API. |
| **Documentation API** | Springdoc OpenAPI (Swagger)                     | Génération automatique de la documentation interactive de l'API.  |
| **Communication**   | Spring Boot Starter Mail                        | Envoi d'emails (utilisé pour la vérification des comptes).        |
| **Gestion Dépendances** | Maven                                           | Outil de build et de gestion des dépendances du projet.           |
| **Conteneurisation**| Docker                                          | Empaquetage de l'application dans un conteneur portable.          |

## 3. Architecture

L'application suit une **architecture en couches (Layered Architecture)** classique, favorisant la séparation des préoccupations et la maintenabilité :

1.  **Couche Contrôleur (`controller`)**: Expose les points de terminaison (endpoints) de l'API REST. Elle reçoit les requêtes HTTP, valide les entrées de base et les transmet à la couche de service. Elle est responsable de la sérialisation des réponses.
2.  **Couche Service (`service`)**: Contient la logique métier principale de l'application. Elle orchestre les opérations, applique les règles de gestion (ex: vérification des soldes, calcul des frais) et effectue les transactions.
3.  **Couche Dépôt (`repository`)**: Gérée par Spring Data JPA, cette couche est responsable de l'accès aux données. Elle fournit une abstraction sur la base de données, permettant d'effectuer des opérations CRUD (Create, Read, Update, Delete) sans écrire de code SQL boilerplate.
4.  **Couche Modèle (`model`)**: Définit les entités de données (ex: `BankUser`, `BankAccount`, `Bank`) qui sont mappées aux tables de la base de données.
5.  **Couche Sécurité (`security`)**: Intégrée via Spring Security, elle intercepte les requêtes pour gérer l'authentification (qui êtes-vous ?) et l'autorisation (que pouvez-vous faire ?).

## 4. Schéma de la Base de Données

Les entités principales sont :

-   `BankUser`: Représente un client de la banque. Contient les informations personnelles, les identifiants de connexion, et un statut de vérification (`verified`).
-   `Bank`: Représente une institution bancaire (ex: CCA, UBA). Contient des attributs spécifiques comme le nom et le `profitRate`.
-   `AccountType`: Définit le type de compte (ex: Épargne, Courant).
-   `BankAccount`: Représente le compte bancaire d'un utilisateur. Il est lié à un `BankUser`, une `Bank` et un `AccountType`. Il contient le solde (`balance`) et un numéro de compte unique.
-   `BankTransaction`: Enregistre chaque transaction (dépôt, retrait, transfert). Contient le type, le montant, la date, et les comptes source/destination.

## 5. Points de Terminaison de l'API (Endpoints)

Tous les endpoints, sauf `/auth/**`, nécessitent un token d'authentification valide dans l'en-tête `Authorization`.

| Méthode | Endpoint                               | Description                                                                 | Authentification |
| ------- | -------------------------------------- | --------------------------------------------------------------------------- | ---------------- |
| `POST`  | `/api/auth/register`                   | Crée un nouvel utilisateur (non vérifié) et envoie un email de vérification. | Non              |
| `POST`  | `/api/auth/verify`                     | Vérifie un utilisateur avec le code reçu par email.                         | Non              |
| `POST`  | `/api/auth/login`                      | Authentifie un utilisateur et retourne un token.                            | Non              |
| `GET`   | `/api/users`                           | Récupère la liste de tous les utilisateurs.                                 | Oui              |
| `GET`   | `/api/users/{id}`                      | Récupère un utilisateur par son ID.                                         | Oui              |
| `PUT`   | `/api/users/{id}`                      | Met à jour les informations d'un utilisateur.                               | Oui              |
| `DELETE`| `/api/users/{id}`                      | Supprime un utilisateur.                                                    | Oui              |
| `POST`  | `/api/accounts`                        | Crée un nouveau compte bancaire pour l'utilisateur authentifié.             | Oui              |
| `GET`   | `/api/accounts/{id}`                   | Récupère les détails d'un compte bancaire.                                  | Oui              |
| `GET`   | `/api/accounts/{id}/transactions`      | Récupère l'historique des transactions pour un compte.                      | Oui              |
| `POST`  | `/api/transactions/deposit`            | Effectue un dépôt sur un compte.                                            | Oui              |
| `POST`  | `/api/transactions/withdraw`           | Effectue un retrait d'un compte.                                            | Oui              |
| `POST`  | `/api/transactions/transfer`           | Transfère des fonds entre deux comptes de l'utilisateur authentifié.        | Oui              |
| `POST`  | `/api/transactions/withdraw-mobile`    | Simule un retrait vers un compte Mobile Money (Momo/OM).                    | Oui              |

## 6. Fonctionnalités Clés

-   **Architecture Multi-Banques**: Le système est conçu pour supporter plusieurs banques distinctes, chacune avec ses propres paramètres.
-   **Vérification par Email**: Les nouveaux utilisateurs doivent vérifier leur compte via un code à 4 chiffres envoyé à leur adresse email, renforçant la sécurité et l'authenticité des comptes.
-   **Opérations Bancaires Complètes**:
    -   **Dépôt/Retrait**: Opérations de base sur un compte.
    -   **Transfert Interne**: Transfert sécurisé entre les propres comptes d'un même utilisateur.
    -   **Retrait Mobile Money**: Simulation d'une passerelle de paiement vers des services tiers comme MTN Mobile Money ou Orange Money, avec une limite de transaction configurable.
-   **Sécurité par Token**: L'accès aux endpoints sensibles est protégé par un système de token simple, garantissant que seul l'utilisateur authentifié peut accéder à ses propres données.
-   **Documentation Swagger**: Une documentation claire et interactive est automatiquement générée, facilitant les tests et l'intégration de l'API.
