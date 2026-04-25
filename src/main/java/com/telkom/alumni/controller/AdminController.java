package com.telkom.alumni.controller;

import com.telkom.alumni.model.Admin;
import com.telkom.alumni.model.Alumni;
import com.telkom.alumni.service.AdminService;
import com.telkom.alumni.service.AlumniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AlumniService alumniService;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin == null) return "redirect:/login";
        
        model.addAttribute("admin", admin);
        model.addAttribute("alumniList", adminService.getAllAlumni());
        model.addAttribute("totalAlumni", adminService.getAllAlumni().size());
        return "admin-dashboard";
    }
    
    @GetMapping("/alumni")
    public String manageAlumni(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin == null) return "redirect:/login";
        
        model.addAttribute("alumniList", adminService.getAllAlumni());
        return "admin-dashboard";
    }
    
    @PostMapping("/alumni/add")
    public String addAlumni(@RequestParam String idUser,
                            @RequestParam String name,
                            @RequestParam String email,
                            @RequestParam String password,
                            @RequestParam int enrollmentYear,
                            @RequestParam String major) {
        
        Alumni alumni = new Alumni(idUser, name, email, password, enrollmentYear, major);
        adminService.addAlumni(alumni);
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/alumni/delete/{id}")
    public String deleteAlumni(@PathVariable String id) {
        adminService.deleteAlumni(id);
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/alumni/verify/{id}")
    public String verifyAlumni(@PathVariable String id) {
        adminService.verifyAlumni(id);
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/alumni/search")
    public String searchAlumni(@RequestParam String keyword, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin == null) return "redirect:/login";
        
        List<Alumni> results = adminService.searchAlumni(keyword);
        model.addAttribute("alumniList", results);
        model.addAttribute("admin", admin);
        model.addAttribute("searchKeyword", keyword);
        return "admin-dashboard";
    }
}