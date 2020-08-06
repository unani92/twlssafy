import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login';
import Signup from '../views/Signup';
import ChangePwd from '../views/ChangePwd';
import SelectSkills from '../views/SelectSkills';
import ArticleCreate from '../views/ArticleCreate';
import ArticleDetail from '../views/ArticleDetail';
import ArticleUpdate from '../views/ArticleUpdate';
import Profile from '@/views/Profile';
import ArticleSearch from '../views/ArticleSearch';
import SocialSignup from '../views/SocialSignup';
import Logout from '../views/Logout';
import ArticleCardList from '@/components/article/ArticleCardList.vue';
import FollowArticleCardList from '@/components/article/FollowArticleCardList.vue';
import PinArticleCardList from '@/components/article/PinArticleCardList.vue';
import FindPwd from '../views/FindPwd.vue';
import Dummy from '../components/Dummy';
import ArticleRecommend from '../views/ArticleRecommend';
import CalendarCardList from '../components/calendar/CalendarCardList.vue';
import store from '@/store/index';
import NotFound from "../views/NotFound";

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    children: [
      {
        path: '',
        name: 'ArticleCardList',
        component: ArticleCardList,
      },
      {
        path: 'follow',
        name: 'myfollowArticles',
        component: FollowArticleCardList,
        beforeEnter: requireLogin,
      },
      {
        path: 'pin',
        name: 'mypinArticles',
        component: PinArticleCardList,
        beforeEnter: requireLogin,
      },
    ],
  },
  {
    path: '/recommend',
    name: 'ArticleRecommend',
    component: ArticleRecommend,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    beforeEnter: alreadyLogin,
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup,
    beforeEnter: alreadyLogin,
  },
  {
    path: '/changepwd',
    name: 'ChangePwd',
    component: ChangePwd,
    props: true,
    beforeEnter: requireLogin,
  },
  {
    path: '/findpwd',
    name: 'FindPwd',
    component: FindPwd,
    beforeEnter: requireLogin,
  },
  {
    path: '/selectskills',
    name: 'SelectSkills',
    component: SelectSkills,
    beforeEnter: requireLogin,
  },
  {
    path: '/create',
    name: 'ArticleCreate',
    component: ArticleCreate,
    beforeEnter: requireLogin,
  },
  {
    path: '/:id',
    name: 'ArticleDetail',
    component: ArticleDetail,
  },
  {
    path: '/:id/update',
    name: 'ArticleUpdate',
    component: ArticleUpdate,
    props: true,
    beforeEnter: requireLogin,
  },
  {
    path: '/account/:nickname',
    name: 'Profile',
    component: Profile,
  },
  {
    path: '/article/search',
    name: 'ArticleSearch',
    component: ArticleSearch,
    children: [
      {
        path: ':q/:category',
        name: 'ArticleSearchByStack',
        component: ArticleSearch,
      },
    ],
  },
  {
    path: '/social/signup',
    name: 'SocialSignup',
    component: SocialSignup,
    props: true,
  },
  {
    path: '/account/logout',
    name: 'Logout',
    component: Logout,
  },
  {
    path: '/dummy/dummy',
    name: 'Dummy',
    component: Dummy,
  },
  {
    path: '/mypage/date',
    name: 'CalendarCardList',
    component: CalendarCardList,
    props: true,
  },
  {
    path : '/error/404/notfound',
    alias: '*',
    name : 'NotFound',
    component: NotFound
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

// router.beforeEach((to, from, next) => {
//   const publicPages = ['Home', 'Login', 'Signup', 'ArticleDetail']
//   const authPages = ['Login', 'Signup']
//   const authRequired = !publicPages.includes(to.name)
//   const unauthRequired = authPages.includes(to.name)
//
// })

function alreadyLogin(to, from, next) {
  store.getters['isLoggedIn'] ? next('/') : next();
}
function requireLogin(to, from, next) {
  store.getters['isLoggedIn'] ? next() : next('/login');
}

export default router;
