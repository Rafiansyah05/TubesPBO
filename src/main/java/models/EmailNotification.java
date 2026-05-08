package models;

import utils.EmailUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


public class EmailNotification extends JDBC {

    private String recipient;
    private String subject;
    private String body;
    private Date   scheduledDate;

   
    public EmailNotification(String recipient, String subject, String body, Date scheduledDate) {
        this.recipient     = recipient;
        this.subject       = subject;
        this.body          = body;
        this.scheduledDate = scheduledDate;
    }

    
    public boolean sendReminder(Alumni alumni) {
        try {
          
            String emailBody = "<h2>Halo, " + alumni.getName() + "!</h2>"
                + "<p>Ini adalah pengingat dari Sistem Informasi Alumni (SiAlumni).</p>"
                + "<p>" + this.body + "</p>"
                + "<p>Silakan login ke sistem untuk memperbarui data Anda.</p>"
                + "<br><p>Salam,<br>Tim SiAlumni</p>";

            
            boolean success = EmailUtil.sendEmail(alumni.getEmail(), this.subject, emailBody);

           
            if (success) {
                logNotification(alumni.getEmail(), "sent");
            } else {
                logNotification(alumni.getEmail(), "failed");
            }

            return success;
        } catch (Exception e) {
            System.out.println("Error sendReminder: " + e.getMessage());
            logNotification(alumni.getEmail(), "failed");
            return false;
        }
    }

  
    public void logNotification(String email, String status) {
        try {
            connect();
            if (conn == null) return;

            String sql = "INSERT INTO email_notifications (recipient_email, subject, body, scheduled_date, sent_at, status) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, this.subject);
            ps.setString(3, this.body);

           
            if (this.scheduledDate != null) {
                ps.setDate(4, new java.sql.Date(this.scheduledDate.getTime()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setString(6, status);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error logNotification: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    
    public static ArrayList<java.util.Map<String, Object>> getAllLogs() {
        ArrayList<java.util.Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = JDBC.getConnection()) {
            String sql = "SELECT * FROM email_notifications ORDER BY sent_at DESC LIMIT 100";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                java.util.Map<String, Object> row = new java.util.HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("recipient_email", rs.getString("recipient_email"));
                row.put("subject", rs.getString("subject"));
                row.put("status", rs.getString("status"));
                row.put("sent_at", rs.getTimestamp("sent_at"));
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Error getAllLogs: " + e.getMessage());
        }
        return list;
    }

   
    public String getRecipient()  { return recipient; }
    public void setRecipient(String r) { this.recipient = r; }

    public String getSubject()    { return subject; }
    public void setSubject(String s) { this.subject = s; }

    public String getBody()       { return body; }
    public void setBody(String b) { this.body = b; }

    public Date getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(Date d) { this.scheduledDate = d; }

    @Override
    public String getProfile() {
        return "Log Notifikasi Email";
    }
}
