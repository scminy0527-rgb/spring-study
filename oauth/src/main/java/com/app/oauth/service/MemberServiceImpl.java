package com.app.oauth.service;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.response.MemberResponseDTO;
import com.app.oauth.domain.vo.MemberVO;
import com.app.oauth.domain.vo.SocialMemberVO;
import com.app.oauth.exception.MemberException;
import com.app.oauth.repository.MemberDAO;
import com.app.oauth.repository.SocialMemberDAO;
import com.app.oauth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class MemberServiceImpl implements MemberService {

    private final MemberDAO memberDAO;
    private final SocialMemberDAO socialMemberDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    //    회원 가입
    @Override
    public ApiResponseDTO join(MemberDTO memberDTO) {
        Map<String, Object> claims = new HashMap<>();

//        해당 이메일로 회원이 있니?
//        응(true) 이미 있는 회원이야 그래서 중복된 이메일 이야
//        아니(false) 회원이 아니야 그래서 이걸로 회원가입 가능 해 ^^
        if(memberDAO.existsByMemberEmailAndSocialMemberProvider(memberDTO)){
            throw new MemberException("중복된 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }

//        화면에서 받은 로그인 요청 정보 (이메일: 비밀번호) 는 memberDTO 에 담아서 서버로 요청됨
//        해당 dto 를 각각의 vo 에 옮겨 담아야 함
        MemberVO memberVO = MemberVO.from(memberDTO);
        SocialMemberVO socialMemberVO = SocialMemberVO.from(memberDTO);

//        socialMemberVO.getSocialMemberProvider().equals("local"
//        아래 코드가 더 좋은 코드임
//        null 에 . 을 찍으면 npe 이나 문자열에 . 을 찍는건 절때로 에러가 안남
        if("local".equals(socialMemberVO.getSocialMemberProvider())){
            memberVO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
        }

        memberDAO.save(memberVO);
        socialMemberVO.setMemberId(memberVO.getId());

        socialMemberDAO.save(socialMemberVO);

//        result.put("success", true);
//        result.put("message", "회원가입이 완료되었습니다.");

        claims.put("id", memberVO.getId());
        claims.put("memberEmail", memberVO.getMemberEmail());
        claims.put("memberProvider", socialMemberVO.getSocialMemberProvider());

        ApiResponseDTO result = ApiResponseDTO
                .of(true, "회원가입이 완료되었습니다.", claims);
        return result;
    }

//    일반 로그인
//    여기서 ApiResponseDTO 로 내보내면 컨트롤러 에서는 토큰을 제거 한 뒤에 쿠키에 심어넣고
//    나머지 것을 화면으로 전달
    @Override
    public JwtTokenDTO login(MemberDTO memberDTO) {
//        사용자가 맞는지 검사 (이메일과 비밀번호, 프로바이더 local)
//        항상 예외를 먼저 던져야 함
//        조건문 안에 들어가는거? 회원이야? 응(true) 회원이야. 아니(false) 회원이 아니야
//        회원이 아닐 때에 회원이 아니라는 문구가 수행 되야 함
        if(!memberDAO.existsByMemberEmailAndSocialMemberProvider(memberDTO)){
            throw new MemberException("회원이 아닙니다.",  HttpStatus.BAD_REQUEST);
        }

        MemberVO memberVO = MemberVO.from(memberDTO);

//        이미 이메일 검수 되었기에 문제 없음
        MemberDTO foundMember = memberDAO
                .findMemberByMemberEmailAndSocialMemberProvider(memberDTO)
                .orElseThrow(() -> {
                    throw new MemberException("에러", HttpStatus.BAD_REQUEST);
                });

//        서버에 있는 유저 비밀번호랑 유저가 입력한 비밀번호가 같니?
        if(!passwordEncoder.matches(memberVO.getMemberPassword(), foundMember.getMemberPassword())){
            throw new MemberException("비밀번호가 틀립니다.",  HttpStatus.BAD_REQUEST);
        }


//        토큰 만들기
        Map<String, String> claims = new HashMap<>();
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();

        claims.put("id", foundMember.getId() + "");
        claims.put("memberEmail", foundMember.getMemberEmail());

        String jwtKey = jwtTokenUtil.generateAccessToken(claims);
        String jwtRefKey = jwtTokenUtil.generateRefreshToken(claims);

//        페이 로드로 각각 담아야 함
        jwtTokenDTO.setAccessToken(jwtKey);
        jwtTokenDTO.setRefreshToken(jwtRefKey);

        return jwtTokenDTO;
    }

//    소셜 로그인
    @Override
    public JwtTokenDTO socialLogin(MemberDTO memberDTO) {
//            ---------서비스--------------------
//            유저를 찾는다
//            만약 유저가 있다면 로그인
//            만약 유저가 없다면 회원가입 후 로그인

        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        Map<String, String> claims = new HashMap<>();

        if(memberDAO.existsByMemberEmailAndSocialMemberProvider(memberDTO)){
//            로그인 (토큰 발급)

        } else {
//            회원 가입
            socialMemberDAO.save(SocialMemberVO.from(memberDTO));
        }

//        마지막에 만들어 져야 함
        claims.put("id", memberDTO.getId() + "");
        claims.put("memberEmail", memberDTO.getMemberEmail());
        claims.put("socialMemberProvider", memberDTO.getSocialMemberProvider());

        String accessToken = jwtTokenUtil.generateAccessToken(claims);
        String refreshToken = jwtTokenUtil.generateRefreshToken(claims);
        jwtTokenDTO.setAccessToken(accessToken);
        jwtTokenDTO.setRefreshToken(refreshToken);

        return jwtTokenDTO;
//        토큰 반환
    }

//    토큰으로 회원 정보를 조회하는 서비스
    @Override
    public ApiResponseDTO me(String token) {
//        전달받은 토큰을 토대로 parse 해서 id 추출
        Claims claims = jwtTokenUtil.parseToken(token);
        Long id = Long.parseLong(claims.get("id").toString());

//        id 를 이용해서 회원 정보를 가져오는 쿼리 실행
        MemberResponseDTO foundMember = memberDAO.findMemberById(id)
                .map(MemberResponseDTO::from)
                .orElseThrow(() -> {
                    throw new MemberException("회원 조회 실패", HttpStatus.BAD_REQUEST);
                });

        ApiResponseDTO apiResponseDTO = ApiResponseDTO.of(
                true, "회원 정보 조회 성공",  foundMember
        );

        return apiResponseDTO;
    }
}
