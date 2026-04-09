package com.app.threetier.controller;

import com.app.threetier.domain.dto.PostDTO;
import com.app.threetier.domain.vo.PostVO;
import com.app.threetier.mapper.PostMapper;
import com.app.threetier.service.PostService;
import com.app.threetier.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/posts/*")
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    @GetMapping("list")
    public void goToList(Model model) {
        List<PostDTO> posts = postService.getPosts();
        model.addAttribute("posts", posts);
    }

    @GetMapping("read")
    public void read(Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
    }

    @GetMapping("update")
    public void update(Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
    }

    @PostMapping("update-ok")
    public RedirectView updatePost(PostVO postVO) {
        postService.updatePost(postVO);
        return new RedirectView("/posts/read?id="+postVO.getId());
    }

    @PostMapping("delete-ok")
    public RedirectView delete(Long id) {
        postService.deletePost(id);
        return new RedirectView("/posts/list");
    }
}
