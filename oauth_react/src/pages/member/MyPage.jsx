import useAuthStore from "../../store/useAuthStore";

// 마이페이지: AuthLayout이 로그인 여부를 이미 검사했으므로
// 이 컴포넌트는 항상 로그인된 상태에서만 렌더링됨
const MyPage = () => {
  const { member } = useAuthStore();

  return (
    <div>
      <p>{member?.memberName}님 환영합니다.</p>
    </div>
  );
};

export default MyPage;
