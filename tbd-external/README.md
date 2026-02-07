# TBD External Application

Enterprise Java EE application running on WebSphere Liberty with Spring Framework and Struts 2.

## Architecture

- **Runtime**: WebSphere Liberty 24.0.0.12
- **Frameworks**: Spring 5.3.39, Struts 6.8.0, Jackson 2.18.2
- **Database**: Oracle Database (via JDBC)
- **Build**: Maven multi-module (WAR + EAR)
- **Deployment**: Local Docker (dev and prod Compose)

## Project Structure

```
tbd-external/
├── tbd-external-war/          # Web application module
│   ├── src/main/
│   │   ├── java/              # Java source code
│   │   ├── resources/         # Configuration files
│   │   └── webapp/            # Web resources (JSP, etc.)
│   └── pom.xml
├── tbd-external-ear/          # EAR packaging module
│   └── pom.xml
├── docker/                     # Docker configuration
│   ├── Dockerfile             # Production image
│   ├── dev.docker-compose.yml # Development environment
│   ├── prod.docker-compose.yml# Production-like environment
│   └── init-scripts/          # Database initialization
├── liberty/                    # Liberty server configuration
│   └── server.xml
├── pom.xml                     # Parent POM
└── README.md
```

## Prerequisites

- **Java 8** or higher
- **Maven 3.6+**
- **Docker** and **Docker Compose**
- **IntelliJ IDEA** (recommended)
- **Oracle JDBC Driver** (ojdbc8.jar)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd tbd-external
```

### 2. Download Oracle JDBC Driver

Download `ojdbc8.jar` from Oracle and place it in `docker/ojdbc8.jar`

Or Maven will download it automatically during build.

### 3. Configure Environment

Copy the example environment file:

```bash
cd docker
cp .env.example .env
```

Edit `.env` with your database credentials.

### 4. Build the Application

```bash
mvn clean package
```

This creates:
- `tbd-external-war/target/tbd-external.war`
- `tbd-external-ear/target/tbd-external.ear`

## Development Workflow

### Option 1: DEV Docker Compose (Recommended)

**Fast edit-build-debug loop with volume mounting:**

```bash
# Build the EAR
mvn clean package -DskipTests

# Start DEV environment
cd docker
docker-compose -f dev.docker-compose.yml up

# Application runs on:
# http://localhost:9080/external
# Debug port: 7777
```

**Hot Reload Workflow:**
1. Make code changes
2. Run `mvn package` (or use IntelliJ build)
3. Liberty automatically detects the updated EAR
4. Test your changes at http://localhost:9080/external

**View Logs:**
```bash
docker-compose -f dev.docker-compose.yml logs -f liberty-dev
```

### Option 2: PROD Docker Compose (Local Testing)

**Test the production image locally:**

```bash
# Build production Docker image
cd docker
docker-compose -f prod.docker-compose.yml build

# Run production-like environment
docker-compose -f prod.docker-compose.yml up

# Application runs on:
# http://localhost:9080/external
```

This builds an **immutable image** with the EAR baked in for local production-like runs.

### IntelliJ IDEA Setup

#### 1. Import Project

- Open IntelliJ IDEA
- File → Open → Select `tbd-external` directory
- IntelliJ detects Maven project automatically

#### 2. Create Run Configurations

**DEV Configuration:**

1. Run → Edit Configurations → + → Shell Script
2. Name: `DEV - Start Liberty`
3. Script text:
   ```bash
   mvn clean package -DskipTests && cd docker && docker-compose -f dev.docker-compose.yml up
   ```

**PROD Configuration:**

1. Run → Edit Configurations → + → Shell Script
2. Name: `PROD - Build and Run`
3. Script text:
   ```bash
   cd docker && docker-compose -f prod.docker-compose.yml up --build
   ```

#### 3. Remote Debugging

1. Run → Edit Configurations → + → Remote JVM Debug
2. Name: `Debug Liberty`
3. Host: `localhost`
4. Port: `7777`
5. Use module classpath: `tbd-external-war`

**To Debug:**
1. Start DEV compose
2. Set breakpoints in IntelliJ
3. Run the "Debug Liberty" configuration
4. Trigger your code via browser

## Available Endpoints

### Web Pages (Struts)
- `http://localhost:9080/external/` - Home page
- `http://localhost:9080/external/home.action` - Main application page

### REST APIs (Spring)
- `GET /external/api/health` - Health check
- `GET /external/api/db-test` - Database connectivity test
- `GET /external/api/validate-ip?ip=<ip>` - IP validation
- `GET /external/api/sample-data` - Sample database query

### Examples

```bash
# Health check
curl http://localhost:9080/external/api/health

# Test database
curl http://localhost:9080/external/api/db-test

# Validate IP
curl "http://localhost:9080/external/api/validate-ip?ip=192.168.1.1"
```

## Database Configuration

The application connects to Oracle Database using environment variables:

```bash
DB_URL=jdbc:oracle:thin:@hostname:1521:SID
DB_USERNAME=tbd_user
DB_PASSWORD=your_password
DB_POOL_SIZE=10
```

### Local Oracle Database

The Docker Compose files include an Oracle XE container for development:

- **Host**: localhost
- **Port**: 1521
- **SID**: XE
- **User**: tbd_user
- **Password**: (from .env file)

## Local Docker

**DEV** (hot reload, debug port 7777):

```bash
mvn clean package -DskipTests
cd docker && docker-compose -f dev.docker-compose.yml up
```

**PROD** (baked image, production-like):

```bash
cd docker && docker-compose -f prod.docker-compose.yml up --build
```

Application: http://localhost:9080/external

## Framework Upgrade

To upgrade Spring or Struts:

### 1. Update tbd-common BOM

```bash
cd ../tbd-common
# Edit pom.xml - update framework versions
mvn clean install
```

### 2. Update Application BOM Reference

```bash
cd ../tbd-external
# Edit pom.xml
<tbd-common.version>2.0.0</tbd-common.version>

mvn clean package
```

### 3. Test Thoroughly

```bash
# Run tests
mvn test

# Start DEV environment
cd docker
docker-compose -f dev.docker-compose.yml up

# Manual testing
# Navigate to http://localhost:9080/external
```

## Monitoring and Logging

### Application Logs

**DEV Environment:**
```bash
docker-compose -f dev.docker-compose.yml logs -f liberty-dev
```

**PROD Environment:**
```bash
docker-compose -f prod.docker-compose.yml logs -f liberty-prod
```

### Liberty Logs Location

Inside container: `/logs/`

Mapped volumes:
- DEV: `liberty-dev-logs`
- PROD: `liberty-prod-logs`

### View Logs

```bash
# Access log volume
docker run --rm -v liberty-dev-logs:/logs alpine ls -la /logs

# Tail messages.log
docker exec -it tbd-external-dev tail -f /logs/messages.log
```

## Troubleshooting

### Application Won't Start

1. Check Docker logs:
   ```bash
   docker-compose -f dev.docker-compose.yml logs liberty-dev
   ```

2. Verify database is running:
   ```bash
   docker-compose -f dev.docker-compose.yml ps
   ```

3. Check database connectivity:
   ```bash
   docker exec -it tbd-oracle-dev sqlplus tbd_user/change_me@XE
   ```

### Database Connection Issues

1. Verify environment variables in `.env` file
2. Check network connectivity:
   ```bash
   docker exec -it tbd-external-dev ping oracle-db
   ```

3. Test JDBC connection manually

### Build Failures

1. Clean Maven cache:
   ```bash
   mvn clean -U
   rm -rf ~/.m2/repository/com/yourcompany/tbd
   ```

2. Rebuild tbd-common:
   ```bash
   cd ../tbd-common
   mvn clean install
   ```

### Port Conflicts

If ports are already in use, modify `docker-compose.yml`:

```yaml
ports:
  - "19080:9080"  # Changed from 9080
  - "19443:9443"  # Changed from 9443
```

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=InternalServiceTest
```

### Integration Tests

```bash
# Start test environment
docker-compose -f dev.docker-compose.yml up -d

# Run integration tests
mvn verify

# Stop environment
docker-compose -f dev.docker-compose.yml down
```

## Performance Tuning

### JVM Options

Edit `liberty/server.xml`:

```xml
<javaPermGen size="512m" maxSize="1024m" />
```

### Connection Pool

Edit `liberty/server.xml`:

```xml
<connectionManager maxPoolSize="50" 
                  minPoolSize="10"
                  connectionTimeout="30s"/>
```

### Liberty Tuning

```xml
<httpEndpoint id="defaultHttpEndpoint"
              host="*"
              httpPort="9080">
    <tcpOptions soReuseAddr="true"
                keepAlive="true"/>
    <httpOptions maxKeepAliveRequests="1000"/>
</httpEndpoint>
```

## Contributing

1. Create feature branch from `main`
2. Make changes and test locally
3. Commit with descriptive messages
4. Push and create merge request
5. Wait for CI/CD pipeline to pass
6. Request code review

## Support

- **Documentation**: See `/docs` folder
- **Issues**: GitHub Issues

## License

Proprietary - Internal Use Only
