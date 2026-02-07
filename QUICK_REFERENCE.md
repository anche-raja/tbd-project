# TBD Project - Quick Reference Card

## Project Contents

```
tbd-project/
├── tbd-common/           # Shared libraries (Spring 5.3.30, Struts 2.5.32)
├── tbd-internal/         # Internal application (WAR+EAR on Liberty)
├── pom.xml               # Root aggregator (open this folder in IntelliJ)
├── INTELLIJ_SETUP.md     # IntelliJ run configs and workflow
├── README.md             # Master documentation
└── GETTING_STARTED.md    # Getting started guide
```

## Quick Start (IntelliJ)

1. **Open** `tbd-project` in IntelliJ (File → Open).
2. **Build**: run the **Build All** run configuration.
3. **Start**: add Docker Compose config for `tbd-internal/docker/dev.docker-compose.yml` and run it, or: `cd tbd-internal/docker && docker-compose -f dev.docker-compose.yml up`
4. **Access**: http://localhost:9080/internal  
See **[INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)** for details.

## Essential Commands

### Build
```bash
cd tbd-common && mvn clean install      # Build common libs
cd tbd-internal && mvn clean package    # Build application
```

### Run DEV (Hot Reload)
```bash
cd tbd-internal/docker
docker-compose -f dev.docker-compose.yml up
# Port 9080 (HTTP), 7777 (Debug)
```

### Run PROD (Immutable Image)
```bash
cd tbd-internal/docker
docker-compose -f prod.docker-compose.yml up --build
# Port 9080 (HTTP)
```

### Rebuild After Changes
```bash
mvn package -DskipTests
# Liberty auto-reloads in DEV mode!
```

## Key URLs

- **Home**: http://localhost:9080/internal
- **Health**: http://localhost:9080/internal/api/health
- **DB Test**: http://localhost:9080/internal/api/db-test

## Debug in IntelliJ

1. Start DEV (Docker Compose or terminal).
2. Run the **Remote Debug Liberty** run configuration (localhost:7777).
3. Set breakpoints and go!

## Database Access

```bash
# Connect to local Oracle
docker exec -it tbd-oracle-dev sqlplus tbd_user/change_me@XE

# View logs
docker-compose logs -f liberty-dev
```

## Configuration

Edit `tbd-internal/docker/.env`:
```bash
DB_URL=jdbc:oracle:thin:@host:1521:SID
DB_USERNAME=your_user
DB_PASSWORD=your_password
```

## Project Architecture

**Common Library (BOM Pattern)**
- tbd-common defines ALL framework versions
- Apps import BOM → get consistent dependencies
- Upgrade: Change common version → rebuild apps

**Application (Multi-Module Maven)**
- WAR module: Spring MVC + Struts 2 code
- EAR module: Packages WAR for Liberty
- Liberty: Provides runtime (Servlet, JSP, JDBC)

**Docker Strategy**
- DEV: Volume-mounted EAR (fast iteration)
- PROD: Baked-in EAR (immutable image)

## Troubleshooting

**Can't find tbd-common**
```bash
cd tbd-common && mvn clean install
```

**Port conflict**
```bash
# Change in docker-compose.yml
ports: - "19080:9080"
```

**Database error**
```bash
# Check DB is running
docker-compose ps
# Verify .env file
cat docker/.env
```

**Build fails**
```bash
mvn clean -U
rm -rf ~/.m2/repository/com/yourcompany/tbd
cd tbd-common && mvn clean install
```

## File Locations

**Application Code**
- Controllers: `tbd-internal-war/src/main/java/.../controller/`
- Services: `tbd-internal-war/src/main/java/.../service/`
- Actions (Struts): `tbd-internal-war/src/main/java/.../action/`
- JSPs: `tbd-internal-war/src/main/webapp/WEB-INF/jsp/`

**Configuration**
- Spring: `tbd-internal-war/src/main/java/.../config/AppConfig.java`
- Struts: `tbd-internal-war/src/main/resources/struts.xml`
- Liberty: `tbd-internal/liberty/server.xml`
- Database: `tbd-internal/docker/.env`

**Build Output**
- WAR: `tbd-internal-war/target/tbd-internal.war`
- EAR: `tbd-internal-ear/target/tbd-internal.ear`

## Technology Versions

| Component | Version |
|-----------|---------|
| Java | 11 |
| Maven | 3.9+ |
| Spring | 5.3.30 |
| Struts | 2.5.32 |
| Hibernate | 5.6.15 |
| Liberty | 24.0.0.12 |
| Oracle JDBC | 21.9.0.0 |

## Next Steps

1. ✅ Open `tbd-project` in IntelliJ and run **Build All** (see [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md))
2. ✅ Read `GETTING_STARTED.md`
3. ✅ Start DEV mode and explore
4. ✅ Review `README.md` for detailed docs
5. ✅ Check `tbd-internal/README.md` for deployment

## Support Resources

- **Master README**: Complete documentation
- **Getting Started**: Step-by-step tutorial
- **App README**: Deployment and operations
- **Common README**: Library documentation

---
**Questions?** Check README.md or GETTING_STARTED.md for details!
