package com.app.threetier.controller;

import com.app.threetier.domain.vo.WorkVO;
import com.app.threetier.service.WorkService;
import com.app.threetier.service.WorkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;

@Controller
@RequestMapping("/works/*")
@RequiredArgsConstructor
public class WorkController {
    private final WorkServiceImpl workService;


    @GetMapping("company")
    public void company(WorkVO workVO) {;}

//    출근 버튼을 눌렀을 때 할 동작
    @PostMapping("work")
    public RedirectView work(WorkVO workVO, RedirectAttributes redirectAttributes){
        LocalDate nowDate = LocalDate.now();
        workVO.setWorkStart(nowDate.toString());
//        출근 이라는 서비스를 처리 해 주기
        if(workService.workStart(workVO)){
//            출근 완료
            return new RedirectView("/works/work-result?id="+workVO.getId());
        } else {
//            중복 출근
            redirectAttributes.addFlashAttribute("isAlreadyWork",true);
            return new RedirectView("/works/company");
        }
    }

    @GetMapping("work-result")
    private void goToWorkResult(Long id, Model model){
        boolean isInTime = workService.isLate(id);
        model.addAttribute("isInTime",isInTime);
    }

//    퇴근 버튼 눌렀을 때
    @GetMapping("exit-result")
    public void goToExitResult(Model model){

    }
}
