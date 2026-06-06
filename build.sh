#!/bin/bash
# Script untuk build project dengan Maven (Git Bash/Unix Shell)

cd "$(dirname "$0")"

echo ""
echo "Building SiAlumni project..."
echo ""

./.maven/apache-maven-3.9.6/bin/mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful! WAR file created at: target/SiAlumni-1.0-SNAPSHOT.war"
else
    echo ""
    echo "Build failed!"
fi
