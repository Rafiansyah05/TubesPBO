@echo off
set "JAVA_HOME=%JAVA_HOME%"
set "M2_HOME=%~dp0.maven\apache-maven-3.9.6"
set "PATH=%M2_HOME%\bin;%PATH%"
"%M2_HOME%\bin\mvn.cmd" %*
