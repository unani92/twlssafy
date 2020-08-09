import axios from 'axios';

const instance = axios.create({
  baseURL: process.env.VUE_APP_API_URL,
});

function checkEmail(email) {
  return instance.post('account/checkEmail', email);
}

function checkNickname(nickname) {
  return instance.post('account/checkNickname', nickname);
}

function registerUser(userData) {
  return instance.post('account/signup', userData);
}

function socialLogin(id_token) {
  return instance.post('/googlelogin',{ id_token },{headers: {id_token}})
}

function googleSignup(userData, token) {
  return instance.post('account/googleSignup', userData, {
    headers: { id_token: token },
  });
}

function getInfo(id_token) {
  return instance.post('account/googleInfo', { id_token }, { headers: { id_token: id_token } })
}

function login(params) {
  return instance.post('account/login', params);
}

function selectSkills(params, token) {
  return instance.post('account/interest/register', params, {
    headers: { id_token: token },
  });
}

function deleteSkill(params, token) {
  return instance.post('account/interest/delete', params, {
    headers: { id_token: token },
  });
}

function createArticle(params, token) {
  return instance.post('article', params, { headers: { id_token: token } });
}

function updateArticle(params, token) {
  return instance.put('article', params, { headers: { id_token: token } });
}

function deleteArticle(id, token) {
  return instance.delete(`article?no=${id}`, { headers: { id_token: token } });
}

function fetchArticles(params, token) {
  return instance.get(`article?page=${params.page}`,{ headers: { id_token: token } });
}

function fetchArticle(id, token) {
  return instance.get(`article/${id}`, { headers: { id_token: token } });
}

function likeArticle(params, token) {
  return instance.post(`article/${params.article_id}/likes`, null, {
    headers: { id_token: token },
  });
}

function pinArticle(params, token) {
  return instance.post(`article/${params.article_id}/pin`, null, {
    headers: { id_token: token },
  });
}

function requestFollow(params, token) {
  return instance.post('account/follow', params, {
    headers: { id_token: token },
  });
}

function createComment(params, token) {
  return instance.post('article/comment', params, {
    headers: { id_token: token },
  });
}

function deleteComment(params) {
  return instance.delete(`article/comment?no=${params}`);
}

function fetchMyArticles(params) {
  return instance.get(`account/${params.nickname}?page=${params.page}`);
}

function searchArticle(params) {
  return instance.get(
    `article/search?q=${params.q}&category=${params.category}&page=${params.page}`
  );
}

function fetchFollowArticles(params, token) {
  return instance.get(`article/following?page=${params.page}`, {
    headers: { id_token: token },
  });
}

function fetchPinArticles(params, token) {
  return instance.get(`article/pin?page=${params.page}`, {
    headers: { id_token: token },
  });
}

function fetchHotArticles(params, token) {
  console.log(params);
  return instance.get(`article/hot?page=${params.page}`, {
    headers: { id_token: token },
  });
}

function fetchHashTags() {
  return instance.get(`hashTag`);
}

function fetchRecommend(token) {
  return instance.get('article/recommend', {
    headers: { id_token: token }
  })
}

export {
  checkEmail,
  checkNickname,
  registerUser,
  socialLogin,
  googleSignup,
  getInfo,
  login,
  selectSkills,
  deleteSkill,
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
  searchArticle,
  fetchFollowArticles,
  fetchPinArticles,
  fetchHotArticles,
  fetchHashTags,
  fetchRecommend
};
