import React from "react";
import { useForm } from "react-hook-form";
import { loginAuth } from "../../api/AuthApi";
import { fetchMe } from "../../api/MemberApi";
import { useNavigate } from "react-router-dom";
import useAuthStore from "../../store/useAuthStore";

const MemberLoginComponent = () => {
  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors },
  } = useForm({ mode: "onChange" });

  const { setIsAuthenticated, setMember } = useAuthStore();
  const navigator = useNavigate();

  // 정규식 (검증 용)
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[!@#])[\da-zA-Z!@#]{8,}$/;

  const join = handleSubmit(async (data) => {
    const { memberPasswordConfirm, memberName, ...memberLoginRequestDTO } = data;
    try {
      await loginAuth(memberLoginRequestDTO);
      const { data: memberData } = await fetchMe();
      setIsAuthenticated(true);
      setMember(memberData);
      navigator(`/members/member-info/${memberData.id}`);
    } catch (e) {
      console.error("로그인 실패:", e.message);
    }
  });

  return (
    <div>
      로그인
      <form onSubmit={join}>
        <div>
          <p>이메일</p>
          <input
            {...register("memberEmail", {
              required: true,
              pattern: {
                value: emailRegex,
              },
            })}
          />
          {errors.memberEmail?.type === "required" && (
            <p>이메일을 입력하세요</p>
          )}
        </div>
        <div>
          <p>비밀번호</p>
          <input
            {...register("memberPassword", {
              required: true,
              pattern: {
                value: passwordRegex,
              },
            })}
          />
          {errors.memberPassword?.type === "required" && (
            <p>비밀번호를 입력하세요</p>
          )}
        </div>

        <button disabled={isSubmitting}>로그인</button>
      </form>
    </div>
  );
};

export default MemberLoginComponent;
