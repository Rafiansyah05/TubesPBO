<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kelola Alumni - SiAlumni</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon.ico"> <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo_sialumni.png">
</head>
<body>

    <div class="dashboard-container">
        <!-- Sidebar (Sama dengan dashboard) -->
        <aside class="sidebar">
            <div class="sidebar-logo">Si<span>Alumni</span>.</div>
            <ul class="nav-menu">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">
                        <i class="fas fa-th-large"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/alumni" class="nav-link active">
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
                    <h2 style="margin-bottom: 5px;">Kelola Data Alumni</h2>
                    <p style="color: var(--text-muted);">Manajemen akun dan pengiriman notifikasi massal.</p>
                </div>
                <div style="display: flex; gap: 15px;">
                    <!-- Tombol Trigger Notifikasi Email -->
                    <button onclick="document.getElementById('modalNotif').style.display='flex'" class="btn btn-primary" style="width: auto;display: flex; align-items: center; gap: 12px;font-size: 14px; padding: 12 24px;font-weight: 600;">
                        <i class="fas fa-paper-plane"></i> Kirim Pengingat Update
                    </button>
                </div>
            </header>

            <!-- Selection Info Bar -->
            <div id="selectionBar" class="selection-info">
                <span><i class="fas fa-check-circle"></i> <span id="selectedCount">0</span> alumni terpilih</span>
                <button type="button" onclick="clearSelection()" style="background: none; border: none; color: var(--danger); cursor: pointer; font-weight: 600;">Batalkan Pilihan</button>
            </div>

            <!-- Search & Filter -->
            <div class="card" style="margin-bottom: 20px; padding: 20px;">
                <form action="${pageContext.request.contextPath}/admin/alumni" method="get" style="display: flex; gap: 15px; flex-wrap: wrap;">
                    <div style="flex: 1; min-width: 280px; position: relative;">
                        <i class="fas fa-search" style="position: absolute; left: 15px; top: 15px; color: var(--text-muted);"></i>
                        <input type="text" name="q" class="form-control" placeholder="Cari nama alumni atau jurusan..." value="${keyword}" style="padding-left: 45px;height: 48px;">
                    </div>
                    <select name="status" class="form-control" style="height: 48px; min-width: 180px;">
                        <option value="all" ${statusFilter == 'all' ? 'selected' : ''}>Semua Status</option>
                        <option value="aktif" ${statusFilter == 'aktif' ? 'selected' : ''}>Terbaru</option>
                        <option value="perlu_update" ${statusFilter == 'perlu_update' ? 'selected' : ''}>Perlu Diperbarui</option>
                        <option value="belum_update" ${statusFilter == 'belum_update' ? 'selected' : ''}>Belum Update</option>
                    </select>
                    <button type="submit" class="btn btn-primary" style="width: 80px; height: 48px;font-size: 14px;font-weight: 600; padding: 0 24px;">Cari</button>
                    <a href="${pageContext.request.contextPath}/admin/alumni" class="btn" style="width: 80px; height: 48px; font-size: 14px; font-weight: 600; background: #EDF2F7; color: #4A5568; display: flex; justify-content: center; align-items: center; text-decoration: none;">Reset</a>
                </form>
            </div>

            <!-- Alumni Table -->
            <div class="card">
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th class="checkbox-cell">
                                    <input type="checkbox" id="selectAll" class="custom-checkbox" onclick="toggleSelectAll(this)">
                                </th>
                                <th>Alumni</th>
                                <th style="text-align: center;">Program Studi</th>
                                <th style="text-align: center;">Tahun Masuk</th>
                                <th style="text-align: center;">Riwayat Pekerjaan</th>
                                <th style="text-align: center;">Status Profil</th>
                                <th style="text-align: center;">Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="alumni" items="${daftarAlumni}">
                                <tr>
                                    <td class="checkbox-cell" >
                                        <input type="checkbox" name="selected_alumni" value="${alumni.idUser}" class="custom-checkbox alumni-checkbox" onclick="updateSelection()">
                                    </td>
                                    <td>
                                        <div style="font-weight: 700; color: var(--primary);">${alumni.name}</div>
                                        <div style="font-size: 13px; color: var(--text-muted);">${alumni.email}</div>
                                    </td>
                                    <td style="text-align: center;">${alumni.major}</td>
                                    <td style="text-align: center;">${alumni.enrollmentYear}</td>
                                    <td style="text-align: center;"><span class="badge badge-warning">${alumni.jumlahJob} Pekerjaan</span></td>
                                    <td style="text-align: center;"><span class="badge ${alumni.statusClass}">${alumni.statusLabel}</span></td>
                                    <td style="text-align: center;">
                                        <form action="${pageContext.request.contextPath}/admin/alumni" method="post" onsubmit="return confirm('Hapus alumni ini?')">
                                            <input type="hidden" name="action" value="deleteAlumni">
                                            <input type="hidden" name="id_alumni" value="${alumni.idUser}">
                                            <button type="submit" style="background: none; border: none; color: var(--danger); cursor: pointer; font-size: 18px;">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <!-- Pagination -->
                <div style="padding: 20px; border-top: 1px solid var(--border); display: flex; justify-content: space-between; align-items: center;">
                    <div style="font-size: 14px; color: var(--text-muted);">
                        Menampilkan ${daftarAlumni.size()} dari ${totalData} alumni
                    </div>
                    <div style="display: flex; gap: 5px;">
                        <c:if test="${currentPage > 1}">
                            <a href="?page=${currentPage - 1}&q=${keyword}&status=${statusFilter}" class="btn" style="width: auto; padding: 8px 16px; font-size: 14px; background: var(--white); border: 1px solid var(--border);">Prev</a>
                        </c:if>
                        <c:if test="${currentPage < totalPages}">
                            <a href="?page=${currentPage + 1}&q=${keyword}&status=${statusFilter}" class="btn" style="width: auto; padding: 8px 16px; font-size: 14px; background: var(--white); border: 1px solid var(--border);">Next</a>
                        </c:if>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- Modal Notifikasi Email (Sederhana dengan CSS) -->
    <div id="modalNotif" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 2000; align-items: center; justify-content: center;">
        <div class="auth-card" style="max-width: 600px;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h3 style="font-weight: 800; color: var(--primary);">Kirim Email Massal</h3>
                <button onclick="document.getElementById('modalNotif').style.display='none'" style="background: none; border: none; font-size: 24px; cursor: pointer;">&times;</button>
            </div>
            <p id="modalRecipientInfo" style="font-size: 14px; color: var(--text-muted); margin-bottom: 20px;">
                Email akan dikirimkan ke <strong>${totalData} alumni</strong> menggunakan Resend API.
            </p>
            <form id="emailForm" action="${pageContext.request.contextPath}/admin/alumni" method="post">
                <input type="hidden" name="action" value="sendNotification">
                <input type="hidden" id="sendToAllInput" name="send_to_all" value="true">
                <div id="selectedIdsContainer"></div>
                <div class="form-group">
                    <label>Subjek Email</label>
                    <input type="text" name="subject" class="form-control" value="Penting: Perbarui Data Alumni - SiAlumni" required>
                </div>
                <div class="form-group">
                    <label>Pesan Tambahan</label>
                    <textarea name="body" class="form-control" rows="5" placeholder="Tulis instruksi tambahan untuk alumni..." required>Kami melihat Anda belum memperbarui data pekerjaan dalam 6 bulan terakhir. Mohon segera update profil Anda untuk tracer study kampus.</textarea>
                </div>
                <button type="submit" class="btn btn-primary" style="display: flex; justify-content: center; align-items: center; gap: 12px; font-size: 16px; padding: 16px 24px; font-weight: 600;">
                    <i class="fas fa-paper-plane"></i> Mulai Kirim Sekarang
                </button>
            </form>
        </div>
    </div>

    <script>
        function toggleSelectAll(source) {
            const checkboxes = document.querySelectorAll('.alumni-checkbox');
            checkboxes.forEach(cb => cb.checked = source.checked);
            updateSelection();
        }

        function updateSelection() {
            const checkboxes = document.querySelectorAll('.alumni-checkbox:checked');
            const count = checkboxes.length;
            const bar = document.getElementById('selectionBar');
            const countSpan = document.getElementById('selectedCount');
            const modalInfo = document.getElementById('modalRecipientInfo');
            const sendToAllInput = document.getElementById('sendToAllInput');
            const selectedIdsContainer = document.getElementById('selectedIdsContainer');

            if (count > 0) {
                bar.classList.add('active');
                countSpan.innerText = count;
                modalInfo.innerHTML = `Email akan dikirimkan ke <strong>${count} alumni terpilih</strong>.`;
                sendToAllInput.value = "false";
                
                // Clear and add selected IDs
                selectedIdsContainer.innerHTML = '';
                checkboxes.forEach(cb => {
                    const input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'selected_alumni_ids';
                    input.value = cb.value;
                    selectedIdsContainer.appendChild(input);
                });
            } else {
                bar.classList.remove('active');
                modalInfo.innerHTML = `Email akan dikirimkan ke <strong>semua ${totalData} alumni</strong>.`;
                sendToAllInput.value = "true";
                selectedIdsContainer.innerHTML = '';
            }
        }

        function clearSelection() {
            document.getElementById('selectAll').checked = false;
            const checkboxes = document.querySelectorAll('.alumni-checkbox');
            checkboxes.forEach(cb => cb.checked = false);
            updateSelection();
        }
    </script>

</body>
</html>
