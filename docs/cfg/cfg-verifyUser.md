# Graphe de Contrôle de Flux (CFG) pour `BankUserService.verifyUser`

```mermaid
graph TD
    A[Début] --> B[userRepository.findByEmail(email)];
    B --> C{Utilisateur trouvé ?};
    C -- Non --> D[throw ResourceNotFoundException];
    C -- Oui --> E{user.isVerified()};
    E -- Vrai --> F[Retourner user];
    E -- Faux --> G{user.getVerificationCode() != null && user.getVerificationCode().equals(code)};
    G -- Faux --> H[throw InvalidVerificationCodeException];
    G -- Vrai --> I[user.setVerified(true)];
    I --> J[user.setVerificationCode(null)];
    J --> K[userRepository.save(user)];
    K --> L[Retourner user sauvegardé];
    D --> M[Fin];
    F --> M;
    H --> M;
    L --> M;
```

## Description du Flux

1.  **A (Début)** : L'exécution de la méthode `verifyUser` commence.
2.  **B (Recherche)** : Le système recherche un utilisateur dans la base de données correspondant à l'e-mail fourni.
3.  **C (Condition)** : Le système vérifie si un utilisateur a été trouvé.
4.  **D (Exception)** : Si aucun utilisateur n'est trouvé (branche "Non"), une exception `ResourceNotFoundException` est levée, et le processus se termine.
5.  **E (Condition)** : Si un utilisateur est trouvé (branche "Oui"), le système vérifie si le compte de cet utilisateur est déjà marqué comme "vérifié".
6.  **F (Retour)** : Si le compte est déjà vérifié (branche "Vrai"), la méthode retourne directement l'objet `user` et se termine.
7.  **G (Condition)** : Si le compte n'est pas encore vérifié (branche "Faux"), le système compare le code fourni dans la requête avec le code de vérification stocké pour l'utilisateur.
8.  **H (Exception)** : Si les codes ne correspondent pas (branche "Faux"), une exception `InvalidVerificationCodeException` est levée, et le processus se termine.
9.  **I (Mise à jour)** : Si les codes correspondent (branche "Vrai"), le statut de l'utilisateur est mis à jour à `verified = true`.
10. **J (Nettoyage)** : Le code de vérification est effacé (`null`) pour qu'il ne puisse pas être réutilisé.
11. **K (Sauvegarde)** : Les modifications apportées à l'objet `user` sont sauvegardées en base de données.
12. **L (Retour)** : L'objet `user` mis à jour est retourné.
13. **M (Fin)** : L'exécution de la méthode se termine.
