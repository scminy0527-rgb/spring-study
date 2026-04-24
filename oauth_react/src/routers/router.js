import { createBrowserRouter, Outlet } from "react-router-dom";
import Header from "../components/Header";
import MemberJoin from "../pages/member/MemberJoin";
import Login from "../pages/member/Login";
import MyPage from "../pages/member/MyPage";
import AuthLayout from "../layouts/AuthLayout";
import UnAuthLayout from "../layouts/UnAuthLayout";

// 모든 페이지 공통 레이아웃: 헤더 + 하위 페이지(Outlet)
function Layout() {
  return (
    <>
      <Header />
      <Outlet />
    </>
  );
}

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      // UnAuthLayout 하위: 비로그인 상태에서만 접근 가능
      // 로그인된 상태에서 접근하면 UnAuthLayout이 /mypage로 리다이렉트
      {
        element: <UnAuthLayout />,
        children: [
          { path: "", element: <MemberJoin /> },
          { path: "/login", element: <Login /> },
        ],
      },

      // AuthLayout 하위: 로그인 상태에서만 접근 가능
      // 비로그인 상태에서 접근하면 AuthLayout이 /login으로 리다이렉트
      {
        element: <AuthLayout />,
        children: [
          { path: "/mypage", element: <MyPage /> },
        ],
      },
    ],
  },
]);

export default router;
