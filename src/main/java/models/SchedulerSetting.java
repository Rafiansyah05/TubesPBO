package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class SchedulerSetting extends JDBC {
    private boolean enabled;
    private int intervalValue;
    private String intervalUnit; // "days" or "hours"
    private String subject;
    private String body;
    private Timestamp lastRun;

    public SchedulerSetting() {}

    public SchedulerSetting(boolean enabled, int intervalValue, String intervalUnit, String subject, String body, Timestamp lastRun) {
        this.enabled = enabled;
        this.intervalValue = intervalValue;
        this.intervalUnit = intervalUnit;
        this.subject = subject;
        this.body = body;
        this.lastRun = lastRun;
    }

    public static SchedulerSetting getSetting() {
        checkAndCreateTable();
        SchedulerSetting setting = new SchedulerSetting(false, 1, "days", "Penting: Perbarui Data Alumni - SiAlumni", "Mohon segera update profil Anda.", null);
        try (Connection conn = JDBC.getConnection()) {
            String sql = "SELECT enabled, interval_value, interval_unit, subject, body, last_run FROM scheduler_settings LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    setting.setEnabled(rs.getBoolean("enabled"));
                    setting.setIntervalValue(rs.getInt("interval_value"));
                    setting.setIntervalUnit(rs.getString("interval_unit"));
                    setting.setSubject(rs.getString("subject"));
                    setting.setBody(rs.getString("body"));
                    setting.setLastRun(rs.getTimestamp("last_run"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error load settings: " + e.getMessage());
        }
        return setting;
    }

    public boolean save() {
        checkAndCreateTable();
        try (Connection conn = JDBC.getConnection()) {
            String sql = "UPDATE scheduler_settings SET enabled = ?, interval_value = ?, interval_unit = ?, subject = ?, body = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBoolean(1, this.enabled);
                ps.setInt(2, this.intervalValue);
                ps.setString(3, this.intervalUnit);
                ps.setString(4, this.subject);
                ps.setString(5, this.body);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            System.out.println("Error save settings: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateLastRun() {
        try (Connection conn = JDBC.getConnection()) {
            String sql = "UPDATE scheduler_settings SET last_run = NOW()";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error update last run: " + e.getMessage());
            return false;
        }
    }

    private static void checkAndCreateTable() {
        try (Connection conn = JDBC.getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS scheduler_settings ("
                       + "  id SERIAL PRIMARY KEY,"
                       + "  enabled BOOLEAN NOT NULL DEFAULT FALSE,"
                       + "  interval_value INT NOT NULL DEFAULT 1,"
                       + "  interval_unit VARCHAR(10) NOT NULL DEFAULT 'days',"
                       + "  subject VARCHAR(255) NOT NULL DEFAULT 'Penting: Perbarui Data Alumni - SiAlumni',"
                       + "  body TEXT NOT NULL DEFAULT 'Kami melihat Anda belum memperbarui data pekerjaan dalam 6 bulan terakhir. Mohon segera update profil Anda untuk tracer study kampus.',"
                       + "  last_run TIMESTAMP"
                       + ")";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }
            
            // Insert default setting if empty
            String checkSql = "SELECT COUNT(*) FROM scheduler_settings";
            try (PreparedStatement ps = conn.prepareStatement(checkSql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertSql = "INSERT INTO scheduler_settings (enabled, interval_value, interval_unit, last_run) VALUES (false, 1, 'days', null)";
                    try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                        psInsert.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error creating scheduler_settings table: " + e.getMessage());
        }
    }

    // Getters and Setters
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public int getIntervalValue() { return intervalValue; }
    public void setIntervalValue(int intervalValue) { this.intervalValue = intervalValue; }

    public String getIntervalUnit() { return intervalUnit; }
    public void setIntervalUnit(String intervalUnit) { this.intervalUnit = intervalUnit; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Timestamp getLastRun() { return lastRun; }
    public void setLastRun(Timestamp lastRun) { this.lastRun = lastRun; }

    @Override
    public String getProfile() {
        return "SchedulerSetting";
    }
}
