#!/bin/bash
# Script untuk menjalankan SiAlumni dengan Tomcat7 (Git Bash/Unix Shell)

cd "$(dirname "$0")"

echo ""
echo "========================================"
echo "  SiAlumni - Sistem Informasi Alumni"
echo "========================================"
echo ""
echo "Starting application on http://localhost:8080"
echo "Press Ctrl+C to stop the server"
echo ""

./.maven/apache-maven-3.9.6/bin/mvn tomcat7:run
