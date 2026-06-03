<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - SiAlumni</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon.ico"> <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo_sialumni.png">
</head>
<body>

    <div class="dashboard-container">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-logo">
                Si<span>Alumni</span>.
            </div>
            
            <ul class="nav-menu">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link active">
                        <i class="fas fa-th-large"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/alumni" class="nav-link">
                        <i class="fas fa-users"></i> Kelola Alumni
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/email-log" class="nav-link">
                        <i class="fas fa-paper-plane"></i> Log Notifikasi
                    </a>
                </li>
            </ul>

            <div class="sidebar-footer">
                <form action="${pageContext.request.contextPath}/auth" method="post">
                    <input type="hidden" name="action" value="logout">
                    <button type="submit" class="nav-link" style="background: none; border: none; width: 100%; cursor: pointer;">
                        <i class="fas fa-sign-out-alt"></i> Keluar
                    </button>
                </form>
            </div>
        </aside>

        <!-- Main Content -->
        <main class="main-content">
            <header class="header">
                <div>
                    <h2 style="margin-bottom: 5px;">Dashboard Admin</h2>
                    <p style="color: var(--text-muted);">Ringkasan data sistem alumni saat ini.</p>
                </div>
                <div class="user-profile">
                    <div style="text-align: right;">
                        <div style="font-weight: 700;">${userName}</div>
                        <div style="font-size: 12px; color: var(--text-muted);">Administrator</div>
                    </div>
                    <div class="avatar">AD</div>
                </div>
            </header>

            <!-- Stats Grid -->
            <div class="stats-grid">
                <div class="stat-card">
                    <span class="label">TOTAL ALUMNI</span>
                    <div class="value">${totalAlumni}</div>
                    <div style="margin-top: 10px; font-size: 12px; color: var(--success);">
                        <i class="fas fa-arrow-up"></i> +12 bulan ini
                    </div>
                </div>
                <div class="stat-card">
                    <span class="label">AKTIF BEKERJA</span>
                    <div class="value">${alumniAktif}</div>
                    <div style="margin-top: 10px; font-size: 12px; color: var(--text-muted);">
                        ${(alumniAktif / totalAlumni * 100).intValue()}% dari total alumni
                    </div>
                </div>
                <div class="stat-card">
                    <span class="label">EMAIL TERKIRIM</span>
                    <div class="value">${emailTerkirim}</div>
                    <div style="margin-top: 10px; font-size: 12px; color: var(--text-muted);">
                        Log pengiriman bulan ini
                    </div>
                </div>
            </div>

            <!-- Recent Alumni Table -->
            <div class="card">
                <div class="card-header" style="height: auto; display: flex; flex-wrap: wrap; gap: 12px; align-items: center; justify-content: space-between;">
                    <div>
                        <h3 class="card-title">Daftar Alumni</h3>
                    </div>
                    <a href="${pageContext.request.contextPath}/admin/alumni" style="color: var(--primary); font-size: 14px; font-weight: 600; text-decoration: none;">Lihat Semua</a>
                </div>

                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>Nama</th>
                                <th style="text-align: center;">Jurusan</th>
                                <th style="text-align: center;">Tahun Masuk</th>
                                <th style="text-align: center;">Riwayat Pekerjaan</th>
                                <th style="text-align: center;">Status Profil</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="alumni" items="${daftarAlumni}">
                                <tr data-name="${alumni.name}" data-year="${alumni.enrollmentYear}">
                                    <td>
                                        <div style="font-weight: 600;">${alumni.name}</div>
                                        <div style="font-size: 12px; color: var(--text-muted);">${alumni.email}</div>
                                    </td>
                                    <td style="text-align: center;">${alumni.major}</td>
                                    <td style="text-align: center;">${alumni.enrollmentYear}</td>
                                    <td style="text-align: center;"><span class="badge badge-warning">${alumni.jumlahJob} Pekerjaan</span></td>
                                    <td style="text-align: center;">
                                        <span class="badge ${alumni.statusClass}">${alumni.statusLabel}</span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>

    <script>
        function resetFilterAdminDashboard() {
            document.getElementById('filterNama').value = '';
            document.getElementById('filterTahun').value = '';
            filterAdminDashboard();
        }

        function filterAdminDashboard() {
            const keyword = (document.getElementById('filterNama').value || '').toLowerCase().trim();
            const year = (document.getElementById('filterTahun').value || '').trim();
            const rows = document.querySelectorAll('table tbody tr');

            rows.forEach(function (row) {
                const name = (row.getAttribute('data-name') || '').toLowerCase();
                const yearValue = String(row.getAttribute('data-year') || '');
                const matchName = !keyword || name.includes(keyword);
                const matchYear = !year || yearValue === year;

                row.style.display = (matchName && matchYear) ? '' : 'none';
            });
        }

        document.getElementById('filterNama').addEventListener('input', filterAdminDashboard);
        document.getElementById('filterTahun').addEventListener('input', filterAdminDashboard);
    </script>

</body>
</html>
