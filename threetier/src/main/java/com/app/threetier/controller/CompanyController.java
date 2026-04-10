package com.app.threetier.controller;

import com.app.threetier.domain.vo.CompanyVO;
import com.app.threetier.service.CompanyService;
import com.app.threetier.service.CompanyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/companies/*")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyServiceImpl companyService;

    @GetMapping("check-in")
    public void goToCheckIn(CompanyVO companyVO){

    }

    @PostMapping("check-in-ok")
    public RedirectView goToCheckInOk(CompanyVO companyVO, String flag){
//        시간 객체 생성 및 출근 퇴근 기준 시간 설정
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetStart = LocalDateTime.of(2026, 4, 10, 9, 0, 59);
        LocalDateTime targetEnd = LocalDateTime.of(2026, 4, 10, 18, 0, 0);

//       포맷 적용
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
        String formatted = now.format(formatter);

        boolean isLate = now.isAfter(targetStart);
        boolean isWork = now.isBefore(targetEnd);

//        해당 호출된 시점을 기준으로 해서 지각 혹은 조퇴 여부를 구한 다음에
//        각 결과에 해당하는 페이지로 리디렉트 해주기
        if(flag.equals("getToWork")){
            companyVO.setCompanyWorkStart(formatted);
            companyService.registerCommute(companyVO);
            return new RedirectView(isLate ? "/companies/late" : "/companies/get-to-work");
        } else if(flag.equals("leaveWork")){
            companyVO.setCompanyWorkEnd(formatted);
            companyService.registerCommute(companyVO);
            return new RedirectView( isWork ? "/companies/work" : "/companies/leave-work");
        }

//        만약 정의되지 아니한거면 다시 원래 check-in 페이지로 다시 가게 하기
        return new RedirectView("/companies/check-in");
    }

    @GetMapping("get-to-work")
    public void goTogetToWork(Model model) {

    }

    @GetMapping("leave-work")
    public void goToLeaveWork(Model model) {

    }

    @GetMapping("late")
    public void goToLate(Model model) {}

    @GetMapping("work")
    public void goToWork(Model model) {}

}
