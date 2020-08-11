<template>
  <div>
    <div class="nav-bar">
      <div class="logo-searchbar">
        <div class="logo">
          <Router-link class="logo-text" to="/">TWL</Router-link>
        </div>
        <div class="input-box">
          <i
            @click="$router.push({ name: 'ArticleSearch' })"
            style="font-size: x-large"
            class="fas fa-search"
          ></i>
        </div>
      </div>
      <div class="article-icon">
        <div v-if="this.$store.getters.isLoggedIn">
          <div
            style="width : 28px;"
            :style="{ backgroundImage:'url('+require('@/assets/image/medal-'+calcGrade+'.png')+')'}"
            class="grade"
          ></div>
        </div>
        <div v-if="this.$store.getters.isLoggedIn" class="mypage">
          <figure
            v-if="$store.state.img"
            class="user-photo"
            :style="{ 'background-image': 'url(' + this.$store.state.img + ')' }"
            @click="goToMyPage"
          ></figure>
          <figure
            v-else
            class="user-photo"
            :style="{ 'background-image': 'url(' + `https://api.adorable.io/avatars/100/${this.$store.state.username}.png` + ')' }"
            @click="goToMyPage"
          ></figure>
        </div>
        <div class="icon" style="display: flex">
          <div style="display: flex">
            <div v-if="$store.getters.isLoggedIn" class="circle">{{ $store.state.notificationCnt }}</div>
            <i v-if="$store.getters.isLoggedIn" @click="notificationIconToggle" class="far fa-bell" />
          </div>
          <i @click="asideBarToggle" class="fas fa-bars"></i>
        </div>
      </div>
    </div>
    <div class="notification disabled" v-if="$store.state.notification.length">
      <Notification
        v-for="noti in $store.state.notification"
        :noti="noti"
        :key="noti.notificationid"
      />
    </div>
    <div class="aside disabled">
      <div class="aside-menu" v-if="!this.$store.getters.isLoggedIn">
        <GoogleLogin />
        <div @click="goToEmailLogin" class="emailogin-text">Email Login</div>
        <div @click="goToSignup" class="signup-text">Signup</div>
      </div>
      <div v-else class="aside-menu-loggedIn">
        <div @click="goToMyPage" class="mypage-text">MyPage</div>
        <div @click="logout" class="emailogin-text">Log Out</div>
      </div>
    </div>
  </div>
</template>

<script>
import GoogleLogin from "./GoogleLogin";
import Notification from "./Notification";
import { getGrade } from "@/utils/calcGrade";
export default {
  data() {
    return {
      grade: getGrade(this.$store.state.articleCount),
      scroll: {
        prev: 0,
        upDown: null,
      },
    };
  },
  computed: {
    calcGrade() {
      return getGrade(this.$store.state.articleCount);
    },
  },
  name: "NavBar",
  components: {
    GoogleLogin,
    Notification,
  },
  methods: {
    asideBarToggle() {
      const aside = document.querySelector(".aside");
      aside.classList.toggle("disabled");
    },
    notificationIconToggle() {
      if (this.$store.state.notification.length) {
        const notiDropdown = document.querySelector(".notification");
        notiDropdown.classList.toggle("disabled");
      }
    },
    goToEmailLogin() {
      const aside = document.querySelector(".aside");
      this.$router.push({ name: "Login" });
      aside.classList.toggle("disabled");
    },
    goToSignup() {
      const aside = document.querySelector(".aside");
      this.$router.push({ name: "Signup" });
      aside.classList.toggle("disabled");
    },
    goToMyPage() {
      this.$router.push({
        name: "Dummy",
        params: { following: this.$store.state.nickname },
      });
    },
    logout() {
      this.$router.push({ name: "Logout" });
    },
    scrollEvent() {
      const navBar = document.querySelector(".notification");
      if (navBar) {
        const nowScrollY = window.scrollY;
        if (nowScrollY > this.scroll.prev) {
          navBar.classList.add("disabled");
          this.scroll.prev = nowScrollY;
        }
      }
    },
  },
  mounted() {
    document.addEventListener("scroll", this.scrollEvent);
  },
};
</script>

<style scoped>
.nav-bar {
  width: 100%;
  height: 60px;
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: fixed;
  z-index: 4;
}
.logo-searchbar {
  display: flex;
  align-items: center;
  justify-content: center;
}
.logo {
  background-color: black;
  width: 80px;
  color: white;
  font-family: "Rowdies", cursive;
  font-size: 28px;
  text-align: center;
  border-radius: 3px;
  margin-left: 1rem;
  text-decoration: none;
}
.input-box {
  margin-left: 2rem;
}
.logo-text {
  text-decoration: none;
  color: white;
}
.article-icon {
  display: flex;
  justify-content: center;
  align-items: center;
}
.grade {
  width: 40px;
  height: 40px;
  flex-shrink: 0;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  margin-right: 0.5em;
}
.notification {
  position: fixed;
  border-radius: 3px;
  background-color: #d5dbd9;
  top: 70px;
  right: 0;
  overflow: auto;
  height: 268px;
  z-index: 10;
}
i {
  margin-left: 1rem;
  margin-right: 1rem;
  font-size: 2rem;
  cursor: pointer;
}
.circle {
  position: absolute;
  top: 15px;
  right: 60px;
  border-radius: 75%;
  width: 20px;
  height: 20px;
  background-color: crimson;
  color: white;
  text-align: center;
  font-weight: bold;
}
i:hover {
  border-radius: 50%;
  background-color: rgb(215, 218, 219);
  transition: 1s;
}
.mypage:hover {
  cursor: pointer;
}
@media (max-width: 414px) {
  .search-bar {
    width: 150px;
  }
  .article {
    display: none;
  }
}
@media (max-width: 376px) {
  .search-bar {
    width: 155px;
  }
  i {
    font-size: 20px;
  }
}
@media (max-width: 320px) {
  .logo {
    width: 56px;
    font-size: 20px;
  }
  .search-bar {
    width: 130px;
    height: 22px;
  }
  .nav-bar {
    height: 40px;
  }
  i {
    font-size: 1rem;
  }
}
.aside {
  position: fixed;
  top: 70px;
  right: 0;
  z-index: 10;
}
.aside-menu {
  width: 300px;
  border: 1px solid black;
  background-color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-right: 1rem;
}
.aside-menu-loggedIn {
  width: 300px;
  border: 1px solid black;
  background-color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-right: 1rem;
}
.aside-menu > div,
.aside-menu-loggedIn > div {
  margin: 10px;
}
.aside-menu-loggedIn > div:nth-child(1) {
  width: 250px;
  height: 50px;
  border-radius: 3px;
  background-color: rgb(143, 182, 204);
  display: flex;
  align-items: center;
  justify-content: center;
}
.aside-menu > div:nth-child(2) {
  width: 250px;
  height: 50px;
  border-radius: 3px;
  background-color: rgb(204, 93, 65);
  display: flex;
  align-items: center;
  justify-content: center;
}
.aside-menu-loggedIn > div:nth-child(2) {
  width: 250px;
  height: 50px;
  border-radius: 3px;
  background-color: rgb(204, 93, 65);
  display: flex;
  align-items: center;
  justify-content: center;
}
.aside-menu > div:nth-child(3) {
  width: 250px;
  height: 50px;
  border-radius: 3px;
  background-color: rgb(143, 182, 204);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
}
.emailogin-text {
  cursor: pointer;
  color: white;
  font-weight: bolder;
}
.signup-text {
  cursor: pointer;
  color: black;
  font-weight: bolder;
}
.disabled {
  display: none;
}
.user-photo {
  width: 40px;
  height: 40px;
  flex-shrink: 0;
  border: 2px solid #333;
  border-radius: 50%;
  background-color: rgb(144, 153, 240);
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  margin-right: 0.5em;
}
.mypage-text {
  cursor: pointer;
  color: white;
  font-weight: bolder;
}
</style>
