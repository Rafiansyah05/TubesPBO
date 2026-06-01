package controllers;

import models.Alumni;
import models.Company;
import models.JobExperience;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;


@WebServlet("/alumni/*")
public class AlumniController extends HttpServlet {

   
    private Alumni getAlumniFromSession(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
      
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return null;
        }
    
        String role = (String) session.getAttribute("role");
        if (!"alumni".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return null;
        }
        return (Alumni) session.getAttribute("user");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Alumni alumni = getAlumniFromSession(request, response);
        if (alumni == null) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/dashboard";

        if (pathInfo.equals("/dashboard")) {
            showDashboard(request, response, alumni);
        } else if (pathInfo.equals("/profile")) {
            showProfile(request, response, alumni);
        } else if (pathInfo.equals("/jobs")) {
            showJobs(request, response, alumni);
        } else {
            response.sendRedirect(request.getContextPath() + "/alumni/dashboard");
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Alumni alumni = getAlumniFromSession(request, response);
        if (alumni == null) return;

        String action = request.getParameter("action");

        if ("addJob".equals(action)) {
            handleAddJob(request, response, alumni);
        } else if ("editJob".equals(action)) {
            handleEditJob(request, response, alumni);
        } else if ("deleteJob".equals(action)) {
            handleDeleteJob(request, response, alumni);
        } else if ("updateProfile".equals(action)) {
            handleUpdateProfile(request, response, alumni);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response, Alumni alumni)
            throws ServletException, IOException {
        
        ArrayList<JobExperience> jobs = alumni.getJobExperience();
        ArrayList<JobExperience> recentJobs = new ArrayList<>();
        for (int i = 0; i < Math.min(3, jobs.size()); i++) {
            recentJobs.add(jobs.get(i));
        }

       
        int aktifCount = 0;
        for (JobExperience job : jobs) {
            if (job.isAktif()) aktifCount++;
        }

        request.setAttribute("jobs", recentJobs);
        request.setAttribute("totalJobs", jobs.size());
        request.setAttribute("aktifJobs", aktifCount);
        request.setAttribute("profileComplete", alumni.isProfileComplete());
        request.getRequestDispatcher("/views/alumni/dashboard.jsp").forward(request, response);
    }

   
    private void showProfile(HttpServletRequest request, HttpServletResponse response, Alumni alumni)
            throws ServletException, IOException {
       
        ArrayList<JobExperience> jobs = alumni.getJobExperience();
        request.setAttribute("jobs", jobs);
        request.setAttribute("allCompanies", Company.getAllCompanies());
        request.setAttribute("profileComplete", alumni.isProfileComplete());
        request.getRequestDispatcher("/views/alumni/profile.jsp").forward(request, response);
    }

    
    private void showJobs(HttpServletRequest request, HttpServletResponse response, Alumni alumni)
            throws ServletException, IOException {
        ArrayList<JobExperience> jobs = alumni.getJobExperience();
        request.setAttribute("jobs", jobs);
        request.setAttribute("allCompanies", Company.getAllCompanies());
        request.getRequestDispatcher("/views/alumni/profile.jsp").forward(request, response);
    }

   
    private void handleAddJob(HttpServletRequest request, HttpServletResponse response, Alumni alumni)
            throws IOException {
        String companyName = request.getParameter("company_name");
        String location    = request.getParameter("location");
        String industri    = request.getParameter("industri");
        String jabatan     = request.getParameter("jabatan");
        String startStr    = request.getParameter("start_date");
        String endStr      = request.getParameter("end_date");

       
        Company company = Company.findOrCreate(companyName, location);
        if (company == null) {
            response.sendRedirect(request.getContextPath() + "/alumni/profile?error=company");
            return;
        }

      
        Date startDate = null;
        Date endDate   = null;
        try {
            if (startStr != null && !startStr.isEmpty()) startDate = Date.valueOf(startStr);
            if (endStr != null && !endStr.isEmpty()) endDate = Date.valueOf(endStr);
        } catch (Exception e) {
            System.out.println("Error parse date: " + e.getMessage());
        }

      
        JobExperience job = new JobExperience(industri, jabatan, startDate, endDate, company);

        
        boolean success = alumni.addJob(job);

       
        request.getSession().setAttribute("user", alumni);
        response.sendRedirect(request.getContextPath() + "/alumni/profile");
    }

    
    private void handleEditJob(HttpServletRequest request, HttpServletResponse response, Alumni alumni)
            throws IOException {
        String idJob       = request.getParameter("id_job");
        String companyName = request.getParameter("company_name");
        String location    = request.getParameter("location");
        String industri    = request.getParameter("industri");
        String jabatan     = request.getParameter("jabatan");
        String startStr    = request.getParameter("start_date");
        String endStr      = request.getParameter("end_date");

        Company company = Company.findOrCreate(companyName, location);
        if (company == null) {
            response.sendRedirect(request.getContextPath() + "/alumni/profile");
            return;
        }

        Date startDate = null;
        Date endDate   = null;
        try {
            if (startStr != null && !startStr.isEmpty()) startDate = Date.valueOf(startStr);
            if (endStr != null && !endStr.isEmpty()) endDate = Date.valueOf(endStr);
        } catch (Exception e) { }

        JobExperience job = new JobExperience(industri, jabatan, startDate, endDate, company);
        job.setIdJobExperience(idJob);
        job.update();

        // refresh job list in alumni object and update session
        alumni.getJobExperience();
        request.getSession().setAttribute("user", alumni);

        response.sendRedirect(request.getContextPath() + "/alumni/profile");
    }

   
    private void handleDeleteJob(HttpServletRequest request, HttpServletResponse response, Alumni alumni)
            throws IOException {
        String idJob = request.getParameter("id_job");
        alumni.deleteJob(idJob);
        
        request.getSession().setAttribute("user", alumni);
        response.sendRedirect(request.getContextPath() + "/alumni/profile");
    }


    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, Alumni alumni)
            throws ServletException, IOException {
        String name     = request.getParameter("name");
        String email    = request.getParameter("email");
        String major    = request.getParameter("major");
        String yearStr  = request.getParameter("enrollment_year");
        String newPass  = request.getParameter("new_password");

        
        if (name != null && !name.isEmpty()) alumni.setName(name);
        if (email != null && !email.isEmpty()) alumni.setEmail(email);
        if (major != null && !major.isEmpty()) alumni.setMajor(major);

        try {
            if (yearStr != null && !yearStr.isEmpty()) {
                alumni.setEnrollmentYear(Integer.parseInt(yearStr));
            }
        } catch (NumberFormatException e) { }

        alumni.updateAlumni();
        
        // Update session
        request.getSession().setAttribute("user", alumni);

        if (newPass != null && !newPass.isEmpty()) {
            alumni.setPassword(newPass);
            alumni.updatePassword(newPass);
        }

        request.setAttribute("user", alumni);
        request.setAttribute("success", "Profil berhasil diperbarui");
        request.setAttribute("profileComplete", alumni.isProfileComplete());
        ArrayList<JobExperience> jobs = alumni.getJobExperience();
        request.setAttribute("jobs", jobs);
        request.getRequestDispatcher("/views/alumni/profile.jsp").forward(request, response);
    }
}
