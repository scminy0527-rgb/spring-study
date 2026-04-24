// 모든 API 요청의 기본 URL
const BASE = "http://localhost:10000/api";

// 공통 요청 함수: url, fetch 옵션, 인증 옵션을 받아서 요청 후 { message, data } 반환
// 응답이 실패(4xx, 5xx)면 서버 메시지로 에러를 throw → 호출부 catch에서 처리
const request = async (url, options = {}, credential = {}) => {
  const response = await fetch(`${BASE}${url}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
    ...credential,
  });

  // 응답 본문을 텍스트로 읽고, 내용이 있을 때만 JSON 파싱 (빈 응답 대비)
  const text = await response.text();
  const body = text ? JSON.parse(text) : null;

  // 응답 상태가 2xx가 아니면 서버 메시지를 담아 에러 throw
  if (!response.ok) throw new Error(body?.message);

  // 성공 시 메시지와 데이터만 추출해서 반환
  // body?.message: 응답 바디가 없는 경우(빈 body = null)에도 TypeError 없이 null 반환
  return {
    message: body?.message ?? null,
    data: body?.data ?? null,
  };
};

// 일반 GET 요청
export const get = (url) => request(url);

// 인증이 필요한 GET 요청: credentials: "include" → 브라우저에 저장된 쿠키(JWT)를 요청에 자동 포함
// 로그인 후 사용자 정보 조회 등 토큰이 있어야 접근 가능한 API에 사용
export const secureGet = (url) => request(url, {}, { credentials: "include" });

// 일반 POST 요청
export const post = (url, data) =>
  request(url, { method: "POST", body: JSON.stringify(data) });

// 로그인 전용 POST 요청: credentials: "include" → 서버가 응답에 담아주는 쿠키(JWT)를 브라우저가 저장
export const loginPost = (url, data) =>
  request(
    url,
    { method: "POST", body: JSON.stringify(data) },
    { credentials: "include" }
  );

// PUT 요청 (수정)
export const put = (url, data) =>
  request(url, { method: "PUT", body: JSON.stringify(data) });

// DELETE 요청 (삭제)
export const del = (url) => request(url, { method: "DELETE" });
