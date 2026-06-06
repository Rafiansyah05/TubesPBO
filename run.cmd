@echo off
REM Script untuk menjalankan SiAlumni dengan Tomcat7
cd /d "%~dp0"
set "PATH=%CD%\.maven\apache-maven-3.9.6\bin;%PATH%"
echo.
echo ========================================
echo   SiAlumni - Sistem Informasi Alumni
echo ========================================
echo.
echo Starting application on http://localhost:8080
echo Press Ctrl+C to stop the server
echo.
call mvn.cmd tomcat7:run
pause
