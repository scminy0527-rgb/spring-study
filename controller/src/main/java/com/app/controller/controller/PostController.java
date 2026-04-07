package com.app.controller.controller;

import com.app.controller.domain.dto.PostDTO;
import com.app.controller.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts/*")
@RequiredArgsConstructor
public class PostController {

    final private PostMapper postMapper;

    @GetMapping("list")
    public void goToList(Model model) {
        List<PostDTO> posts = postMapper.selectAll();
        model.addAttribute("posts", posts);
    }
}
