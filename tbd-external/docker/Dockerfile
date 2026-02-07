FROM icr.io/appcafe/websphere-liberty:24.0.0.12-full-java8-openj9-ubi

# Copy Liberty server configuration
COPY liberty/server.xml /config/

# Create lib directory for Oracle JDBC driver
RUN mkdir -p /config/lib

# Download Oracle JDBC driver (or COPY from local if you have it)
# Option 1: Download from Maven Central
RUN curl -L -o /config/lib/ojdbc8.jar \
    https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc8/21.9.0.0/ojdbc8-21.9.0.0.jar

# Option 2: Copy from local (uncomment if you have the JAR locally)
# COPY docker/ojdbc8.jar /config/lib/

# Copy the EAR file
COPY tbd-external-ear/target/tbd-external.ear /config/apps/

# Set environment variables with defaults
ENV DB_URL=jdbc:oracle:thin:@oracle-db:1521:ORCL \
    DB_USERNAME=tbd_user \
    DB_PASSWORD=change_me \
    DB_POOL_SIZE=10

# Expose ports
EXPOSE 9080 9443 7777

# Configure Liberty for production
RUN configure.sh

# Run Liberty
CMD ["/opt/ibm/wlp/bin/server", "run", "defaultServer"]
