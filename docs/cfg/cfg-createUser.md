# Graphe de Contrôle de Flux (CFG) pour `BankUserService.createUser`

```mermaid
graph TD
    A[Début] --> B{userRepository.existsByEmail(user.getEmail())};
    B -- Vrai --> C[throw DuplicateResourceException];
    B -- Faux --> D[generateVerificationCode()];
    D --> E[user.setVerificationCode(code)];
    E --> F[user.setVerified(false)];
    F --> G[userRepository.save(user)];
    G --> H[emailService.sendVerificationEmail(email, code)];
    H --> I[Retourner savedUser];
    I --> J[Fin];
    C --> J;
```

## Description du Flux

1.  **A (Début)** : L'exécution de la méthode `createUser` commence.
2.  **B (Condition)** : Le système vérifie si un utilisateur avec la même adresse e-mail existe déjà dans la base de données.
3.  **C (Exception)** : Si l'e-mail existe déjà (branche "Vrai"), une exception `DuplicateResourceException` est levée, et le processus se termine.
4.  **D (Génération)** : Si l'e-mail n'existe pas (branche "Faux"), un code de vérification à 4 chiffres est généré.
5.  **E (Assignation)** : Le code généré est assigné à l'objet `user`.
6.  **F (Initialisation)** : Le statut de vérification de l'utilisateur est initialisé à `false`.
7.  **G (Sauvegarde)** : L'objet `user` est sauvegardé dans la base de données.
8.  **H (Envoi E-mail)** : Le service d'e-mail est appelé pour envoyer le code de vérification à l'adresse e-mail de l'utilisateur.
9.  **I (Retour)** : L'entité utilisateur sauvegardée (`savedUser`) est retournée par la méthode.
10. **J (Fin)** : L'exécution de la méthode se termine.
