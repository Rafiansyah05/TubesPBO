package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class CareerStatistic extends JDBC {

    private ArrayList<Company> companyList;
    private int jumlahAlumni;

    public CareerStatistic() {
        this.companyList = new ArrayList<>();
    }


    public ArrayList<Company> getTopCompanies() {
        ArrayList<Company> list = new ArrayList<>();
        try {
            connect();
            if (conn == null) return list;

            String sql = "SELECT c.id_company, c.name, c.location, COUNT(DISTINCT j.id_alumni) as jml "
                       + "FROM companies c "
                       + "LEFT JOIN job_experience j ON c.id_company = j.id_company AND j.end_date IS NULL "
                       + "GROUP BY c.id_company, c.name, c.location "
                       + "ORDER BY jml DESC LIMIT 10";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Company c = new Company(rs.getString("id_company"), rs.getString("name"), rs.getString("location"));
                c.setJumlahAlumni(rs.getInt("jml"));
                list.add(c);
            }


            Collections.sort(list, new Comparator<Company>() {
                @Override
                public int compare(Company a, Company b) {
                    return b.getJumlahAlumni() - a.getJumlahAlumni();
                }
            });

            this.companyList = list;
        } catch (Exception e) {
            System.out.println("Error getTopCompanies: " + e.getMessage());
        } finally {
            disconnect();
        }
        return list;
    }

    public ArrayList<Company> getTopTenCompaniesByMajor(String major) {
        ArrayList<Company> list = new ArrayList<>();
        try {
            connect();
            if (conn == null) return list;

            String sql;
            PreparedStatement ps;

            if (major == null || major.isEmpty() || major.equalsIgnoreCase("semua")) {
                sql = "SELECT c.id_company, c.name, c.location, COUNT(DISTINCT j.id_alumni) as jml "
                    + "FROM companies c "
                    + "LEFT JOIN job_experience j ON c.id_company = j.id_company AND j.end_date IS NULL "
                    + "GROUP BY c.id_company, c.name, c.location "
                    + "ORDER BY jml DESC LIMIT 10";
                ps = conn.prepareStatement(sql);
            } else {
                sql = "SELECT c.id_company, c.name, c.location, COUNT(DISTINCT j.id_alumni) as jml "
                    + "FROM companies c "
                    + "JOIN job_experience j ON c.id_company = j.id_company AND j.end_date IS NULL "
                    + "JOIN alumni a ON j.id_alumni = a.id_user "
                    + "WHERE LOWER(a.major) = LOWER(?) "
                    + "GROUP BY c.id_company, c.name, c.location "
                    + "ORDER BY jml DESC LIMIT 10";
                ps = conn.prepareStatement(sql);
                ps.setString(1, major);
            }

            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                Company c = new Company(rs.getString("id_company"), rs.getString("name"), rs.getString("location"));
                c.setJumlahAlumni(rs.getInt("jml"));
                list.add(c);
                rank++;
            }

            Collections.sort(list, new Comparator<Company>() {
                @Override
                public int compare(Company a, Company b) {
                    return b.getJumlahAlumni() - a.getJumlahAlumni();
                }
            });

        } catch (Exception e) {
            System.out.println("Error getTopTenCompaniesByMajor: " + e.getMessage());
        } finally {
            disconnect();
        }
        return list;
    }

    public Map<String, Integer> getJobDistributionByMajor() {
        Map<String, Integer> distribution = new HashMap<>();
        try {
            connect();
            if (conn == null) return distribution;

            String sql = "SELECT industri, COUNT(*) as jumlah FROM job_experience GROUP BY industri ORDER BY jumlah DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                distribution.put(rs.getString("industri"), rs.getInt("jumlah"));
            }
        } catch (Exception e) {
            System.out.println("Error getJobDistribution: " + e.getMessage());
        } finally {
            disconnect();
        }
        return distribution;
    }

    public ArrayList<Alumni> tampilAlumniByCompany(String namaCompany) {
        ArrayList<Alumni> result = new ArrayList<>();
        try {
            connect();
            if (conn == null) return result;

            String sql = "SELECT DISTINCT u.id_user, u.name, u.email, u.password, a.major, a.enrollment_year, a.jumlah_job "
                       + "FROM users u "
                       + "JOIN alumni a ON u.id_user = a.id_user "
                       + "JOIN job_experience j ON a.id_user = j.id_alumni "
                       + "JOIN companies c ON j.id_company = c.id_company "
                       + "WHERE LOWER(c.name) LIKE LOWER(?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + namaCompany + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new Alumni(
                    rs.getString("id_user"), rs.getString("name"),
                    rs.getString("email"), rs.getString("password"),
                    rs.getString("major"), rs.getInt("enrollment_year"),
                    rs.getInt("jumlah_job")
                );
                if (user instanceof Alumni) {
                    result.add((Alumni) user);
                }
            }
        } catch (Exception e) {
            System.out.println("Error tampilAlumniByCompany: " + e.getMessage());
        } finally {
            disconnect();
        }
        return result;
    }

    public ArrayList<Map<String, Object>> countViewTop10(String filterJurusan) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        try {
            connect();
            if (conn == null) return result;

            String sql;
            PreparedStatement ps;

            if (filterJurusan == null || filterJurusan.isEmpty()) {
                sql = "SELECT a.major, c.name as company_name, COUNT(DISTINCT j.id_alumni) as alumni_count "
                    + "FROM job_experience j "
                    + "JOIN alumni a ON j.id_alumni = a.id_user "
                    + "JOIN companies c ON j.id_company = c.id_company "
                    + "WHERE j.end_date IS NULL "
                    + "GROUP BY a.major, c.name "
                    + "ORDER BY alumni_count DESC LIMIT 10";
                ps = conn.prepareStatement(sql);
            } else {
                sql = "SELECT a.major, c.name as company_name, COUNT(DISTINCT j.id_alumni) as alumni_count "
                    + "FROM job_experience j "
                    + "JOIN alumni a ON j.id_alumni = a.id_user "
                    + "JOIN companies c ON j.id_company = c.id_company "
                    + "WHERE j.end_date IS NULL AND LOWER(a.major) = LOWER(?) "
                    + "GROUP BY a.major, c.name "
                    + "ORDER BY alumni_count DESC LIMIT 10";
                ps = conn.prepareStatement(sql);
                ps.setString(1, filterJurusan);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("major", rs.getString("major"));
                row.put("company_name", rs.getString("company_name"));
                row.put("alumni_count", rs.getInt("alumni_count"));
                result.add(row);
            }
        } catch (Exception e) {
            System.out.println("Error countViewTop10: " + e.getMessage());
        } finally {
            disconnect();
        }
        return result;
    }

    public ArrayList<Company> getCompanyList() { return companyList; }
    public int getJumlahAlumni() { return jumlahAlumni; }
    public void setJumlahAlumni(int j) { this.jumlahAlumni = j; }

    @Override
    public String getProfile() {
        return "Statistik Karier";
    }
}
