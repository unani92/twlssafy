import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  /*변수명: 리스트 앞에 단수형 사용
  like요청 시에만 백엔드에 요청 보낼 때 복수형 사용 --> article/{article_id}/likes/{email}
  */
  state: {
    username: "",
    nickname: "",
    followList: [],
    likeList: [],
    pinList: [],
    interestList: [],
  },
  getters: {
    isLogin(state) {
      return state.username !== "";
    },
  },
  mutations: {
    setUsername(state, username) {
      state.username = username;
    },
    setNickname(state, nickname) {
      state.nickname = nickname;
    },
    setFollowList(state, followList) {
      state.followList = followList;
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
    clearUsername(state) {
      state.username = "";
      state.nickname = "";
    },
  },
  actions: {},
  modules: {},
});
