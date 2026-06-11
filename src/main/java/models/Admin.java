package models;

import interfaces.Searching;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
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

    public boolean verifyAdminData(Admin admin) {
        return admin.getName()  != null && !admin.getName().isEmpty()
            && admin.getEmail() != null && !admin.getEmail().isEmpty()
            && admin.getJabatan() != null && !admin.getJabatan().isEmpty();
    }

    public boolean tambahAdmin(Admin admin) {
        try {
            connect();
            if (conn == null) return false;
            
            // Insert into users table
            String sqlUser = "INSERT INTO users (id_user, name, email, password, role) VALUES (?, ?, ?, ?, 'admin')";
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, admin.getIdUser());
            psUser.setString(2, admin.getName());
            psUser.setString(3, admin.getEmail());
            psUser.setString(4, hashPassword(admin.getPassword()));
            psUser.executeUpdate();

            // Insert into admin table
            String sqlAdmin = "INSERT INTO admin (id_user, jabatan) VALUES (?, ?)";
            PreparedStatement psAdmin = conn.prepareStatement(sqlAdmin);
            psAdmin.setString(1, admin.getIdUser());
            psAdmin.setString(2, admin.getJabatan());
            psAdmin.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error tambah admin: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

   
    public ArrayList<Alumni> getDaftarAlumni() {
        ArrayList<Alumni> list = new ArrayList<>();
        try {
            connect();
            if (conn == null) return list;
            String sql = "SELECT u.id_user, u.name, u.email, u.password, u.role, "
                       + "a.enrollment_year, a.major, a.jumlah_job, u.created_at, "
                       + "COALESCE((SELECT MAX(j.created_at) FROM job_experience j WHERE j.id_alumni = u.id_user), u.created_at) AS last_touch "
                       + "FROM users u "
                       + "JOIN alumni a ON u.id_user = a.id_user "
                       + "ORDER BY u.name ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
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
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    Timestamp lastTouch = rs.getTimestamp("last_touch");
                    String[] status = buildStatus(createdAt, lastTouch);
                    alumni.setStatusLabel(status[0]);
                    alumni.setStatusClass(status[1]);
                    alumni.setStatusCode(status[2]);
                    list.add(alumni);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getDaftarAlumni: " + e.getMessage());
        } finally {
            disconnect();
        }
        this.daftarAlumni = list;
        this.jumlahAlumni = list.size();
        return list;
    }

    /**
     * Retrieve paginated list of alumni with optional offset and limit.
     * Uses SQL LIMIT/OFFSET for efficient data fetching.
     */
    public java.util.List<Alumni> getDaftarAlumniPaginated(int offset, int limit) {
        java.util.List<Alumni> list = new java.util.ArrayList<>();
        String sql = "SELECT u.id_user, u.name, u.email, u.password, u.role, "
                   + "a.enrollment_year, a.major, a.jumlah_job, u.created_at, "
                   + "COALESCE((SELECT MAX(j.created_at) FROM job_experience j WHERE j.id_alumni = u.id_user), u.created_at) AS last_touch "
                   + "FROM users u JOIN alumni a ON u.id_user = a.id_user "
                   + "ORDER BY u.name ASC LIMIT ? OFFSET ?";
        try {
            connect();
            if (conn == null) return list;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, limit);
                ps.setInt(2, offset);
                try (ResultSet rs = ps.executeQuery()) {
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
                        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
                        java.sql.Timestamp lastTouch = rs.getTimestamp("last_touch");
                        String[] status = buildStatus(createdAt, lastTouch);
                        alumni.setStatusLabel(status[0]);
                        alumni.setStatusClass(status[1]);
                        alumni.setStatusCode(status[2]);
                        list.add(alumni);
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error getDaftarAlumniPaginated: " + e.getMessage());
        } finally {
            disconnect();
        }
        this.daftarAlumni = new ArrayList<>(list);
        this.jumlahAlumni = list.size();
        return list;
    }

  
    public ArrayList<Alumni> searchAlumni(String keyword) {
        ArrayList<Alumni> result = new ArrayList<>();
        try {
            connect();
            if (conn == null) return result;
            String sql = "SELECT u.id_user, u.name, u.email, u.password, "
                       + "a.enrollment_year, a.major, a.jumlah_job, u.created_at, "
                       + "COALESCE((SELECT MAX(j.created_at) FROM job_experience j WHERE j.id_alumni = u.id_user), u.created_at) AS last_touch "
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
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp lastTouch = rs.getTimestamp("last_touch");
                String[] status = buildStatus(createdAt, lastTouch);
                alumni.setStatusLabel(status[0]);
                alumni.setStatusClass(status[1]);
                alumni.setStatusCode(status[2]);
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

   
    private String[] buildStatus(Timestamp createdAt, Timestamp lastTouch) {
        if (lastTouch == null) {
            lastTouch = createdAt;
        }
        if (createdAt == null || lastTouch.equals(createdAt)) {
            return new String[]{"Belum diperbarui", "badge-warning", "belum_update"};
        }

        long days = Duration.between(lastTouch.toInstant(), Instant.now()).toDays();
        if (days <= 180) {
            return new String[]{"Terbaru", "badge-success", "aktif"};
        }
        return new String[]{"Perlu Diperbarui", "badge-warning", "perlu_update"};
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
