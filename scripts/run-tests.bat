@echo off
setlocal
echo [INFO] Running JUnit tests from src/test/java and generating Markdown/PDF report...
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0run-tests.ps1"
exit /b %ERRORLEVEL%
