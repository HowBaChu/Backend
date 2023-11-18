package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.service.MailService;
import com.HowBaChu.howbachu.utils.RedisUtil;
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
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final String verificationCode = createKey();

    @Value("${spring.mail.username}")
    private String id;

    @Value("${spring.mail.properties.auth-code-expiration-millis}")
    private Long expired;

    @Override
    public void sendMessage(String to) {
        MimeMessage message = createMessage(to);
        redisUtil.setDataExpire(verificationCode, to, expired);
        javaMailSender.send(message);
    }

    @Override
    public ResponseCode certificate(String email, String inputCode) {
        String storedCode = redisUtil.getData(inputCode);
        return storedCode.equals(email)
            ? ResponseCode.VERIFICATION_SUCCESS
            : ResponseCode.VERIFICATION_FAILED;
    }

    private MimeMessage createMessage(String to) {
        log.info("이메일 인증 시작" + "\n" + "보내는 대상: " + to + "\n" + "인증 번호: " + verificationCode);
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

    private String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    private String emailContent() {
        StringBuffer sb = new StringBuffer();
        sb.append(
            "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">[이메일 주소 확인]</h1>");
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