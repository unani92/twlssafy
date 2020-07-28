import Vue from "vue";
import Vuex from "vuex";
import cookies from 'vue-cookies'
import { registerUser, login } from "../api";
Vue.use(Vuex);

export default new Vuex.Store({
  /*변수명: 리스트 앞에 단수형 사용
  like요청 시에만 백엔드에 요청 보낼 때 복수형 사용 --> article/{article_id}/likes/{email}
  */
  state: {
    username: "",
    nickname: "",
    authToken: cookies.get('auth-token'),
    followList: [],
    likeList: [],
    pinList: [],
  },
  getters: {
    isLoggedIn: state => !!state.authToken,
    config: state => ({
      headers: {Authorization: `Token ${state.authToken}`}
    })
  },
  mutations: {
    setToken(state,token) {
      state.authToken = token
      cookies.set('auth-token',token)
    },
    setUsername(state, username) {
      state.username = username;
    },
    setNickname(state, nickname) {
      state.nickname = nickname;
    },
    setFollowList(state, followList) {
      state.followList = followList;
    },
    setLikeList(state, likeList) {
      state.likeList = likeList;
    },
    setPinList(state, pinList) {
      state.pinList = pinList;
    },
    clearUsername(state) {
      state.username = "";
      state.nickname = "";
    },
  },
  actions: {
    socialLogin({ commit }, loginData) {
      login(loginData)
        .then(res => {
          // 이메일, 닉네임, 좋아요, 핀, 팔로우 리스트, 토큰 받아서 커밋으로 집어넣는다.
          commit("setToken",res.data.key)
          this.$router.push('/')
        })
        .catch(err => console.log(err))
    },
    socialSignUp({ commit }, signUpData) {
      registerUser(signUpData)
        .then(res => {
          // 이메일, 닉네임, 토큰 받아서 커밋으로 집어넣는다.
          commit("setToken", res.data.key)
          this.$router.push({ name: "SelectSkills" })
        })
        .catch(err => console.log(err))
    }
  },
  modules: {},
});
