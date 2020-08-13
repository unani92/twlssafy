import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import VueCookies from 'vue-cookies'
import moment from "moment";
import VueMomentJS from "vue-momentjs";
import firebase from "firebase";

import vueHeadful from 'vue-headful';

moment.locale("ko")
Vue.use(VueMomentJS, moment);
Vue.use(VueCookies)
Vue.component('vue-headful', vueHeadful);

firebase.initializeApp({
  apiKey: process.env.VUE_APP_FIREBASE_API_KEY,
  authDomain: process.env.VUE_APP_AUTH_DOMAIN,
  databaseURL: process.env.VUE_APP_DATABASE_URL,
  projectId: process.env.VUE_APP_PROJECT_ID,
  storageBucket: process.env.VUE_APP_STORAGE_BUCKET,
  messagingSenderId: process.env.VUE_APP_MESSAGING_SENDER_ID,
  appId: process.env.VUE_APP_APP_ID
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
