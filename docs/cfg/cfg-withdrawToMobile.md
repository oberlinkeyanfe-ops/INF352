# Graphe de Contrôle de Flux (CFG) pour `BankTransactionService.withdrawToMobile`

```mermaid
graph TD
    A[Début] --> B[bankAccountService.getAccount(fromAccountNumber)];
    B --> C{Solde suffisant ? (before < amount)};
    C -- Non --> D[throw InsufficientFundsException];
    C -- Oui --> E{Montant > 500000 ?};
    E -- Vrai --> F[throw IllegalArgumentException];
    E -- Faux --> G[Calculer nouveau solde];
    G --> H[Créer transaction MOBILE_WITHDRAWAL];
    H --> I[Ajouter description (numéro mobile)];
    I --> J[Sauvegarder transaction];
    J --> K[Retourner transaction];
    K --> L[Fin];
    D --> L;
    F --> L;
```

## Description du Flux

1.  **A (Début)** : L'exécution de la méthode `withdrawToMobile` commence.
2.  **B (Recherche)** : Le système récupère les informations du compte bancaire source.
3.  **C (Condition)** : Le système vérifie si le solde du compte est suffisant pour le retrait.
4.  **D (Exception)** : Si le solde est insuffisant (branche "Non"), une `InsufficientFundsException` est levée.
5.  **E (Condition)** : Si le solde est suffisant (branche "Oui"), le système vérifie si le montant demandé dépasse la limite de 500 000 pour les retraits mobiles.
6.  **F (Exception)** : Si le montant dépasse la limite (branche "Vrai"), une `IllegalArgumentException` est levée.
7.  **G (Calcul)** : Si le montant est dans la limite (branche "Faux"), le nouveau solde du compte est calculé.
8.  **H (Création)** : Une nouvelle transaction de type `MOBILE_WITHDRAWAL` est créée.
9.  **I (Description)** : Une description, incluant le numéro de téléphone mobile du destinataire, est ajoutée à la transaction.
10. **J (Sauvegarde)** : La transaction est sauvegardée en base de données.
11. **K (Retour)** : La transaction sauvegardée est retournée.
12. **L (Fin)** : L'exécution de la méthode se termine.
