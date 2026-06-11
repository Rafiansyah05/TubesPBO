package controllers;

import models.Admin;
import models.Alumni;
import models.JDBC;
import models.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;


@WebServlet("/auth")
public class AuthController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

      
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            String role = (String) session.getAttribute("role");
            if ("admin".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/alumni/dashboard");
            }
            return;
        }

      
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            handleLogin(request, response);
        } else if ("logout".equals(action)) {
            handleLogout(request, response);
        } else if ("register".equals(action)) {
            handleRegister(request, response);
        }
    }

  
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

     
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Email dan password tidak boleh kosong");
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            return;
        }

        User loginUser = new User();
        if (loginUser.login(email, password)) {
            if ("admin".equals(loginUser.getRole())) {
                Admin admin = new Admin(loginUser.getIdUser(), loginUser.getName(), loginUser.getEmail(), loginUser.getPassword(), null);
                HttpSession session = request.getSession();
                session.setAttribute("user", admin);
                session.setAttribute("role", "admin");
                session.setAttribute("userName", admin.getName());
                session.setMaxInactiveInterval(30 * 60); // 30 menit
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            } else if ("alumni".equals(loginUser.getRole())) {
                Alumni alumni = new Alumni(loginUser.getIdUser(), loginUser.getName(), loginUser.getEmail(), loginUser.getPassword(), null, 0, 0);
                alumni = getAlumniData(alumni);
               
                HttpSession session = request.getSession();
                session.setAttribute("user", alumni);
                session.setAttribute("role", "alumni");
                session.setAttribute("userName", alumni.getName());
                session.setMaxInactiveInterval(30 * 60);
                response.sendRedirect(request.getContextPath() + "/alumni/dashboard");
                return;
            }
        }

   
        request.setAttribute("error", "Email atau password salah. Silakan coba lagi.");
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }

    private Alumni getAlumniData(Alumni alumni) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBC.getConnection();
            String sql = "SELECT a.enrollment_year, a.major, a.jumlah_job "
                       + "FROM alumni a WHERE a.id_user = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, alumni.getIdUser());
            rs = ps.executeQuery();

            if (rs.next()) {
                alumni.setEnrollmentYear(rs.getInt("enrollment_year"));
                alumni.setMajor(rs.getString("major"));
                alumni.setJumlahJob(rs.getInt("jumlah_job"));
                System.out.println("Loaded alumni data for: " + alumni.getIdUser() + ", Year: " + alumni.getEnrollmentYear());
            } else {
                System.out.println("No alumni record found for id_user: " + alumni.getIdUser());
            }
        } catch (Exception e) {
            System.out.println("Error getAlumniData for " + alumni.getIdUser() + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources in getAlumniData: " + e.getMessage());
            }
        }
        return alumni;
    }


    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

 
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/auth");
    }


    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name     = request.getParameter("name");
        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String major    = request.getParameter("major");
        String yearStr  = request.getParameter("enrollment_year");


        Alumni tempUser = new Alumni();
        if (!tempUser.validateEmail(email)) {
            request.setAttribute("error", "Format email tidak valid");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }

        int enrollmentYear = 2020;
        try {
            enrollmentYear = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            enrollmentYear = 2020;
        }


        Alumni alumni = new Alumni(name, email, password, major, enrollmentYear);
        alumni.setIdUser(alumni.generateID());
        alumni.setRole("alumni");

  
        boolean success = alumni.insertAlumni();

        if (success) {
            request.setAttribute("success", "Registrasi berhasil! Silakan login.");
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Registrasi gagal. Email mungkin sudah digunakan.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
        }
    }
}
