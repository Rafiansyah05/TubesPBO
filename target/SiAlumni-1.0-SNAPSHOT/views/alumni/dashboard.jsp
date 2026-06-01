<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alumni Dashboard - SiAlumni</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon.ico"> <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo_sialumni.png">
</head>
<body>

    <div class="dashboard-container">
        <!-- Sidebar Alumni -->
        <aside class="sidebar">
            <div class="sidebar-logo">Si<span>Alumni</span>.</div>
            <ul class="nav-menu">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/alumni/dashboard" class="nav-link active">
                        <i class="fas fa-th-large"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/alumni/profile" class="nav-link">
                        <i class="fas fa-user-circle"></i> Profil & Karier
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
                    <h2 style="margin-bottom: 5px;">Halo, ${userName}!</h2>
                    <p style="color: var(--text-muted);">Selamat datang kembali di portal SiAlumni.</p>
                </div>
                <div class="user-profile">
                    <div style="text-align: right;">
                        <div style="font-weight: 700;">${userName}</div>
                        <div style="font-size: 12px; color: var(--text-muted);">Alumni</div>
                    </div>
                    <div class="avatar" style="background: var(--primary); color: var(--white);">
                        ${userName.substring(0,1)}
                    </div>
                </div>
            </header>

            <!-- Profil Alert -->
            <c:if test="${not profileComplete}">
                <div class="alert" style="background: #FFFBEB; border: 1px solid #FEF3C7; color: #92400E; display: flex; align-items: center; justify-content: space-between;">
                    <div>
                        <i class="fas fa-exclamation-triangle"></i> <strong>Profil Belum Lengkap!</strong> 
                        Mohon lengkapi data riwayat pekerjaan Anda untuk meningkatkan statistik kampus.
                    </div>
                    <a href="${pageContext.request.contextPath}/alumni/profile" class="btn btn-gold" style="width: auto; padding: 8px 20px; font-size: 13px;">Lengkapi Sekarang</a>
                </div>
            </c:if>

            <!-- Stats Grid -->
            <div class="stats-grid">
                <div class="stat-card">
                    <span class="label">TOTAL PENGALAMAN</span>
                    <div class="value">${totalJobs}</div>
                    <p style="font-size: 12px; color: var(--text-muted); margin-top: 5px;">Perusahaan yang pernah diikuti</p>
                </div>
                <div class="stat-card">
                    <span class="label">STATUS SAAT INI</span>
                    <div class="value" style="font-size: 24px; margin-top: 10px;">
                        <c:choose>
                            <c:when test="${aktifJobs > 0}">
                                <span style="color: var(--success);"><i class="fas fa-briefcase"></i> Aktif Bekerja</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: var(--text-muted);"><i class="fas fa-search"></i> Mencari Peluang</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Recent Jobs -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Riwayat Pekerjaan Terbaru</h3>
                    <a href="${pageContext.request.contextPath}/alumni/profile" style="color: var(--primary); font-size: 14px; font-weight: 600; text-decoration: none;">Kelola Semua</a>
                </div>
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>Perusahaan</th>
                                <th>Jabatan</th>
                                <th>Industri</th>
                                <th>Periode</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="job" items="${jobs}">
                                <tr>
                                    <td>
                                        <div style="font-weight: 700;">${job.company.name}</div>
                                        <div style="font-size: 12px; color: var(--text-muted);">${job.company.location}</div>
                                    </td>
                                    <td>${job.jabatan}</td>
                                    <td>${job.industri}</td>
                                    <td>
                                        <fmt:formatDate value="${job.startDate}" pattern="MMM yyyy" /> - 
                                        <c:choose>
                                            <c:when test="${not empty job.endDate}">
                                                <fmt:formatDate value="${job.endDate}" pattern="MMM yyyy" />
                                            </c:when>
                                            <c:otherwise>Sekarang</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty job.endDate}">
                                                <span class="badge badge-success">Aktif</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge" style="background: #EDF2F7; color: #4A5568;">Selesai</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty jobs}">
                                <tr>
                                    <td colspan="5" style="text-align: center; padding: 40px; color: var(--text-muted);">
                                        Anda belum menambahkan riwayat pekerjaan.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
