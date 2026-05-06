$ErrorActionPreference = "Stop"

$projectRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Set-Location $projectRoot

Write-Host "[INFO] Running Maven tests..."
& .\mvnw.cmd test

if ($LASTEXITCODE -ne 0) {
	throw "Maven tests failed with exit code $LASTEXITCODE"
}

$reportDir = Join-Path $projectRoot "target\surefire-reports"
$docsDir = Join-Path $projectRoot "docs"
$reportFile = Join-Path $docsDir "test-report.md"
$pdfGenerator = Join-Path $projectRoot "scripts\md-report-to-pdf.py"
$pdfReportFile = Join-Path $docsDir "test-report.pdf"

$xmlReports = Get-ChildItem -Path $reportDir -Filter "TEST-*.xml" -ErrorAction SilentlyContinue
if (-not $xmlReports) {
	throw "No surefire XML reports found in $reportDir"
}

$totalTests = 0
$totalFailures = 0
$totalErrors = 0
$totalSkipped = 0
$totalTime = 0.0

$suiteRows = @()

foreach ($xmlFile in $xmlReports) {
	[xml]$xml = Get-Content -Path $xmlFile.FullName
	$suite = $xml.testsuite

	$tests = [int]$suite.tests
	$failures = [int]$suite.failures
	$errors = [int]$suite.errors
	$skipped = [int]$suite.skipped
	$time = [double]$suite.time
	$name = [string]$suite.name

	$totalTests += $tests
	$totalFailures += $failures
	$totalErrors += $errors
	$totalSkipped += $skipped
	$totalTime += $time

	$suiteRows += "| $name | $tests | $failures | $errors | $skipped | $([Math]::Round($time, 3)) |"
}

$status = if (($totalFailures + $totalErrors) -eq 0) { "SUCCES" } else { "ECHEC" }
$generatedAt = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
$conclusion = if ($status -eq "SUCCES") { "Le logiciel passe tous les tests automatiques disponibles." } else { "Le logiciel ne passe pas tous les tests. Des corrections sont necessaires." }

$content = @(
	"# Rapport de tests",
	"",
	"- Date: $generatedAt",
	"- Statut global: $status",
	"",
	"## Methode d'execution",
	"",
	"Scripts disponibles:",
	"",
	"- scripts/run-tests.ps1",
	"- scripts/run-tests.bat",
	"- scripts/run-tests.sh",
	"",
	"Commande utilisee pour ce rapport:",
	"",
	"- powershell -ExecutionPolicy Bypass -File scripts/run-tests.ps1",
	"",
	"## Resume",
	"",
	"| Metrique | Valeur |",
	"|---|---:|",
	"| Tests executes | $totalTests |",
	"| Echecs | $totalFailures |",
	"| Erreurs | $totalErrors |",
	"| Ignores | $totalSkipped |",
	"| Temps total (s) | $([Math]::Round($totalTime, 3)) |",
	"",
	"## Detail par suite",
	"",
	"| Suite | Tests | Echecs | Erreurs | Ignores | Temps (s) |",
	"|---|---:|---:|---:|---:|---:|"
) + $suiteRows + @(
	"",
	"## Emplacement des rapports bruts",
	"",
	"- target/surefire-reports",
	"",
	"## Conclusion",
	"",
	$conclusion
)

$content | Set-Content -Path $reportFile -Encoding UTF8

Write-Host "[INFO] Test report generated at: $reportFile"

if (Test-Path $pdfGenerator) {
	Write-Host "[INFO] Generating PDF test report..."
	python $pdfGenerator
	if ($LASTEXITCODE -ne 0) {
		throw "PDF generation failed with exit code $LASTEXITCODE"
	}
	Write-Host "[INFO] PDF test report generated at: $pdfReportFile"
} else {
	Write-Warning "PDF generator script not found: $pdfGenerator"
}
