import axios from "axios";

const instance = axios.create({
  baseURL: process.env.VUE_APP_API_URL,
});
function checkEmail(email) {
  return instance.post("account/checkEmail", email);
}
function checkNickname(nickname) {
  return instance.post("account/checkNickname", nickname);
}

function registerUser(userData) {
  return instance.post("account/signup", userData);
}
function socialSignup(userData,token) {
  return instance.post("account/googleSignup", userData,{headers:{id_token: token}})
}

function login(params) {
  return instance.post("account/login", params);
}

function selectSkill(params) {
  return instance.post("account/interest/register", params);
}

function createArticle(params) {
  return instance.post("article", params);
}

function updateArticle(params) {
  return instance.put("article", params);
}

function deleteArticle(id) {
  return instance.delete(`article?no=${id}`);
}

function fetchArticles(params) {
  return instance.get(`article?page=${params.page}`);
}

function fetchArticle(id) {
  return instance.get(`article/${id}`);
}

function likeArticle(params) {
  return instance.post(`article/${params.article_id}/likes/${params.email}`);
}

function pinArticle(params) {
  return instance.post(`article/${params.article_id}/pin/${params.email}`);
}

function requestFollow(params) {
  return instance.post("account/follow", params);
}

function createComment(params) {
  return instance.post("article/comment", params);
}

function deleteComment(params) {
  return instance.delete(`article/comment?no=${params}`);
}
function fetchMyArticles(params) {
  return instance.get(`account/${params.nickname}?page=${params.page}`);
}
function searchArticle(params) {
  return instance.get(`article/search?q=${params.q}&category=${params.category}&page=${params.page}`)
}

export {
  checkEmail,
  checkNickname,
  registerUser,
  socialSignup,
  login,
  selectSkill,
  createArticle,
  updateArticle,
  deleteArticle,
  fetchArticles,
  fetchArticle,
  likeArticle,
  pinArticle,
  requestFollow,
  createComment,
  deleteComment,
  fetchMyArticles,
  searchArticle
};
