# Getting Started with TBD Project

This guide will help you get up and running with the TBD project in under 10 minutes.

## What You'll Need

Before starting, ensure you have:

- ✅ **Java 11** or higher installed
- ✅ **Maven 3.6+** installed
- ✅ **Docker** and **Docker Compose** installed
- ✅ **IntelliJ IDEA** (recommended) or another IDE
- ✅ **Git** installed

## Quick Start (5 minutes)

### 1. Open in IntelliJ IDEA

1. **File → Open** and select the **tbd-project** folder.
2. Wait for Maven to import (root `pom.xml` includes `tbd-common` and `tbd-internal`).

**Detailed IntelliJ steps:** see **[INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)**.

### 2. Build the project

In IntelliJ, use the run configuration **Build All** (or run **Build tbd-common** then **Build tbd-internal**).  
First time: copy `tbd-internal/docker/.env.example` to `tbd-internal/docker/.env` if needed.

### 3. Start the application

- **Option A:** Add a **Docker Compose** run configuration for `tbd-internal/docker/dev.docker-compose.yml` and run it (see [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)).
- **Option B:** In a terminal: `cd tbd-internal/docker && docker-compose -f dev.docker-compose.yml up`

### 4. Access the application

Open your browser:
- **Web UI**: http://localhost:9080/internal
- **Health Check**: http://localhost:9080/internal/api/health

🎉 **You're done!** The application is running.

## What's Included

### tbd-common (Shared Libraries)

Contains reusable components:
- **networkvalidation** - Network utilities
- **common-shared** - DTOs and utilities
- **common-services** - Business services
- **tbd-common-bom** - Version management

### tbd-internal (Application)

Full-stack Java EE application:
- **Spring MVC** REST APIs
- **Struts 2** web pages
- **Oracle** database connectivity
- **WebSphere Liberty** runtime

## Development Workflow

### Making Code Changes

**Option 1: Hot Reload (Recommended)**

```bash
# Terminal 1: Keep server running
cd tbd-internal/docker
docker-compose -f dev.docker-compose.yml up

# Terminal 2: Make changes, then rebuild
cd tbd-internal
# Edit your code...
mvn package -DskipTests
# Liberty auto-reloads!
```

**Option 2: Full Restart**

```bash
# Stop the server (Ctrl+C)
# Make your changes
mvn clean package
cd docker
docker-compose -f dev.docker-compose.yml up
```

### Debugging in IntelliJ

1. **Start DEV mode**:
   ```bash
   cd tbd-internal/docker
   docker-compose -f dev.docker-compose.yml up
   ```

2. **In IntelliJ**: Select the **Remote Debug Liberty** run configuration and click **Debug** (already configured for localhost:7777).

3. **Set breakpoints** and trigger your code!

## Project Structure Overview

```
tbd-project/
│
├── tbd-common/              # Shared libraries (build first)
│   ├── networkvalidation/   # Network utilities
│   ├── common-shared/       # DTOs, utilities
│   ├── common-services/     # Business services
│   └── tbd-common-bom/      # BOM for version management
│
├── tbd-internal/            # Internal application
│   ├── tbd-internal-war/    # Web application (Spring + Struts)
│   ├── tbd-internal-ear/    # EAR packaging
│   ├── docker/              # Docker configs (DEV & PROD)
│   ├── liberty/             # Liberty server.xml
│
├── README.md                # Master documentation
├── GETTING_STARTED.md       # This file
├── INTELLIJ_SETUP.md        # IntelliJ run configs and workflow
└── pom.xml                  # Root Maven aggregator (for IntelliJ)
```

## Common Tasks

### Build Everything

```bash
# Build common libraries
cd tbd-common
mvn clean install

# Build application
cd ../tbd-internal
mvn clean package
```

### Run Tests

```bash
# Run all tests
cd tbd-internal
mvn test

# Run specific test
mvn test -Dtest=InternalServiceTest
```

### View Logs

```bash
# DEV environment
cd tbd-internal/docker
docker-compose -f dev.docker-compose.yml logs -f liberty-dev

# Or follow live logs
docker exec -it tbd-internal-dev tail -f /logs/messages.log
```

### Stop the Application

```bash
# In the terminal running docker-compose:
Ctrl+C

# Or from another terminal:
cd tbd-internal/docker
docker-compose -f dev.docker-compose.yml down
```

### Clean Restart

```bash
cd tbd-internal/docker

# Stop and remove volumes
docker-compose -f dev.docker-compose.yml down -v

# Start fresh
docker-compose -f dev.docker-compose.yml up
```

## Testing the Application

### Web Interface

Navigate to: http://localhost:9080/internal

You'll see:
- Application status
- Database connection test
- Links to API endpoints

### REST APIs

Test with curl:

```bash
# Health check
curl http://localhost:9080/internal/api/health

# Database test
curl http://localhost:9080/internal/api/db-test

# IP validation
curl "http://localhost:9080/internal/api/validate-ip?ip=192.168.1.1"

# Sample data
curl http://localhost:9080/internal/api/sample-data
```

## Database

### Local Oracle Database

The DEV environment includes an Oracle XE database:

- **Host**: localhost
- **Port**: 1521
- **SID**: XE
- **User**: tbd_user
- **Password**: (from .env file)

### Connect to Database

```bash
# Using Docker
docker exec -it tbd-oracle-dev sqlplus tbd_user/change_me@XE

# Using SQL*Plus (if installed locally)
sqlplus tbd_user/change_me@localhost:1521/XE
```

### Sample Queries

```sql
-- Check connection
SELECT 'Connected!' FROM DUAL;

-- View sample table
SELECT * FROM TBD_STATUS;

-- Check current time
SELECT SYSDATE FROM DUAL;
```

## Customization

### Change Database Connection

Edit `tbd-internal/docker/.env`:

```bash
DB_URL=jdbc:oracle:thin:@your-db-host:1521:YOUR_SID
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### Change Ports

Edit `tbd-internal/docker/dev.docker-compose.yml`:

```yaml
ports:
  - "19080:9080"  # Changed from 9080
  - "19443:9443"  # Changed from 9443
  - "17777:7777"  # Changed from 7777
```

### Add Dependencies

1. Add to `tbd-common/pom.xml` (if shared)
2. Or add to `tbd-internal-war/pom.xml` (if app-specific)
3. Rebuild:
   ```bash
   mvn clean package
   ```

## IntelliJ IDEA Setup

**Full guide:** **[INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)**

- **Open** the **tbd-project** folder (not just tbd-internal) so both modules are loaded.
- Use the included run configs: **Build All**, **Build tbd-common**, **Build tbd-internal**, **Remote Debug Liberty**.
- Add a **Docker Compose** run config for `tbd-internal/docker/dev.docker-compose.yml` to start DEV from the IDE.

### Recommended plugins

- **Docker** – manage containers and Compose from the IDE
- **Maven Helper** – dependency analysis

## Troubleshooting

### "Maven can't find tbd-common modules"

**Solution**: Build tbd-common first

```bash
cd tbd-common
mvn clean install
```

### "Port 9080 already in use"

**Solution**: Change port or stop conflicting service

```bash
# Find what's using the port
lsof -i :9080

# Or change port in docker-compose.yml
```

### "Database connection failed"

**Solution**: Check database is running

```bash
cd tbd-internal/docker
docker-compose ps

# If oracle-db is not running:
docker-compose up oracle-db
```

### "Liberty won't start"

**Solution**: Check logs

```bash
docker-compose -f dev.docker-compose.yml logs liberty-dev

# Look for errors in messages.log
docker exec -it tbd-internal-dev cat /logs/messages.log
```

### "Application builds but 404 error"

**Solution**: Check context root

The application runs at: `/internal` not `/`

Correct URL: http://localhost:9080/internal

## Next Steps

### Production Deployment

See `tbd-internal/README.md` for:
- Building production Docker image
- Deploying to AWS ECS
- CI/CD pipeline setup

### Create New Application

To create `tbd-external`:

```bash
cp -r tbd-internal tbd-external
cd tbd-external
# Update pom.xml files
# Change artifactId and application names
# Update Docker container names
```

### Framework Upgrade

See master `README.md` for:
- Upgrading Spring/Struts
- Testing strategy
- Migration guide

## Getting Help

### Documentation

- **Master README**: `README.md`
- **Application README**: `tbd-internal/README.md`
- **Common README**: `tbd-common/README.md`

### Resources

- **Liberty Docs**: https://openliberty.io/docs/
- **Spring Docs**: https://spring.io/projects/spring-framework
- **Struts Docs**: https://struts.apache.org/

### Support

Open an issue on GitHub with:
- What you were trying to do
- What went wrong
- Error messages
- Steps to reproduce

## Summary

You should now have:

✅ Built the entire project  
✅ Started the application  
✅ Accessed it in your browser  
✅ Understand the project structure  
✅ Know how to make changes  

**Welcome to the TBD Project! Happy coding! 🚀**
