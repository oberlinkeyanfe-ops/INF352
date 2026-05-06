# Rapport de tests

- Date: 2026-04-21 22:32:05
- Statut global: SUCCES

## Methode d'execution

Scripts disponibles:

- scripts/run-tests.ps1
- scripts/run-tests.bat
- scripts/run-tests.sh

Commande utilisee pour ce rapport:

- powershell -ExecutionPolicy Bypass -File scripts/run-tests.ps1

## Resume

| Metrique | Valeur |
|---|---:|
| Tests executes | 4 |
| Echecs | 0 |
| Erreurs | 0 |
| Ignores | 0 |
| Temps total (s) | 5.72 |

## Detail par suite

| Suite | Tests | Echecs | Erreurs | Ignores | Temps (s) |
|---|---:|---:|---:|---:|---:|
| com.inf352.bankapi.controller.AccountControllerTest | 2 | 0 | 0 | 0 | 4.527 |
| com.inf352.bankapi.controller.AuthControllerTest | 1 | 0 | 0 | 0 | 0.741 |
| com.inf352.bankapi.controller.TransactionControllerTest | 1 | 0 | 0 | 0 | 0.452 |

## Emplacement des rapports bruts

- target/surefire-reports

## Conclusion

Le logiciel passe tous les tests automatiques disponibles.
