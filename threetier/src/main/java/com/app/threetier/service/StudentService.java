package com.app.threetier.service;

import com.app.threetier.domain.dto.StudentDTO;
import com.app.threetier.domain.vo.StudentVO;

import java.util.List;
import java.util.Optional;

public interface StudentService {
//    학생 추가
    public void registerStudent(StudentVO studentVO);

    //    모든 학생 정보 불러오기
    public List<StudentDTO> getStudentList();

//    단일 학생 불러오기
//    서비스 부분 부터 Optional 로 감싸기
    public Optional<StudentDTO> getStudent(Long id);

//    학생 정보 수정
    public void updateStudent(StudentVO studentVO);

//    학생 삭제
    public void deleteStudent(Long id);
}
