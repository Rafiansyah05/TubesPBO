# SiAlumni - Sistem Informasi Alumni

Aplikasi web Alumni Management System berbasis Java, Servlet, dan JSP.

## Prerequisites

- Java 11+ (sudah terinstall)
- Maven 3.9.6 (sudah ada di folder `.maven/`)

## Struktur Project

```
src/
├── main/
│   ├── java/
│   │   ├── controllers/     # Servlet Controllers
│   │   ├── models/          # Database Models
│   │   ├── interfaces/      # Interface definitions
│   │   └── utils/           # Utility classes
│   └── webapp/
│       ├── views/           # JSP files
│       ├── assets/          # CSS, JS, Images
│       └── WEB-INF/         # Configuration files
```

## Build & Run

### 1. Build Project

```bash
mvn clean package -DskipTests
```

Atau gunakan script:

```bash
build.cmd
```

### 2. Run dengan Tomcat7 Maven Plugin

```bash
mvn tomcat7:run
```

Atau gunakan script:

```bash
run.cmd
```

Aplikasi akan berjalan di: **http://localhost:8080**

### 3. Compile Tanpa Packaging

```bash
mvn clean compile
```

## Database Connection

Konfigurasi database ada di `src/main/webapp/WEB-INF/web.xml`:

```xml
<context-param>
    <param-name>db.url</param-name>
    <param-value>jdbc:postgresql://db.tereexilkchduyiclgoy.supabase.co:6543/postgres?sslmode=require</param-value>
</context-param>
```

**Database**: PostgreSQL (Supabase)

## Dependencies

- PostgreSQL JDBC Driver (42.6.0)
- Servlet API 4.0.1
- JSP API 2.3.3
- JSTL 1.2

## Troubleshooting

### Port 8080 sudah terpakai?

Edit `pom.xml` dan ubah port di plugin `tomcat7-maven-plugin`:

```xml
<configuration>
    <path>/</path>
    <port>8081</port>  <!-- Ganti dengan port lain -->
</configuration>
```

### Maven tidak ditemukan?

Pastikan Anda berada di folder project dan jalankan:

```bash
.\run.cmd
```

## Development

Untuk development, edit file di `src/main/java/` dan `src/main/webapp/`, kemudian rebuild dengan Maven.

## Build Output

- **WAR file**: `target/SiAlumni-1.0-SNAPSHOT.war`
- **Classes**: `target/classes/`
- **Exploded WAR**: `target/SiAlumni-1.0-SNAPSHOT/`
