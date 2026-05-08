package controllers;

import models.CareerStatistic;
import models.Company;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet("")
public class PublicController extends HttpServlet {

 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

  
        String major = request.getParameter("major");

  
        CareerStatistic stat = new CareerStatistic();

     
        ArrayList<Company> companies = stat.getTopTenCompaniesByMajor(major);

 
        request.setAttribute("companies", companies);
        request.setAttribute("selectedMajor", major);

        String[] majors = {
            "Teknik Informatika", "Sistem Informasi",
            "Manajemen Bisnis", "Akuntansi", "Teknik Elektro"
        };
        request.setAttribute("majors", majors);

        request.getRequestDispatcher("/views/public/index.jsp").forward(request, response);
    }
}
