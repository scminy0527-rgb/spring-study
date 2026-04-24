import { Navigate, Outlet } from "react-router-dom";
import useAuthStore from "../store/useAuthStore";

// AuthLayout: 로그인한 사용자만 접근 가능한 페이지를 감싸는 레이아웃
// 예) 마이페이지 등 인증이 필요한 페이지에 사용
const AuthLayout = () => {
  const { isAuthenticated, _hasHydrated } = useAuthStore();

  // 하이드레이션이 끝나지 않은 상태: localStorage에서 로그인 정보를 아직 못 읽어온 시점
  // 이 시점에 isAuthenticated는 초기값(false)이라 잘못된 리다이렉트가 발생할 수 있음
  // null을 반환해서 아무것도 렌더링하지 않으면 깜빡임 없이 자연스럽게 처리됨
  if (!_hasHydrated) return null;

  // 하이드레이션 완료 후 비로그인 상태 → /login으로 이동
  // <Navigate>: render 시점에 즉시 리다이렉트 (useNavigate + useEffect보다 깔끔)
  // replace: 히스토리에 현재 경로를 남기지 않아 뒤로가기로 돌아오는 것을 방지
  if (!isAuthenticated) return <Navigate to="/login" replace />;

  // 로그인 상태 → 자식 페이지 정상 렌더링
  return <Outlet />;
};

export default AuthLayout;
