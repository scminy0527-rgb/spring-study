package com.app.ncs.controller;

import com.app.ncs.domain.vo.MemberVO;
import com.app.ncs.mapper.MemberMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public void goToLogin(MemberVO memberVO) {;}

    @PostMapping("login")
    public RedirectView login(MemberVO memberVO, RedirectAttributes redirectAttributes) {
        Optional<MemberVO> foundMember = memberMapper.selectOneByMemberEmailAndMemberPassword(memberVO);
        if(!foundMember.isPresent()) {
            redirectAttributes.addFlashAttribute("isLogin", false);
            return new RedirectView("/members/login");
        }
        MemberVO member = foundMember.get();
        session.setAttribute("member", member);

        return new RedirectView("/members/my-page");
    }

    @GetMapping("my-page")
    public void goToMyPage() {;}

    @GetMapping("update")
    public void goToUpdate(Model model) {
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        model.addAttribute("memberVO", memberVO);
    }

    @PostMapping("update")
    public RedirectView update(MemberVO memberVO) {
        memberMapper.update(memberVO);
        Optional<MemberVO> foundMember = memberMapper.selectOneByMemberEmailAndMemberPassword(memberVO);
        if(foundMember.isPresent()) {
            session.setAttribute("member", foundMember.get());
        }

        return new RedirectView("/members/my-page");
    }

    @PostMapping("logout")
    public RedirectView logout() {
        session.invalidate();
        return new RedirectView("/members/login");
    }

    @PostMapping("withdraw")
    public RedirectView withdraw() {
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        memberMapper.delete(memberVO.getId());
        session.invalidate();

        return new RedirectView("/members/join");
    }
}
