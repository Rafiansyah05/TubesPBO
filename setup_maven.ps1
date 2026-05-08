$mavenVersion = "3.9.6"
$mavenZip = "apache-maven-$mavenVersion-bin.zip"
$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/$mavenZip"
$targetDir = "$PSScriptRoot\.maven"

if (-not (Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir -Force
}

$zipPath = "$targetDir\$mavenZip"
Write-Host "Downloading Maven $mavenVersion..."
Invoke-WebRequest -Uri $mavenUrl -OutFile $zipPath

Write-Host "Extracting Maven..."
Expand-Archive -Path $zipPath -DestinationPath $targetDir -Force

$mavenBin = Get-ChildItem -Path $targetDir -Filter "apache-maven-*" -Directory | Select-Object -First 1
$binPath = "$($mavenBin.FullName)\bin"

# Create a simple mvnw.cmd for the user
$mvnwContent = @"
@echo off
set "JAVA_HOME=%JAVA_HOME%"
set "M2_HOME=%~dp0.maven\apache-maven-3.9.6"
set "PATH=%M2_HOME%\bin;%PATH%"
"%M2_HOME%\bin\mvn.cmd" %*
"@

$mvnwContent | Out-File -FilePath "$PSScriptRoot\mvnw.cmd" -Encoding ascii

Write-Host "Maven setup complete. You can now run: .\mvnw tomcat7:run"
Remove-Item $zipPath
