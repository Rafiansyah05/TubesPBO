package utils;

import models.Alumni;
import models.EmailNotification;
import models.SchedulerSetting;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class EmailSchedulerListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // Run check every 1 minute, with an initial delay of 1 minute
        scheduler.scheduleAtFixedRate(this::checkAndSendEmails, 1, 1, TimeUnit.MINUTES);
        System.out.println("EmailSchedulerListener initialized successfully.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
            System.out.println("EmailSchedulerListener destroyed.");
        }
    }

    private void checkAndSendEmails() {
        try {
            SchedulerSetting setting = SchedulerSetting.getSetting();
            if (!setting.isEnabled()) {
                return;
            }

            Timestamp lastRun = setting.getLastRun();
            long now = System.currentTimeMillis();
            boolean shouldRun = false;

            if (lastRun == null) {
                shouldRun = true;
            } else {
                long diffMs = now - lastRun.getTime();
                long requiredMs;
                if ("minutes".equals(setting.getIntervalUnit())) {
                    requiredMs = setting.getIntervalValue() * 60L * 1000L;
                } else if ("hours".equals(setting.getIntervalUnit())) {
                    requiredMs = setting.getIntervalValue() * 60L * 60L * 1000L;
                } else { // "days"
                    requiredMs = setting.getIntervalValue() * 24L * 60L * 60L * 1000L;
                }
                if (diffMs >= requiredMs) {
                    shouldRun = true;
                }
            }

            if (shouldRun) {
                System.out.println("Auto email scheduler running at: " + new Timestamp(now));
                // 1. Update last_run immediately to avoid duplicate runs
                SchedulerSetting.updateLastRun();

                // 2. Fetch target alumni
                ArrayList<Alumni> targets = getTargetAlumni();
                System.out.println("Sending auto emails to " + targets.size() + " alumni...");

                // 3. Send emails
                for (Alumni alumni : targets) {
                    EmailNotification notif = new EmailNotification(
                        alumni.getEmail(),
                        setting.getSubject(),
                        setting.getBody(),
                        new java.util.Date()
                    );
                    notif.sendReminder(alumni);
                }
                System.out.println("Auto email scheduler finished sending emails.");
            }
        } catch (Exception e) {
            System.out.println("Error in EmailSchedulerListener checkAndSendEmails: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ArrayList<Alumni> getTargetAlumni() {
        ArrayList<Alumni> targets = new ArrayList<>();
        try {
            try (Connection conn = models.JDBC.getConnection()) {
                String sql = "SELECT u.id_user, u.name, u.email, u.password, a.enrollment_year, a.major, a.jumlah_job "
                           + "FROM users u JOIN alumni a ON u.id_user = a.id_user";
                try (PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        targets.add(new Alumni(
                            rs.getString("id_user"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("major"),
                            rs.getInt("enrollment_year"),
                            rs.getInt("jumlah_job")
                        ));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading alumni list for scheduler: " + e.getMessage());
        }
        return targets;
    }
}
