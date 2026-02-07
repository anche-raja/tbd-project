# TBD Project - Complete Application Stack

This repository contains the complete TBD (Case Management Core) application stack with common libraries and multiple applications.

## Repository Structure

```
tbd-project/
├── tbd-common/              # Shared libraries and BOM
│   ├── tbd-common-bom/      # Bill of Materials
│   ├── networkvalidation/   # Network validation utilities
│   ├── common-shared/       # Shared DTOs and utilities
│   └── common-services/     # Common business services
│
└── tbd-internal/            # Internal application
    ├── tbd-internal-war/    # Web application
    ├── tbd-internal-ear/    # EAR packaging
    ├── docker/              # Docker configuration
    └── liberty/             # Liberty server config
```

## Technology Stack

### Common Libraries (tbd-common)
- **Spring Framework**: 5.3.30
- **Struts 2**: 2.5.32
- **Hibernate**: 5.6.15.Final
- **Jackson**: 2.15.2
- **Java**: 11

### Applications
- **Runtime**: WebSphere Liberty 24.0.0.12
- **Database**: Oracle Database (JDBC)
- **Build**: Maven 3.9+
- **Deployment**: Docker + AWS ECS

## Quick Start

### 1. Build Common Libraries

```bash
cd tbd-common
mvn clean install
```

This publishes the common modules to your local Maven repository.

### 2. Build and Run Application

**IntelliJ (recommended):** Open the `tbd-project` folder in IntelliJ, run **Build All**, then start DEV via a Docker Compose run config for `tbd-internal/docker/dev.docker-compose.yml`. See **[INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)**.

**Terminal:**
```bash
cd tbd-internal
mvn clean package -DskipTests
cd docker
docker-compose -f dev.docker-compose.yml up
```

### 3. Access Application

- **Web UI**: http://localhost:9080/internal
- **Health Check**: http://localhost:9080/internal/api/health
- **Debug Port**: 7777

## Development Workflow

### Standard Development

1. **Make changes** to common libraries or application
2. **Build**:
   ```bash
   # If common changed
   cd tbd-common && mvn clean install
   
   # Build application
   cd tbd-internal && mvn clean package -DskipTests
   ```
3. **Test** in DEV mode:
   ```bash
   cd docker
   docker-compose -f dev.docker-compose.yml up
   ```

### IntelliJ IDEA Setup

1. **Import Projects**:
   - File → Open → `tbd-common`
   - File → Open → `tbd-internal`

2. **Configure Maven**:
   - Settings → Build Tools → Maven
   - Set Maven home directory
   - Enable "Import Maven projects automatically"

3. **Run Configurations** (see tbd-internal/README.md for detailed setup):
   - DEV mode with hot reload
   - PROD mode with immutable image
   - Remote debugging on port 7777

## Repository Organization

This monorepo contains:

### Repository structure

- `tbd-common` – shared libraries (version 1.2.0), built first and installed to local Maven repo
- `tbd-internal` – main application (2.0.0-SNAPSHOT), depends on tbd-common
- `tbd-external` / `tbd-vi` – additional apps can be created from the tbd-internal template

To split into separate repositories later:

```bash
# tbd-common as its own repo
cd tbd-common
git init
git add .
git commit -m "Initial commit - tbd-common"
git remote add origin git@github.com:YOUR_ORG/tbd-common.git
git push -u origin main

# tbd-internal as its own repo
cd ../tbd-internal
git init
git add .
git commit -m "Initial commit - tbd-internal"
git remote add origin git@github.com:YOUR_ORG/tbd-internal.git
git push -u origin main
```

## Framework Version Management

### Centralized in tbd-common BOM

All framework versions are defined in `tbd-common/pom.xml`:

```xml
<properties>
  <spring.version>5.3.30</spring.version>
  <struts.version>2.5.32</struts.version>
  <hibernate.version>5.6.15.Final</hibernate.version>
</properties>
```

Applications import the BOM:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.yourcompany.tbd</groupId>
      <artifactId>tbd-common-bom</artifactId>
      <version>1.2.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

### Upgrading Frameworks

To upgrade Spring/Struts across all applications:

1. **Update tbd-common**:
   ```bash
   cd tbd-common
   # Edit pom.xml - update version properties
   # Bump version to 2.0.0 (major version for breaking change)
   mvn clean install
   git commit -am "Upgrade to Spring 6"
   git push
   ```

2. **Update applications** when ready:
   ```bash
   cd tbd-internal
   # Edit pom.xml
   # <tbd-common.version>2.0.0</tbd-common.version>
   mvn clean package
   # Test thoroughly
   git commit -am "Upgrade to tbd-common 2.0.0"
   git push
   ```

## Docker Development

### DEV Mode (Fast Iteration)

- **EAR mounted as volume** for hot reload
- **Debug port exposed** (7777)
- **Logs accessible** in real-time
- **Oracle DB included** for development

```bash
cd tbd-internal/docker
docker-compose -f dev.docker-compose.yml up
```

**Edit → Build → Test cycle:**
```bash
# Terminal 1: Keep compose running
docker-compose -f dev.docker-compose.yml up

# Terminal 2: Rebuild on changes
cd ..
mvn package -DskipTests
# Liberty auto-detects and reloads!
```

### PROD Mode (Production Testing)

- **EAR baked into image** (immutable)
- **Same image goes to ECS**
- **No debug ports**
- **Production-like configuration**

```bash
cd tbd-internal/docker
docker-compose -f prod.docker-compose.yml up --build
```

## CI/CD

You can add GitHub Actions (or another CI) to:

- **tbd-common**: Build, test, and optionally publish to a Maven repository (e.g. GitHub Packages)
- **tbd-internal**: Build, test, build Docker image, push to ECR (or another registry), and deploy to ECS

## Environment Variables

### Database Configuration

All applications use these environment variables:

- `DB_URL` - JDBC connection string
- `DB_USERNAME` - Database user
- `DB_PASSWORD` - Database password
- `DB_POOL_SIZE` - Connection pool size

### Configuration Files

1. **docker/.env** - Docker Compose environment variables
2. **src/main/resources/application.properties** - Application defaults
3. **liberty/server.xml** - Liberty configuration with env var substitution

## Creating Additional Applications

To create `tbd-external` or `tbd-vi`:

1. **Copy tbd-internal** as template:
   ```bash
   cp -r tbd-internal tbd-external
   ```

2. **Update pom.xml** files:
   - Change `artifactId` from `tbd-internal` to `tbd-external`
   - Update package names in Java code
   - Update Docker container names

3. **Update Liberty config**:
   - Change context root in `tbd-external-ear/pom.xml`
   - Update application name in `liberty/server.xml`

4. **Build and test**:
   ```bash
   cd tbd-external
   mvn clean package
   cd docker
   docker-compose -f dev.docker-compose.yml up
   ```

## Production Deployment (AWS ECS)

### Prerequisites

1. **ECR Repository**: Create in AWS Console
2. **ECS Cluster**: Set up cluster and task definition
3. **RDS Oracle**: Set up database
4. **Secrets Manager**: Store DB credentials
5. **Load Balancer**: Configure ALB

### Deployment Process

1. **CI pipeline** builds and pushes image to ECR
2. **Manual trigger** updates ECS service
3. **ECS** pulls new image and performs rolling deployment
4. **Health checks** verify deployment
5. **Rollback** if health checks fail

### Manual Deployment

```bash
# Build and tag
docker build -t tbd-internal:latest -f docker/Dockerfile .
docker tag tbd-internal:latest \
  123456789012.dkr.ecr.us-east-1.amazonaws.com/tbd-internal:latest

# Push to ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin \
  123456789012.dkr.ecr.us-east-1.amazonaws.com
docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/tbd-internal:latest

# Update ECS
aws ecs update-service \
  --cluster tbd-cluster \
  --service tbd-internal-service \
  --force-new-deployment
```

## Troubleshooting

### Common Issues

1. **Maven can't find tbd-common**:
   ```bash
   cd tbd-common
   mvn clean install
   ```

2. **Port conflicts**:
   - Edit `docker-compose.yml` and change port mappings

3. **Database connection fails**:
   - Check `.env` file
   - Verify Oracle container is running
   - Test connection: `docker exec -it tbd-oracle-dev sqlplus tbd_user/password@XE`

4. **Liberty won't start**:
   - Check Docker logs: `docker-compose logs liberty-dev`
   - Verify EAR is built: `ls tbd-internal-ear/target/`
   - Check server.xml for errors

### Getting Help

- **Documentation**: See individual README.md files
- **Logs**: `docker-compose logs -f <service>`
- **Issues**: GitHub Issues

## Project Roadmap

### Phase 1: Modernization (Current)
- ✅ Maven multi-module structure
- ✅ Spring + Struts integration
- ✅ Docker containerization
- ✅ DEV/PROD compose files
- ✅ CI/CD (e.g. GitHub Actions)

### Phase 2: Framework Upgrades
- ⏳ Spring 5 → Spring 6
- ⏳ Struts 2 → Modern alternative
- ⏳ Java 11 → Java 17

### Phase 3: Modernization
- ⏳ Microservices architecture
- ⏳ Kubernetes deployment
- ⏳ Cloud-native patterns

## Contributing

1. Create feature branch
2. Make changes
3. Test locally with Docker Compose
4. Commit with descriptive messages
5. Push and create merge request
6. Wait for CI/CD to pass
7. Request code review

## License

Proprietary - Internal Use Only

## Support Contacts

- **Development Team**: dev-team@yourcompany.com
- **DevOps**: devops@yourcompany.com
- **Database Team**: dba@yourcompany.com
