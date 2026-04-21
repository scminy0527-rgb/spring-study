package com.app.restful.api;

import com.app.restful.domain.dto.*;
import com.app.restful.domain.vo.MemberVO;
import com.app.restful.service.MemberServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
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
    private final MemberLoginRequestDTO memberLoginRequestDTO;

    //    회원 목록 조회 서비스
//    해당 기능은 주소창에 api/members 로 들어 왔을 때 수행 되는 메서드
    @Operation(summary = "회원 목록을 조회하는 서비스", description = "회원 목록을 조회해서 리스트로 반환하는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "회원 조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<ApiResponseDTO> getAllMembers() {
        List<MemberResponseDTO> members = memberService.getMembers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("멤버 정보 불러오기 성공", members));
    }

    // 회원 정보 조희 서비스
//    members 뒤에 오는 회원 id 값을 토대로 해서 해당 조건의 데이터를 반환
//    패스 배리어블로 해서 요청을 보냈어야 함
    @GetMapping("{id}")
    @Operation(summary = "회원 단일 조회 서비스", description = "회원을 조회 해서 단일 객체로 반환하는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "회원 조회 실패")
    })
    @Parameter(
            name = "id",
            description = "회원번호",
            required = true,
            in = ParameterIn.PATH,
            example = "1",
            schema = @Schema(type = "number") // 스키마 타입
    )
    public ResponseEntity<ApiResponseDTO> getMemberInfo(@PathVariable Long id) {
        MemberResponseDTO member = memberService.getMemberInfo(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("단일 회원 조회 성공", member));
    }

//    회원 추가 서비스
    @PostMapping("/join")
    @Operation(summary = "회원가입 서비스", description = "회원 정보를 받아서 회원가입을 시켜주는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    public ResponseEntity<ApiResponseDTO> join(@RequestBody MemberJoinRequestDTO memberJoinRequestDTO) {
        memberService.join(memberJoinRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.of("회원가입 성공하였습니다."));
    }

//    회원 로그인 서비스
    @PostMapping("/login")
    @Operation(summary = "회원 로그인 서비스", description = "회원의 이메일과 비밀번호를 받아서 로그인 시켜주는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패"),
            @ApiResponse(responseCode = "401", description = "토큰 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    public ResponseEntity<ApiResponseDTO> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        MemberResponseDTO member = memberService.login(memberLoginRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("로그인 성공하였습니다.", member));
    }

//    회원정보 수정 서비스
    @PutMapping("/{id}")
    @Operation(summary = "회원 정소 수정 서비스", description = "회원의 정보를 입력 받아서 회원 정보를 수정해주는 서비스")
    @Parameter(
            name = "id",
            description = "회원번호",
            required = true,
            in = ParameterIn.PATH,
            example = "1",
            schema = @Schema(type = "number") // 스키마 타입
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 접근"),
            @ApiResponse(responseCode = "401", description = "토큰 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    public ResponseEntity<ApiResponseDTO> modifyMemberInfo(@PathVariable Long id, @RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO) {
//        사실은 비밀번호 암호화 로직 도 필요함
        memberService.modifyMemberInfo(memberUpdateRequestDTO, id);

//        결과 응답 (요청에 대해서는 무조건 응답을 해줘야 한다)
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("회원 정보 수정 성공"));
    }

//    회원 탈퇴 서비스
//    그런데 원래는 세션에 회원 정보가 저장이 되어있었지만 여기서는 어떤 식으로 해야할 지 생각 필요
    @DeleteMapping("/{id}")
    @Operation(summary = "회원 탈퇴 서비스", description = "회원 번호를 토대로 해서 해당 회원 정보를 삭제(탈퇴) 하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"), // 200으로 도 해야 함
            @ApiResponse(responseCode = "401", description = "토큰 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @Parameter(
            name = "id",
            description = "회원번호",
            required = true,
            in = ParameterIn.PATH,
            example = "1",
            schema = @Schema(type = "number") // 스키마 타입
    )
    public ResponseEntity<ApiResponseDTO> deleteMember(@PathVariable Long id) {
        memberService.withdrawMember(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponseDTO.of("회원 탈퇴 성공하였습니다."));
    }
}
