package com.app.oauth.service;

import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.request.VerificationRequestDTO;
import com.app.oauth.domain.vo.MemberVO;
import com.app.oauth.domain.vo.SocialMemberVO;
import com.app.oauth.exception.JwtTokenException;
import com.app.oauth.exception.MemberException;
import com.app.oauth.repository.MemberDAO;
import com.app.oauth.repository.SocialMemberDAO;
import com.app.oauth.util.AuthCodeGenerator;
import com.app.oauth.util.JwtTokenUtil;
import com.app.oauth.util.SmsUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthCodeGenerator authCodeGenerator;
    private final SmsUtil smsUtil;
    @Value("${jwt.token-blacklist-prefix}")
    private String BLACKLIST_TOKEN_PREFIX;

    @Value("${jwt.refresh-blacklist-prefix}")
    private String REFRESH_TOKEN_PREFIX;

    private final MemberDAO memberDAO;
    private final SocialMemberDAO socialMemberDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate redisTemplate;

    //    일반 로그인
//    순수데이터(JwtTokenDTO) 반환
    @Override
    public JwtTokenDTO login(MemberDTO memberDTO) {
        // 사용자가 맞는지 (이메일, 비밀번호, 프로바이더(local)

        // elary return
        MemberVO memberVO = MemberVO.from(memberDTO);
        log.info("memberDTO: {}", memberDTO);
        // 회원 유무 검사
        MemberDTO foundMember = memberDAO
                .findMemberByMemberEmailAndSocialMemberProvider(memberDTO)
                .orElseThrow(() -> {
                    throw new MemberException("회원이 아닙니다.", HttpStatus.BAD_REQUEST);
                });

        // 회원 비밀번호 일치 검사
        // 화면에서 받은 비밀번호, DB에 있는 비밀번호 검사
        if(!passwordEncoder.matches(memberVO.getMemberPassword(), foundMember.getMemberPassword())){
            throw new MemberException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 토큰 생성(access, refresh)
        Map<String, String> claims = new HashMap<>();
        claims.put("id", foundMember.getId().toString());
        claims.put("memberEmail", foundMember.getMemberEmail());
        claims.put("socialMemberProvider", "local");

        String accessToken = jwtTokenUtil.generateAccessToken(claims);
        String refreshToken = jwtTokenUtil.generateRefreshToken(claims);

        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();

        jwtTokenDTO.setAccessToken(accessToken);
        jwtTokenDTO.setRefreshToken(refreshToken);

        // redis에 refresh 토큰 저장
        saveRefreshToken(jwtTokenDTO);

        return jwtTokenDTO;
    }

//    소셜 로그인 요청 시
//    요청 시 받은 데이터를 토대로 회원 탐색 및 db 에 회원 insert
    @Override
    public JwtTokenDTO socialLogin(MemberDTO memberDTO) {

        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        Map<String, String> claims = new HashMap<String, String>();


        if(memberDAO.existsByMemberEmailAndSocialMemberProvider(memberDTO)){
            // 만약 유저가 있다면 -> 토큰 발급(id)
            // 조회

            // 여기서 만약 기존에 전통 회원가입 됬지만 소셜 연동 이라면 이거에 대해서도 해줘야 함
            MemberDTO foundMember = memberDAO
                    .findMemberByMemberEmailAndSocialMemberProvider(memberDTO)
                    .orElseThrow(() -> { throw new MemberException("socialLogin 회원 조회 실패", HttpStatus.BAD_REQUEST);});

            claims.put("id", foundMember.getId().toString());

        }else {
            // 만약 유저가 없다면 회원가입 후 -> 토큰 발급
            MemberVO memberVO = MemberVO.from(memberDTO);
            SocialMemberVO socialMemberVO = SocialMemberVO.from(memberDTO);

            memberDAO.save(memberVO);
            socialMemberVO.setMemberId(memberVO.getId());

            socialMemberDAO.save(socialMemberVO);
            claims.put("id", memberVO.getId().toString());
        }

        claims.put("memberEmail", memberDTO.getMemberEmail());
        claims.put("socialMemberProvider", memberDTO.getSocialMemberProvider());

        String accessToken = jwtTokenUtil.generateAccessToken(claims);
        String refreshToken = jwtTokenUtil.generateRefreshToken(claims);

        jwtTokenDTO.setAccessToken(accessToken);
        jwtTokenDTO.setRefreshToken(refreshToken);

        // redis에 refresh 토큰 저장
        saveRefreshToken(jwtTokenDTO);

        return jwtTokenDTO;
    }

//    로그아웃
//    accessToken
    @Override
    public void logout(JwtTokenDTO jwtTokenDTO) {
//        토큰 블랙리스트에 저장
        if(validateRefreshToken(jwtTokenDTO)){
            saveBlackListedToken(jwtTokenDTO);
        } else {
            throw new JwtTokenException("권한 없음", HttpStatus.UNAUTHORIZED);
        }
    }

    // Redis에 refresh Token 저장
    // 콜론체이닝(:) 방법으로 저장
    @Override
    public boolean saveRefreshToken(JwtTokenDTO jwtTokenDTO) {
        String refreshToken = jwtTokenDTO.getRefreshToken();
        Long id = Long.parseLong((String)jwtTokenUtil.parseToken(refreshToken).get("id"));
        String key = REFRESH_TOKEN_PREFIX + id;

        try {
            redisTemplate.opsForValue().set(key, refreshToken, 30, TimeUnit.DAYS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Redis에 저장된 refresh Token이 유효한지 검증
    @Override
    public boolean validateRefreshToken(JwtTokenDTO jwtTokenDTO) {
        String refreshToken = jwtTokenDTO.getRefreshToken();
        Long id = Long.parseLong((String)jwtTokenUtil.parseToken(refreshToken).get("id"));
        String key = REFRESH_TOKEN_PREFIX + id;

        try {
//            key 를 통해서 value (redis 에 저장 된 리프레시 토큰) 가져오기
            Object storedToken = redisTemplate.opsForValue().get(key);

//            유저가 가지고 있던 refreshToken 이랑 redis 에 저장 되었던 refreshToken 이 같은지 확인
            if(!refreshToken.equals(storedToken)){
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Redis에 블랙리스트를 등록 (로그아웃 시 토큰 무효화)
    @Override
    public boolean saveBlackListedToken(JwtTokenDTO jwtTokenDTO) {
        String refreshToken = jwtTokenDTO.getRefreshToken();
        Long refreshId = Long.parseLong((String)jwtTokenUtil.parseToken(refreshToken).get("id"));
        String refreshKey = BLACKLIST_TOKEN_PREFIX + refreshId;

        String accessToken = jwtTokenDTO.getAccessToken();
        Long accessId = Long.parseLong((String)jwtTokenUtil.parseToken(accessToken).get("id"));
        String accessKey = BLACKLIST_TOKEN_PREFIX + accessId;

        try {
            redisTemplate.opsForSet().add(refreshKey, refreshToken);
            redisTemplate.opsForSet().add(accessKey, accessToken);
            // TTL
            redisTemplate.expire(refreshKey, 30, TimeUnit.DAYS);
            redisTemplate.expire(accessKey, 30, TimeUnit.DAYS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Redis에 등록된 블랙리스트인지 검증
    @Override
    public boolean isBlackListedToken(JwtTokenDTO jwtTokenDTO) {
        String refreshToken = jwtTokenDTO.getRefreshToken();
        Long id = Long.parseLong((String)jwtTokenUtil.parseToken(refreshToken).get("id"));
        String key = BLACKLIST_TOKEN_PREFIX + id;

        try {
            Boolean isMember = redisTemplate.opsForSet().isMember(key, refreshToken);
            return isMember != null && isMember;
        } catch (Exception e) {
            return false;
        }
    }

    // refreshToken 토큰 -> accessToken을 재발급
    @Override
    public JwtTokenDTO reissueAccessToken(JwtTokenDTO jwtTokenDTO) {
        String refreshToken = jwtTokenDTO.getRefreshToken();
        Long id = Long.parseLong((String)jwtTokenUtil.parseToken(refreshToken).get("id"));

        if(isBlackListedToken(jwtTokenDTO)){
            throw new JwtTokenException("이미 로그아웃 된 토큰입니다", HttpStatus.BAD_REQUEST);
        }

        // 리프레쉬 검증 (사용자가 가진거랑 redis 에 있는 키 를 가지고 비교)
        if(!validateRefreshToken(jwtTokenDTO)){
            throw new JwtTokenException("유효하지 않은 토큰입니다", HttpStatus.BAD_REQUEST);
        }

//        refreshToken 키 가 유효 하다면 해당 키에 있는 id 정보를 가지고 유저 판별
        Map<String, String> claims = new HashMap<>();
        MemberDTO member = memberDAO
                .findMemberById(id).orElseThrow(() -> new MemberException("회원이 없습니다"));

//        유저 정보를 claims 에 넣기
        claims.put("id", member.getId().toString());
        claims.put("memberEmail", member.getMemberEmail());
        claims.put("socialMemberProvider", member.getSocialMemberProvider());

        // 새로운 토큰 생성
        String newAccessToken = jwtTokenUtil.generateAccessToken(claims);
        jwtTokenDTO.setAccessToken(newAccessToken);

//        반환
        return jwtTokenDTO;
    }

//    휴대폰 인증 관련한 내용

    @Override
    public boolean sendMemberPhoneVerificationCode(
            VerificationRequestDTO verificationRequestDTO
    ) {
        String phoneNumber = verificationRequestDTO.getMemberPhone();

//        인증번호 생성 (이상 무)
        String verifyCode = AuthCodeGenerator.generateByRange();

//        redis 에 verifyCode:휴대폰 번호 형태로 체이닝 해서 넣기
        String key = "verifyCode:" + phoneNumber;
//        레디스에 값 넣기
        redisTemplate.opsForSet().add(key, verifyCode);
        // TTL
        redisTemplate.expire(key, 3, TimeUnit.MINUTES);

//        해당 휴대폰 번호에 문자 전송
//        전송 성공하면 true, 실패 하면 false 반환
        try {
            smsUtil.sendOneMemberPhone(phoneNumber, verifyCode);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifyMemberPhoneVerificationCode(VerificationRequestDTO verificationRequestDTO) {
        String phoneNumber = verificationRequestDTO.getMemberPhone();
        String code = verificationRequestDTO.getCode();
        String key = "verifyCode:" + phoneNumber;

        try {
            Boolean isMatch = redisTemplate.opsForSet().isMember(key, code);
            if (Boolean.TRUE.equals(isMatch)) {
                redisTemplate.delete(key);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

//    사용자에게 이메일 인증번호를 제공하는 매서드
    @Override
    public boolean sendMemberEmailVerificationCode(VerificationRequestDTO verificationRequestDTO) {
        String email = verificationRequestDTO.getMemberEmail();

//        랜덤 6자리 코드 생성 후 레디스 저장 및 유저에게 전송
//        String verifyCode = AuthCodeGenerator.generateByRange();
        String verifyCode = smsUtil.generateRandomCode();
        String key = "verifyCode:email:" + email;
        redisTemplate.opsForValue().set(key, verifyCode, 3, TimeUnit.MINUTES);

        String subject = "[EUM/이음 말하지 않아도 되는 언어] 이메일 인증번호";
        String content = "인증번호: " + verifyCode +
                "\n\n3분 내에 입력해주세요." +
                "\n\n 본 메일은 발신전용 이메일 입니다.\n\n" +
                "토막 개그: 반성문을 영어로 하면??\n\n" +
                "글로벌";

        try {
            smsUtil.sendMemberEmail(email, subject, content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    프론트 에서 사용자가 입력 한 이메일 인증번호 검증
    @Override
    public boolean verifyMemberEmailVerificationCode(VerificationRequestDTO verificationRequestDTO) {
        String email = verificationRequestDTO.getMemberEmail();
        String code = verificationRequestDTO.getCode();
        String key = "verifyCode:email:" + email;

        try {
            Object storedCode = redisTemplate.opsForValue().get(key);
            if (storedCode == null || !code.equals(storedCode.toString())) {
                return false;
            }
//            인증 성공 하면 더이상 필요 없기에 레디스 에서 키 지움
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}













