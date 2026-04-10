package com.app.restful.api;

import com.app.restful.domain.dto.MemberResponseDTO;
import com.app.restful.domain.vo.MemberVO;
import com.app.restful.service.MemberService;
import com.app.restful.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

//리턴값을 JSON으로 처리
//해당 컨트롤러는 화면에 데이터를 전달하기 위한 컨트롤러
//즉 페이지 중심 대신 데이터 중심 컨트롤러
//데이터 중심 컨트롤러는 업무처 마다 다르지만 클래스 명 끝에 API 라는 키워드 붙임
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members") // 추가적으로 api 라는 식으로 경로를 정해줘야 컨트롤러 에서의 req mapping 이랑 안겹침
public class MemberAPI {
    private final MemberServiceImpl memberService;

//    회원 목록 조회 서비스
//    해당 기능은 주소창에 api/members 로 들어 왔을 때 수행 되는 메서드
    @GetMapping("")
    public List<MemberResponseDTO> getAllMembers() {
        return memberService.getMembers();
    }

    // 회원 정보 조희 서비스
//    members 뒤에 오는 회원 id 값을 토대로 해서 해당 조건의 데이터를 반환
//    패스 배리어블로 해서 요청을 보냈어야 함
    @GetMapping("{id}")
    public MemberResponseDTO getMemberInfo(@PathVariable Long id) {
        Optional<MemberResponseDTO> foundMember = memberService.getMemberInfo(id);
        if (foundMember.isPresent()) {
            return foundMember.get();
        }
        return new MemberResponseDTO();
    }
}
