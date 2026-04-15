package com.app.restful.api;

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
            @ApiResponse(responseCode = "200", description = "게시글 전체 목록 조회 성공")
    })
    @GetMapping("")
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

//    게시글 상세조회 하는 서비스
    @Operation(summary = "게시글 조회", description = "게시글 상세 내용을 조회하는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 불러오기 성공")
    })
    @Parameter(
            name = "id",
            description = "게시글 번호",
            required = true,
            in = ParameterIn.PATH,
            example = "1",
            schema = @Schema(type = "number")
    )
    @GetMapping("{id}")
    public PostDTO getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

//    게시글 작성하는 서비스
    @Operation(summary = "게시글 작성 서비스", description = "게시글울 적성해주는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공")
    })
    @PostMapping("/write")
    public void writePost(@RequestBody PostWriteRequestDTO postWriteRequestDTO) {
        postService.writePost(postWriteRequestDTO);
    }

//    게시글을 수정하는 서비스
    @PutMapping("/update/{id}")
    @Operation(summary = "게시글 수정", description = "유저가 작성한 게시글을 수정하는 서비스")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @Parameter(
            name = "id",
            description = "게시글 번호",
            required = true,
            in = ParameterIn.PATH,
            example = "1",
            schema = @Schema(type = "number")
    )
    public void updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDTO postUpdateRequestDTO
    ) {
        postUpdateRequestDTO.setId(id);
        postService.updatePost(postUpdateRequestDTO);
    }

//    오류 해결용
    @PutMapping("/update")
    public void updatePostOne(
            @RequestBody PostUpdateRequestDTO postUpdateRequestDTO
    ) {
        postService.updatePost(postUpdateRequestDTO);
    }

//    게시글을 삭제하는 서비스
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제해주는 서비스")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    @Parameter(
            name = "id",
            description = "게시글 번호",
            example = "1",
            in = ParameterIn.PATH,
            required = true,
            schema = @Schema(type = "number")
    )
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

//    회원 탈퇴 시 모든 게시글 삭제
    @Operation(summary = "멤버 글 삭제", description = "멤버가 탈퇴를 할 시 작성한 모든 글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공")
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
    public void deleteMembersAllPosts(@PathVariable Long memberId) {
        postService.deleteMembersAllPosts(memberId);
    }
}
