#!/bin/bash

# Quick Start Script for TBD External Application
# This script helps you get started quickly

set -e

echo "================================================"
echo "TBD External Application - Quick Start"
echo "================================================"
echo ""

# Check prerequisites
echo "Checking prerequisites..."

if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 11 or higher."
    exit 1
fi
echo "✅ Java found: $(java -version 2>&1 | head -n 1)"

if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven 3.6+."
    exit 1
fi
echo "✅ Maven found: $(mvn -version | head -n 1)"

if ! command -v docker &> /dev/null; then
    echo "❌ Docker not found. Please install Docker."
    exit 1
fi
echo "✅ Docker found: $(docker --version)"

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose not found. Please install Docker Compose."
    exit 1
fi
echo "✅ Docker Compose found: $(docker-compose --version)"

echo ""
echo "================================================"
echo "Choose your startup mode:"
echo "================================================"
echo "1. DEV mode  - Fast development with hot reload"
echo "2. PROD mode - Production-like environment"
echo "3. Build only - Just build the application"
echo ""
read -p "Enter choice (1-3): " choice

case $choice in
    1)
        echo ""
        echo "Starting in DEV mode..."
        echo ""
        
        # Check for .env file
        if [ ! -f "docker/.env" ]; then
            echo "Creating .env file from example..."
            cp docker/.env.example docker/.env
            echo "⚠️  Please edit docker/.env with your database credentials"
            read -p "Press Enter to continue after editing .env file..."
        fi
        
        # Build application
        echo "Building application..."
        mvn clean package -DskipTests
        
        # Start DEV environment
        echo ""
        echo "Starting DEV environment..."
        echo "Application will be available at: http://localhost:9080/external"
        echo "Debug port: 7777"
        echo ""
        cd docker
        docker-compose -f dev.docker-compose.yml up
        ;;
        
    2)
        echo ""
        echo "Starting in PROD mode..."
        echo ""
        
        # Check for .env file
        if [ ! -f "docker/.env" ]; then
            echo "Creating .env file from example..."
            cp docker/.env.example docker/.env
            echo "⚠️  Please edit docker/.env with your database credentials"
            read -p "Press Enter to continue after editing .env file..."
        fi
        
        # Build application and Docker image
        echo "Building application and Docker image..."
        cd docker
        docker-compose -f prod.docker-compose.yml build
        
        # Start PROD environment
        echo ""
        echo "Starting PROD environment..."
        echo "Application will be available at: http://localhost:9080/external"
        echo ""
        docker-compose -f prod.docker-compose.yml up
        ;;
        
    3)
        echo ""
        echo "Building application..."
        mvn clean package
        echo ""
        echo "✅ Build complete!"
        echo ""
        echo "WAR file: tbd-external-war/target/tbd-external.war"
        echo "EAR file: tbd-external-ear/target/tbd-external.ear"
        echo ""
        echo "To run:"
        echo "  DEV:  cd docker && docker-compose -f dev.docker-compose.yml up"
        echo "  PROD: cd docker && docker-compose -f prod.docker-compose.yml up"
        ;;
        
    *)
        echo "Invalid choice. Exiting."
        exit 1
        ;;
esac
