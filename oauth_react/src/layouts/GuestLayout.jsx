import { Navigate, Outlet } from "react-router-dom";
import useAuthStore from "../store/useAuthStore";

// GuestLayout: 로그인하지 않은 사용자만 접근 가능한 페이지를 감싸는 레이아웃
// 예) 로그인 페이지, 회원가입 페이지 → 이미 로그인한 사람이 접근하면 마이페이지로 보냄
const GuestLayout = () => {
  const { isAuthenticated } = useAuthStore();

  // 이미 로그인된 상태라면 로그인/회원가입 페이지 접근을 막고 마이페이지로 이동
  // replace: true → 히스토리에 /login, / 경로를 남기지 않음
  if (isAuthenticated) return <Navigate to="/mypage" replace />;

  // 비로그인 상태라면 자식 라우트(로그인, 회원가입 페이지)를 정상 렌더링
  return <Outlet />;
};

export default GuestLayout;
