package com.app.threetier.controller;

import com.app.threetier.domain.vo.MemberVO;
import com.app.threetier.mapper.MemberMapper;
import com.app.threetier.service.MemberServiceImpl;
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
@RequestMapping("/members/*") // restful
@RequiredArgsConstructor
public class MemberController {
//    해당 인터페이스를 사용 하려면 의존성 주입을 받아야 하지만 필두 주입을 받으면 안됨
//    따라서 생성자를 통해서 주입 받는 식으로 해야 하고 @RequiredArgs~~ 를 이용 (아니면 @Data 도 가능은 함)
    private final MemberMapper memberMapper;
    private final MemberServiceImpl memberService;
    private final HttpSession session;

    //    회원가입
    @GetMapping("join")
    public void goToJoin(MemberVO memberVO) {;}

//    값을 받아서 db 에 저장
    @PostMapping("join")
    public RedirectView join(MemberVO memberVO) {
        memberService.joinMember(memberVO);
        return new RedirectView("/members/login");
    }

    @GetMapping("login")
    public void goToLogin(MemberVO memberVO) {;}

//    로그인 실습
//    로그인이 완료 되면 /members/my-page 로 응답
    @PostMapping("login")
    public RedirectView login(MemberVO memberVO, RedirectAttributes redirectAttributes) {
        String memberEmail = memberVO.getMemberEmail();
//        로그인 관련 로직 실행
        MemberVO member = memberService.loginMember(memberVO);
        session.setAttribute("member", member);
        return new RedirectView("/members/my-page");
    }

    @GetMapping("my-page")
    public void goToMyPage() {;}

    @GetMapping("update")
    public void goToUpdate(Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        model.addAttribute("memberVO", member);
    }

    @PostMapping("update")
    public RedirectView update(MemberVO memberVO, RedirectAttributes redirectAttributes) {
//        원래는 이메일 중복 여부도 확인 해야 함
//        바꾸려는 이메일이 이미 존재하면 못바꾸게 함
        if(memberMapper.existByMemberIdAndMemberEmailForUpdate(memberVO) != 0){
            redirectAttributes.addFlashAttribute("isUpdate", false);
            return new RedirectView("/members/update");
        }
        memberMapper.update(memberVO);
        Optional<MemberVO> foundMember = memberMapper.selectOneById(memberVO.getId());
        if(foundMember.isPresent()){
            session.setAttribute("member", memberVO);
        }

        return new RedirectView("/members/my-page");
    }

//    로그 아웃 (invalidate 를 이용해서 세션의 모든 값을 초기화 하는게 브라우저 무리 줄일 수 있음)
    @PostMapping("logout")
    public RedirectView logout() {
        session.invalidate();
        return new RedirectView("/members/login");
    }

//    회원 탈퇴
    @PostMapping("delete")
    public RedirectView delete() {
        MemberVO member = (MemberVO) session.getAttribute("member");
        memberMapper.delete(member.getId());
        session.invalidate();
        return new RedirectView("/members/login");
    }
}

