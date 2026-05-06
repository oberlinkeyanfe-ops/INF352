# Graphe de Contrôle de Flux (CFG) pour `BankTransactionService.transfer`

```mermaid
graph TD
    A[Début] --> B{fromAccountNumber.equals(toAccountNumber)};
    B -- Vrai --> C[throw IllegalArgumentException];
    B -- Faux --> D[bankAccountService.getAccount(fromAccountNumber)];
    D --> E{Solde suffisant ? (fromBefore < amount)};
    E -- Non --> F[throw InsufficientFundsException];
    E -- Oui --> G{Montant > Limite de virement ?};
    G -- Vrai --> H[throw IllegalArgumentException];
    G -- Faux --> I[Calculer nouveau solde source];
    I --> J[Créer et sauvegarder transaction de débit];
    J --> K[bankAccountService.getAccount(toAccountNumber)];
    K --> L[Calculer nouveau solde destination];
    L --> M[Créer et sauvegarder transaction de crédit];
    M --> N[Fin];
    C --> N;
    F --> N;
    H --> N;
```

## Description du Flux

1.  **A (Début)** : L'exécution de la méthode `transfer` commence.
2.  **B (Condition)** : Le système vérifie si le compte source est identique au compte de destination.
3.  **C (Exception)** : Si les comptes sont identiques (branche "Vrai"), une `IllegalArgumentException` est levée.
4.  **D (Recherche)** : Si les comptes sont différents (branche "Faux"), le système récupère les informations du compte source.
5.  **E (Condition)** : Le système vérifie si le solde du compte source est suffisant pour couvrir le montant du virement.
6.  **F (Exception)** : Si le solde est insuffisant (branche "Non"), une `InsufficientFundsException` est levée.
7.  **G (Condition)** : Si le solde est suffisant (branche "Oui"), le système vérifie si le montant du virement dépasse la limite autorisée par la banque du compte source.
8.  **H (Exception)** : Si la limite est dépassée (branche "Vrai"), une `IllegalArgumentException` est levée.
9.  **I (Débit)** : Si la limite n'est pas dépassée (branche "Faux"), le nouveau solde du compte source est calculé (solde - montant).
10. **J (Transaction Débit)** : Une transaction de type `TRANSFER_DEBIT` est créée et sauvegardée pour le compte source.
11. **K (Recherche)** : Le système récupère les informations du compte de destination.
12. **L (Crédit)** : Le nouveau solde du compte de destination est calculé (solde + montant).
13. **M (Transaction Crédit)** : Une transaction de type `TRANSFER_CREDIT` est créée et sauvegardée pour le compte de destination.
14. **N (Fin)** : L'exécution de la méthode se termine.
