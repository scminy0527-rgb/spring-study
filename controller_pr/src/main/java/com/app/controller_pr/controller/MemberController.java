package com.app.controller_pr.controller;

import com.app.controller_pr.domain.vo.MemberVO;
import com.app.controller_pr.mapper.MemberMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequestMapping("/members/*")
@RequiredArgsConstructor
public class MemberController {
    final private MemberMapper memberMapper;
    final private HttpSession session;

    @GetMapping("join")
    public void goToJoin(MemberVO memberVO) {;}

    @PostMapping("join")
    public RedirectView join(MemberVO memberVO) {
        memberMapper.join(memberVO);
        return new RedirectView("/members/login");
    }

    @GetMapping("login")
    public void goToLogin(MemberVO memberVO) {}

    @PostMapping("login")
    public RedirectView login(MemberVO memberVO, RedirectAttributes redirectAttributes) {
        Optional<MemberVO> foundMember = memberMapper.selectOneByEmeberEmailAndMemberPassword(memberVO);

        if (!foundMember.isPresent()) {
            redirectAttributes.addFlashAttribute("isLogin", false);
            return  new RedirectView("/members/login");
        }

        return  new RedirectView("/members/my-page");
    }


}
