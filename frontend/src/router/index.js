import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Login from "../views/Login";
import Signup from "../views/Signup";
import ChangePwd from "../views/ChangePwd";
import SelectSkills from "../views/SelectSkills";
import ArticleCreate from "../views/ArticleCreate";
import ArticleDetail from "../views/ArticleDetail";
import ArticleUpdate from "../views/ArticleUpdate";
import Profile from "@/views/Profile";
import ArticleSearch from "../views/ArticleSearch";
import SocialSignup from "../views/SocialSignup";
import Logout from "../views/Logout";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/signup",
    name: "Signup",
    component: Signup,
  },
  {
    path: "/changepwd",
    name: "ChangePwd",
    component: ChangePwd,
  },
  {
    path: "/selectskills",
    name: "SelectSkills",
    component: SelectSkills,
  },
  {
    path: "/create",
    name: "ArticleCreate",
    component: ArticleCreate,
  },
  {
    path: "/:id",
    name: "ArticleDetail",
    component: ArticleDetail,
  },
  {
    path: "/:id/update",
    name: "ArticleUpdate",
    component: ArticleUpdate,
    props: true,
  },
  {
    path: "/account/:nickname",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/article/search",
    name: "ArticleSearch",
    component: ArticleSearch,
  },
  {
    path: "/social/signup",
    name: "SocialSignup",
    component: SocialSignup,
    props: true
  },
  {
    path: "/account/logout",
    name: "Logout",
    component: Logout
  }
];

const router = new VueRouter({
  mode: "history",
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
