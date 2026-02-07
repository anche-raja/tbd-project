# IntelliJ IDEA Setup — TBD Project

This project is configured for IntelliJ. Use the IDE run configurations instead of shell scripts.

## 1. Open the project

1. **File → Open**
2. Select the **tbd-project** folder (the one that contains `tbd-common`, `tbd-external`, and the root `pom.xml`).
3. Choose **Open as Project**.
4. Wait for Maven import to finish (right-hand side progress).

## 2. Prerequisites

- **JDK 8**: File → Project Structure → Project → Project SDK.
- **Maven**: Use the bundled Maven or your own (Settings → Build, Execution, Deployment → Build Tools → Maven).
- **Docker**: For running the app in DEV/PROD (optional for building only).

## 3. Run configurations (included)

These are in **.idea/runConfigurations/** and appear in the Run/Debug dropdown:

| Configuration           | What it does |
|-------------------------|--------------|
| **Build All**            | Builds `tbd-common` then `tbd-external` (same as the old setup step). |
| **Build tbd-common**     | `mvn clean install -DskipTests` in `tbd-common`. |
| **Build tbd-external**   | `mvn clean package -DskipTests` in `tbd-external`. |
| **Remote Debug Liberty** | Attach debugger to Liberty in Docker (host: `localhost`, port: `7777`). |

### Build from IntelliJ

1. Use **Build All** (or run **Build tbd-common** then **Build tbd-external**).
2. First time: ensure **tbd-external/docker/.env** exists (copy from `tbd-external/docker/.env.example` if needed).

## 4. Running the application (DEV)

The app runs in Docker. You can start it in either of these ways:

### Option A: Docker Compose run configuration (recommended)

1. **Run → Edit Configurations → + → Docker Compose**.
2. **Name**: e.g. `Start DEV`.
3. **Compose file(s)**: add `tbd-external/docker/dev.docker-compose.yml` (use the path under this project).
4. **Working directory**: leave default or set to `tbd-external/docker`.
5. Save and run **Start DEV**.

### Option B: Terminal inside IntelliJ

```bash
cd tbd-external/docker
docker-compose -f dev.docker-compose.yml up
```

After startup:

- Web UI: http://localhost:9080/external  
- Health: http://localhost:9080/external/api/health  

## 5. Debugging

1. Start the app in DEV (Docker Compose or terminal as above).
2. In IntelliJ, select **Remote Debug Liberty** and click **Debug**.
3. Set breakpoints in your code and use the app; execution will stop at breakpoints.

## 6. Optional: run config for “build + start DEV”

If you want one action that builds and then starts Docker:

1. **Run → Edit Configurations → + → Compound**.
2. **Name**: e.g. `Build and Start DEV`.
3. Add in order: **Build All**, then your **Start DEV** (Docker Compose) configuration.

Run the compound config when you want a full build and start.

## Summary

- **Open**: `tbd-project` folder in IntelliJ.
- **Build**: Use **Build All** (or the two Maven configs).
- **Run**: Use a Docker Compose run config for `tbd-external/docker/dev.docker-compose.yml` or the terminal.
- **Debug**: Use **Remote Debug Liberty** while DEV is running.

No `setup.sh` or `quick-start.sh` is required when using this IntelliJ setup.
