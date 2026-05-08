package models;

import interfaces.Searching;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Admin extends User implements Searching {

    private String  jabatan;
    private ArrayList<Alumni> daftarAlumni;
    private int     jumlahAlumni;

    public Admin() {
        super();
        this.daftarAlumni = new ArrayList<>();
    }

    public Admin(String idUser, String name, String email, String password, String jabatan) {
        super(idUser, name, email, password, "admin");
        this.jabatan      = jabatan;
        this.daftarAlumni = new ArrayList<>();
    }

    public boolean tambahAlumni(Alumni alumni) {
        return alumni.insertAlumni();
    }

    
    public boolean deleteAlumni(String idAlumni) {
        try {
            connect();
            if (conn == null) return false;
          
            String sql = "DELETE FROM users WHERE id_user = ? AND role = 'alumni'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idAlumni);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error delete alumni: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

   
    public boolean verifyAlumniData(Alumni alumni) {
        return alumni.getName()  != null && !alumni.getName().isEmpty()
            && alumni.getEmail() != null && !alumni.getEmail().isEmpty()
            && alumni.getMajor() != null && !alumni.getMajor().isEmpty()
            && alumni.getEnrollmentYear() > 0;
    }

   
    public ArrayList<Alumni> getDaftarAlumni() {
        ArrayList<Alumni> list = new ArrayList<>();
        try {
            connect();
            if (conn == null) return list;
            String sql = "SELECT u.id_user, u.name, u.email, u.password, u.role, "
                       + "a.enrollment_year, a.major, a.jumlah_job "
                       + "FROM users u "
                       + "JOIN alumni a ON u.id_user = a.id_user "
                       + "ORDER BY u.name ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Alumni alumni = new Alumni(
                    rs.getString("id_user"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("major"),
                    rs.getInt("enrollment_year"),
                    rs.getInt("jumlah_job")
                );
                list.add(alumni);
            }
            this.daftarAlumni = list;
            this.jumlahAlumni = list.size();
        } catch (SQLException e) {
            System.out.println("Error getDaftarAlumni: " + e.getMessage());
        } finally {
            disconnect();
        }
        return list;
    }

  
    public ArrayList<Alumni> searchAlumni(String keyword) {
        ArrayList<Alumni> result = new ArrayList<>();
        try {
            connect();
            if (conn == null) return result;
            String sql = "SELECT u.id_user, u.name, u.email, u.password, "
                       + "a.enrollment_year, a.major, a.jumlah_job "
                       + "FROM users u "
                       + "JOIN alumni a ON u.id_user = a.id_user "
                       + "WHERE LOWER(u.name) LIKE ? OR LOWER(a.major) LIKE ? "
                       + "ORDER BY u.name ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Alumni alumni = new Alumni(
                    rs.getString("id_user"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("major"),
                    rs.getInt("enrollment_year"),
                    rs.getInt("jumlah_job")
                );
                result.add(alumni);
            }
        } catch (SQLException e) {
            System.out.println("Error searchAlumni: " + e.getMessage());
        } finally {
            disconnect();
        }
        return result;
    }

  
    @Override
    public boolean cekKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) return true;
        String kw = keyword.toLowerCase();
        return getName() != null && getName().toLowerCase().contains(kw);
    }

   
    public int getTotalAlumni() {
        try {
            connect();
            if (conn == null) return 0;
            String sql = "SELECT COUNT(*) FROM alumni";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getTotalAlumni: " + e.getMessage());
        } finally {
            disconnect();
        }
        return 0;
    }

    public int getAlumniAktifBekerja() {
        try {
            connect();
            if (conn == null) return 0;
            String sql = "SELECT COUNT(DISTINCT id_alumni) FROM job_experience WHERE end_date IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getAlumniAktifBekerja: " + e.getMessage());
        } finally {
            disconnect();
        }
        return 0;
    }

    public int getEmailTerkirimBulanIni() {
        try {
            connect();
            if (conn == null) return 0;
            String sql = "SELECT COUNT(*) FROM email_notifications "
                       + "WHERE status = 'sent' AND DATE_TRUNC('month', sent_at) = DATE_TRUNC('month', NOW())";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getEmailTerkirim: " + e.getMessage());
        } finally {
            disconnect();
        }
        return 0;
    }

   
    @Override
    public String getProfile() {
        return "Admin: " + getName() + " | Jabatan: " + jabatan;
    }

    
    public String getJabatan()  { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }

    public ArrayList<Alumni> getDaftarAlumniList() { return daftarAlumni; }
    public int getJumlahAlumni() { return jumlahAlumni; }
}
