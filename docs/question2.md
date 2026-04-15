# Specification de l'application BANK-API

Nom: KEYANFE DJUNE OBERLIN

Matricule: 23V2021

## 1. Objectif

L'application BANK-API est une API Spring Boot minimaliste qui permet de creer et de lister des utilisateurs bancaires. Les donnees sont conservees localement dans un fichier H2 et l'API est documentee avec Swagger.

## 2. Fonctionnalites

- Ajouter un utilisateur avec les champs `firstName`, `lastName`, `email` et `phone`.
- Valider les donnees saisies avant l'enregistrement.
- Generer automatiquement un identifiant interne `id`.
- Generer automatiquement un numero de compte `accountNumber`.
- Horodater chaque utilisateur avec `createdAt`.
- Lister tous les utilisateurs enregistres.
- Exposer la documentation API avec Swagger UI.
- Conserver les donnees dans un fichier H2 local.
- Afficher la console H2 pour consulter la base.

## 3. Specifications non fonctionnelles

- Simplicite: l'application contient uniquement un controleur, un modele et un repository.
- Portabilite: elle peut etre lancee sur une machine standard avec Java 21.
- Maintenabilite: le code reste court et facile a comprendre.
- Fiabilite: les donnees sont validees avant traitement.
- Persistance: les donnees sont conservees dans un fichier local H2.
- Lisibilite: l'API est documentee avec Swagger et des annotations explicites.

## 4. Interface admin

L'application ne contient pas encore une interface d'administration dediee de type tableau de bord. Pour la version actuelle, les specifications de l'interface admin sont les suivantes:

- l'administrateur utilise Swagger UI pour tester les endpoints `POST /api/users` et `GET /api/users`;
- l'administrateur utilise la console H2 pour consulter les donnees stockees dans le fichier local;
- l'interface exposee permet seulement l'ajout et la consultation des utilisateurs;
- il n'existe pas encore de fonctions de suppression, modification, suspension ou filtrage avance;
- la documentation des routes et des champs est fournie par `@Operation`, `@ApiResponse` et `@Schema`.

Dans cette version, l'interface admin se limite donc a la consultation et a l'ajout des utilisateurs via les outils de documentation et de stockage fournis par le prototype.

## 5. Licence utilisateur

L'application est fournie dans un cadre academique et pedagogique.

L'utilisateur est autorise a:

- executer l'application pour le devoir;
- consulter la documentation Swagger;
- modifier le code dans un contexte scolaire;
- reutiliser la structure pour des travaux similaires.

L'utilisateur n'est pas autorise a:

- revendre l'application sans autorisation;
- retirer la paternite academique dans un rendu;
- utiliser le projet comme produit commercial sans validation supplementaire.

## 6. Documentation Swagger

Swagger UI est accessible apres demarrage de l'application:

- http://localhost:8080/swagger-ui.html

La specification OpenAPI est disponible ici:

- http://localhost:8080/api-docs

Endpoints documentes:

- `POST /api/users` pour ajouter un utilisateur.
- `GET /api/users` pour lister les utilisateurs.

Exemple de corps JSON pour `POST /api/users`:

```json
{
  "firstName": "Awa",
  "lastName": "Diallo",
  "email": "awa@example.com",
  "phone": "+237699000000"
}
```

Les champs de `BankUser` sont visibles dans Swagger grace aux annotations `@Schema`.