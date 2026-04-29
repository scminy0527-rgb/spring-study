package com.app.oauth.api;

import com.app.oauth.domain.dto.request.VerificationRequestDTO;
import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsApi {

    private final AuthService authService;

    // 핸드폰 전송
    @PostMapping("/phone/verification-code")
    public ResponseEntity<ApiResponseDTO> sendMemberPhoneVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ){
        String memberPhone = verificationRequestDTO.getMemberPhone();
        ApiResponseDTO apiResponseDTO = null;
        if(authService.sendMemberPhoneVerificationCode(verificationRequestDTO)){
            apiResponseDTO = ApiResponseDTO.of(true, "메세지가 발송되었습니다.");
        }else {
            apiResponseDTO = ApiResponseDTO.of(false, "휴대폰 번호를 확인해주세요.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    // 핸드폰 인증코드 검증
    @PostMapping("/phone/verification-code/verify")
    public ResponseEntity<ApiResponseDTO> verifyMemberPhoneVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ){
        String memberPhone = verificationRequestDTO.getMemberPhone();
        String code = verificationRequestDTO.getCode();
        ApiResponseDTO apiResponseDTO = null;
        if(authService.verifyMemberPhoneVerificationCode("")){
            apiResponseDTO = ApiResponseDTO.of(true, "인증이 완료되었습니다.");
        }else{
            apiResponseDTO = ApiResponseDTO.of(false, "인증번호를 확인해주세요.");
        }

        apiResponseDTO = ApiResponseDTO.of(false, "인증번호를 확인해주세요.");

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    // 이메일 전송
    @PostMapping("/email/verification-code")
    public ResponseEntity<ApiResponseDTO> sendMemberEmailVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ) {
        String memberEmail = verificationRequestDTO.getMemberEmail();



//        이메일 전송 요청하는 코드

//        이때 try catch 를 이용해서 이메일 전송 여부 확인

        return  ResponseEntity.status(HttpStatus.OK).body(null);
    }



}
