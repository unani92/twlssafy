import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

import { library as faLibrary } from '@fortawesome/fontawesome-svg-core' 
import { faHome, faSearch, faPlus } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome' 
// Set up FontAwesome
faLibrary.add(faHome, faSearch, faPlus)
Vue.component('font-awesome-icon', FontAwesomeIcon)
