# Rapport de tests

## Périmètre
- Contrôleur utilisateurs
- Contrôleur authentification par token
- Contrôleur comptes
- Contrôleur transactions

## Exécution
Lancer les tests avec :

```powershell
.\scripts\run-tests.ps1
```

ou :

```bash
./scripts/run-tests.sh
```

## Résultats attendus
- Les tests unitaires et web MVC doivent passer.
- Les rapports Maven sont générés dans `target/surefire-reports/`.

## Remarques
- Le projet utilise un token API de type Bearer pour protéger les endpoints sensibles.
- Le endpoint `POST /api/users` reste public pour permettre l’initialisation du premier utilisateur.
