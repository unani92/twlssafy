import Vue from 'vue';
import Vuex from 'vuex';
import cookies from 'vue-cookies';
import { getInfo, registerUser } from '../api';
// import axios from 'axios';
// import http from '../api/http-common.js'

Vue.use(Vuex);

export default new Vuex.Store({
  /*변수명: 리스트 앞에 단수형 사용
  like요청 시에만 백엔드에 요청 보낼 때 복수형 사용 --> article/{article_id}/likes/{email}
  */
  state: {
    username: '',
    nickname: '',
    img: '',
    id_token: cookies.get('id_token'),
    followList: { follow: [], followNickname: [] },
    likeList: [],
    pinList: [],
    interestList: [],
    notification: [],
    userSkills: [],
    notificationCnt: '',
    articleCount: 0,
    score: 0,
    isGoogleUser: false,
  },
  getters: {
    isLoggedIn: (state) => !!state.id_token,
    config: (state) => ({
      headers: { id_token: state.id_token },
    }),
    // notificationCnt: state => state.notification.length
  },
  mutations: {
    setToken(state, token) {
      state.id_token = token;
      cookies.set('id_token', token);
    },
    setUsername(state, username) {
      state.username = username;
    },
    setNickname(state, nickname) {
      state.nickname = nickname;
    },
    setImg(state, img) {
      state.img = img;
    },
    setFollowList(state, followList) {
      state.followList = followList;
    },
    setFollowListByEmail(state, follow) {
      state.followList.follow = follow;
    },
    setFollowListByNickname(state, followList) {
      state.followList.followNickname = followList;
    },
    setLikeList(state, likeList) {
      state.likeList = likeList;
    },
    setPinList(state, pinList) {
      state.pinList = pinList;
    },
    setInterestList(state, interestList) {
      state.interestList = interestList;
    },
    setNotificationlist(state, notification) {
      state.notification = notification;
    },
    setUserSkills(state, userSkills) {
      state.userSkills = userSkills;
    },
    setArticleCount(state, articleCount) {
      state.articleCount = articleCount;
    },
    setScore(state, score) {
      state.score = score;
    },
    setIsGoogleUser(state, flag) {
      state.isGoogleUser = flag;
    },
    clearUser(state) {
      state.username = '';
      state.nickname = '';
      state.id_token = '';
      state.img = '';
      state.followList = { follow: [], followNickname: [] };
      state.likeList = [];
      state.pinList = [];
      state.interestList = [];
      state.notification = [];
      state.articleCount = 0;
      state.score = 0;
      state.isGoogleUser = false;
    },
    setNotificationCnt(state, notificationCnt) {
      state.notificationCnt = notificationCnt;
    },
  },
  actions: {
    getGoogleUserInfo({ commit }, id_token) {
      getInfo(id_token)
        .then((res) => {
          const {
            email,
            followList,
            img,
            nickname,
            interestList,
            id_token,
            likesList,
            notification,
            pinList,
            notificationCnt,
            articleCount,
            score,
            isGoogle,
          } = res.data.object;
          commit('setUsername', email);
          commit('setNickname', nickname);
          commit('setImg', img);
          commit('setFollowList', followList);
          commit('setToken', id_token);
          commit('setLikeList', likesList);
          commit('setNotificationlist', notification);
          commit('setPinList', pinList);
          commit('setInterestList', interestList);
          commit('setNotificationCnt', notificationCnt);
          commit('setArticleCount', articleCount);
          commit('setScore', score);
          commit('setIsGoogleUser', !!isGoogle);
        })
        .catch((err) => console.log(err));
    },
    socialSignUp({ commit }, signUpData) {
      registerUser(signUpData)
        .then((res) => {
          // 이메일, 닉네임, 토큰 받아서 커밋으로 집어넣는다.
          commit('setToken', res.data.key);
          this.$router.push({ name: 'SelectSkills' });
        })
        .catch((err) => console.log(err));
    },
  },
  modules: {},
});
