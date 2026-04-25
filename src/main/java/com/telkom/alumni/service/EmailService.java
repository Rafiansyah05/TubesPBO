package com.telkom.alumni.service;

import com.telkom.alumni.model.Alumni;
import com.telkom.alumni.model.EmailNotification;
import com.telkom.alumni.repository.AlumniRepository;
import com.telkom.alumni.repository.EmailNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private EmailNotificationRepository notificationRepository;
    
    @Autowired
    private AlumniRepository alumniRepository;
    
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
    
    public EmailNotification saveNotification(EmailNotification notification) {
        return notificationRepository.save(notification);
    }
    
    @Scheduled(cron = "0 0 0 1 1/6 ?")
    public void sendReminderToAllAlumni() {
        List<Alumni> allAlumni = alumniRepository.findAll();
        
        for (Alumni alumni : allAlumni) {
            String subject = "Update Data Alumni - Pengingat 6 Bulan";
            String body = "Halo " + alumni.getName() + ",\n\n" +
                         "Mohon perbarui data riwayat pekerjaan Anda di sistem kami.\n" +
                         "Kunjungi: http://localhost:8080/profile\n\n" +
                         "Terima kasih.\n" +
                         "Tim Career Center";
            
            sendEmail(alumni.getEmail(), subject, body);
            
            EmailNotification notification = new EmailNotification(
                alumni.getEmail(), subject, body, new Date()
            );
            notification.setSent(true);
            notification.setSentDate(new Date());
            notificationRepository.save(notification);
        }
        
        System.out.println("Reminder emails sent to " + allAlumni.size() + " alumni");
    }
    
    public List<EmailNotification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}