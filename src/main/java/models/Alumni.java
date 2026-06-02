package models;

import interfaces.Searching;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Alumni extends User implements Searching {


    private int    enrollmentYear;
    private String major;
    private ArrayList<JobExperience> jobExperience;
    private int    jumlahJob;
    private String statusLabel;
    private String statusClass;
    private String statusCode;


    public Alumni() {
        super();
        this.jobExperience = new ArrayList<>();
    }

    public Alumni(String name, String email, String password, String major, int enrollmentYear) {
        super(name, email, password);
        this.major          = major;
        this.enrollmentYear = enrollmentYear;
        this.jobExperience  = new ArrayList<>();
        this.setRole("alumni");
    }

    public Alumni(String idUser, String name, String email, String password, String major, int enrollmentYear, int jumlahJob) {
        super(idUser, name, email, password, "alumni");
        this.major          = major;
        this.enrollmentYear = enrollmentYear;
        this.jumlahJob      = jumlahJob;
        this.jobExperience  = new ArrayList<>();
    }

  
    public boolean insertAlumni() {
        try {
            connect();
            if (conn == null) return false;
           
            String sqlUser = "INSERT INTO users (id_user, name, email, password, role) VALUES (?, ?, ?, ?, 'alumni')";
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, getIdUser());
            psUser.setString(2, getName());
            psUser.setString(3, getEmail());
            psUser.setString(4, getPassword());
            psUser.executeUpdate();

       
            String sqlAlumni = "INSERT INTO alumni (id_user, enrollment_year, major, jumlah_job) VALUES (?, ?, ?, 0)";
            PreparedStatement psAlumni = conn.prepareStatement(sqlAlumni);
            psAlumni.setString(1, getIdUser());
            psAlumni.setInt(2, this.enrollmentYear);
            psAlumni.setString(3, this.major);
            psAlumni.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error insert alumni: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }


    public boolean updateAlumni() {
        try {
            connect();
            if (conn == null) return false;
            // update tabel users
            String sqlUser = "UPDATE users SET name = ?, email = ? WHERE id_user = ?";
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, getName());
            psUser.setString(2, getEmail());
            psUser.setString(3, getIdUser());
            psUser.executeUpdate();

 
            String sqlAlumni = "UPDATE alumni SET major = ?, enrollment_year = ? WHERE id_user = ?";
            PreparedStatement psAlumni = conn.prepareStatement(sqlAlumni);
            psAlumni.setString(1, this.major);
            psAlumni.setInt(2, this.enrollmentYear);
            psAlumni.setString(3, getIdUser());
            psAlumni.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error update alumni: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    @Override
    public boolean cekKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) return true;
        String kw = keyword.toLowerCase();
        return (getName()  != null && getName().toLowerCase().contains(kw))
            || (this.major != null && this.major.toLowerCase().contains(kw));
    }

  
    public boolean addJob(JobExperience job) {
      
        this.jobExperience.add(job);
      
        boolean success = job.insert(getIdUser());
        if (success) {
            this.jumlahJob++;
      
            try {
                connect();
                if (conn == null) return false;
                String sql = "UPDATE alumni SET jumlah_job = ? WHERE id_user = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, this.jumlahJob);
                ps.setString(2, getIdUser());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error update jumlah_job: " + e.getMessage());
            } finally {
                disconnect();
            }
        }
        return success;
    }

    
    public boolean deleteJob(String idJob) {
        try {
            connect();
            if (conn == null) return false;
            String sql = "DELETE FROM job_experience WHERE id_job = ? AND id_alumni = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idJob);
            ps.setString(2, getIdUser());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                
                this.jumlahJob = Math.max(0, this.jumlahJob - 1);
                String sqlUpdate = "UPDATE alumni SET jumlah_job = ? WHERE id_user = ?";
                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                psUpdate.setInt(1, this.jumlahJob);
                psUpdate.setString(2, getIdUser());
                psUpdate.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error delete job: " + e.getMessage());
        } finally {
            disconnect();
        }
        return false;
    }

  
    public ArrayList<JobExperience> getJobExperience() {
        ArrayList<JobExperience> list = new ArrayList<>();
        try {
            connect();
            if (conn == null) return list;
           
            String sql = "SELECT j.*, c.name as company_name, c.location as company_location "
                       + "FROM job_experience j "
                       + "JOIN companies c ON j.id_company = c.id_company "
                       + "WHERE j.id_alumni = ? "
                       + "ORDER BY j.start_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, getIdUser());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Company company = new Company();
                company.setIdCompany(rs.getString("id_company"));
                company.setName(rs.getString("company_name"));
                company.setLocation(rs.getString("company_location"));

                JobExperience job = new JobExperience();
                job.setIdJobExperience(rs.getString("id_job"));
                job.setIndustri(rs.getString("industri"));
                job.setJabatan(rs.getString("jabatan"));
                job.setCompany(company);
                job.setStartDate(rs.getDate("start_date"));
                job.setEndDate(rs.getDate("end_date"));
                list.add(job);
            }
            this.jobExperience = list;
        } catch (SQLException e) {
            System.out.println("Error getJobExperience: " + e.getMessage());
        } finally {
            disconnect();
        }
        return list;
    }

  
    public List<Object> displayJobExperience() {
        List<Object> list = new ArrayList<>();
        ArrayList<JobExperience> jobs = getJobExperience();
        for (JobExperience job : jobs) {
           
            Object obj = (Object) job;
            list.add(obj);
        }
        return list;
    }

    public boolean isProfileComplete() {
        return this.major != null && !this.major.isEmpty()
            && this.enrollmentYear > 0
            && getName() != null && !getName().isEmpty()
            && getEmail() != null && !getEmail().isEmpty();
    }

  
    @Override
    public String getProfile() {
        return "Alumni: " + getName()
             + " | Jurusan: " + major
             + " | Tahun Masuk: " + enrollmentYear
             + " | Total Pekerjaan: " + jumlahJob;
    }

   
    public int    getEnrollmentYear()  { return enrollmentYear; }
    public void   setEnrollmentYear(int y) { this.enrollmentYear = y; }

    public String getMajor()           { return major; }
    public void   setMajor(String m)   { this.major = m; }

    public int    getJumlahJob()       { return jumlahJob; }
    public void   setJumlahJob(int j)  { this.jumlahJob = j; }

    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }

    public String getStatusClass() { return statusClass; }
    public void setStatusClass(String statusClass) { this.statusClass = statusClass; }

    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }

    public ArrayList<JobExperience> getJobExperienceList() { return jobExperience; }
    public void setJobExperienceList(ArrayList<JobExperience> j) { this.jobExperience = j; }
}
