# Graphe de Contrôle de Flux (CFG) pour le Service `createUser`

```mermaid
graph TD
    subgraph Service: createUser
        A[Début] --> B{"Email déjà utilisé ?"};
        B -- Oui --> C[throw IllegalArgumentException];
        B -- Non --> D[Générer un code de vérification à 4 chiffres];
        D --> E[Encoder le mot de passe];
        E --> F[Créer une nouvelle instance de BankUser];
        F --> G[Sauvegarder l'utilisateur (non vérifié)];
        G --> H[Envoyer l'email de vérification];
        H --> I{Envoi email réussi ?};
        I -- Non --> J[Log l'erreur d'envoi];
        I -- Oui --> K[Retourner l'utilisateur sauvegardé];
    end
    C --> Z[Fin];
    J --> K;
    K --> Z[Fin];
```

## Description du Flux Abstrait

1.  **A (Début)** : La création d'un nouvel utilisateur commence.
2.  **B (Validation Email)** : Vérifie si l'adresse email fournie est déjà enregistrée dans la base de données.
3.  **C (Exception Email Existant)** : Si l'email existe déjà, une exception est levée pour empêcher les doublons.
4.  **D (Génération Code)** : Un code de vérification aléatoire (ex: 4 chiffres) est généré.
5.  **E (Encodage MDP)** : Le mot de passe fourni par l'utilisateur est encodé (haché) pour des raisons de sécurité.
6.  **F (Création Entité)** : Une nouvelle entité `BankUser` est créée avec les informations fournies, le mot de passe haché, le code de vérification, et le statut `verified` à `false`.
7.  **G (Sauvegarde)** : Le nouvel utilisateur est sauvegardé en base de données.
8.  **H (Envoi Email)** : Le service de messagerie est appelé pour envoyer le code de vérification à l'adresse email de l'utilisateur.
9.  **I (Validation Envoi)** : Le système vérifie si l'email a été envoyé avec succès.
10. **J (Log Erreur)** : En cas d'échec de l'envoi, l'erreur est enregistrée (loggée) pour diagnostic, mais le processus ne s'arrête pas pour ne pas bloquer l'inscription.
11. **K (Retour)** : L'entité `BankUser` sauvegardée est retournée.
12. **Z (Fin)** : Le processus de création est terminé.
