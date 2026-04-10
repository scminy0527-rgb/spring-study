package com.app.threetier.repository;

import com.app.threetier.domain.dto.StudentDTO;
import com.app.threetier.domain.vo.StudentVO;
import com.app.threetier.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// FM: Mapper 와 DAO 에서는 Optional 로 감싸지 않는다 (혼란 방지)
// 단일 객체의 결과물을 그대로 가져옴
// Optional 은 서비스 단에서 return 할 때 결정함
@Repository
@RequiredArgsConstructor
public class StudentDAO {
    private final StudentMapper studentMapper;

//    테이블에 학생 추가
    public void write(StudentVO studentVO){
        studentMapper.insert(studentVO);
    }

//    모든 학생 정보 불러오기
    public List<StudentDTO> findAll(){
        return studentMapper.selectAll();
    }

//    단일 학생 정보 불러오기 (optiaonal 은 객체지향 설계 중심인 dao 에서 수행)
//    매퍼는 RDB 중심 설계 이기에 DB 규칙을 따라야 함
//    대기업 에서는 서비스 영역에서 Mapper 랑 DAO에서 Optional 안감쌈
    public StudentDTO findById(Long id){
        return studentMapper.selectById(id);
    }

//    학생 정보 수정
    public void update(StudentVO studentVO){
        studentMapper.update(studentVO);
    }

//    학생 정보 삭제
    public void delete(Long id){
        studentMapper.delete(id);
    }
}

