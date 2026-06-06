# Setup & Configuration Report - SiAlumni Project

## ✅ Perbaikan yang Sudah Dilakukan

### 1. **Perbaiki File pom.xml**

- **Masalah**: Ada teks `mvn -version` di awal file yang merusak XML declaration
- **Solusi**: Dihapus teks yang tidak perlu itu
- **Status**: ✅ FIXED

### 2. **Verifikasi Java Installation**

- **Java Version**: 25.0.2 LTS
- **Status**: ✅ Ready

### 3. **Verifikasi Maven Installation**

- **Maven Version**: 3.9.6
- **Location**: `.maven/apache-maven-3.9.6/`
- **Status**: ✅ Ready

### 4. **Compile Proyek**

```
✅ 15 source files compiled successfully
✅ 0 compilation errors
⚠️  Minor deprecation warning in EmailUtil.java (not critical)
```

### 5. **Build WAR File**

```
✅ Created: target/SiAlumni-1.0-SNAPSHOT.war (2.8 MB)
```

### 6. **Buat Helper Scripts**

- ✅ `run.cmd` - Script untuk menjalankan aplikasi
- ✅ `build.cmd` - Script untuk build project
- ✅ `README.md` - Dokumentasi lengkap

---

## 🚀 Cara Menjalankan Aplikasi

### **Metode 1: Menggunakan Script (Recommended)**

#### Windows Command Prompt:

```bash
run.cmd
```

Atau untuk build ulang:

```bash
build.cmd
```

### **Metode 2: Menjalankan Maven Langsung**

```bash
cd d:\TUGAS\TUBES PBO\CareerTrace2\code_careerTrace
.\.maven\apache-maven-3.9.6\bin\mvn.cmd tomcat7:run
```

### **Metode 3: Build JAR/WAR Terlebih Dahulu**

```bash
.\.maven\apache-maven-3.9.6\bin\mvn.cmd clean package -DskipTests
```

---

## 📋 Project Information

| Property         | Value                              |
| ---------------- | ---------------------------------- |
| **Project Name** | SiAlumni - Sistem Informasi Alumni |
| **Artifact ID**  | SiAlumni                           |
| **Version**      | 1.0-SNAPSHOT                       |
| **Packaging**    | WAR (Web Application Archive)      |
| **Java Version** | 11                                 |
| **Database**     | PostgreSQL (Supabase)              |
| **Server**       | Apache Tomcat 7                    |

---

## 🔌 Dependencies

```xml
✅ org.postgresql:postgresql:42.6.0
✅ javax.servlet:javax.servlet-api:4.0.1
✅ javax.servlet.jsp:javax.servlet.jsp-api:2.3.3
✅ javax.servlet:jstl:1.2
```

---

## 📂 Project Structure

```
code_careerTrace/
├── src/main/
│   ├── java/
│   │   ├── controllers/
│   │   │   ├── AdminController.java
│   │   │   ├── AlumniController.java
│   │   │   ├── AuthController.java
│   │   │   └── PublicController.java
│   │   ├── interfaces/
│   │   │   ├── GenerateID.java
│   │   │   └── Searching.java
│   │   ├── models/
│   │   │   ├── Admin.java
│   │   │   ├── Alumni.java
│   │   │   ├── CareerStatistic.java
│   │   │   ├── Company.java
│   │   │   ├── EmailNotification.java
│   │   │   ├── JDBC.java
│   │   │   ├── JobExperience.java
│   │   │   └── User.java
│   │   └── utils/
│   │       └── EmailUtil.java
│   └── webapp/
│       ├── views/
│       │   ├── admin/
│       │   ├── alumni/
│       │   ├── auth/
│       │   └── public/
│       ├── assets/
│       │   ├── css/
│       │   ├── images/
│       │   └── js/
│       └── WEB-INF/
│           ├── web.xml
│           └── context.xml
├── pom.xml ✅ FIXED
├── mvnw.cmd
├── .maven/ (Local Maven 3.9.6)
├── database.sql
├── run.cmd ✅ NEW
├── build.cmd ✅ NEW
└── README.md ✅ NEW
```

---

## 🌐 Application Access

Setelah menjalankan `run.cmd`:

```
URL: http://localhost:8080
```

---

## 🔧 Troubleshooting

### Problem: Port 8080 sudah digunakan

**Solution**: Edit `pom.xml` section `<port>8080</port>` menjadi port lain (misal 8081)

### Problem: Maven command not found

**Solution**: Gunakan `run.cmd` atau `build.cmd` yang sudah menyetup PATH otomatis

### Problem: Database connection error

**Check**: `src/main/webapp/WEB-INF/web.xml` untuk kredensial database Supabase

---

## 📝 Database Setup

Database sudah dikonfigurasi di `web.xml`:

```xml
<context-param>
    <param-name>db.url</param-name>
    <param-value>jdbc:postgresql://db.tereexilkchduyiclgoy.supabase.co:6543/postgres?sslmode=require</param-value>
</context-param>
<context-param>
    <param-name>db.user</param-name>
    <param-value>postgres</param-value>
</context-param>
```

Untuk setup database awal, jalankan:

```sql
-- Load file: database.sql
```

---

## ✨ Next Steps

1. **Jalankan aplikasi**: `run.cmd`
2. **Akses di browser**: `http://localhost:8080`
3. **Setup database** jika belum ada dengan `database.sql`
4. **Login** dengan credentials di aplikasi
5. **Development**: Edit file di `src/` dan rebuild dengan `build.cmd`

---

## 📞 Support

Jika ada error, check:

1. Java version: `java -version`
2. Maven config: `mvn -v`
3. Port availability: `netstat -ano | findstr :8080`
4. Database connection di `web.xml`

---

**Last Updated**: 2026-06-06
**Status**: ✅ Ready to Run
