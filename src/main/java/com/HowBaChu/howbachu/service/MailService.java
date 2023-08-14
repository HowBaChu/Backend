package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@PropertySource("classpath:application-mail.yml")
@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    private final String verificationCode = createKey();

    @Value("${spring.mail.username}")
    private String id;

    public void sendMessage(String to) {
        MimeMessage message = createMessage(to);
        javaMailSender.send(message);
    }

    public MimeMessage createMessage(String to) {
        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + verificationCode);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
            message.setSubject("HowBaChu 이메일 인증 코드: "); //메일 제목
            message.setText(emailContent(), "utf-8", "html"); //내용, charset타입, subtype
            message.setFrom(id); //보내는 사람의 메일 주소, 보내는 사람 이름
        } catch (MessagingException e) {
            throw new CustomException(ErrorCode.EMAIL_VERIFICATION_FAILED);
        }
        return message;
    }

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) { key.append((rnd.nextInt(10))); }
        return key.toString();
    }

    // 인증 메일 내용
    public String emailContent() {
        StringBuffer sb = new StringBuffer();
        sb.append(
            "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>");
        sb.append(
            "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 인증 화면에 입력해주세요.</p>");
        sb.append(
            "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\">"
                + "<table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\">"
                + "<tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">");
        sb.append(verificationCode);
        sb.append("</td></tr></tbody></table></div>");
        return sb.toString();
    }


}