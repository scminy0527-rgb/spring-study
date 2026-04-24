import { loginPost } from "./http";

const AUTH_BASE = "/auth";

// 로그인: POST /api/auth/login → 성공 시 서버가 쿠키에 JWT 발급
export const loginAuth = (data) => loginPost(`${AUTH_BASE}/login`, data);
