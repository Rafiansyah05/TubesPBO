<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daftar Akun - SiAlumni</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon.ico"> <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo_sialumni.png">
</head>
<body>

    <div class="auth-wrapper">
        <div class="auth-card" style="max-width: 500px;">
            <div class="auth-header">
                <h1>Daftar <span>Alumni</span>.</h1>
                <p>Lengkapi data untuk bergabung ke komunitas.</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="register">
                
                <div class="form-group">
                    <label for="name">Nama Lengkap</label>
                    <input type="text" id="name" name="name" class="form-control" placeholder="Masukkan nama sesuai ijazah" required>
                </div>

                <div class="form-group">
                    <label for="email">Email Kampus</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="nim@sialumni.ac.id" required>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                    <div class="form-group">
                        <label for="major">Program Studi</label>
                        <select id="major" name="major" class="form-control" required>
                            <option value="">Pilih Jurusan</option>
                            <option value="Teknik Informatika">Teknik Informatika</option>
                            <option value="Sistem Informasi">Sistem Informasi</option>
                            <option value="Manajemen Bisnis">Manajemen Bisnis</option>
                            <option value="Akuntansi">Akuntansi</option>
                            <option value="Teknik Elektro">Teknik Elektro</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="enrollment_year">Tahun Masuk</label>
                        <input type="number" id="enrollment_year" name="enrollment_year" class="form-control" placeholder="2020" min="2000" max="2024" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Buat password baru" required>
                </div>

                <button type="submit" class="btn btn-gold">Daftar Sekarang</button>
            </form>

            <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid var(--border);">
                <p style="color: var(--text-muted); font-size: 14px;">
                    Sudah punya akun? 
                    <a href="${pageContext.request.contextPath}/views/auth/login.jsp" style="color: var(--primary); font-weight: 700; text-decoration: none;">Login Disini</a>
                </p>
            </div>
        </div>
    </div>

</body>
</html>
