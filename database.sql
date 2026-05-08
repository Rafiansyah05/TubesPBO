-- =============================================
-- SiAlumni - Sistem Informasi Alumni
-- File SQL untuk Supabase (PostgreSQL)
-- Bisa langsung di-paste ke Supabase SQL Editor
-- =============================================

-- Hapus tabel lama kalau ada (urutan harus benar karena ada foreign key)
DROP VIEW IF EXISTS career_statistic;
DROP TABLE IF EXISTS email_notifications;
DROP TABLE IF EXISTS job_experience;
DROP TABLE IF EXISTS alumni;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS companies;
DROP TABLE IF EXISTS users;

-- =============================================
-- Tabel users: menyimpan semua akun pengguna
-- role bisa 'alumni', 'admin', atau 'mahasiswa'
-- =============================================
CREATE TABLE users (
    id_user   VARCHAR(36) PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    email     VARCHAR(100) UNIQUE NOT NULL,
    password  VARCHAR(255) NOT NULL,
    role      VARCHAR(20) NOT NULL DEFAULT 'alumni',
    created_at TIMESTAMP DEFAULT NOW()
);

-- =============================================
-- Tabel alumni: data tambahan untuk user alumni
-- FK ke tabel users
-- =============================================
CREATE TABLE alumni (
    id_user         VARCHAR(36) PRIMARY KEY REFERENCES users(id_user) ON DELETE CASCADE,
    enrollment_year INTEGER,
    major           VARCHAR(100),
    jumlah_job      INTEGER DEFAULT 0
);

-- =============================================
-- Tabel admin: data tambahan untuk user admin
-- FK ke tabel users
-- =============================================
CREATE TABLE admin (
    id_user VARCHAR(36) PRIMARY KEY REFERENCES users(id_user) ON DELETE CASCADE,
    jabatan VARCHAR(100)
);

-- =============================================
-- Tabel companies: data perusahaan tempat alumni bekerja
-- =============================================
CREATE TABLE companies (
    id_company    VARCHAR(36) PRIMARY KEY,
    name          VARCHAR(150) NOT NULL,
    location      VARCHAR(150),
    jumlah_alumni INTEGER DEFAULT 0
);

-- =============================================
-- Tabel job_experience: riwayat pekerjaan alumni
-- FK ke alumni dan companies
-- industri pakai nilai yang sudah ditentukan
-- =============================================
CREATE TABLE job_experience (
    id_job     VARCHAR(36) PRIMARY KEY,
    id_alumni  VARCHAR(36) NOT NULL REFERENCES alumni(id_user) ON DELETE CASCADE,
    id_company VARCHAR(36) NOT NULL REFERENCES companies(id_company) ON DELETE CASCADE,
    industri   VARCHAR(50) NOT NULL,
    jabatan    VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date   DATE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- =============================================
-- Tabel email_notifications: log pengiriman email
-- status bisa 'pending', 'sent', 'failed'
-- =============================================
CREATE TABLE email_notifications (
    id              SERIAL PRIMARY KEY,
    recipient_email VARCHAR(100) NOT NULL,
    subject         VARCHAR(255),
    body            TEXT,
    scheduled_date  DATE,
    sent_at         TIMESTAMP,
    status          VARCHAR(20) DEFAULT 'pending'
);

-- =============================================
-- INDEX untuk mempercepat query yang sering dipakai
-- =============================================
CREATE INDEX idx_users_email      ON users(email);
CREATE INDEX idx_alumni_major     ON alumni(major);
CREATE INDEX idx_job_id_alumni    ON job_experience(id_alumni);
CREATE INDEX idx_job_id_company   ON job_experience(id_company);

-- =============================================
-- VIEW career_statistic: statistik karier alumni
-- dipakai untuk halaman publik top companies
-- =============================================
CREATE VIEW career_statistic AS
SELECT
    a.major                    AS major,
    c.name                     AS company_name,
    c.location                 AS company_location,
    c.id_company               AS id_company,
    COUNT(j.id_job)            AS alumni_count
FROM job_experience j
JOIN alumni a   ON j.id_alumni  = a.id_user
JOIN companies c ON j.id_company = c.id_company
GROUP BY a.major, c.name, c.location, c.id_company
ORDER BY alumni_count DESC;

-- =============================================
-- DATA PERUSAHAAN (10 perusahaan)
-- =============================================
INSERT INTO companies (id_company, name, location, jumlah_alumni) VALUES
('comp-001', 'Google Indonesia',       'Jakarta',   45),
('comp-002', 'Bank Mandiri',           'Jakarta',   38),
('comp-003', 'Gojek',                  'Jakarta',   52),
('comp-004', 'Telkom Indonesia',       'Bandung',   29),
('comp-005', 'Shopee International',   'Jakarta',   33),
('comp-006', 'Astra International',    'Jakarta',   21),
('comp-007', 'BCA',                    'Jakarta',   18),
('comp-008', 'Pertamina',              'Jakarta',   25),
('comp-009', 'Bukalapak',              'Jakarta',   17),
('comp-010', 'Tokopedia',              'Jakarta',   41);

-- =============================================
-- AKUN ADMIN (1 admin)
-- =============================================
INSERT INTO users (id_user, name, email, password, role) VALUES
('admin-001', 'Administrator Sistem', 'admin@sialumni.ac.id', 'admin123', 'admin');

INSERT INTO admin (id_user, jabatan) VALUES
('admin-001', 'Kepala Bidang Alumni');

-- =============================================
-- AKUN ALUMNI (100 alumni dari 5 jurusan)
-- password semua: alumni123
-- =============================================

-- Teknik Informatika (alumni01 - alumni20)
INSERT INTO users (id_user, name, email, password, role) VALUES
('alm-001','Aditya Pratama','alumni01@sialumni.ac.id','alumni123','alumni'),
('alm-002','Budi Santoso','alumni02@sialumni.ac.id','alumni123','alumni'),
('alm-003','Citra Dewi','alumni03@sialumni.ac.id','alumni123','alumni'),
('alm-004','Dian Rahmawati','alumni04@sialumni.ac.id','alumni123','alumni'),
('alm-005','Eko Purnomo','alumni05@sialumni.ac.id','alumni123','alumni'),
('alm-006','Fajar Nugroho','alumni06@sialumni.ac.id','alumni123','alumni'),
('alm-007','Gita Kusuma','alumni07@sialumni.ac.id','alumni123','alumni'),
('alm-008','Hendra Wijaya','alumni08@sialumni.ac.id','alumni123','alumni'),
('alm-009','Indah Permata','alumni09@sialumni.ac.id','alumni123','alumni'),
('alm-010','Joko Susilo','alumni10@sialumni.ac.id','alumni123','alumni'),
('alm-011','Kartika Sari','alumni11@sialumni.ac.id','alumni123','alumni'),
('alm-012','Lukman Hakim','alumni12@sialumni.ac.id','alumni123','alumni'),
('alm-013','Maya Anggraini','alumni13@sialumni.ac.id','alumni123','alumni'),
('alm-014','Nanda Putra','alumni14@sialumni.ac.id','alumni123','alumni'),
('alm-015','Olivia Tanoto','alumni15@sialumni.ac.id','alumni123','alumni'),
('alm-016','Pandu Wibowo','alumni16@sialumni.ac.id','alumni123','alumni'),
('alm-017','Qori Amalia','alumni17@sialumni.ac.id','alumni123','alumni'),
('alm-018','Rizky Firmansyah','alumni18@sialumni.ac.id','alumni123','alumni'),
('alm-019','Sinta Maharani','alumni19@sialumni.ac.id','alumni123','alumni'),
('alm-020','Taufik Hidayat','alumni20@sialumni.ac.id','alumni123','alumni');

INSERT INTO alumni (id_user, enrollment_year, major, jumlah_job) VALUES
('alm-001',2018,'Teknik Informatika',2),
('alm-002',2019,'Teknik Informatika',1),
('alm-003',2018,'Teknik Informatika',3),
('alm-004',2020,'Teknik Informatika',1),
('alm-005',2017,'Teknik Informatika',4),
('alm-006',2019,'Teknik Informatika',2),
('alm-007',2020,'Teknik Informatika',1),
('alm-008',2018,'Teknik Informatika',3),
('alm-009',2021,'Teknik Informatika',1),
('alm-010',2017,'Teknik Informatika',5),
('alm-011',2019,'Teknik Informatika',2),
('alm-012',2020,'Teknik Informatika',2),
('alm-013',2018,'Teknik Informatika',1),
('alm-014',2021,'Teknik Informatika',1),
('alm-015',2019,'Teknik Informatika',3),
('alm-016',2020,'Teknik Informatika',1),
('alm-017',2018,'Teknik Informatika',2),
('alm-018',2017,'Teknik Informatika',4),
('alm-019',2021,'Teknik Informatika',1),
('alm-020',2019,'Teknik Informatika',2);

-- Sistem Informasi (alumni21 - alumni40)
INSERT INTO users (id_user, name, email, password, role) VALUES
('alm-021','Ucup Setiawan','alumni21@sialumni.ac.id','alumni123','alumni'),
('alm-022','Vina Lestari','alumni22@sialumni.ac.id','alumni123','alumni'),
('alm-023','Wahyu Hartono','alumni23@sialumni.ac.id','alumni123','alumni'),
('alm-024','Xena Puspita','alumni24@sialumni.ac.id','alumni123','alumni'),
('alm-025','Yoga Pratama','alumni25@sialumni.ac.id','alumni123','alumni'),
('alm-026','Zahra Amini','alumni26@sialumni.ac.id','alumni123','alumni'),
('alm-027','Arif Budiman','alumni27@sialumni.ac.id','alumni123','alumni'),
('alm-028','Bella Oktavia','alumni28@sialumni.ac.id','alumni123','alumni'),
('alm-029','Chandra Kurnia','alumni29@sialumni.ac.id','alumni123','alumni'),
('alm-030','Devi Rahayu','alumni30@sialumni.ac.id','alumni123','alumni'),
('alm-031','Edwin Santoso','alumni31@sialumni.ac.id','alumni123','alumni'),
('alm-032','Fani Kusumawati','alumni32@sialumni.ac.id','alumni123','alumni'),
('alm-033','Gilang Ramadhan','alumni33@sialumni.ac.id','alumni123','alumni'),
('alm-034','Hani Fitriani','alumni34@sialumni.ac.id','alumni123','alumni'),
('alm-035','Ivan Maulana','alumni35@sialumni.ac.id','alumni123','alumni'),
('alm-036','Julia Anggraini','alumni36@sialumni.ac.id','alumni123','alumni'),
('alm-037','Kevin Adrianto','alumni37@sialumni.ac.id','alumni123','alumni'),
('alm-038','Lina Mardianti','alumni38@sialumni.ac.id','alumni123','alumni'),
('alm-039','Muhamad Ridwan','alumni39@sialumni.ac.id','alumni123','alumni'),
('alm-040','Nita Cahyani','alumni40@sialumni.ac.id','alumni123','alumni');

INSERT INTO alumni (id_user, enrollment_year, major, jumlah_job) VALUES
('alm-021',2018,'Sistem Informasi',2),
('alm-022',2019,'Sistem Informasi',1),
('alm-023',2018,'Sistem Informasi',3),
('alm-024',2020,'Sistem Informasi',1),
('alm-025',2017,'Sistem Informasi',4),
('alm-026',2019,'Sistem Informasi',2),
('alm-027',2020,'Sistem Informasi',1),
('alm-028',2018,'Sistem Informasi',3),
('alm-029',2021,'Sistem Informasi',1),
('alm-030',2017,'Sistem Informasi',5),
('alm-031',2019,'Sistem Informasi',2),
('alm-032',2020,'Sistem Informasi',2),
('alm-033',2018,'Sistem Informasi',1),
('alm-034',2021,'Sistem Informasi',1),
('alm-035',2019,'Sistem Informasi',3),
('alm-036',2020,'Sistem Informasi',1),
('alm-037',2018,'Sistem Informasi',2),
('alm-038',2017,'Sistem Informasi',4),
('alm-039',2021,'Sistem Informasi',1),
('alm-040',2019,'Sistem Informasi',2);

-- Manajemen Bisnis (alumni41 - alumni60)
INSERT INTO users (id_user, name, email, password, role) VALUES
('alm-041','Oscar Permadi','alumni41@sialumni.ac.id','alumni123','alumni'),
('alm-042','Paula Dewanti','alumni42@sialumni.ac.id','alumni123','alumni'),
('alm-043','Qiko Andrianto','alumni43@sialumni.ac.id','alumni123','alumni'),
('alm-044','Rani Sulistyowati','alumni44@sialumni.ac.id','alumni123','alumni'),
('alm-045','Surya Darma','alumni45@sialumni.ac.id','alumni123','alumni'),
('alm-046','Tia Rahmawati','alumni46@sialumni.ac.id','alumni123','alumni'),
('alm-047','Umar Hamid','alumni47@sialumni.ac.id','alumni123','alumni'),
('alm-048','Vera Kusuma','alumni48@sialumni.ac.id','alumni123','alumni'),
('alm-049','Wisnu Bayu','alumni49@sialumni.ac.id','alumni123','alumni'),
('alm-050','Yanti Mulyani','alumni50@sialumni.ac.id','alumni123','alumni'),
('alm-051','Zainul Abidin','alumni51@sialumni.ac.id','alumni123','alumni'),
('alm-052','Agus Supriyanto','alumni52@sialumni.ac.id','alumni123','alumni'),
('alm-053','Bayu Setiawan','alumni53@sialumni.ac.id','alumni123','alumni'),
('alm-054','Cahya Nurlita','alumni54@sialumni.ac.id','alumni123','alumni'),
('alm-055','Dodi Prakoso','alumni55@sialumni.ac.id','alumni123','alumni'),
('alm-056','Elly Saputri','alumni56@sialumni.ac.id','alumni123','alumni'),
('alm-057','Firmansyah','alumni57@sialumni.ac.id','alumni123','alumni'),
('alm-058','Galih Prabowo','alumni58@sialumni.ac.id','alumni123','alumni'),
('alm-059','Hesti Yuniari','alumni59@sialumni.ac.id','alumni123','alumni'),
('alm-060','Irwan Setiadi','alumni60@sialumni.ac.id','alumni123','alumni');

INSERT INTO alumni (id_user, enrollment_year, major, jumlah_job) VALUES
('alm-041',2018,'Manajemen Bisnis',2),
('alm-042',2019,'Manajemen Bisnis',1),
('alm-043',2018,'Manajemen Bisnis',3),
('alm-044',2020,'Manajemen Bisnis',1),
('alm-045',2017,'Manajemen Bisnis',4),
('alm-046',2019,'Manajemen Bisnis',2),
('alm-047',2020,'Manajemen Bisnis',1),
('alm-048',2018,'Manajemen Bisnis',3),
('alm-049',2021,'Manajemen Bisnis',1),
('alm-050',2017,'Manajemen Bisnis',5),
('alm-051',2019,'Manajemen Bisnis',2),
('alm-052',2020,'Manajemen Bisnis',2),
('alm-053',2018,'Manajemen Bisnis',1),
('alm-054',2021,'Manajemen Bisnis',1),
('alm-055',2019,'Manajemen Bisnis',3),
('alm-056',2020,'Manajemen Bisnis',1),
('alm-057',2018,'Manajemen Bisnis',2),
('alm-058',2017,'Manajemen Bisnis',4),
('alm-059',2021,'Manajemen Bisnis',1),
('alm-060',2019,'Manajemen Bisnis',2);

-- Akuntansi (alumni61 - alumni80)
INSERT INTO users (id_user, name, email, password, role) VALUES
('alm-061','Jasmine Putri','alumni61@sialumni.ac.id','alumni123','alumni'),
('alm-062','Krisna Bagas','alumni62@sialumni.ac.id','alumni123','alumni'),
('alm-063','Layla Hasanah','alumni63@sialumni.ac.id','alumni123','alumni'),
('alm-064','Marno Subekti','alumni64@sialumni.ac.id','alumni123','alumni'),
('alm-065','Noni Andriani','alumni65@sialumni.ac.id','alumni123','alumni'),
('alm-066','Oki Hendra','alumni66@sialumni.ac.id','alumni123','alumni'),
('alm-067','Putri Rahayu','alumni67@sialumni.ac.id','alumni123','alumni'),
('alm-068','Riko Susanto','alumni68@sialumni.ac.id','alumni123','alumni'),
('alm-069','Sari Wahyuni','alumni69@sialumni.ac.id','alumni123','alumni'),
('alm-070','Teguh Prasetyo','alumni70@sialumni.ac.id','alumni123','alumni'),
('alm-071','Ulfa Nadiyah','alumni71@sialumni.ac.id','alumni123','alumni'),
('alm-072','Vicky Haryanto','alumni72@sialumni.ac.id','alumni123','alumni'),
('alm-073','Wulan Septiani','alumni73@sialumni.ac.id','alumni123','alumni'),
('alm-074','Xander Putra','alumni74@sialumni.ac.id','alumni123','alumni'),
('alm-075','Yudi Kurniawan','alumni75@sialumni.ac.id','alumni123','alumni'),
('alm-076','Zaenab Hasibuan','alumni76@sialumni.ac.id','alumni123','alumni'),
('alm-077','Alvin Saputra','alumni77@sialumni.ac.id','alumni123','alumni'),
('alm-078','Bintang Hartono','alumni78@sialumni.ac.id','alumni123','alumni'),
('alm-079','Cindy Permata','alumni79@sialumni.ac.id','alumni123','alumni'),
('alm-080','Deny Kusuma','alumni80@sialumni.ac.id','alumni123','alumni');

INSERT INTO alumni (id_user, enrollment_year, major, jumlah_job) VALUES
('alm-061',2018,'Akuntansi',2),
('alm-062',2019,'Akuntansi',1),
('alm-063',2018,'Akuntansi',3),
('alm-064',2020,'Akuntansi',1),
('alm-065',2017,'Akuntansi',4),
('alm-066',2019,'Akuntansi',2),
('alm-067',2020,'Akuntansi',1),
('alm-068',2018,'Akuntansi',3),
('alm-069',2021,'Akuntansi',1),
('alm-070',2017,'Akuntansi',5),
('alm-071',2019,'Akuntansi',2),
('alm-072',2020,'Akuntansi',2),
('alm-073',2018,'Akuntansi',1),
('alm-074',2021,'Akuntansi',1),
('alm-075',2019,'Akuntansi',3),
('alm-076',2020,'Akuntansi',1),
('alm-077',2018,'Akuntansi',2),
('alm-078',2017,'Akuntansi',4),
('alm-079',2021,'Akuntansi',1),
('alm-080',2019,'Akuntansi',2);

-- Teknik Elektro (alumni81 - alumni100)
INSERT INTO users (id_user, name, email, password, role) VALUES
('alm-081','Evan Marcellino','alumni81@sialumni.ac.id','alumni123','alumni'),
('alm-082','Fifi Nuraini','alumni82@sialumni.ac.id','alumni123','alumni'),
('alm-083','Guntur Prasetyo','alumni83@sialumni.ac.id','alumni123','alumni'),
('alm-084','Hilda Suharto','alumni84@sialumni.ac.id','alumni123','alumni'),
('alm-085','Imam Santosa','alumni85@sialumni.ac.id','alumni123','alumni'),
('alm-086','Jihan Aulia','alumni86@sialumni.ac.id','alumni123','alumni'),
('alm-087','Kurnia Adi','alumni87@sialumni.ac.id','alumni123','alumni'),
('alm-088','Lidya Suhendra','alumni88@sialumni.ac.id','alumni123','alumni'),
('alm-089','Mirza Fauzan','alumni89@sialumni.ac.id','alumni123','alumni'),
('alm-090','Nadia Savitri','alumni90@sialumni.ac.id','alumni123','alumni'),
('alm-091','Omen Pratama','alumni91@sialumni.ac.id','alumni123','alumni'),
('alm-092','Prita Handayani','alumni92@sialumni.ac.id','alumni123','alumni'),
('alm-093','Qolby Nurhadi','alumni93@sialumni.ac.id','alumni123','alumni'),
('alm-094','Reza Maulana','alumni94@sialumni.ac.id','alumni123','alumni'),
('alm-095','Sinta Kurniasih','alumni95@sialumni.ac.id','alumni123','alumni'),
('alm-096','Tommy Setiawan','alumni96@sialumni.ac.id','alumni123','alumni'),
('alm-097','Ulfah Damayanti','alumni97@sialumni.ac.id','alumni123','alumni'),
('alm-098','Vani Tristanti','alumni98@sialumni.ac.id','alumni123','alumni'),
('alm-099','Wahid Alamsyah','alumni99@sialumni.ac.id','alumni123','alumni'),
('alm-100','Yuliana Dewi','alumni100@sialumni.ac.id','alumni123','alumni');

INSERT INTO alumni (id_user, enrollment_year, major, jumlah_job) VALUES
('alm-081',2018,'Teknik Elektro',2),
('alm-082',2019,'Teknik Elektro',1),
('alm-083',2018,'Teknik Elektro',3),
('alm-084',2020,'Teknik Elektro',1),
('alm-085',2017,'Teknik Elektro',4),
('alm-086',2019,'Teknik Elektro',2),
('alm-087',2020,'Teknik Elektro',1),
('alm-088',2018,'Teknik Elektro',3),
('alm-089',2021,'Teknik Elektro',1),
('alm-090',2017,'Teknik Elektro',5),
('alm-091',2019,'Teknik Elektro',2),
('alm-092',2020,'Teknik Elektro',2),
('alm-093',2018,'Teknik Elektro',1),
('alm-094',2021,'Teknik Elektro',1),
('alm-095',2019,'Teknik Elektro',3),
('alm-096',2020,'Teknik Elektro',1),
('alm-097',2018,'Teknik Elektro',2),
('alm-098',2017,'Teknik Elektro',4),
('alm-099',2021,'Teknik Elektro',1),
('alm-100',2019,'Teknik Elektro',2);

-- =============================================
-- DATA JOB EXPERIENCE (sample untuk beberapa alumni)
-- =============================================
INSERT INTO job_experience (id_job, id_alumni, id_company, industri, jabatan, start_date, end_date) VALUES
('job-001','alm-001','comp-001','Teknologi','Software Engineer','2022-01-01',NULL),
('job-002','alm-001','comp-003','Teknologi','Junior Developer','2020-06-01','2021-12-31'),
('job-003','alm-002','comp-002','Keuangan','Data Analyst','2021-03-01',NULL),
('job-004','alm-003','comp-001','Teknologi','Backend Engineer','2021-01-01',NULL),
('job-005','alm-003','comp-009','Teknologi','Frontend Developer','2019-07-01','2020-12-31'),
('job-006','alm-003','comp-005','Teknologi','Intern','2019-01-01','2019-06-30'),
('job-007','alm-004','comp-010','Teknologi','Product Manager','2022-04-01',NULL),
('job-008','alm-005','comp-001','Teknologi','Senior Engineer','2018-01-01',NULL),
('job-009','alm-005','comp-003','Teknologi','Developer','2016-01-01','2017-12-31'),
('job-010','alm-006','comp-004','Teknologi','Network Engineer','2021-01-01',NULL),
('job-011','alm-021','comp-002','Keuangan','Business Analyst','2021-08-01',NULL),
('job-012','alm-022','comp-007','Keuangan','Finance Analyst','2022-01-01',NULL),
('job-013','alm-023','comp-005','Teknologi','IT Consultant','2020-03-01',NULL),
('job-014','alm-041','comp-008','Lainnya','Marketing Manager','2021-05-01',NULL),
('job-015','alm-042','comp-006','Manufaktur','Supply Chain','2022-02-01',NULL),
('job-016','alm-061','comp-002','Keuangan','Accountant','2021-01-01',NULL),
('job-017','alm-062','comp-007','Keuangan','Auditor','2021-06-01',NULL),
('job-018','alm-081','comp-004','Teknologi','Electrical Engineer','2020-09-01',NULL),
('job-019','alm-082','comp-008','Lainnya','Power Engineer','2022-01-01',NULL),
('job-020','alm-083','comp-006','Manufaktur','Automation Engineer','2019-04-01',NULL);

-- Update jumlah_alumni di companies berdasarkan data job_experience
UPDATE companies SET jumlah_alumni = (
    SELECT COUNT(DISTINCT id_alumni) FROM job_experience WHERE id_company = companies.id_company
);
