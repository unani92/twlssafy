import Vue from "vue";
import Vuex from "vuex";
import cookies from "vue-cookies";
import { registerUser } from "../api";
import axios from "axios";

Vue.use(Vuex);

export default new Vuex.Store({
  /*변수명: 리스트 앞에 단수형 사용
  like요청 시에만 백엔드에 요청 보낼 때 복수형 사용 --> article/{article_id}/likes/{email}
  */
  state: {
    username: "",
    nickname: "",
    img: "",
    id_token: cookies.get("id_token"),
    followList: [],
    likeList: [],
    pinList: [],
    interestList: [],
    notification: [],
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
      cookies.set("id_token", token);
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
    seetFollowListByEmail(state,follow) {
      state.followList.follow = follow
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
    clearUsername(state) {
      state.username = "";
      state.nickname = "";
    },
  },
  actions: {
    getGoogleUserInfo({ commit }, id_token) {
      axios
        .post(
          "http://localhost:8080/account/googleInfo",
          { id_token },
          { headers: { id_token: id_token } }
        )
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
          } = res.data.object;
          commit("setUsername", email);
          commit("setNickname", nickname);
          commit("setImg", img);
          commit("setFollowList", followList);
          commit("setToken", id_token);
          commit("setLikeList", likesList);
          commit("setNotificationlist", notification);
          commit("setPinList", pinList);
          commit("setInterestList", interestList);
        })
        .catch((err) => console.log(err));
    },
    socialSignUp({ commit }, signUpData) {
      registerUser(signUpData)
        .then((res) => {
          // 이메일, 닉네임, 토큰 받아서 커밋으로 집어넣는다.
          commit("setToken", res.data.key);
          this.$router.push({ name: "SelectSkills" });
        })
        .catch((err) => console.log(err));
    },
  },
  modules: {},
});
