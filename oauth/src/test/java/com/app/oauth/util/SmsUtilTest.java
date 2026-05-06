package com.app.oauth.util;

import com.app.oauth.domain.dto.request.VerificationRequestDTO;
import com.app.oauth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SmsUtilTest {
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private AuthService authService;

//    @Test
//    public void sendOneTest() {
//        smsUtil.sendOne("01021756070", "반성문을 영어로 하면? 글로벌");
//    }

    @Test
    public void phoneVerificationTest() {
        VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
        verificationRequestDTO.setMemberPhone("01021756070");
        authService.sendMemberPhoneVerificationCode(verificationRequestDTO);
    }

    @Test
    public void sendVerificationCodeTest() {
        VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
        verificationRequestDTO.setMemberEmail("cjfals1015@naver.com");

        authService.sendMemberEmailVerificationCode(verificationRequestDTO);
    }
}
