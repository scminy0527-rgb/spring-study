import { Navigate, Outlet } from "react-router-dom";
import useAuthStore from "../store/useAuthStore";

// UnAuthLayout: 비로그인 사용자만 접근 가능한 페이지를 감싸는 레이아웃
// 예) 로그인 페이지, 회원가입 페이지 → 이미 로그인한 사람이 접근하면 마이페이지로 보냄
const UnAuthLayout = () => {
  const { isAuthenticated, _hasHydrated } = useAuthStore();

  // 하이드레이션 완료 전: 아직 localStorage를 읽기 전이므로 아무것도 렌더링하지 않음
  // 이렇게 하면 로그인 상태인데도 로그인 페이지가 순간 보이는 깜빡임 현상을 방지할 수 있음
  if (!_hasHydrated) return null;

  // 하이드레이션 완료 후 로그인 상태 → 마이페이지로 이동
  // replace: 히스토리에 /login, / 경로를 남기지 않음
  if (isAuthenticated) return <Navigate to="/mypage" replace />;

  // 비로그인 상태 → 자식 페이지(로그인, 회원가입) 정상 렌더링
  return <Outlet />;
};

export default UnAuthLayout;
