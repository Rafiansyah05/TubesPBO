package models;

import interfaces.GenerateID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class Company extends JDBC implements GenerateID, interfaces.Searching {

    private String idCompany;
    private String name;
    private String location;
    private java.util.ArrayList<Alumni> daftarAlumni;
    private int jumlahAlumni;
    private static int compCounter = 10;

    public Company() {
        this.daftarAlumni = new java.util.ArrayList<>();
    }

    public Company(String idCompany, String name, String location) {
        this.idCompany    = idCompany;
        this.name         = name;
        this.location     = location;
        this.daftarAlumni = new java.util.ArrayList<>();
    }

    private static String normalizeText(String value) {
        if (value == null) return "";
        return value.trim().replaceAll("\\s+", " ");
    }

    private static String normalizeKey(String value) {
        return normalizeText(value).toLowerCase(Locale.ROOT);
    }

    @Override
    public String generateID() {
        compCounter++;
        return "comp-" + String.format("%03d", compCounter);
    }

    @Override
    public boolean cekKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) return true;
        return this.name != null && this.name.toLowerCase().contains(keyword.toLowerCase());
    }

    public boolean insertCompany() {
        try {
            connect();
            if (conn == null) return false;
            String sql = "INSERT INTO companies (id_company, name, location, jumlah_alumni) VALUES (?, ?, ?, 0) "
                       + "ON CONFLICT (id_company) DO NOTHING";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.idCompany);
            ps.setString(2, this.name);
            ps.setString(3, this.location);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error insert company: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

 
    public static Company findOrCreate(String companyName, String location) {
        String normalizedName = normalizeText(companyName);
        String normalizedLocation = normalizeText(location);

        if (normalizedName.isEmpty()) {
            return null;
        }

        try (Connection conn = JDBC.getConnection()) {
            String sqlFind = "SELECT * FROM companies "
                           + "WHERE LOWER(TRIM(REGEXP_REPLACE(name, '\\s+', ' ', 'g'))) = LOWER(?)";
            PreparedStatement psFind = conn.prepareStatement(sqlFind);
            psFind.setString(1, normalizedName);
            java.sql.ResultSet rs = psFind.executeQuery();

            if (rs.next()) {
                Company existing = new Company(rs.getString("id_company"), rs.getString("name"), rs.getString("location"));
                if (normalizedLocation.isEmpty()) {
                    normalizedLocation = existing.getLocation();
                }
                return existing;
            } else {
                String newId = new Company().generateID();
                String sqlInsert = "INSERT INTO companies (id_company, name, location, jumlah_alumni) VALUES (?, ?, ?, 0)";
                PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                psInsert.setString(1, newId);
                psInsert.setString(2, normalizedName);
                psInsert.setString(3, normalizedLocation.isEmpty() ? "Indonesia" : normalizedLocation);
                psInsert.executeUpdate();
                return new Company(newId, normalizedName, normalizedLocation.isEmpty() ? "Indonesia" : normalizedLocation);
            }
        } catch (Exception e) {
            System.out.println("Error findOrCreate company: " + e.getMessage());
            return null;
        }
    }

    public static java.util.List<Company> getAllCompanies() {
        Map<String, Company> unique = new LinkedHashMap<>();
        try (Connection conn = JDBC.getConnection()) {
            String sql = "SELECT * FROM companies ORDER BY name ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String key = normalizeKey(rs.getString("name"));
                if (!unique.containsKey(key)) {
                    Company c = new Company(rs.getString("id_company"), rs.getString("name"), rs.getString("location"));
                    c.setJumlahAlumni(rs.getInt("jumlah_alumni"));
                    unique.put(key, c);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getAllCompanies: " + e.getMessage());
        }
        return new java.util.ArrayList<>(unique.values());
    }

    @Override
    public String getProfile() {
        return getInfoCompany();
    }

   
    public String getInfoCompany() {
        return "Perusahaan: " + name + " | Lokasi: " + location + " | Jumlah Alumni: " + jumlahAlumni;
    }

  
    public String getIdCompany()  { return idCompany; }
    public void setIdCompany(String id) { this.idCompany = id; }

    public String getName()       { return name; }
    public void setName(String n) { this.name = n; }

    public String getLocation()   { return location; }
    public void setLocation(String l) { this.location = l; }

    public int getJumlahAlumni() { return jumlahAlumni; }
    public void setJumlahAlumni(int j) { this.jumlahAlumni = j; }

    public java.util.ArrayList<Alumni> getDaftarAlumni() { return daftarAlumni; }
    public void setDaftarAlumni(java.util.ArrayList<Alumni> d) { this.daftarAlumni = d; }
}
