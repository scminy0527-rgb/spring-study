import "./App.css";
import { RouterProvider } from "react-router-dom";
import router from "./routers/router";
import { useEffect } from "react";
import useAuthStore from "./store/useAuthStore";
import { fetchMe } from "./api/MemberApi";

function App() {
  const { setIsAuthenticated, setMember } = useAuthStore();

  // 앱이 처음 로드될 때 딱 한 번 실행 (의존성 배열 [] → mount 시 1회)
  // 목적: 쿠키에 토큰이 있는지 서버에 확인해서 Zustand 상태와 동기화
  // 이렇게 하지 않으면 쿠키를 삭제해도 localStorage에 남은 isAuthenticated: true 때문에
  // 로그인된 것처럼 계속 보이는 문제가 생김
  useEffect(() => {
    const syncAuth = async () => {
      try {
        // 쿠키의 토큰을 서버로 보내서 유효한지 확인 + 유저 정보 수신
        const { data } = await fetchMe();

        // 토큰이 유효하면 최신 유저 정보로 Zustand 갱신
        setMember(data);
        setIsAuthenticated(true);
      } catch {
        // 토큰이 없거나 만료된 경우 → Zustand 상태를 초기화
        // localStorage에 남아있는 로그인 정보를 지워서 비로그인 상태로 만듦
        setMember(null);
        setIsAuthenticated(false);
      }
    };

    syncAuth();
  // setMember, setIsAuthenticated는 Zustand setter라 참조가 바뀌지 않아 무한 루프 없음
  }, [setMember, setIsAuthenticated]);

  return <RouterProvider router={router} />;
}

export default App;
