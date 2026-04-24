import React from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import useAuthStore from "../../store/useAuthStore";
import { loginAuth } from "../../api/AuthApi";
import { fetchMe } from "../../api/MemberApi";

const Login = () => {
  // useForm: 입력값 관리 및 유효성 검사 라이브러리
  // mode: "onChange" → 입력할 때마다 실시간으로 유효성 검사
  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors },
  } = useForm({ mode: "onChange" });

  // 이메일 형식 정규식
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  // 비밀번호: 소문자, 숫자, 특수문자(!@#) 각 1개 이상, 8자리 이상
  const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[!@#])[\da-zA-Z!@#]{8,}$/;

  // 전역 상태에서 유저 정보 저장 함수, 인증 상태 변경 함수 가져오기
  const { setMember, setIsAuthenticated } = useAuthStore();

  // 페이지 이동 함수
  const navigate = useNavigate();

  // handleSubmit: 유효성 검사를 통과한 경우에만 아래 함수 실행
  const login = handleSubmit(async (data) => {
    // 폼 데이터에서 이메일, 비밀번호만 추출해서 요청 DTO 생성
    const { memberEmail, memberPassword } = data;

    // 1단계: 로그인 요청 → 서버가 JWT를 쿠키로 발급
    // auth 컨트롤러 담당: 인증(로그인/로그아웃) 관련 책임만 가짐
    // 성공/실패를 분리해서 catch하면 어디서 문제가 생겼는지 정확하게 파악 가능
    try {
      await loginAuth({ memberEmail, memberPassword });
    } catch (err) {
      alert("로그인 실패: " + err.message);
      return; // 로그인 자체가 실패했으면 이후 단계 실행하지 않음
    }

    // 2단계: 발급된 쿠키를 이용해 /me 호출 → 현재 로그인한 유저 정보 조회
    try {
      const { data: memberData } = await fetchMe();

      // 3단계: 조회한 유저 정보를 Zustand 전역 상태에 저장
      setMember(memberData);
      // 인증 상태를 true로 변경 → 헤더에서 로그인/회원가입 링크 숨겨짐
      setIsAuthenticated(true);

      // 마이페이지로 이동
      navigate("/mypage");
    } catch (err) {
      alert("유저 정보 조회 실패: " + err.message);
    }
  });

  return (
    <div>
      로그인
      <form onSubmit={login}>
        <div>
          <p>이메일</p>
          {/* register: 해당 input을 react-hook-form에 등록, 유효성 규칙 설정 */}
          <input
            {...register("memberEmail", {
              required: true,
              pattern: { value: emailRegex },
            })}
          />
          {errors.memberEmail?.type === "required" && <p>이메일을 입력하세요</p>}
          {errors.memberEmail?.type === "pattern" && <p>이메일 형식에 맞게 입력해주세요</p>}
        </div>

        <div>
          <p>비밀번호</p>
          <input
            type="password"
            {...register("memberPassword", {
              required: true,
              pattern: { value: passwordRegex },
            })}
          />
          {errors.memberPassword?.type === "required" && <p>비밀번호를 입력해주세요</p>}
          {errors.memberPassword?.type === "pattern" && (
            <p>소문자, 숫자, 특수문자(!@#)를 각 하나씩 포함한 8자리 이상이어야 합니다</p>
          )}
        </div>

        {/* isSubmitting: 요청 중일 때 버튼 비활성화 → 중복 제출 방지 */}
        <button disabled={isSubmitting}>로그인</button>
      </form>
    </div>
  );
};

export default Login;
