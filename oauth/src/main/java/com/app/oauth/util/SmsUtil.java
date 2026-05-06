package com.app.oauth.util;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsUtil {
    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    private DefaultMessageService messageService;
    private final JavaMailSender mailSender;
    private final SecureRandom SECURE_RANDOM = new SecureRandom();

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

//    해당 메서드는 특정 휴대폰 번호로 문자 메세지를 전송 하는 매서드
//    매개변수: to (받는 사람) / content (메세지 내용)
    public SingleMessageSentResponse sendOneMemberPhone(String to, String content){
        Message message = new Message();

        // "01012341234" <- 형태로 전송해야 함.
        String toPhoneNumber = to.replaceAll("-", "");

        message.setTo(toPhoneNumber);
        message.setFrom("01047099813");
        message.setText(content);

        SingleMessageSentResponse response = this
                .messageService
                .sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

//    인증번호 생성
    public String generateRandomCode() {
        int code = SECURE_RANDOM.nextInt(1_000_000); // 0 ~ 999999
        return String.format("%06d", code);           // 000000 ~ 999999
    }


    // 이메일 전송
    public void sendMemberEmail(String to, String subject, String content){
        MimeMessage mineMessage = mailSender.createMimeMessage();

//        codefuling@gmail.com

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mineMessage, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject); // 제목
            helper.setText(content); // 내용
            helper.setFrom(emailUsername, "테스트"); // 보낸 이메일 , 보낸 사람 이름

            mailSender.send(mineMessage);

        } catch (Exception e) {
            throw new RuntimeException("메일 전송 실패 " + e.getMessage());
        }

    }
}












