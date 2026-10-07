#!/bin/bash
# run.sh — Compile and run the Electricity Billing Management System
# Usage: bash run.sh

JAVA_HOME="/Users/mansishekarshetty/Library/Java/JavaVirtualMachines/openjdk-26.0.2/Contents/Home"
JAVAC="$JAVA_HOME/bin/javac"
JAVA="$JAVA_HOME/bin/java"

echo "Compiling Electricity Billing Management System..."
mkdir -p out
$JAVAC -d out model/*.java exception/*.java datastructure/*.java service/*.java gui/*.java Main.java
if [ $? -eq 0 ]; then
    echo "Compilation successful! Launching application..."
    $JAVA -cp out Main
else
    echo "Compilation failed. Check errors above."
fi
