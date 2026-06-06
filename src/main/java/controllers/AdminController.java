package controllers;

import models.Admin;
import models.Alumni;
import models.CareerStatistic;
import models.EmailNotification;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {

    
    private Admin getAdminFromSession(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
    
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return null;
        }
        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return null;
        }
        return (Admin) session.getAttribute("user");
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Admin admin = getAdminFromSession(request, response);
        if (admin == null) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/dashboard";

        if (pathInfo.equals("/dashboard")) {
            showDashboard(request, response, admin);
        } else if (pathInfo.equals("/alumni")) {
            showManageAlumni(request, response, admin);
        } else if (pathInfo.equals("/email-log")) {
            showEmailLog(request, response, admin);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Admin admin = getAdminFromSession(request, response);
        if (admin == null) return;

        String action = request.getParameter("action");

        if ("addAlumni".equals(action)) {
            handleAddAlumni(request, response, admin);
        } else if ("addAdmin".equals(action)) {
            handleAddAdmin(request, response, admin);
        } else if ("deleteAlumni".equals(action)) {
            handleDeleteAlumni(request, response, admin);
        } else if ("sendNotification".equals(action)) {
            handleSendNotification(request, response, admin);
        }
    }

  
    private void showDashboard(HttpServletRequest request, HttpServletResponse response, Admin admin)
            throws ServletException, IOException {

       
        int totalAlumni      = admin.getTotalAlumni();
        int alumniAktif      = admin.getAlumniAktifBekerja();
        int emailTerkirim    = admin.getEmailTerkirimBulanIni();

     
        ArrayList<Alumni> daftarAlumni = admin.getDaftarAlumni();
        ArrayList<Alumni> recentAlumni = new ArrayList<>();
        for (int i = 0; i < Math.min(10, daftarAlumni.size()); i++) {
            recentAlumni.add(daftarAlumni.get(i));
        }

        request.setAttribute("totalAlumni", totalAlumni);
        request.setAttribute("alumniAktif", alumniAktif);
        request.setAttribute("emailTerkirim", emailTerkirim);
        request.setAttribute("daftarAlumni", recentAlumni);
        request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
    }

    
    private void showManageAlumni(HttpServletRequest request, HttpServletResponse response, Admin admin)
            throws ServletException, IOException {

     
        String keyword = request.getParameter("q");
        String statusFilter = request.getParameter("status");
        ArrayList<Alumni> daftarAlumni;

        if (keyword != null && !keyword.isEmpty()) {
            daftarAlumni = admin.searchAlumni(keyword);
            request.setAttribute("keyword", keyword);
        } else {
            daftarAlumni = admin.getDaftarAlumni();
        }

        if (statusFilter != null && !statusFilter.isEmpty() && !"all".equalsIgnoreCase(statusFilter)) {
            ArrayList<Alumni> filtered = new ArrayList<>();
            for (Alumni alumni : daftarAlumni) {
                if (statusFilter.equalsIgnoreCase(alumni.getStatusCode())) {
                    filtered.add(alumni);
                }
            }
            daftarAlumni = filtered;
        }

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) { page = 1; }

        int pageSize = 10;
        int totalData = daftarAlumni.size();
        int totalPages = (int) Math.ceil((double) totalData / pageSize);

        int start = (page - 1) * pageSize;
        int end   = Math.min(start + pageSize, totalData);
        ArrayList<Alumni> pageData = new ArrayList<>();
        for (int i = start; i < end; i++) {
            pageData.add(daftarAlumni.get(i));
        }

        request.setAttribute("daftarAlumni", pageData);
        request.setAttribute("totalData", totalData);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("statusFilter", statusFilter != null ? statusFilter : "all");
        request.getRequestDispatcher("/views/admin/manage_alumni.jsp").forward(request, response);
    }

    
    private void showEmailLog(HttpServletRequest request, HttpServletResponse response, Admin admin)
            throws ServletException, IOException {

        ArrayList<Map<String, Object>> logs = EmailNotification.getAllLogs();
        request.setAttribute("emailLogs", logs);
        request.getRequestDispatcher("/views/admin/email_log.jsp").forward(request, response);
    }

    
    private void handleAddAlumni(HttpServletRequest request, HttpServletResponse response, Admin admin)
            throws IOException, ServletException {

        String name     = request.getParameter("name");
        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String major    = request.getParameter("major");
        String yearStr  = request.getParameter("enrollment_year");

        int enrollmentYear = 2020;
        try { enrollmentYear = Integer.parseInt(yearStr); } catch (Exception e) { }

       
        Alumni alumni = new Alumni(name, email, password, major, enrollmentYear);
        alumni.setIdUser(alumni.generateID());
        alumni.setRole("alumni");

       
        if (!admin.verifyAlumniData(alumni)) {
            request.setAttribute("error", "Data alumni tidak lengkap");
            showManageAlumni(request, response, admin);
            return;
        }

        boolean success = admin.tambahAlumni(alumni);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/alumni?success=add");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/alumni?error=add");
        }
    }

    private void handleAddAdmin(HttpServletRequest request, HttpServletResponse response, Admin admin)
            throws IOException, ServletException {

        String name     = request.getParameter("name");
        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String jabatan  = request.getParameter("jabatan");

        Admin newAdmin = new Admin();
        newAdmin.setIdUser(newAdmin.generateID());
        newAdmin.setName(name);
        newAdmin.setEmail(email);
        newAdmin.setPassword(password);
        newAdmin.setRole("admin");
        newAdmin.setJabatan(jabatan);

        if (!admin.verifyAdminData(newAdmin)) {
            request.setAttribute("errorAdmin", "Data admin tidak lengkap");
            showManageAlumni(request, response, admin);
            return;
        }

        boolean success = admin.tambahAdmin(newAdmin);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/alumni?successAdmin=1");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/alumni?errorAdmin=1");
        }
    }

    private void handleDeleteAlumni(HttpServletRequest request, HttpServletResponse response, Admin admin)
            throws IOException {

        String idAlumni = request.getParameter("id_alumni");
        boolean success = admin.deleteAlumni(idAlumni);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/alumni?success=delete");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/alumni?error=delete");
        }
    }


    private void handleSendNotification(HttpServletRequest request, HttpServletResponse response, Admin admin)
            throws IOException, ServletException {

        String subject = request.getParameter("subject");
        String body    = request.getParameter("body");

        if (subject == null || subject.isEmpty()) {
            subject = "Pengingat Update Data Alumni - SiAlumni";
        }
        subject = "SiAlumni: " + subject;
        if (body == null || body.isEmpty()) {
            body = "Mohon perbarui data riwayat pekerjaan Anda di sistem SiAlumni.";
        }

        
        ArrayList<Alumni> allAlumni = admin.getDaftarAlumni();
        ArrayList<Alumni> targetAlumni = new ArrayList<>();

        String sendToAll = request.getParameter("send_to_all");
        String[] selectedIds = request.getParameterValues("selected_alumni_ids");

        if ("true".equals(sendToAll) || selectedIds == null || selectedIds.length == 0) {
            targetAlumni = allAlumni;
        } else {
          
            for (String id : selectedIds) {
                for (Alumni a : allAlumni) {
                    if (a.getIdUser().equals(id)) {
                        targetAlumni.add(a);
                        break;
                    }
                }
            }
        }

        int berhasil = 0;
        int gagal    = 0;

        
        for (Alumni alumni : targetAlumni) {
            EmailNotification notif = new EmailNotification(
                alumni.getEmail(), subject, body, new java.util.Date()
            );
            boolean ok = notif.sendReminder(alumni);
            if (ok) berhasil++;
            else gagal++;
        }

        request.setAttribute("notifBerhasil", berhasil);
        request.setAttribute("notifGagal", gagal);
        ArrayList<java.util.Map<String, Object>> logs = EmailNotification.getAllLogs();
        request.setAttribute("emailLogs", logs);
        request.getRequestDispatcher("/views/admin/email_log.jsp").forward(request, response);
    }
}
