package com.app.rest_pr.api;

import com.app.rest_pr.domain.dto.*;
import com.app.rest_pr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAPI {
    private final UserService userService;

    @Operation(description = "회원가입 진행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이메일 중복")
    })
    @PostMapping("")
    public ResponseEntity<ApiResponseDTO> registerUser(@RequestBody UserJoinRequestDTO userJoinRequestDTO) {
        userService.registerUser(userJoinRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.of("회원가입 성공"));
    }

    @Operation(description = "전체 유저 정보 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<ApiResponseDTO> getUsers() {
        List<UserResponseDTO> userResponseDTO = userService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("유저 정보 불러오기 성공", userResponseDTO));
    }

//    유저 로그인 서비스
    @Operation(description = "전체 유저 정보 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패"),
            @ApiResponse(responseCode = "401", description = "토큰 없음 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> loginUser(
            @RequestBody UserLoginRequestDTO userLoginRequestDTO
    ) {
        UserResponseDTO user = userService.loginUser(userLoginRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("유저 불러오기 성공",  user));
    }

//    유저 정보 불러오기
    @Operation(description = "id 로 유저 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getUserById(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("유저 불러오기 성공", userService.getUserInfo(id)));
    }

//    유저 정보 수정하는거도 구현하기
    @PutMapping("/{id}")
    @Operation(description = "유저 정보 수정 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 수정 성공"),
            @ApiResponse(responseCode = "400", description = "수정 실패")
    })
    public ResponseEntity<ApiResponseDTO> modifyUser(
            @PathVariable long id,
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        userService.updateUserInfo(id, userUpdateRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("유저 정보 수정 완료"));
    }

//    유저 삭제하는 서비스
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> withdrawUser(@PathVariable long id) {
        userService.withdrawUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponseDTO.of("유저 삭제 성공하였습니다."));
    }
}
