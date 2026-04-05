package com.app.mybatis.mapper;

import com.app.mybatis.domain.dto.PostCountDTO;
import com.app.mybatis.domain.dto.PostDTO;
import com.app.mybatis.domain.dto.PostResponseDTO;
import com.app.mybatis.domain.vo.PostLikeVO;
import com.app.mybatis.domain.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
public class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostLikeMapper postLikeMapper;

    @Test
    public void insertTest(){
        PostVO postVO = new PostVO();
        postVO.setPostTitle("안녕하세요?");
        postVO.setPostContent("오늘도 반가운 하루 보내세요");
        postVO.setMemberId(24L);

        postMapper.insert(postVO);
    }

    @Test
    public void newInsertTest(){
        for(int i = 0; i < 50; i++){
            PostVO postVO = new PostVO();
            postVO.setPostTitle("테스트 게시글" + (i + 1));
            postVO.setPostContent("테스트 내용" + (i + 1));
            postVO.setMemberId(1L);
            postMapper.insert(postVO);
        }
    }

    @Test
    public void newUpdateReadCountTest(){
        Random random = new Random();
        random.nextInt(50);
        for(int i = 0; i < 5000; i++){
            Long id = Long.valueOf(random.nextInt(50));
            postMapper.updatePostReadCount(id);
        }
    }

    @Test
    public void selectAllTest(){
        log.info("{}",postMapper.selectAll());
    }

    @Test
//    그리고 글을 읽으면 동시에 조회수도 1 증가 하도록 해야함
    public void selectByIdTest(){
        log.info("dto 테스트");
        postMapper.selectById(2L).map(PostDTO::toString).ifPresent(log::info);
        postMapper.updatePostReadCount(2L);
    }

    @Test
    public void selectPostReadCountTest(){
        log.info("조회수 조회 테스트");
        log.info("게시글 조회 수: {}", postMapper.selectPostReadCountById(4L));
    }

    @Test
    public void updateTest(){
        PostVO postVO = new PostVO();
        postVO.setId(1L);
        postVO.setPostTitle("추가적인 테스트");
        postVO.setPostContent("안녕하세요???");

        postMapper.update(postVO);
    }

    @Test
    public void deleteTest(){
        postMapper.delete(3L);
    }

    @Test
    public void updateReadCountTest(){
        postMapper.updatePostReadCount(2L);
    }


    @Test
    public void selectAllWithOrderTest(){
        String order = "";
        HashMap<String,Object> orders = new HashMap<>();
        orders.put("order","popular");
        orders.put("cursor",1);
        orders.put("limit",5);
        postMapper.selectAllWithOrder(orders)
                .stream()
                .map(PostDTO::toString)
                .forEach(log::info);
    }

    @Test
    public void selectTotalPostCountAndTotalPageCount(){
        int limit = 5;
        log.info("DTO 정보: {}", postMapper.selectTotalPostCountAndTotalPageCount(limit).toString());
    }

//    게시판 목록 조회
    @Test
    public void seeTotal(){
        String order = "popular";
        int limit = 5;
        HashMap<String,Object> orders = new HashMap<>();
        orders.put("order", order);
        orders.put("cursor",1);
        orders.put("limit",5);
        postMapper.selectAllWithOrder(orders)
                .stream()
                .map(PostDTO::toString)
                .forEach(log::info);

        List<PostDTO> posts = postMapper.selectAllWithOrder(orders);
        PostCountDTO  postCountDTO = postMapper.selectTotalPostCountAndTotalPageCount(limit);
        PostResponseDTO postResponseDTO = new PostResponseDTO();

        postResponseDTO.setTotalPostCount(postCountDTO.getTotalPostCount());
        postResponseDTO.setTotalPageCount(postCountDTO.getTotalPageCount());
        postResponseDTO.setPosts(posts);

        log.info("postResponseDTO 결과: {}",postResponseDTO.toString());
    }

    @Test
    public void keywordTest(){
        HashMap<String,Object> orders = new HashMap<>();
        String keyword = "4", finalKeyword = "";
        if (!keyword.isEmpty()){
            finalKeyword = '%' + keyword + '%';
//            혹은 sql 에서 % || keyword || % 방식으로도 가능
        }

        orders.put("order","popular");
        orders.put("cursor",1);
        orders.put("limit",50);
        orders.put("keyword", finalKeyword);

        PostCountDTO postCountDTO = postMapper.selectNewTotalPostCountAndTotalPageCount(orders);
        List<PostDTO> posts = postMapper.selectAllWithOrder(orders);

        log.info("{}", postCountDTO.toString());
        posts.stream()
                .map(PostDTO::toString)
                .forEach(log::info);
    }


    @Test
    public void postLikeTest(){
        PostLikeVO postLikeVO = new PostLikeVO();

//        1 ~ 50 까지 글
        for(int i = 0; i < 50; i++){
            Random random = new Random();
            int randomNum = random.nextInt(1, 51);
            postLikeVO.setPostId(Long.valueOf(randomNum));

            randomNum = random.nextInt(1, 4);
            postLikeVO.setMemberId(Long.valueOf(randomNum));
            postLikeMapper.insert(postLikeVO);
        }
    }

//    랜덤하게 포스트 좋아요 를 넣기 위한거
    @Test
    public void randomPostLikeTest(){
        Random random = new Random();

        List<Integer> randomList = IntStream.rangeClosed(1, 50)
                .boxed()                          // int → Integer 박싱
                .collect(Collectors.toList());

        Collections.shuffle(randomList);

        for(int i = 0; i < 3; i++){
            int randomCount = random.nextInt(1, 51);
            for(int j = 0; j < randomCount; j++){
                PostLikeVO postLikeVO = new PostLikeVO();
                postLikeVO.setPostId(Long.valueOf(randomList.get(j)));
                postLikeVO.setMemberId(Long.valueOf(i + 1));
                postLikeMapper.insert(postLikeVO);
            }
        }
    }


//  모든 게시글
    @Test
    public void selectTestAllPostWithLikeClick(){
        postLikeMapper.selectAllAndIsClick(null).stream()
                .map(PostDTO::toString)
                .forEach(log::info);
    }

//    전체 게시글 목록 + 현재 회원(memberId=1)의 좋아요 여부 확인
    @Test
    public void postLikeSelectAllTest(){
        Long memberId = 1L;
        postLikeMapper.selectAll(memberId)
                .stream()
                .map(PostDTO::toString)
                .forEach(log::info);
    }

//    단건 게시글 조회 + 좋아요 수 + 현재 회원의 좋아요 여부 (IS_CLICK)
    @Test
    public void postLikeSelectByIdTest(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("postId", 1L);
        params.put("memberId", 1L);

        postLikeMapper.selectByIdAndPostLike(params)
                .map(PostDTO::toString)
                .ifPresent(log::info);
    }

//    키워드 검색 + 현재 회원의 좋아요 여부 확인
    @Test
    public void postLikeSelectWithKeywordTest(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("keyword", "테스트");
        params.put("memberId", 1L);

        postLikeMapper.selectAllWithKeyword(params)
                .stream()
                .map(PostDTO::toString)
                .forEach(log::info);
    }

//    좋아요 취소 테스트
    @Test
    public void postUnlikeTest(){
        PostLikeVO postLikeVO = new PostLikeVO();
        postLikeVO.setPostId(1L);
        postLikeVO.setMemberId(1L);

        postLikeMapper.delete(postLikeVO);
        log.info("좋아요 취소 완료 - postId: {}, memberId: {}", postLikeVO.getPostId(), postLikeVO.getMemberId());
    }
}
