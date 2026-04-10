package com.app.threetier.controller;

import com.app.threetier.domain.dto.StudentDTO;
import com.app.threetier.domain.vo.StudentVO;
import com.app.threetier.service.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/students/*")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    @GetMapping("register")
    public void goToRegister(StudentVO studentVO) {;}

    @PostMapping("register-ok")
    public RedirectView registerOk(StudentVO studentVO) {
        studentService.registerStudent(studentVO);
        return new RedirectView("/students/read?id=" + studentVO.getId());
    }

    @GetMapping("read")
    public void goToRead(Long id, Model model) {
        StudentDTO studentDTO = studentService.getStudent(id).orElse(new StudentDTO());
        model.addAttribute("studentDTO", studentDTO);
    }

    @GetMapping("list")
    public void goToList(Model model) {
        List<StudentDTO> studentDTOList = studentService.getStudentList();
        model.addAttribute("students", studentDTOList);
    }
}
