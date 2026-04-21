@echo off
cd /d %~dp0\..
call mvnw.cmd test
