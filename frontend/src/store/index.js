import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    username: "",
    nickname: "",
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
    clearUsername(state) {
      state.username = "";
      state.nickname = "";
    },
  },
  actions: {},
  modules: {},
});
