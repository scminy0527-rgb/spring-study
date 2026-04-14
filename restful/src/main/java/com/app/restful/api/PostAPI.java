package com.app.restful.api;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.service.PostServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
