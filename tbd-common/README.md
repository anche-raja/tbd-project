# TBD Common

Common libraries and utilities shared across TBD applications.

## Modules

### tbd-common-bom
Bill of Materials that defines all framework versions and common module versions. Import this in your application to get consistent dependency versions.

### networkvalidation
Network validation utilities (IP addresses, ports, etc.)

### common-shared
Shared DTOs, utilities, and common code.

### common-services
Reusable business services (audit logging, etc.)

## Version Strategy

All framework versions are defined in the parent POM and exposed through the BOM:
- **Spring**: 5.3.30
- **Struts**: 2.5.32
- **Hibernate**: 5.6.15.Final
- **Jackson**: 2.15.2

## Usage in Applications

Add to your application's `pom.xml`:

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

<dependencies>
  <!-- No version needed - comes from BOM -->
  <dependency>
    <groupId>com.yourcompany.tbd</groupId>
    <artifactId>common-services</artifactId>
  </dependency>
  
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
  </dependency>
</dependencies>
```

## Building

```bash
mvn clean install
```

## Publishing to GitLab Registry

The CI/CD pipeline automatically publishes to GitLab Maven registry on commits to `main` and on tags.

Manual publish:
```bash
mvn clean deploy -DskipTests -s ci_settings.xml
```

## Framework Upgrades

To upgrade frameworks (e.g., Spring 5 → Spring 6):

1. Update versions in parent `pom.xml`
2. Update code for any breaking changes (e.g., `javax.*` → `jakarta.*`)
3. Bump version to indicate breaking change (e.g., 1.x → 2.x)
4. Test thoroughly
5. Commit and push

Applications will then upgrade by updating their BOM version reference.
