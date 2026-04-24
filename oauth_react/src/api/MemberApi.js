import { get, secureGet, post, put, del, loginPost } from "./http";
const MEMBER_BASE = "/members";

export const fetchMemberList = () => get(MEMBER_BASE);
export const fetchMember = (id) => get(`${MEMBER_BASE}/${id}`);
export const loginMember = (data) => loginPost(`${MEMBER_BASE}/login`, data);
// fetchMe: 로그인 후 쿠키(JWT)를 이용해 현재 로그인한 사용자 정보를 서버에서 가져옴
// 로그인 API가 유저 정보를 반환하지 않을 때 이 함수로 별도 조회
export const fetchMe = () => secureGet(`${MEMBER_BASE}/me`);
export const joinMember = (data) => post(`${MEMBER_BASE}/join`, data);
export const updateMember = (id, data) => put(`${MEMBER_BASE}/${id}`, data);
export const deleteMember = (id) => del(`${MEMBER_BASE}/${id}`);
