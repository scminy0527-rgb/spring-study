package com.app.controller.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Arrays;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/ex/*")
public class ExampleController {
//    해당 매서드는 doget 과 비슷함
    @GetMapping("/ex01")
//HttpServletRequest req 로 받을 준비 가능
    public String ex01(String name, int age) {
        log.info("ex01 응답 완료");

//        이름과 나이를 요청 보내고
//        이름과 나이 만 나이 로그 출력
        log.info("이름: {}", name);
        log.info("나이: {}세", age + "");
        log.info("만 나이: {}세", (age - 1) + "");

//        응답하는 페이지의 파일 경로
//        templates 의 ex01.html 파일 경로
//        /를 붙이면 계속 폴더 타고 들어갈 수 있게 가능
        return "ex01";
    }

    @PostMapping("/ex01")
    public void ex01Post(String name, int age) {
        log.info("post 테스트");
        log.info("이름: {}", name);
        log.info("나이: {}세", age + "");
        log.info("만 나이: {}세", (age - 1) + "");

//        return "ex01";
    }

    @GetMapping("ex02")
    public String ex02(String name, Model model) {
        model.addAttribute("name", name);
        return "ex02";
    }

    @GetMapping("ex03")
    public String ex03(Model model) {
//        ["홍길동", "장보고", "이순신"]
        List<String> names = List.of("홍길동", "이순신", "장보고");
        model.addAttribute("names", names);

        return "ex03";
    }

//    ModelAttribute 반드시 쿼리스트링으로 값을 전달해야한다
    @GetMapping("ex04")
    public String ex04(@ModelAttribute("name") String name) {
        return "ex04";
    }

//    Attribute
//    이름, 취미를 받고 화면에 이름:ㅇㅇㅇ 취미:ㅇㅇㅇ 출력

    @GetMapping("ex05")
    public  String ex05(@ModelAttribute("name") String name, @ModelAttribute("hobby") String hobby) {
        return "ex05";
    }
}
