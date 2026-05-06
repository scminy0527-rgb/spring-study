package com.app.oauth.api;

import com.app.oauth.domain.dto.request.VerificationRequestDTO;
import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
@Slf4j
public class SmsApi {

    private final AuthService authService;

    // 핸드폰 전송
    @PostMapping("/phone/verification-code")
    public ResponseEntity<ApiResponseDTO> sendMemberPhoneVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ){
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
        ApiResponseDTO apiResponseDTO;
        if(authService.verifyMemberPhoneVerificationCode(verificationRequestDTO)){
            apiResponseDTO = ApiResponseDTO.of(true, "인증이 완료되었습니다.");
        }else{
            apiResponseDTO = ApiResponseDTO.of(false, "인증번호를 확인해주세요.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    // 이메일 전송
    @PostMapping("/email/verification-code")
    public ResponseEntity<ApiResponseDTO> sendMemberEmailVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ) {
//        log.info("프론트에서 요청 받음");
//        log.info("프론트에서 요청 받은 이메일 주소: {}", verificationRequestDTO.getMemberEmail());
        ApiResponseDTO apiResponseDTO;
        if (authService.sendMemberEmailVerificationCode(verificationRequestDTO)) {
            apiResponseDTO = ApiResponseDTO.of(true, "이메일이 발송되었습니다.");
        } else {
            apiResponseDTO = ApiResponseDTO.of(false, "이메일 전송에 실패했습니다. 이메일 주소를 확인해주세요.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    // 이메일 인증코드 검증
    @PostMapping("/email/verification-code/verify")
    public ResponseEntity<ApiResponseDTO> verifyMemberEmailVerificationCode(
            @RequestBody VerificationRequestDTO verificationRequestDTO
    ) {
        log.info("프론트에서 인증 코드 입력 하였음: {}",  verificationRequestDTO.getCode());
        log.info("대상 이메일: {}",  verificationRequestDTO.getMemberEmail());
        ApiResponseDTO apiResponseDTO;
        if (authService.verifyMemberEmailVerificationCode(verificationRequestDTO)) {
            apiResponseDTO = ApiResponseDTO.of(true, "인증이 완료되었습니다.");
        } else {
            apiResponseDTO = ApiResponseDTO.of(false, "인증번호를 확인해주세요.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }



}
