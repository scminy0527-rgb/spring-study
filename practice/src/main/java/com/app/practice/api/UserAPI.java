package com.app.practice.api;

import com.app.practice.domain.dto.*;
import com.app.practice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;

    //    회원가입 하는 서비스
    @PostMapping("")
    @Operation(description = "유저 회원가입 진행")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @ApiResponse(responseCode = "400", description = "이메일 중복")
    public ResponseEntity<ApiResponseDTO> registerUser(@RequestBody UserJoinRequestDTO userJoinRequestDTO) {
        userService.registerUser(userJoinRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("회원가입 성공"));
    }

//    유저 조회 서비스
    @GetMapping("/{id}")
    @Operation(description = "유저 조회 진행")
    @ApiResponse(responseCode = "200", description = "유저 조회 성공")
    @ApiResponse(responseCode = "400", description = "유저 조회 실패")
    @Parameter(
            name = "id",
            description = "유저 아이디",
            in = ParameterIn.PATH,
            example = "1",
            required = true,
            schema = @Schema(type = "number")
    )
    public ResponseEntity<ApiResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("유저 불러오기 성공", userResponseDTO));
    }

//    유저 로그인 서비스
    @PostMapping("/login")
    @Operation(description = "유저 로그인 진행")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "400", description = "로그인 실패")
    public ResponseEntity<ApiResponseDTO> loginUser(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        UserResponseDTO userResponseDTO = userService.loginUser(userLoginRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("로그인 성공",  userResponseDTO));
    }

//    유저 정보 수정 서비스
    @PutMapping("/{id}")
    @Operation(description = "유저 정보 수정 진행")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @ApiResponse(responseCode = "400", description = "수정 실패")
    @Parameter(
            name = "id",
            description = "유저 아이디",
            in = ParameterIn.PATH,
            example = "1",
            required = true,
            schema = @Schema(type = "number")
    )
    public ResponseEntity<ApiResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        userService.modifyUserInfo(id, userUpdateRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("정보 수정 성공"));
    }
}
