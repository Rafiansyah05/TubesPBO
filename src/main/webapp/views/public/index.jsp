<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SiAlumni - Sistem Informasi Alumni Terintegrasi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
     <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon.ico"> <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo_sialumni.png">
    <!-- Font Awesome untuk icon -->

    <link rel="stylesheet" href="https://cdnjs.cloudlare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <!-- Navigation -->
    <nav style="background: var(--white); padding: 20px 0; border-bottom: 1px solid var(--border); position: sticky; top: 0; z-index: 1000;">
        <div class="container" style="display: flex; justify-content: space-between; align-items: center;">
            <div style="font-size: 24px; font-weight: 800; color: var(--primary);">
                Si<span>Alumni</span>.
            </div>
            <div style="display: flex; gap: 30px; align-items: center;">
                <a href="${pageContext.request.contextPath}/auth" class="btn btn-primary" style="padding: 10px 24px; width: auto;">Login Alumni</a>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section style="background: linear-gradient(135deg, var(--primary) 0%, #053a72 100%); color: var(--white); padding: 100px 0; text-align: center;">
        <div class="container">
            <h1 style="font-size: 48px; font-weight: 800; margin-bottom: 20px;">Tracer Study Alumni Terintegrasi</h1>
            <p style="font-size: 18px; opacity: 0.9; max-width: 700px; margin: 0 auto 40px;">
                Pantau perkembangan karier alumni dan temukan peluang kolaborasi dengan perusahaan ternama di seluruh Indonesia.
            </p>
            <div style="display: flex; justify-content: center; gap: 20px;">
                <div class="stat-card" style="background: rgba(255,255,255,0.1); border: 1px solid rgba(255,255,255,0.2); text-align: center; min-width: 150px;">
                    <div class="value" style="color: var(--secondary); font-size: 36px;">100+</div>
                    <div class="label" style="color: var(--white); opacity: 0.8;">Alumni Terdaftar</div>
                </div>
                <div class="stat-card" style="background: rgba(255,255,255,0.1); border: 1px solid rgba(255,255,255,0.2); text-align: center; min-width: 150px;">
                    <div class="value" style="color: var(--secondary); font-size: 36px;">50+</div>
                    <div class="label" style="color: var(--white); opacity: 0.8;">Mitra Perusahaan</div>
                </div>
            </div>
        </div>
    </section>

    <!-- Main Content: Career Stats -->
    <main class="container" style="padding: 60px 0;">
        <div style="text-align: center; margin-bottom: 50px;">
            <h2 style="font-size: 32px; font-weight: 800; color: var(--primary);">Top 10 Perusahaan Favorit</h2>
            <p style="color: var(--text-muted);">Berdasarkan data penempatan kerja alumni per jurusan</p>
        </div>

        <!-- Filter -->
        <div style="margin-bottom: 40px; display: flex; justify-content: center;">
            <form action="${pageContext.request.contextPath}/" method="get" style="display: flex; gap: 10px; width: 100%; max-width: 600px;">
                <select name="major" class="form-control" style="flex: 1;">
                    <option value="">Semua Jurusan</option>
                    <c:forEach var="m" items="${majors}">
                        <option value="${m}" ${selectedMajor == m ? 'selected' : ''}>${m}</option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn btn-primary" style="width: auto; padding: 0 30px;">
                    <i class="fas fa-filter"></i> Filter
                </button>
            </form>
        </div>

        <!-- Companies Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 24px;">
            <c:forEach var="company" items="${companies}" varStatus="status">
                <div class="card" style="padding: 30px; transition: transform 0.3s ease; border-radius: 20px;">
                    <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 20px;">
                        <div style="width: 50px; height: 50px; background: var(--bg-light); border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 24px; color: var(--primary);">
                            <i class="fas fa-building"></i>
                        </div>
                        <div style="font-weight: 800; font-size: 24px; color: var(--border);">
                            #${status.index + 1}
                        </div>
                    </div>
                    <h3 style="font-size: 18px; font-weight: 700; margin-bottom: 8px;">${company.name}</h3>
                    <p style="color: var(--text-muted); font-size: 14px; margin-bottom: 20px;">
                        <i class="fas fa-map-marker-alt"></i> ${company.location}
                    </p>
                    <div style="padding-top: 20px; border-top: 1px solid var(--border); display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-size: 14px; font-weight: 600; color: var(--primary);">${company.jumlahAlumni} Alumni</span>
                        <span class="badge badge-success">Top Recruiter</span>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty companies}">
                <div style="grid-column: 1 / -1; text-align: center; padding: 50px; background: var(--white); border-radius: 20px;">
                    <i class="fas fa-search" style="font-size: 48px; color: var(--border); margin-bottom: 20px;"></i>
                    <p style="color: var(--text-muted);">Data belum tersedia untuk filter ini.</p>
                </div>
            </c:if>
        </div>
    </main>

    <!-- Footer -->
    <footer style="background: var(--white); padding: 60px 0; border-top: 1px solid var(--border);">
        <div class="container" style="text-align: center;">
            <div style="font-size: 24px; font-weight: 800; color: var(--primary); margin-bottom: 20px;">
                Si<span>Alumni</span>.
            </div>
            <p style="color: var(--text-muted); margin-bottom: 30px;">Sistem Informasi Alumni Terintegrasi &copy; 2024</p>
            <div style="display: flex; justify-content: center; gap: 20px;">
                <a href="#" style="color: var(--primary); font-size: 20px;"><i class="fab fa-facebook"></i></a>
                <a href="#" style="color: var(--primary); font-size: 20px;"><i class="fab fa-twitter"></i></a>
                <a href="#" style="color: var(--primary); font-size: 20px;"><i class="fab fa-linkedin"></i></a>
            </div>
        </div>
    </footer>

</body>
</html>
