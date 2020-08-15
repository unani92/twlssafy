import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login';
import Signup from '../views/Signup';
import ChangePwd from '../views/ChangePwd';
import ChangePwdLogin from '../views/ChangePwdLogin';
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
import NotFound from '../views/NotFound';
import store from '@/store/index';
import PrivacyPolicy from '../views/policies/PrivacyPolicy.vue';
import TermsOfUse from '../views/policies/TermsOfUse.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/article',
    name: 'Home',
    component: Home,
    children: [
      {
        path: '/latest',
        name: 'ArticleCardList',
        component: ArticleCardList,
      },
      {
        path: '/follow',
        name: 'myfollowArticles',
        component: FollowArticleCardList,
        beforeEnter: requireLogin,
      },
      {
        path: '/pin',
        name: 'mypinArticles',
        component: PinArticleCardList,
        beforeEnter: requireLogin,
      },
    ],
  },
  {
    path: '/',
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
    path: '/findpwd',
    name: 'FindPwd',
    component: FindPwd,
  },
  {
    path: '/changepwd',
    name: 'ChangePwd',
    component: ChangePwd,
    props: true,
  },
  {
    path: '/changePwdLogin',
    name: 'ChangePwdLogin',
    component: ChangePwdLogin,
    beforeEnter: !isGoogleUser,
  },
  {
    path: '/selectskills',
    name: 'SelectSkills',
    component: SelectSkills,
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
    path: '/error/404/notfound',
    alias: '*',
    name: 'NotFound',
    component: NotFound,
  },
  {
    path: '/PrivacyPolicy',
    name: 'PrivacyPolicy',
    component: PrivacyPolicy,
  },
  {
    path: '/TermsOfUse',
    name: 'TermsOfUse',
    component: TermsOfUse,
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

function alreadyLogin(to, from, next) {
  store.getters['isLoggedIn'] ? next('/') : next();
}
function requireLogin(to, from, next) {
  store.getters['isLoggedIn'] ? next() : next('/login');
}
function isGoogleUser(to, from, next) {
  store.state.isGoogleUser ? next() : next('/login');
}

export default router;
