# Panduan Migrasi Kode (SiAlumni)

Dokumen ini berisi analisis struktur proyek **SiAlumni (Sistem Informasi Alumni)**, pemetaan kepemilikan file secara mutlak per-orang menggunakan tabel terstruktur, dan panduan langkah-langkah Git untuk migrasi bebas konflik tanpa error kompilasi.

---

## 📊 Analisis Teknologi & Arsitektur
Proyek ini dikembangkan dengan stack teknologi berikut:
1. **Backend / Logic**: Java SE (Servlet API 4.0.1 & JSTL 1.2).
2. **Frontend UI**: JSP (JavaServer Pages 2.3.3) & Vanilla CSS & Vanilla Javascript.
3. **Database**: PostgreSQL (koneksi ke Supabase cloud menggunakan PostgreSQL JDBC Driver 42.6.0).
4. **Email Service**: Resend API (pengiriman HTTP POST manual tanpa pustaka eksternal JavaMail).
5. **Build Tool**: Apache Maven 3.9.6 (menggunakan Tomcat7 Maven Plugin untuk embedded server).

---

## ⚠️ Penanganan Folder Lokal (`target/` dan lainnya)
Di dalam proyek Java Maven, terdapat folder dan file yang **TIDAK BOLEH DI-PUSH** ke GitHub karena bersifat lokal (build artifacts atau konfigurasi IDE).

### 1. Folder `target/`
* **Deskripsi**: Folder yang dibuat secara otomatis oleh Maven saat menjalankan kompilasi (`mvn clean package` atau `build.cmd`). Berisi file `.class` hasil kompilasi, resource yang disalin, dan arsip akhir aplikasi `.war`.
* **Mengapa dilarang masuk Git?**: Ukurannya sangat besar, selalu berubah setiap kali kode di-build ulang, dan akan menyebabkan konflik merge yang sangat parah antar anggota tim.
* **Solusi**: Folder ini diabaikan oleh Git secara otomatis melalui file `.gitignore`. Setiap kontributor menghasilkan folder ini secara mandiri di laptop masing-masing dengan menjalankan script build lokal.

### 2. Folder Metadata IDE (`.idea/`, `.settings/`, `*.iml`)
* **Deskripsi**: Folder konfigurasi editor (IntelliJ IDEA, Eclipse, VS Code).
* **Mengapa dilarang masuk Git?**: Mengandung konfigurasi path yang spesifik untuk setiap komputer user.
* **Solusi**: Masukkan ke dalam file `.gitignore`.

### 3. File `.gitignore` Utama Proyek (Wajib Dibuat Oleh Munawir)
Munawir wajib menaruh file `.gitignore` ini di root folder proyek sebelum push pertama:
```text
# Maven build target
target/
*.war
*.ear
*.zip
*.tar.gz
*.rar

# IDE Metadata
.idea/
*.iml
.classpath
.project
.settings/
.vscode/

# OS temporary files
Thumbs.db
Desktop.ini
.DS_Store
```

---

## 🔑 Urutan Migrasi: Muchammad Munawir H. R. Harus Pertama!
**Muchammad Munawir H. R. (User & Auth)** memegang **pondasi utama** aplikasi (file konfigurasi Maven, skema database PostgreSQL, servlet mapping, base JDBC, dan model User/Admin). 

Jika anggota lain melakukan migrasi terlebih dahulu, proyek tidak akan bisa di-compile karena ketergantungan class parent (`User`, `JDBC`) dan file konfigurasi servlet (`web.xml`) belum terpasang.

Berikut adalah tabel pembagian tanggung jawab detail per-orang untuk memastikan seluruh file terpetakan tanpa ada yang terlewat.

---

## 👥 Tabel Distribusi & Tanggung Jawab Anggota Tim

### 1. 🟥 Bagian Muchammad Munawir H. R. (Pondasi, Auth, & Admin Base)
*Sebagai setup inisiator, Munawir bertugas menyiapkan pondasi awal proyek, database SQL, skema koneksi JDBC, base model user, dan halaman kelola admin.*

| No | Path File / Folder | Deskripsi & Kegunaan | Status Tindakan | Keterangan Tambahan |
| :---: | :--- | :--- | :--- | :--- |
| **1** | `pom.xml` | Konfigurasi dependensi Maven & Tomcat plugin | **Push Awal** | Harus bersih dari teks sampah agar tidak error XML parser. |
| **2** | `Dockerfile` | Konfigurasi Docker containerization untuk Tomcat | **Push Awal** | Setup port 8080 dan file copy build WAR. |
| **3** | `database.sql` | Skema database PostgreSQL & sampel data awal Supabase | **Push Awal** | Digunakan untuk inisialisasi tabel di database. |
| **4** | `mvnw.cmd` | Maven wrapper script untuk Windows | **Push Awal** | Memungkinkan menjalankan Maven tanpa instalasi global. |
| **5** | `.maven/` *(Folder)* | Folder binary Apache Maven 3.9.6 local | **Push Awal** | Berisi file biner Maven agar seragam di semua tim. |
| **6** | `build.cmd` | Script Windows batch untuk compile & package WAR | **Push Awal** | Memudahkan kompilasi lokal bagi pengguna Windows. |
| **7** | `build.sh` | Script shell Linux/Mac untuk compile & package WAR | **Push Awal** | Memudahkan kompilasi lokal bagi pengguna Linux/macOS. |
| **8** | `run.cmd` | Script Windows batch untuk menjalankan server Tomcat | **Push Awal** | Mempermudah running server Tomcat lokal di port 8080. |
| **9** | `run.sh` | Script shell Linux/Mac untuk menjalankan server Tomcat | **Push Awal** | Mempermudah running server Tomcat lokal di Linux/macOS. |
| **10** | `setup_maven.ps1` | Script PowerShell untuk path configuration | **Push Awal** | Mengotomatiskan registrasi path biner Maven lokal. |
| **11** | `.gitignore` | Konfigurasi file yang harus diabaikan Git | **Push Awal** | Wajib dibuat di root folder proyek sebelum push pertama. |
| **12** | `README.md` | Panduan cara menjalankan dan dokumentasi proyek | **Push Awal** | File dokumentasi utama repository. |
| **13** | `SETUP_REPORT.md` | Laporan pengecekan setup dan build pertama | **Push Awal** | Hasil pengecekan kesiapan compile. |
| **14** | `src/main/webapp/WEB-INF/web.xml` | Deployment descriptor (Servlet & DB config) | **Push Awal** | Mengatur pemetaan URL servlet dan konfigurasi db Supabase. |
| **15** | `src/main/webapp/META-INF/context.xml` | Tomcat Context Parameter config | **Push Awal** | Konfigurasi path resource server Tomcat. |
| **16** | `src/main/webapp/assets/css/style.css` | File style global CSS | **Push Awal** | Berisi class styling visual utama aplikasi. |
| **17** | `src/main/webapp/assets/js/main.js` | File javascript global | **Push Awal** | Script behavior global untuk aplikasi. |
| **18** | `src/main/webapp/assets/images/logo_sialumni.png` | File logo utama SiAlumni | **Push Awal** | Aset gambar logo yang digunakan di header & navbar. |
| **19** | `src/main/java/models/JDBC.java` | Kelas basis abstrak untuk koneksi JDBC PostgreSQL | **Push Awal** | Hub penghubung koneksi query SQL ke Supabase. |
| **20** | `src/main/java/interfaces/GenerateID.java` | Antarmuka utilitas pembuatan ID | **Push Awal** | Interface untuk method `generateID()`. |
| **21** | `src/main/java/interfaces/Searching.java` | Antarmuka pencarian data berdasarkan keyword | **Push Awal** | Interface untuk method `cekKeyword()`. |
| **22** | `src/main/java/models/User.java` | Kelas model dasar data pengguna (User) | **Push Awal** | Menyimpan enkripsi password SHA-256 dan logic login/logout. |
| **23** | `src/main/java/models/Admin.java` | Kelas model data tambahan untuk Administrator | **Push Awal** | Berisi data jabatan dan statistik total alumni pada sistem. |
| **24** | `src/main/java/controllers/AuthController.java` | Controller servlet untuk modul Auth (`/auth`) | **Push Awal** | Handle request login, register, dan logout. |
| **25** | `src/main/webapp/views/auth/login.jsp` | View JSP antarmuka halaman login | **Push Awal** | Form login yang mengarah ke AuthController. |
| **26** | `src/main/webapp/views/auth/register.jsp` | View JSP antarmuka halaman registrasi | **Push Awal** | Form registrasi alumni baru. |
| **27** | `src/main/java/controllers/AdminController.java` | Controller servlet untuk modul Admin (`/admin/*`) | **[SHARED] Push Awal** | Membuat kerangka dasar dan fungsi kelola alumni (add/delete). |
| **28** | `src/main/webapp/views/admin/dashboard.jsp` | View JSP halaman dashboard utama admin | **[SHARED] Push Awal** | Kerangka layout admin dashboard, sidebar menu, & total stat. |
| **29** | `src/main/webapp/views/admin/manage_alumni.jsp` | View JSP tabel data alumni dan modal manajemen | **[SHARED] Push Awal** | Tabel alumni, filter status, form admin baru, dan hapus alumni. |

---

### 2. 🟨 Bagian Muhammad Alvin Fa'iz (Manajemen Profil Alumni)
*Alvin bertanggung jawab atas antarmuka dashboard alumni serta proses pembaruan informasi data diri (nama, email, angkatan, jurusan, password).*

| No | Path File / Folder | Deskripsi & Kegunaan | Status Tindakan | Keterangan Tambahan |
| :---: | :--- | :--- | :--- | :--- |
| **1** | `src/main/webapp/views/alumni/dashboard.jsp` | View JSP halaman dashboard beranda alumni | **Push Mandiri** | Menampilkan info profil belum lengkap, total job, dan status aktif. |
| **2** | `src/main/java/models/Alumni.java` | Kelas model data detail alumni | **[SHARED] Push Pertama** | Alvin menulis model dasar dan method `insertAlumni()`, `updateAlumni()`. |
| **3** | `src/main/java/controllers/AlumniController.java` | Controller servlet untuk modul Alumni (`/alumni/*`) | **[SHARED] Push Pertama** | Alvin mendirikan controller, `showDashboard`, `showProfile`, `updateProfile`. |
| **4** | `src/main/webapp/views/alumni/profile.jsp` | View JSP halaman kelola profil pribadi alumni | **[SHARED] Push Pertama** | Alvin membuat form Data Pribadi di sisi kiri halaman. |

---

### 3. 🟩 Bagian Sintiya Devi (Career Tracking System)
*Sintiya bertanggung jawab mengintegrasikan riwayat pekerjaan alumni, termasuk fitur untuk menambah, mengubah, dan menghapus pengalaman kerja.*

| No | Path File / Folder | Deskripsi & Kegunaan | Status Tindakan | Keterangan Tambahan |
| :---: | :--- | :--- | :--- | :--- |
| **1** | `src/main/java/models/JobExperience.java` | Kelas model riwayat pekerjaan alumni | **Push Mandiri** | Mengatur query SQL `insert()`, `update()`, dan `delete()` pekerjaan. |
| **2** | `src/main/webapp/views/alumni/job_history.jsp` | View JSP cadangan untuk riwayat pekerjaan | **Push Mandiri** | Berisi file kosong (0 byte) untuk melengkapi registrasi views. |
| **3** | `src/main/java/models/Alumni.java` | Kelas model data detail alumni | **[SHARED] Pull & Merge** | Menyisipkan method `addJob()`, `deleteJob()`, dan `getJobExperience()`. |
| **4** | `src/main/java/controllers/AlumniController.java` | Controller servlet untuk modul Alumni | **[SHARED] Pull & Merge** | Menambahkan trigger POST action: `addJob`, `editJob`, dan `deleteJob`. |
| **5** | `src/main/webapp/views/alumni/profile.jsp` | View JSP halaman kelola profil pribadi alumni | **[SHARED] Pull & Merge** | Membuat tabel kerja kanan, modal tambah/edit pekerjaan, & javascript. |

---

### 4. 🟦 Bagian Ahmad Rafiansyah (Career Insight & Statistik)
*Ahmad fokus pada pencatatan instansi perusahaan dan pengolahan data statistik alumni berdasarkan industri serta jurusan.*

| No | Path File / Folder | Deskripsi & Kegunaan | Status Tindakan | Keterangan Tambahan |
| :---: | :--- | :--- | :--- | :--- |
| **1** | `src/main/java/models/Company.java` | Kelas model data mitra perusahaan | **Push Mandiri** | Memvalidasi dan melacak record ganda perusahaan (`findOrCreate`). |
| **2** | `src/main/java/models/CareerStatistic.java` | Kelas model pemrosesan data statistik karier | **Push Mandiri** | Memanggil statistik top companies dan statistik distribusi jurusan. |
| **3** | `src/main/java/controllers/PublicController.java` | Controller servlet untuk halaman index publik | **Push Mandiri** | Memproses trigger request filter landing page umum. |
| **4** | `src/main/webapp/views/public/index.jsp` | View JSP landing page utama publik | **Push Mandiri** | Menampilkan Top 10 perusahaan perekrut dan status filter prodi. |

---

### 5. 🟪 Bagian Kayla Balqis Syahira (Email Notification System)
*Kayla bertanggung jawab mengelola sistem notifikasi email pengingat tracer study dan pencatatan log historis.*

| No | Path File / Folder | Deskripsi & Kegunaan | Status Tindakan | Keterangan Tambahan |
| :---: | :--- | :--- | :--- | :--- |
| **1** | `src/main/java/utils/EmailUtil.java` | Utilitas pengiriman email via Resend API | **Push Mandiri** | Membuat request POST HTTPS ke API REST Resend. |
| **2** | `src/main/java/utils/EmailSchedulerListener.java` | Servlet listener pengirim email otomatis di background | **Push Mandiri** | Mengelola daemon background thread pengiriman otomatis. |
| **3** | `src/main/java/models/EmailNotification.java` | Kelas model log history email terkirim | **Push Mandiri** | Menangani query pencatatan pengiriman email (`logNotification`). |
| **4** | `src/main/java/models/SchedulerSetting.java` | Kelas model konfigurasi scheduler | **Push Mandiri** | Mengontrol status interval waktu scheduler otomatis. |
| **5** | `src/main/webapp/views/admin/email_log.jsp` | View JSP log pengiriman email & form setting scheduler | **Push Mandiri** | Membuat tabel log history email dan form edit scheduler. |
| **6** | `src/main/java/controllers/AdminController.java` | Controller servlet untuk modul Admin | **[SHARED] Pull & Merge** | Menambahkan backend logic handling email logs & save scheduler settings. |
| **7** | `src/main/webapp/views/admin/manage_alumni.jsp` | View JSP tabel data alumni dan modal manajemen | **[SHARED] Pull & Merge** | Menyisipkan modal pengirim email massal & penampung parameter checkbox. |

---

## 🛠️ Alur Kerja Kolaborasi Git (Bebas Konflik & Bebas Error)

Ikuti urutan pengerjaan fase berikut demi menjaga kelancaran proyek dan mencegah file konflik.

### 📍 Fase 1: Setup Repository Utama (Tanggung Jawab Munawir)
1. **Munawir** membuat repositori baru di GitHub.
2. **Munawir** memindahkan semua file miliknya (29 file/folder pada tabel 1) ke direktori lokalnya.
3. Di dalam direktori tersebut, jalankan perintah git berikut:
   ```bash
   git init
   git add .
   git commit -m "chore: initial project structure with maven, context configs, and auth module"
   git branch -M main
   git remote add origin <URL_GITHUB_BARU>
   git push -u origin main
   ```

### 📍 Fase 2: Penyiapan Workspace Lokal (Seluruh Anggota Tim)
1. Setiap anggota melakukan `clone` dari repositori utama:
   ```bash
   git clone <URL_GITHUB_BARU>
   cd SiAlumni
   ```
2. Setiap anggota membuat branch kerjanya masing-masing agar tidak saling menimpa secara langsung:
   * Alvin: `git checkout -b feature-alumni`
   * Sintiya: `git checkout -b feature-job`
   * Ahmad: `git checkout -b feature-insight`
   * Kayla: `git checkout -b feature-email`

### 📍 Fase 3: Migrasi Fitur Mandiri (Bukan File Bersama)
Setiap anggota menyalin file miliknya yang **bukan merupakan SHARED file** ke workspace lokal, kemudian melakukan push ke branch masing-masing.

* **Ahmad**: Menyalin dan push:
  * `src/main/java/models/Company.java`
  * `src/main/java/models/CareerStatistic.java`
  * `src/main/java/controllers/PublicController.java`
  * `src/main/webapp/views/public/index.jsp`
* **Alvin**: Menyalin dan push:
  * `src/main/webapp/views/alumni/dashboard.jsp`
* **Sintiya**: Menyalin dan push:
  * `src/main/java/models/JobExperience.java`
  * `src/main/webapp/views/alumni/job_history.jsp`
* **Kayla**: Menyalin dan push:
  * `src/main/java/utils/EmailUtil.java`
  * `src/main/java/utils/EmailSchedulerListener.java`
  * `src/main/java/models/EmailNotification.java`
  * `src/main/java/models/SchedulerSetting.java`
  * `src/main/webapp/views/admin/email_log.jsp`

*Perintah Push (Contoh Ahmad):*
```bash
git add .
git commit -m "feat: migrate company models, stats calculation, and landing index view"
git push origin feature-insight
```

### 📍 Fase 4: Penggabungan & Penyelesaian File Bersama (Shared Files)

#### 🤝 Langkah 1: Integrasi Modul Alumni & Pekerjaan
1. **Alvin** membuat Pull Request (PR) branch `feature-alumni` ke `main` di GitHub, lalu klik **Merge**.
2. **Sintiya** berpindah ke main lokal, melakukan pull terbaru, lalu menggabungkannya ke branch-nya sendiri:
   ```bash
   git checkout main
   git pull origin main
   git checkout feature-job
   git merge main
   ```
3. Di dalam branch `feature-job`, **Sintiya** menyalin perubahan logika pekerjaan miliknya ke dalam file-file bersama yang sekarang sudah berisi kode milik Alvin:
   * Menambahkan method `addJob()`, `deleteJob()`, `getJobExperience()` di [Alumni.java](file:///d:/TUGAS/TUBES%20PBO/CareerTrace2/code_careerTrace/src/main/java/models/Alumni.java).
   * Menambahkan mapping `addJob`, `editJob`, `deleteJob` di [AlumniController.java](file:///d:/TUGAS/TUBES%20PBO/CareerTrace2/code_careerTrace/src/main/java/controllers/AlumniController.java).
   * Menambahkan tabel riwayat pekerjaan, modal tambah kerja, modal edit kerja, dan script validator di [profile.jsp](file:///d:/TUGAS/TUBES%20PBO/CareerTrace2/code_careerTrace/src/main/webapp/views/alumni/profile.jsp).
4. Sintiya melakukan push branch `feature-job` dan menggabungkannya (Merge PR) ke `main`.

#### 🤝 Langkah 2: Integrasi Modul Admin & Notifikasi Email
1. **Kayla** berpindah ke main lokal, melakukan pull terbaru, lalu menggabungkannya ke branch-nya sendiri:
   ```bash
   git checkout main
   git pull origin main
   git checkout feature-email
   git merge main
   ```
2. Di dalam branch `feature-email`, **Kayla** menyalin bagian kode miliknya ke file bersama milik Munawir:
   * Menambahkan method routing `showEmailLog()`, `handleSaveSchedulerSettings()`, `handleSendNotification()` di [AdminController.java](file:///d:/TUGAS/TUBES%20PBO/CareerTrace2/code_careerTrace/src/main/java/controllers/AdminController.java).
   * Menambahkan modal form email massal dan penampung array target di [manage_alumni.jsp](file:///d:/TUGAS/TUBES%20PBO/CareerTrace2/code_careerTrace/src/main/webapp/views/admin/manage_alumni.jsp).
3. Kayla melakukan push branch `feature-email` dan menggabungkannya (Merge PR) ke `main`.

---

## 🚀 Validasi Bebas Error (Testing Lokal)
Setiap kali melakukan penggabungan (merge) di lokal, lakukan validasi berikut untuk memastikan tidak ada compiler error:

1. **Jalankan pembersihan kompilasi**:
   ```bash
   build.cmd
   ```
   *Hasil harus menampilkan `BUILD SUCCESS` dan menghasilkan file `SiAlumni-1.0-SNAPSHOT.war` baru di folder `target/`.*

2. **Jalankan Server Tomcat**:
   ```bash
   run.cmd
   ```
   *Aplikasi harus berhasil deploy di `http://localhost:8080` tanpa ada runtime exception.*
