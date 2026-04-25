package com.telkom.alumni.controller;

import com.telkom.alumni.model.Alumni;
import com.telkom.alumni.service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/career")
public class CareerController {
    
    @Autowired
    private CareerService careerService;
    
    @GetMapping("/statistic")
    public String careerStatistic(Model model) {
        model.addAttribute("topCompanies", careerService.getTopCompanies());
        model.addAttribute("distribusi", careerService.getDistribusiByMajor());
        model.addAttribute("companies", careerService.getAllCompanies());
        return "career-statistic";
    }
    
    @GetMapping("/statistic/major")
    public String statisticByMajor(@RequestParam String major, Model model) {
        model.addAttribute("topCompanies", careerService.getTopCompaniesByMajor(major));
        model.addAttribute("alumniList", careerService.getAlumniByMajor(major));
        model.addAttribute("selectedMajor", major);
        return "career-statistic";
    }
    
    @GetMapping("/statistic/company")
    public String statisticByCompany(@RequestParam String company, Model model) {
        model.addAttribute("alumniList", careerService.getAlumniByCompany(company));
        model.addAttribute("selectedCompany", company);
        return "career-statistic";
    }
}