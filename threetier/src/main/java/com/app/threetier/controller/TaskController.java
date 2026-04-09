package com.app.threetier.controller;

import com.app.threetier.domain.vo.TaskVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks/*")
public class TaskController {

    @GetMapping("register")
    public void goToRegister(TaskVO taskVO){;}

    @GetMapping("result")
    public void goToResult(TaskVO taskVO){;}
}
