package com.congvo.be_myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    private JavaMailSender emailSender;

    @GetMapping("/test-email")
    public String sendTestEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("test@myapp.com");
            message.setTo("user@example.com");
            message.setSubject("Test kết nối");
            message.setText("Nếu bạn thấy mail này, kết nối đã thành công!");

            System.out.println("Đang bắt đầu gửi mail...");
            emailSender.send(message);
            System.out.println("Đã gửi thành công!"); // Nếu dòng này hiện ra thì Spring Boot tin là nó đã gửi được

            return "Đã gửi lệnh gửi mail. Check log console!";
        } catch (Exception e) {
            // Đây là phần quan trọng nhất: In lỗi ra để xem
            e.printStackTrace();
            return "LỖI: " + e.getMessage();
        }
    }

}
