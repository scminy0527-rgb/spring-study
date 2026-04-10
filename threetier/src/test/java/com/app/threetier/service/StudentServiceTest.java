package com.app.threetier.service;

import com.app.threetier.domain.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class StudentServiceTest {
    @Autowired
    private StudentServiceImpl studentService;

    @Test
    public void registerStudentTest(){
        StudentVO studentVO = new StudentVO();
        studentVO.setStudentName("이규학");
        studentVO.setStudentEng(20);
        studentVO.setStudentKor(50);
        studentVO.setStudentMath(80);

        studentService.registerStudent(studentVO);
    }

    @Test
    public void getStudentListTest(){
        studentService.getStudentList().stream()
                .forEach((studentDTO)->{
                    log.info(studentDTO.toString());
                });
    }

    @Test
    public void getStudentTest(){
        log.info("학생: {}", studentService.getStudent(21L));
    }

    @Test
    public void updateStudentTest(){
        log.info("수정 전: {}", studentService.getStudent(21L));

        StudentVO studentVO = new StudentVO();
        studentVO.setId(21L);
        studentVO.setStudentName("이규학");
        studentVO.setStudentEng(90);
        studentVO.setStudentKor(90);
        studentVO.setStudentMath(88);
        studentService.updateStudent(studentVO);

        log.info("수정 후: {}", studentService.getStudent(21L));
    }

    @Test
    public void deleteStudentTest(){
        studentService.deleteStudent(21L);
    }
}
