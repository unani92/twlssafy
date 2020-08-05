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
import HotArticleCardList from '@/components/article/HotArticleCardList.vue';
import FindPwd from '../views/FindPwd.vue';
import Dummy from "../components/Dummy";
import ArticleRecommend from "../views/ArticleRecommend";
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
      },
      {
        path: 'pin',
        name: 'mypinArticles',
        component: PinArticleCardList,
      },
      {
        path: 'hot',
        name: 'myhotArticles',
        component: HotArticleCardList,
      },
    ],
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup,
  },
  {
    path: '/changepwd',
    name: 'ChangePwd',
    component: ChangePwd,
    props: true
  },
  {
    path: '/findpwd',
    name: 'FindPwd',
    component: FindPwd,
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
  },
  {
    path: '/recommend',
    name: "ArticleRecommend",
    component: ArticleRecommend
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
    name: "Dummy",
    component: Dummy
  }
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

export default router;
