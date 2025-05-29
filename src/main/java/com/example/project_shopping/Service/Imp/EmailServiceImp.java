package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.Constant.ApplicationConstants;
import com.example.project_shopping.Entity.PasswordReset;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Exception.InvalidTokenException;
import com.example.project_shopping.Repository.PasswordResetRepository;
import com.example.project_shopping.Repository.UserRepository;
import com.example.project_shopping.Service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@AllArgsConstructor
public class EmailServiceImp implements EmailService {
    private PasswordResetRepository passwordResetRepository;
    private UserRepository userRepository;
    private JavaMailSender javaMailSender;

    @Override
    public boolean sendToken(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(()->
                new EntityNotFoundException("User not found with email: "+email));

        passwordResetRepository.deleteByEmail(email);

        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32]; // 32 bytes = 256 bits
        secureRandom.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setEmail(email);
        passwordReset.setToken(token);
        passwordReset.setExpires(LocalDateTime.now().plusMinutes(10));
        passwordResetRepository.save(passwordReset);

        String resetUrl = ApplicationConstants.host+"/auth/resetPassword/"+token;

        String subject = "Yêu cầu đặt lại mật khẩu";
        String content = "Xin chào,\n\n"
                + "Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.\n"
                + "Vui lòng nhấn vào liên kết bên dưới để đặt lại mật khẩu:\n\n"
                + resetUrl + "\n\n"
                + "Nếu bạn không yêu cầu điều này, hãy bỏ qua email này.\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ hỗ trợ";

        this.sendEmail(ApplicationConstants.email, email, subject, content);
        return true;
    }

    @Override
    public boolean verifyToken(String email, String token) {
        PasswordReset passwordReset = passwordResetRepository.findByEmail(email).orElseThrow(()
                ->new EntityNotFoundException("Not found with email: "+email));
        LocalDateTime now = LocalDateTime.now();

        if(!isTokenValid(passwordReset)){
            throw new InvalidTokenException("Token is expired!");
        }

        if(!passwordReset.getToken().equals(token)){
            return false;
        }

        return true;
    }

    @Override
    public void sendEmail(String from, String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    public boolean isTokenValid(PasswordReset token) {
        return LocalDateTime.now().isBefore(token.getExpires());
    }
}
