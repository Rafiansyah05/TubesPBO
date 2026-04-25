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

@Controller
public class AuthController {
    
    @Autowired
    private AlumniService alumniService;
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                        @RequestParam String password,
                        HttpSession session, 
                        Model model) {
        

        Admin admin = adminService.getAdminByEmail(email);
        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("user", admin);
            session.setAttribute("role", "admin");
            return "redirect:/admin/dashboard";
        }
        
        Alumni alumni = alumniService.getAlumniByEmail(email);
        if (alumni != null && alumni.getPassword().equals(password)) {
            session.setAttribute("user", alumni);
            session.setAttribute("role", "alumni");
            return "redirect:/alumni/dashboard";
        }
        
        model.addAttribute("error", "Email atau password salah!");
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}