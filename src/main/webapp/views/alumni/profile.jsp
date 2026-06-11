<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profil & Karier - SiAlumni</title>
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
                    <a href="${pageContext.request.contextPath}/alumni/dashboard" class="nav-link">
                        <i class="fas fa-th-large"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/alumni/profile" class="nav-link active">
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
                    <h2 style="margin-bottom: 5px;">Profil & Karier</h2>
                    <p style="color: var(--text-muted);">Kelola data pribadi dan riwayat pekerjaan Anda.</p>
                </div>
            </header>

            <c:if test="${not empty success}">
                <div class="alert alert-success"><i class="fas fa-check-circle"></i> ${success}</div>
            </c:if>

            <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 30px;">
                
                <!-- Kolom Kiri: Data Pribadi -->
                <div>
                    <div class="card" style="padding: 30px;">
                        <h3 style="margin-bottom: 25px; font-weight: 800;">Data Pribadi</h3>
                        <form action="${pageContext.request.contextPath}/alumni/profile" method="post">
                            <input type="hidden" name="action" value="updateProfile">
                            
                            <div class="form-group">
                                <label>Nama Lengkap</label>
                                <input type="text" name="name" class="form-control" value="${user.name}" required>
                            </div>
                            <div class="form-group">
                                <label>Email Kampus</label>
                                <input type="email" name="email" class="form-control" value="${user.email}" required>
                            </div>
                            <div class="form-group">
                                <label>Program Studi</label>
                                <select name="major" class="form-control" required>
                                    <option value="Teknik Informatika" ${user.major == 'Teknik Informatika' ? 'selected' : ''}>Teknik Informatika</option>
                                    <option value="Sistem Informasi" ${user.major == 'Sistem Informasi' ? 'selected' : ''}>Sistem Informasi</option>
                                    <option value="Manajemen Bisnis" ${user.major == 'Manajemen Bisnis' ? 'selected' : ''}>Manajemen Bisnis</option>
                                    <option value="Akuntansi" ${user.major == 'Akuntansi' ? 'selected' : ''}>Akuntansi</option>
                                    <option value="Teknik Elektro" ${user.major == 'Teknik Elektro' ? 'selected' : ''}>Teknik Elektro</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Tahun Masuk</label>
                                <input type="number" name="enrollment_year" class="form-control" value="${user.enrollmentYear}" required>
                            </div>
                            <hr style="margin: 20px 0; border: none; border-top: 1px solid var(--border);">
                          
                            <button type="submit" class="btn btn-primary">Simpan Perubahan</button>
                        </form>
                    </div>
                </div>

                <!-- Kolom Kanan: Riwayat Pekerjaan -->
                <div>
                    <div class="card">
                        <div class="card-header">
                            <h3 class="card-title">Riwayat Pekerjaan</h3>
                            <button onclick="openAddJob()" class="btn btn-gold" style="width: auto; padding: 10px 20px; font-size: 14px;">
                                <i class="fas fa-plus"></i> Tambah Kerja
                            </button>
                        </div>
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Perusahaan</th>
                                        <th>Jabatan</th>
                                        <th>Periode</th>
                                        <th style="text-align: center;">Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="job" items="${jobs}">
                                        <tr data-id="${job.idJobExperience}"
                                            data-company="${job.company.name}"
                                            data-location="${job.company.location}"
                                            data-industri="${job.industri}"
                                            data-jabatan="${job.jabatan}"
                                            data-start="<fmt:formatDate value='${job.startDate}' pattern='yyyy-MM-dd' />"
                                            data-end="<c:choose><c:when test='${not empty job.endDate}'><fmt:formatDate value='${job.endDate}' pattern='yyyy-MM-dd' /></c:when><c:otherwise></c:otherwise></c:choose>">
                                            <td>
                                                <div style="font-weight: 700;">${job.company.name}</div>
                                                <div style="font-size: 12px; color: var(--text-muted);">${job.industri}</div>
                                            </td>
                                            <td>${job.jabatan}</td>
                                            <td style="font-size: 14px;">
                                                <fmt:formatDate value="${job.startDate}" pattern="MMM yyyy" /> - 
                                                <c:choose>
                                                    <c:when test="${not empty job.endDate}"><fmt:formatDate value="${job.endDate}" pattern="MMM yyyy" /></c:when>
                                                    <c:otherwise>Sekarang</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="text-align: center;">
                                                <button type="button" onclick="openEditJob(this)" title="Edit" style="background: none; border: none; color: var(--primary); cursor: pointer; margin-right: 8px;">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <form action="${pageContext.request.contextPath}/alumni/profile" method="post" onsubmit="return confirm('Hapus pekerjaan ini?')" style="display: inline-block;">
                                                    <input type="hidden" name="action" value="deleteJob">
                                                    <input type="hidden" name="id_job" value="${job.idJobExperience}">
                                                    <button type="submit" style="background: none; border: none; color: var(--danger); cursor: pointer;">
                                                        <i class="fas fa-trash-alt"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty jobs}">
                                        <tr>
                                            <td colspan="4" style="text-align: center; padding: 40px; color: var(--text-muted);">
                                                Belum ada data pekerjaan. Klik "Tambah Kerja" untuk memulai.
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </main>
    </div>

    <!-- Modal Tambah / Edit Pekerjaan -->
    <div id="modalJob" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 2000; align-items: center; justify-content: center;">
        <div class="auth-card" style="max-width: 600px;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h3 id="modalJobTitle" style="font-weight: 800; color: var(--primary);">Tambah Riwayat Pekerjaan</h3>
                <button type="button" onclick="closeModalJob()" style="background: none; border: none; font-size: 24px; cursor: pointer;">&times;</button>
            </div>
            <form id="jobForm" action="${pageContext.request.contextPath}/alumni/profile" method="post">
                <input type="hidden" name="action" id="modal_action" value="addJob">
                <input type="hidden" name="id_job" id="modal_id_job" value="">
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                    <div class="form-group">
                        <label>Nama Perusahaan</label>
                        <input type="text" name="company_name" id="company_name_input" class="form-control" placeholder="Contoh: Google" list="companyList" autocomplete="off" required>
                        <small style="color: var(--text-muted); font-size: 12px;">Ketik nama perusahaan, lalu pilih dari daftar yang muncul.</small>
                        <datalist id="companyList">
                            <c:forEach var="company" items="${allCompanies}">
                                <option value="${company.name}" data-location="${company.location}">
                            </c:forEach>
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label>Lokasi Kantor</label>
                        <input type="text" name="location" id="company_location_input" class="form-control" placeholder="Jakarta / Remote" required>
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                    <div class="form-group">
                        <label>Bidang Industri</label>
                        <select name="industri" id="industri_input" class="form-control" required>
                            <option value="Teknologi">Teknologi</option>
                            <option value="Keuangan">Keuangan</option>
                            <option value="Pendidikan">Pendidikan</option>
                            <option value="Manufaktur">Manufaktur</option>
                            <option value="Kesehatan">Kesehatan</option>
                            <option value="Lainnya">Lainnya</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Jabatan</label>
                        <input type="text" name="jabatan" id="jabatan_input" class="form-control" placeholder="Software Engineer" required>
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                    <div class="form-group">
                        <label>Tanggal Mulai</label>
                        <input type="date" name="start_date" id="start_date_input" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Tanggal Selesai (Kosongkan jika aktif)</label>
                        <input type="date" name="end_date" id="end_date_input" class="form-control">
                    </div>
                </div>
                <button type="submit" id="modal_submit_btn" class="btn btn-primary">Tambah Sekarang</button>
            </form>
        </div>
    </div>

    <script>
        const companyNameInput = document.getElementById('company_name_input');
        const companyLocationInput = document.getElementById('company_location_input');
        const industriInput = document.getElementById('industri_input');
        const jabatanInput = document.getElementById('jabatan_input');
        const startDateInput = document.getElementById('start_date_input');
        const endDateInput = document.getElementById('end_date_input');
        const submitBtn = document.getElementById('modal_submit_btn');

        function validateJobForm() {
            const isCompanyNameValid = companyNameInput.value.trim() !== '';
            const isLocationValid = companyLocationInput.value.trim() !== '';
            const isIndustriValid = industriInput.value !== '';
            const isJabatanValid = jabatanInput.value.trim() !== '';
            const isStartDateValid = startDateInput.value !== '';

            let isEndDateValid = true;
            if (startDateInput.value && endDateInput.value) {
                isEndDateValid = endDateInput.value >= startDateInput.value;
            }

            const isValid = isCompanyNameValid && isLocationValid && isIndustriValid && isJabatanValid && isStartDateValid && isEndDateValid;
            submitBtn.disabled = !isValid;
        }

        function closeModalJob() {
            document.getElementById('modalJob').style.display = 'none';
            document.getElementById('modal_action').value = 'addJob';
            document.getElementById('modal_id_job').value = '';
            document.getElementById('modalJobTitle').textContent = 'Tambah Riwayat Pekerjaan';
            document.getElementById('modal_submit_btn').textContent = 'Tambah Sekarang';
            document.getElementById('jobForm').reset();
            endDateInput.removeAttribute('min');
            validateJobForm();
        }

        function openAddJob() {
            closeModalJob();
            document.getElementById('modalJob').style.display = 'flex';
            validateJobForm();
        }

        function openEditJob(button) {
            const tr = button.closest('tr');
            if (!tr) return;

            companyNameInput.value = tr.dataset.company || '';
            companyLocationInput.value = tr.dataset.location || '';
            industriInput.value = tr.dataset.industri || 'Teknologi';
            jabatanInput.value = tr.dataset.jabatan || '';
            
            const startDate = tr.dataset.start || '';
            startDateInput.value = startDate;
            endDateInput.value = tr.dataset.end || '';

            if (startDate) {
                endDateInput.min = startDate;
            } else {
                endDateInput.removeAttribute('min');
            }

            document.getElementById('modal_action').value = 'editJob';
            document.getElementById('modal_id_job').value = tr.dataset.id || '';
            document.getElementById('modalJobTitle').textContent = 'Edit Riwayat Pekerjaan';
            document.getElementById('modal_submit_btn').textContent = 'Simpan Perubahan';
            document.getElementById('modalJob').style.display = 'flex';
            validateJobForm();
        }

        startDateInput.addEventListener('change', function() {
            endDateInput.min = this.value;
            if (endDateInput.value && endDateInput.value < this.value) {
                endDateInput.value = '';
            }
            validateJobForm();
        });

        endDateInput.addEventListener('change', validateJobForm);
        companyLocationInput.addEventListener('input', validateJobForm);
        industriInput.addEventListener('change', validateJobForm);
        jabatanInput.addEventListener('input', validateJobForm);

        companyNameInput.addEventListener('input', function() {
            const val = this.value;
            const opts = document.getElementById('companyList').childNodes;
            for (let i = 0; i < opts.length; i++) {
                if (opts[i].value === val) {
                    const loc = opts[i].getAttribute('data-location');
                    if (loc) {
                        companyLocationInput.value = loc;
                    }
                    break;
                }
            }
            validateJobForm();
        });
    </script>
</body>
</html>
