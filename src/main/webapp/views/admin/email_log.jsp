<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log Notifikasi - SiAlumni</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon.ico"> <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo_sialumni.png">
</head>
<body>

    <div class="dashboard-container">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-logo">Si<span>Alumni</span>.</div>
            <ul class="nav-menu">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">
                        <i class="fas fa-th-large"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/alumni" class="nav-link">
                        <i class="fas fa-users"></i> Kelola Alumni
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/email-log" class="nav-link active">
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
                    <h2 style="margin-bottom: 5px;">Riwayat Pengiriman Email</h2>
                    <p style="color: var(--text-muted);">Status integrasi Resend API secara real-time.</p>
                </div>
            </header>

            <!-- Status Terakhir Broadcast -->
            <c:if test="${not empty notifBerhasil}">
                <div class="alert alert-success" style="margin-bottom: 30px;">
                    <i class="fas fa-check-circle"></i> Broadcast selesai! 
                    <strong>${notifBerhasil} Berhasil</strong>, 
                    <strong>${notifGagal} Gagal</strong>.
                </div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert alert-success" style="margin-bottom: 30px;">
                    <i class="fas fa-check-circle"></i> ${successMessage}
                </div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" style="margin-bottom: 30px; background: #FED7D7; color: #9B2C2C;">
                    <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                </div>
            </c:if>

            <!-- Pengaturan Email Otomatis -->
            <div class="card" style="margin-bottom: 30px; padding: 25px;">
                <h3 style="margin-bottom: 20px; font-weight: 800; color: var(--primary);">Pengaturan Email Otomatis</h3>
                <form action="${pageContext.request.contextPath}/admin/email-log" method="post" style="display: flex; flex-direction: column; gap: 15px;">
                    <input type="hidden" name="action" value="saveSchedulerSettings">
                    
                    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 15px;">
                        <div class="form-group">
                            <label>Status Pengiriman Otomatis</label>
                            <select name="enabled" class="form-control" style="height: 48px;">
                                <option value="true" ${schedulerSetting.enabled ? 'selected' : ''}>Aktif</option>
                                <option value="false" ${!schedulerSetting.enabled ? 'selected' : ''}>Nonaktif</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Setiap Berapa Waktu</label>
                            <input type="number" name="interval_value" class="form-control" style="height: 48px;" min="1" value="${schedulerSetting.intervalValue}" required>
                        </div>
                        <div class="form-group">
                            <label>Satuan Waktu</label>
                            <select name="interval_unit" class="form-control" style="height: 48px;">
                                <option value="days" ${schedulerSetting.intervalUnit == 'days' ? 'selected' : ''}>Hari (24 Jam)</option>
                                <option value="hours" ${schedulerSetting.intervalUnit == 'hours' ? 'selected' : ''}>Jam</option>
                                <option value="minutes" ${schedulerSetting.intervalUnit == 'minutes' ? 'selected' : ''}>Menit</option>
                            </select>
                        </div>
                    </div>
                    
                    <div style="display: grid; grid-template-columns: 1fr; gap: 15px;">
                        <div class="form-group">
                            <label>Subjek Email Otomatis</label>
                            <input type="text" name="subject" class="form-control" style="height: 48px;" value="${schedulerSetting.subject}" required>
                        </div>
                        <div class="form-group">
                            <label>Isi Email Otomatis</label>
                            <textarea name="body" class="form-control" rows="4" required>${schedulerSetting.body}</textarea>
                        </div>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">
                        <span style="font-size: 13px; color: var(--text-muted);">
                            Terakhir dikirim otomatis: 
                            <c:choose>
                                <c:when test="${not empty schedulerSetting.lastRun}">
                                    <fmt:formatDate value="${schedulerSetting.lastRun}" pattern="dd MMM yyyy, HH:mm" />
                                </c:when>
                                <c:otherwise>Belum pernah</c:otherwise>
                            </c:choose>
                        </span>
                        <button type="submit" class="btn btn-primary" style="width: auto; padding: 12px 24px; font-weight: 600;">Simpan Pengaturan</button>
                    </div>
                </form>
            </div>

            <div class="card">
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Penerima</th>
                                <th>Subjek</th>
                                <th>Waktu Kirim</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="log" items="${emailLogs}">
                                <tr>
                                    <td>#${log.id}</td>
                                    <td style="font-weight: 600;">${log.recipient_email}</td>
                                    <td>${log.subject}</td>
                                    <td style="font-size: 14px; color: var(--text-muted);">
                                        <fmt:formatDate value="${log.sent_at}" pattern="dd MMM yyyy, HH:mm" />
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${log.status == 'sent'}">
                                                <span class="badge badge-success">
                                                    <i class="fas fa-check"></i> Sent
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger" style="background: #FED7D7; color: #9B2C2C;">
                                                    <i class="fas fa-times"></i> Failed
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty emailLogs}">
                                <tr>
                                    <td colspan="5" style="text-align: center; padding: 50px; color: var(--text-muted);">
                                        Belum ada riwayat pengiriman email.
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
