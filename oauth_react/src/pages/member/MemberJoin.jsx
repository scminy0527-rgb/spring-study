import React from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { handleError } from "../../api/errerHandler";
import { joinMember } from "../../api/MemberApi";

const MemberJoin = () => {
  const {
    register,
    handleSubmit,
    getValues,
    formState: { isSubmitting, errors },
  } = useForm({ mode: "onChange" });

  // 정규식 (검증 용)
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[!@#])[\da-zA-Z!@#]{8,}$/;

  const navigator = useNavigate();

  // 회원가입 함수 정의
  const join = handleSubmit(async (data) => {
    // 테스트 용 연습
    console.log(data);
    const { memberPasswordConfirm, ...memberJoinRequestDTO } = data;

    joinMember(memberJoinRequestDTO)
      .then(({ message, success, data }) => {
        console.log(message);
        console.log(success);
        console.log(data);

        navigator("/login");
      })
      .catch(handleError);
  });

  return (
    <div>
      회원가입
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
          {errors.memberEmail?.type === "pattern" && (
            <p>이메일 형식을 지켜주세요</p>
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
          {errors.memberPassword?.type === "pattern" && (
            <p>
              소문자, 숫자, 특수문자를 각 하니씩 포함한 8자리 이상이어야 합니다.
            </p>
          )}
        </div>
        <div>
          <p>비밀번호 확인</p>
          <input
            {...register("memberPasswordConfirm", {
              required: true,
              validate: (value) => {
                // getValues 통해서 기존에 입력한 값 가져올 수 있음
                const isMatch = value === getValues("memberPassword");
                console.log("비밀번호 일치 여부:", isMatch);
                return isMatch;
              },
            })}
          />
          {errors.memberPasswordConfirm?.type === "required" && (
            <p>비밀번호 확인을 입력하세요</p>
          )}
          {errors.memberPasswordConfirm?.type === "validate" && (
            <p>비밀번호가 일치하지 않습니다</p>
          )}
        </div>
        <div>
          <p>이름</p>
          <input {...register("memberName")} />
        </div>
        <div>
          <p>닉네임</p>
          <input {...register("memberNickname")} />
        </div>
        <button disabled={isSubmitting}>회원가입</button>
      </form>
    </div>
  );
};

export default MemberJoin;
