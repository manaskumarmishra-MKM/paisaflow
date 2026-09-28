#!/bin/bash
# PaisaFlow - Full Project Setup & Build

# Get the root directory
ROOT_DIR="/Users/manaskumarmishra/paisaflow"
cd "$ROOT_DIR"

echo "--- 1. Starting Backend (FastAPI) ---"
cd "$ROOT_DIR/backend"
# Setup python environment if needed
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
# Run backend in the background
uvicorn app.main:app --host 0.0.0.0 --port 8000 &
BACKEND_PID=$!
echo "Backend started in background (PID: $BACKEND_PID)"

echo "--- 2. Building Android App (APK) ---"
cd "$ROOT_DIR/android"
# Ensure the gradle wrapper is executable
chmod +x gradlew
# Build the APK (This "combines" all your Kotlin/Java/XML files)
./gradlew assembleDebug

echo "------------------------------------------------"
echo "BUILD COMPLETE"
echo "Backend is running at: http://localhost:8000"
echo "Android APK location: $ROOT_DIR/android/app/build/outputs/apk/debug/app-debug.apk"
echo "To stop the backend, run: kill $BACKEND_PID"
