package com.gongjakso.server.global.util.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailClient {

    private final JavaMailSender mailSender;

    public void sendOneEmail(String to) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // 수신자, 제목, 본문 등 설정
            String subject = "";
            String body = "";

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            // Email 전송
            mailSender.send(mimeMessage);

        } catch (MessagingException e){
            // TODO: 이메일 오류 핸들링 방법 정의 필요
            log.error("Error sending email", e);
        }

    }

    // TODO: 추후, 메일 전체 발송 메소드 개발 필요
}
