package com.telkom.alumni.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "email_notifications")
public class EmailNotification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String recipient;
    private String subject;
    @Column(length = 1000)
    private String body;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleDate;
    
    private boolean sent;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
    
    public EmailNotification() {}
    
    public EmailNotification(String recipient, String subject, String body, Date scheduleDate) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.scheduleDate = scheduleDate;
        this.sent = false;
    }
    
    public String sendReminder(Alumni alumni) {
        System.out.println("Mengirim email ke : " + recipient);
        System.out.println("Subjek            : " + subject);
        System.out.println("Isi               : " + body);
        this.sent = true;
        this.sentDate = new Date();
        return "Reminder sent to " + recipient;
    }
    
    public String getResponse() {
        return "Respons diterima dari: " + recipient;
    }
    
    public String logNotification() {
        return "Log — Kepada: " + recipient + " | Subjek: " + subject + " | Jadwal: " + scheduleDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public Date getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(Date scheduleDate) { this.scheduleDate = scheduleDate; }
    
    public boolean isSent() { return sent; }
    public void setSent(boolean sent) { this.sent = sent; }
    
    public Date getSentDate() { return sentDate; }
    public void setSentDate(Date sentDate) { this.sentDate = sentDate; }
}