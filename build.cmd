@echo off
REM Script untuk build project dengan Maven
cd /d "%~dp0"
set "PATH=%CD%\.maven\apache-maven-3.9.6\bin;%PATH%"
echo.
echo Building SiAlumni project...
echo.
call mvn.cmd clean package -DskipTests
echo.
if %ERRORLEVEL% EQU 0 (
    echo Build successful! WAR file created at: target\SiAlumni-1.0-SNAPSHOT.war
) else (
    echo Build failed!
)
pause
