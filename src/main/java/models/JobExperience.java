package models;

import interfaces.GenerateID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.UUID;


public class JobExperience extends JDBC implements GenerateID {

    private String  idJobExperience;
    private String  industri;
    private String  jabatan;
    private Company company;
    private Date    startDate;
    private Date    endDate;

    public JobExperience() {
    }

   
    public JobExperience(String industri, String jabatan, Date startDate, Date endDate, Company company) {
        this.idJobExperience = generateID();
        this.industri        = industri;
        this.jabatan         = jabatan;
        this.startDate       = startDate;
        this.endDate         = endDate;
        this.company         = company;
    }

    
    @Override
    public String generateID() {
        return UUID.randomUUID().toString();
    }

    
    public boolean insert(String idAlumni) {
        if (endDate != null && endDate.before(startDate)) return false;
        try {
            connect();
            if (conn == null) return false;

            if (this.idJobExperience == null) {
                this.idJobExperience = generateID();
            }

            String sql = "INSERT INTO job_experience (id_job, id_alumni, id_company, industri, jabatan, start_date, end_date) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.idJobExperience);
            ps.setString(2, idAlumni);
            ps.setString(3, this.company.getIdCompany());
            ps.setString(4, this.industri);
            ps.setString(5, this.jabatan);
            ps.setDate(6, this.startDate);
            ps.setDate(7, this.endDate);
            ps.executeUpdate();

            
            String sqlUpdateCompany = "UPDATE companies SET jumlah_alumni = "
                                     + "(SELECT COUNT(DISTINCT id_alumni) FROM job_experience WHERE id_company = ?) "
                                     + "WHERE id_company = ?";
            PreparedStatement psCompany = conn.prepareStatement(sqlUpdateCompany);
            psCompany.setString(1, this.company.getIdCompany());
            psCompany.setString(2, this.company.getIdCompany());
            psCompany.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println("Error insert job: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    
    public boolean update() {
        if (endDate != null && endDate.before(startDate)) return false;
        try {
            connect();
            if (conn == null) return false;
            // ambil company lama untuk mengupdate statistik jika berubah
            String oldCompanyId = null;
            String selSql = "SELECT id_company FROM job_experience WHERE id_job = ?";
            PreparedStatement selPs = conn.prepareStatement(selSql);
            selPs.setString(1, this.idJobExperience);
            ResultSet rs = selPs.executeQuery();
            if (rs.next()) oldCompanyId = rs.getString("id_company");

            String sql = "UPDATE job_experience SET id_company = ?, industri = ?, jabatan = ?, "
                       + "start_date = ?, end_date = ? WHERE id_job = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.company.getIdCompany());
            ps.setString(2, this.industri);
            ps.setString(3, this.jabatan);
            ps.setDate(4, this.startDate);
            ps.setDate(5, this.endDate);
            ps.setString(6, this.idJobExperience);
            int rows = ps.executeUpdate();

            // update jumlah_alumni untuk company lama dan company baru
            try {
                String sqlUpdateCompany = "UPDATE companies SET jumlah_alumni = "
                                        + "(SELECT COUNT(DISTINCT id_alumni) FROM job_experience WHERE id_company = ?) "
                                        + "WHERE id_company = ?";
                PreparedStatement psCompany = conn.prepareStatement(sqlUpdateCompany);
                // update for new company
                psCompany.setString(1, this.company.getIdCompany());
                psCompany.setString(2, this.company.getIdCompany());
                psCompany.executeUpdate();

                // update for old company if different
                if (oldCompanyId != null && !oldCompanyId.equals(this.company.getIdCompany())) {
                    psCompany.setString(1, oldCompanyId);
                    psCompany.setString(2, oldCompanyId);
                    psCompany.executeUpdate();
                }
            } catch (Exception ex) {
                System.out.println("Error update company count after job update: " + ex.getMessage());
            }

            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error update job: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

   
    public boolean delete() {
        try {
            connect();
            if (conn == null) return false;

            String sql = "DELETE FROM job_experience WHERE id_job = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.idJobExperience);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error delete job: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    @Override
    public String getProfile() {
        return "Pekerjaan: " + jabatan + " di " + company.getName();
    }

   
    public boolean isAktif() {
        return this.endDate == null;
    }

    
    public String  getIdJobExperience()  { return idJobExperience; }
    public void    setIdJobExperience(String id) { this.idJobExperience = id; }

    public String  getIndustri()         { return industri; }
    public void    setIndustri(String i) { this.industri = i; }

    public String  getJabatan()          { return jabatan; }
    public void    setJabatan(String j)  { this.jabatan = j; }

    public Company getCompany()          { return company; }
    public void    setCompany(Company c) { this.company = c; }

    public Date    getStartDate()        { return startDate; }
    public void    setStartDate(Date d)  { this.startDate = d; }

    public Date    getEndDate()          { return endDate; }
    public void    setEndDate(Date d)    { this.endDate = d; }
}
