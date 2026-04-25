package com.telkom.alumni.controller;

import com.telkom.alumni.model.Alumni;
import com.telkom.alumni.model.Company;
import com.telkom.alumni.model.Industri;
import com.telkom.alumni.model.JobExperience;
import com.telkom.alumni.service.AlumniService;
import com.telkom.alumni.service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/alumni")
public class AlumniController {
    
    @Autowired
    private AlumniService alumniService;
    
    @Autowired
    private CareerService careerService;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Alumni alumni = (Alumni) session.getAttribute("user");
        if (alumni == null) return "redirect:/login";
        
        model.addAttribute("alumni", alumni);
        model.addAttribute("jobCount", alumni.getJobExperience().size());
        return "alumni-dashboard";
    }
    
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Alumni alumni = (Alumni) session.getAttribute("user");
        if (alumni == null) return "redirect:/login";
        
        model.addAttribute("alumni", alumni);
        return "profile";
    }
    
    @PostMapping("/profile/update")
    public String updateProfile(HttpSession session,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam(required = false) String password) {
        Alumni alumni = (Alumni) session.getAttribute("user");
        if (alumni == null) return "redirect:/login";
        
        alumni.setName(name);
        alumni.setEmail(email);
        if (password != null && !password.isEmpty()) {
            alumni.setPassword(password);
        }
        
        alumniService.saveAlumni(alumni);
        session.setAttribute("user", alumni);
        
        return "redirect:/alumni/profile";
    }
    
    @GetMapping("/career")
    public String careerTracking(HttpSession session, Model model) {
        Alumni alumni = (Alumni) session.getAttribute("user");
        if (alumni == null) return "redirect:/login";
        
        model.addAttribute("alumni", alumni);
        model.addAttribute("industries", Industri.values());
        model.addAttribute("companies", careerService.getAllCompanies());
        return "career-tracking";
    }
    
    @PostMapping("/career/add")
    public String addJob(HttpSession session,
                         @RequestParam String idJobExperience,
                         @RequestParam Industri industri,
                         @RequestParam String jabatan,
                         @RequestParam String companyId,
                         @RequestParam String startDate,
                         @RequestParam(required = false) String endDate) throws Exception {
        
        Alumni alumni = (Alumni) session.getAttribute("user");
        if (alumni == null) return "redirect:/login";
        
        Company company = careerService.getCompanyByName(companyId);
        if (company == null) {
            company = new Company(companyId, companyId, "Lokasi belum diisi");
            careerService.saveCompany(company);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        Date end = (endDate != null && !endDate.isEmpty()) ? sdf.parse(endDate) : null;
        
        JobExperience job = new JobExperience(idJobExperience, industri, jabatan, company, start, end);
        alumniService.addJobExperience(alumni.getIdUser(), job);
        
        return "redirect:/alumni/career";
    }
    
    @PostMapping("/career/delete/{index}")
    public String deleteJob(HttpSession session, @PathVariable int index) {
        Alumni alumni = (Alumni) session.getAttribute("user");
        if (alumni == null) return "redirect:/login";
        
        alumniService.deleteJobExperience(alumni.getIdUser(), index);
        return "redirect:/alumni/career";
    }
}