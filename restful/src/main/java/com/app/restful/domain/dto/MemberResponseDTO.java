package com.app.restful.domain.dto;

import com.app.restful.domain.vo.MemberVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

//해당 DTO 는 화면으로 보내주기 위해 (응답) 사용하는 DTO
//화면에는 줘야 하는 값과 주면 안되는 값이 존재한다. 따라서 이를 설정해야 한다.
@Component
@Data
@Schema(description = "회원 정보 응답 DTO")
public class MemberResponseDTO {
    @Schema(description = "회원 번호", required = true, example = "1")
    private Long id;
    @Schema(description = "회원 이메일", required = true, example = "test123@naver.com")
    private String memberEmail;
    @Schema(description = "회원 이름", example = "홍길동")
    private String memberName;

//    초기화 생성자 만들면 기본 생성자가 없어서 에러가 발생할 수 있다.
//    그래서 정적 펙토리 매서드 고려
//    만약 생성자를 만들면 무슨 역할인지 햇갈리지만 from 이라는 정적 매서드를 만들면 외부에서 받아서 형변환 하는거라고 알기 쉬움
    public static MemberResponseDTO from(MemberVO memberVO) {
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        memberResponseDTO.setId(memberVO.getId());
        memberResponseDTO.setMemberEmail(memberVO.getMemberEmail());
        memberResponseDTO.setMemberName(memberVO.getMemberName());

        return  memberResponseDTO;
    }
}
