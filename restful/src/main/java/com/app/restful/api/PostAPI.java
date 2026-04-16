package com.app.restful.api;

import com.app.restful.domain.dto.ApiResponseDTO;
import com.app.restful.domain.dto.PostDTO;
import com.app.restful.domain.dto.PostUpdateRequestDTO;
import com.app.restful.domain.dto.PostWriteRequestDTO;
import com.app.restful.service.PostServiceImpl;
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

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostAPI {
    private final PostServiceImpl postService;

//    게시글 목록 불러오는 서비스
    @Operation(summary = "전체 글 불러오기", description = "전체 게시글을 불러와주는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 전체 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<ApiResponseDTO> getAllPosts(
            @RequestParam(value = "order", defaultValue = "desc") String order
    ) {
        List<PostDTO> posts = postService.getAllPosts(order);
//        윕 표준: http 로 요청을 했으면 반드시 응답값을 줘야 함
//        따라서 데이터가 있으면 데이터와 함께, 반환할 데이터가 없다면 결과 를 보여줘야 한다.
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("게시글 목록 조회 성공", posts));
    }

//    게시글 단일조회 하는 서비스
    @Operation(summary = "게시글 조회", description = "해당 게시글 상세 내용을 조회하는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 조회 실패")
    })
    @Parameter(
            name = "id",
            description = "게시글 번호",
            required = true,
            in = ParameterIn.PATH,
            example = "1",
            schema = @Schema(type = "number")
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getPost(@PathVariable Long id) {
        PostDTO post = postService.getPostDetail(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("게시글 상세 조회 성공", post));
    }

//    게시글 작성하는 서비스
    @Operation(summary = "게시글 작성 서비스", description = "게시글울 적성해주는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
//    restful 에서 member 를 제외 하고는 상태를 대표할 수 있는 posts 가 게시판의 작성을 대표하는 경로가 되어야함
    @PostMapping("")
    public ResponseEntity<ApiResponseDTO> writePost(@RequestBody PostWriteRequestDTO postWriteRequestDTO) {
//        멤버 아이디는 추후 토큰으로 추출 하기에 임의로 넣어놓음
        postService.writePost(postWriteRequestDTO, 3L);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.of("게시글 작성 성공"));
    }

//    게시글을 수정하는 서비스
    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "해당 번호의 게시글을 수정하는 서비스")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
    @ApiResponse(responseCode = "404", description = "게시글 수정 실패")
    @Parameter(
            name = "id",
            description = "게시글 번호",
            required = true,
            in = ParameterIn.PATH,
            example = "1",
            schema = @Schema(type = "number")
    )
    public ResponseEntity<ApiResponseDTO> modifyPost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDTO postUpdateRequestDTO
    ) {
        postService.updatePost(postUpdateRequestDTO, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("게시글 수정 성공하였습니다."));
    }

//    오류 해결용
//    @PutMapping("/update")
//    public void updatePostOne(
//            @RequestBody PostUpdateRequestDTO postUpdateRequestDTO
//    ) {
//        postService.updatePost(postUpdateRequestDTO, );
//    }

//    게시글을 삭제하는 서비스
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제해주는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @Parameter(
            name = "id",
            description = "게시글 번호",
            example = "1",
            in = ParameterIn.PATH,
            required = true,
            schema = @Schema(type = "number")
    )
    public ResponseEntity<ApiResponseDTO> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponseDTO.of("게시글 삭제가 성공하였습니다."));
    }

//    회원 탈퇴 시 모든 게시글 삭제
//    또한 이거는 회원 탈퇴 서비스 에도 탈퇴와 동시에 게시글 삭제 되도록 하는거도 넣어둬야함
    @Operation(summary = "멤버 글 삭제", description = "멤버가 탈퇴를 할 시 작성한 모든 글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @Parameter(
            name = "memberId",
            description = "멤버의 아이디",
            example = "1",
            in = ParameterIn.PATH,
            required = true,
            schema = @Schema(type = "number")
    )
    @DeleteMapping("/deleteAll/{memberId}")
    public ResponseEntity deleteMembersAllPosts(@PathVariable Long memberId) {
        postService.deleteMembersAllPosts(memberId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponseDTO.of("회원의 게시글 모두 삭제 성공"));
    }
}
