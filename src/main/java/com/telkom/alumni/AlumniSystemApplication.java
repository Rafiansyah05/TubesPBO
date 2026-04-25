package com.telkom.alumni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlumniSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlumniSystemApplication.class, args);
        System.out.println("========================================");
        System.out.println("Sistem Informasi Alumni Berjalan!");
        System.out.println("Akses di: http://localhost:8080");
        System.out.println("========================================");
    }
}