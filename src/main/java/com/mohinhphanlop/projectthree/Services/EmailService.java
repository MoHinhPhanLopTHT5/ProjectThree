package com.mohinhphanlop.projectthree.Services;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean SendMessage(
            String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("Hệ thống Quản lý thành viên <noreply@tienminhvy.id.vn>");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(message);
            return true;
        } catch (MessagingException ex) {
            // TODO: handle exception
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String GuiEmailQuenMatKhau(String email, HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        String uuid = UUID.randomUUID().toString();

        String url = baseUrl + "/quenmatkhau?code=" + uuid;
        if (SendMessage(email, "Khôi phục mật khẩu",
                "<h2>Xin chào!</h2>" +
                        "<p>Bạn nhận được email <b>Khôi phục mật khẩu</b> vì hệ thống vừa nhận được yêu cầu khôi phục mật khẩu từ một ai đó có thể là bạn."
                        +
                        "Nếu bạn không thực hiện hành động trên, vui lòng bỏ qua email này.</p>" +
                        "<p>Để khôi phục mật khẩu, hãy truy cập đường dẫn sau: </p>" +
                        "<p><a href='" + url + "'>Truy cập đường dẫn này</a></p>" +
                        "<p>Hoặc sao chép và dán liên kết sau vào thanh địa chỉ: " + url + " </p>" +
                        "<p>Trân trọng!</p>" +
                        "<p>Hệ thống quản lý thành viên bởi nhóm 9</p>")) {

            return uuid;
        } else {
            return null;
        }
    }
}