import { get, post, put, del } from "./http";
const POST_BASE_URL = "/posts";

export const fetchPostList = () => get(POST_BASE_URL);
export const fetchPost = (id) => get(`${POST_BASE_URL}/${id}`);
export const createPost = (data) => post(POST_BASE_URL, data);
export const updatePost = (id, data) => put(`${POST_BASE_URL}/${id}`, data);
export const deletePost = (id) => del(`${POST_BASE_URL}/${id}`);
