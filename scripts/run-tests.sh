#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."

echo "[INFO] Running Maven tests..."
./mvnw test

REPORT_DIR="target/surefire-reports"
REPORT_FILE="docs/test-report.md"
PDF_REPORT_FILE="docs/test-report.pdf"
PDF_GENERATOR="scripts/md-report-to-pdf.py"

if ! ls "$REPORT_DIR"/TEST-*.xml >/dev/null 2>&1; then
	echo "No surefire XML reports found in $REPORT_DIR" >&2
	exit 1
fi

TOTAL_TESTS=0
TOTAL_FAILURES=0
TOTAL_ERRORS=0
TOTAL_SKIPPED=0
TOTAL_TIME="0"

SUITE_ROWS=""

for f in "$REPORT_DIR"/TEST-*.xml; do
	tests=$(grep -o 'tests="[0-9]*"' "$f" | head -n1 | tr -dc '0-9')
	failures=$(grep -o 'failures="[0-9]*"' "$f" | head -n1 | tr -dc '0-9')
	errors=$(grep -o 'errors="[0-9]*"' "$f" | head -n1 | tr -dc '0-9')
	skipped=$(grep -o 'skipped="[0-9]*"' "$f" | head -n1 | tr -dc '0-9')
	time=$(grep -o 'time="[0-9.]*"' "$f" | head -n1 | sed 's/time="\([0-9.]*\)"/\1/')
	suite=$(grep -o 'name="[^"]*"' "$f" | head -n1 | sed 's/name="\([^"]*\)"/\1/')

	tests=${tests:-0}
	failures=${failures:-0}
	errors=${errors:-0}
	skipped=${skipped:-0}
	time=${time:-0}

	TOTAL_TESTS=$((TOTAL_TESTS + tests))
	TOTAL_FAILURES=$((TOTAL_FAILURES + failures))
	TOTAL_ERRORS=$((TOTAL_ERRORS + errors))
	TOTAL_SKIPPED=$((TOTAL_SKIPPED + skipped))
	TOTAL_TIME=$(awk "BEGIN { printf \"%.3f\", $TOTAL_TIME + $time }")

	SUITE_ROWS+="| $suite | $tests | $failures | $errors | $skipped | $time |\n"
done

STATUS="SUCCES"
if [ $((TOTAL_FAILURES + TOTAL_ERRORS)) -ne 0 ]; then
	STATUS="ECHEC"
fi

CONCLUSION="Le logiciel passe tous les tests automatiques disponibles."
if [ "$STATUS" = "ECHEC" ]; then
	CONCLUSION="Le logiciel ne passe pas tous les tests. Des corrections sont necessaires."
fi

GENERATED_AT=$(date '+%Y-%m-%d %H:%M:%S')

cat > "$REPORT_FILE" <<EOF
# Rapport de tests

- Date: $GENERATED_AT
- Statut global: $STATUS

## Methode d'execution

Scripts disponibles:

- scripts/run-tests.ps1
- scripts/run-tests.bat
- scripts/run-tests.sh

Commande utilisee pour ce rapport:

\`\`\`bash
./scripts/run-tests.sh
\`\`\`

## Resume

| Metrique | Valeur |
|---|---:|
| Tests executes | $TOTAL_TESTS |
| Echecs | $TOTAL_FAILURES |
| Erreurs | $TOTAL_ERRORS |
| Ignores | $TOTAL_SKIPPED |
| Temps total (s) | $TOTAL_TIME |

## Detail par suite

| Suite | Tests | Echecs | Erreurs | Ignores | Temps (s) |
|---|---:|---:|---:|---:|---:|
$(printf "%b" "$SUITE_ROWS")

## Emplacement des rapports bruts

- target/surefire-reports

## Conclusion

$CONCLUSION
EOF

echo "[INFO] Test report generated at: $REPORT_FILE"

if [ -f "$PDF_GENERATOR" ]; then
	echo "[INFO] Generating PDF test report..."
	python "$PDF_GENERATOR"
	echo "[INFO] PDF test report generated at: $PDF_REPORT_FILE"
else
	echo "[WARN] PDF generator script not found: $PDF_GENERATOR"
fi
