# Sistem Informasi Alumni Terintegrasi Berbasis Web

## Deskripsi

Sistem Informasi Alumni Terintegrasi Berbasis Web adalah aplikasi untuk mengelola data alumni, riwayat karir, statistik pekerjaan, serta administrasi data alumni dalam satu platform terpusat.

Aplikasi ini dibangun menggunakan arsitektur berbasis Java Spring Boot dengan pendekatan Object-Oriented Programming (OOP), sehingga sistem lebih terstruktur, scalable, dan mudah dikembangkan.

---

## Teknologi yang Digunakan

| Komponen      | Teknologi                          | Fungsi                            |
| ------------- | ---------------------------------- | --------------------------------- |
| Backend       | Java 11, Spring Boot 2.7.0         | Logika bisnis dan server aplikasi |
| Frontend      | Thymeleaf, HTML5, CSS3, JavaScript | Tampilan dan interaksi pengguna   |
| Database      | MySQL, JPA/Hibernate               | Penyimpanan data                  |
| Build Tool    | Gradle                             | Compile dan menjalankan project   |
| Email Testing | Mailtrap                           | Simulasi pengiriman email         |

---

## Struktur Folder dan Fungsi File

### Folder `controller/`

Berisi class yang menangani HTTP request dari user dan mengembalikan response halaman web.

| File                  | Fungsi                                  |
| --------------------- | --------------------------------------- |
| AuthController.java   | Menangani login, logout, dan beranda    |
| AlumniController.java | Dashboard alumni, profil, riwayat karir |
| AdminController.java  | Kelola data alumni oleh admin           |
| CareerController.java | Statistik karir alumni                  |

### Folder `model/`

Berisi class representasi objek/data dalam sistem.

| File                   | Fungsi                                      |
| ---------------------- | ------------------------------------------- |
| User.java              | Abstract class induk untuk Alumni dan Admin |
| Alumni.java            | Entity pengguna alumni                      |
| Admin.java             | Entity pengguna admin                       |
| JobExperience.java     | Riwayat pekerjaan alumni                    |
| Company.java           | Data perusahaan                             |
| CareerStatistic.java   | Statistik karir                             |
| EmailNotification.java | Notifikasi email                            |
| Industri.java          | Enum kategori industri                      |
| Searching.java         | Interface fitur pencarian                   |

### Folder `repository/`

Berisi interface akses database menggunakan Spring Data JPA.

| File                             | Fungsi                |
| -------------------------------- | --------------------- |
| AlumniRepository.java            | CRUD data alumni      |
| AdminRepository.java             | CRUD data admin       |
| CompanyRepository.java           | CRUD data perusahaan  |
| JobExperienceRepository.java     | CRUD riwayat kerja    |
| EmailNotificationRepository.java | CRUD notifikasi email |

### Folder `service/`

Berisi logika bisnis utama aplikasi.

| File               | Fungsi                            |
| ------------------ | --------------------------------- |
| AlumniService.java | Profil alumni dan manajemen karir |
| AdminService.java  | Pengelolaan data alumni           |
| CareerService.java | Statistik karir                   |
| EmailService.java  | Pengiriman email                  |

### Folder `config/`

Konfigurasi tambahan aplikasi.

| File             | Fungsi                          |
| ---------------- | ------------------------------- |
| EmailConfig.java | Konfigurasi SMTP Mailtrap       |
| WebConfig.java   | Resource static CSS, JS, gambar |

### Folder `templates/`

File HTML yang dirender oleh Thymeleaf.

| File                  | Fungsi               |
| --------------------- | -------------------- |
| index.html            | Halaman utama        |
| login.html            | Halaman login        |
| alumni-dashboard.html | Dashboard alumni     |
| admin-dashboard.html  | Dashboard admin      |
| profile.html          | Edit profil          |
| career-tracking.html  | Riwayat pekerjaan    |
| career-statistic.html | Statistik perusahaan |

### Folder `static/`

File asset statis.

| Folder/File   | Fungsi                      |
| ------------- | --------------------------- |
| css/style.css | Styling website             |
| js/main.js    | Fungsi global               |
| js/alumni.js  | Validasi form alumni        |
| js/admin.js   | Search, export, bulk delete |
| images/       | Gambar dan icon             |

### File Utama

| File                         | Fungsi                        |
| ---------------------------- | ----------------------------- |
| AlumniSystemApplication.java | Entry point aplikasi          |
| application.properties       | Konfigurasi server, DB, email |
| build.gradle                 | Dependency project            |

---

## Penerapan OOP (Object-Oriented Programming)

| Konsep OOP        | Lokasi                                                     | Penjelasan                                                                                                                                  |
| ----------------- | ---------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| **Inheritance**   | `model/User.java`, `model/Alumni.java`, `model/Admin.java` | Alumni dan Admin mewarisi atribut (idUser, name, email, password) dan method dari class User                                                |
| **Encapsulation** | Semua file di folder `model/`                              | Seluruh atribut dalam class menggunakan private dan diakses melalui getter/setter                                                           |
| **Polymorphism**  | `model/Alumni.java`, `model/Admin.java`                    | Method `getProfile()` memiliki implementasi berbeda di class Alumni dan Admin                                                               |
| **Abstraction**   | `model/User.java`, `model/Searching.java`                  | User sebagai abstract class tidak bisa diinstansiasi langsung. Searching sebagai interface untuk kontrak method pencarian                   |
| **Association**   | `model/Alumni.java`, `model/JobExperience.java`            | Relasi one-to-many antara Alumni dan JobExperience (satu alumni memiliki banyak riwayat pekerjaan)                                          |
| **Composition**   | `model/Alumni.java`                                        | Menggunakan cascade = CascadeType.ALL pada relasi Alumni ke JobExperience, sehingga jika Alumni dihapus, riwayat pekerjaannya ikut terhapus |

---

## Fitur Aplikasi

| Fitur                  | Pengguna | Deskripsi                                             |
| ---------------------- | -------- | ----------------------------------------------------- |
| Login / Logout         | Semua    | Sistem autentikasi membedakan role alumni dan admin   |
| Kelola Profil          | Alumni   | Edit nama, email, dan password                        |
| Kelola Riwayat Karir   | Alumni   | Tambah dan hapus riwayat pekerjaan                    |
| Top 10 Perusahaan      | Semua    | Menampilkan perusahaan dengan jumlah alumni terbanyak |
| Distribusi per Jurusan | Semua    | Menampilkan top perusahaan per program studi          |
| Kelola Data Alumni     | Admin    | Tambah, edit, hapus, dan verifikasi data alumni       |
| Notifikasi Email       | Sistem   | Pengingat update data alumni setiap 6 bulan           |

---

## Akun Demo

| Role   | Email               | Password  |
| ------ | ------------------- | --------- |
| Alumni | budi@email.com      | pass1     |
| Alumni | sari@email.com      | pass2     |
| Alumni | andi@email.com      | pass3     |
| Alumni | rina@email.com      | pass4     |
| Admin  | hendra@kampus.ac.id | adminpass |

---

## Cara Menjalankan Aplikasi

### 1. Jalankan XAMPP

Aktifkan layanan berikut pada XAMPP Control Panel:

- **Apache** (port 80)
- **MySQL** (port 3306)

### 2. Buat Database

Buka phpMyAdmin di browser: `http://localhost/phpmyadmin`
Kemudian buat database baru dengan nama: "alumni_db"

### 3. Jalankan Project

Buka terminal di folder project `CODE_CAREERTRACE`

**Mac / Linux:**

````bash
./gradlew bootRun

**Windows:**
```bash
.\gradlew.bat bootRun

4. Kungjungi browser ini:
Kunjungi : http://localhost:8080

````
