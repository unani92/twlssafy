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

function login(params) {
  return instance.post("account/login", params);
}

function createArticle(params) {
  return instance.post("article", params);
}
function fetchArticles(params) {
  return instance.get(`article?page=${params.page}`);
}
function fetchArticle(id) {
  return instance.get(`article/${id}`)
}

export {
  checkEmail,
  checkNickname,
  registerUser,
  login,
  createArticle,
  fetchArticles,
  fetchArticle
};
