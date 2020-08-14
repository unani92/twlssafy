<template>
  <div>
    <div class="nav-bar">
      <div class="logo-searchbar">
        <div class="logo">
          <div
            :style="{
              backgroundImage: 'url(' + require('../assets/logo.gif') + ')',
            }"
            class="logo-gif"
            @click="gotoMain"
          ></div>
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
        <div class="icon" style="display: flex">
          <div style="display: flex">
            <div v-if="$store.getters.isLoggedIn" class="circle">{{ $store.state.notificationCnt }}</div>
            <i
              v-if="$store.getters.isLoggedIn"
              @click="notificationIconToggle"
              class="far fa-bell"
            />
          </div>
          <i v-if="!this.$store.getters.isLoggedIn" @click="asideBarToggle" class="fas fa-bars"></i>
        </div>
        <div v-if="this.$store.getters.isLoggedIn">
          <div
            style="width : 35px;"
            :style="{
              backgroundImage:
                'url(' +
                require('@/assets/image/medal-' + calcGrade + '.png') +
                ')',
            }"
            class="grade"
          ></div>
        </div>

        <div v-if="this.$store.getters.isLoggedIn" class="mypage">
          <figure
            v-if="$store.state.img"
            class="user-photo"
            :style="{
              'background-image': 'url(' + this.$store.state.img + ')',
            }"
            @click="userToggle"
          ></figure>
          <figure
            v-else
            class="user-photo"
            :style="{
              'background-image':
                'url(' +
                `https://api.adorable.io/avatars/100/${this.$store.state.username}.png` +
                ')',
            }"
            @click="userToggle"
          ></figure>
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
    <!-- 로그인 안한 경우 사이드 바 -->
    <div v-if="!this.$store.getters.isLoggedIn" class="aside disabled">
      <div class="aside-menu">
        <GoogleLogin />
        <div @click="goToEmailLogin" class="emailogin-text">Email Login</div>
        <div @click="goToSignup" class="signup-text">Signup</div>
      </div>
    </div>
    <div class="userMenu disabled">
      <div class="userMenu-menu">
        <div @click="goToMyPage" class="mypage-text">MyPage</div>
        <div v-if="!isGoogleUser" @click="goToPwChange" class="mypage-text">비밀번호 변경</div>
        <div @click="logout" class="mypage-text">Log Out</div>
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
      grade: getGrade(this.$store.state.score),
      scroll: {
        prev: 0,
        upDown: null,
      },
    };
  },
  computed: {
    calcGrade() {
      return getGrade(this.$store.state.score);
    },
    isGoogleUser() {
      return this.$store.state.isGoogleUser;
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
    userToggle() {
      const userMenu = document.querySelector(".userMenu");
      userMenu.classList.toggle("disabled");
    },
    gotoMain() {
      console.log(document.location.href)
      if(document.location.href==="http://localhost:4000/latest") {
        window.scrollTo(0,0);
      }
      else this.$router.push("/latest");
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
    goToPwChange() {
      this.$router.push("/ChangePwdLogin");
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
  height: 60px;
  font-family: "Rowdies", cursive;
  font-size: 28px;
  text-align: center;
  border-radius: 3px;
  margin-left: 0;
  text-decoration: none;
}
.logo:hover {
  cursor: pointer;
}
.logo-gif {
  height: 100%;
  background-size: 80px 60px;
  background-position: center;
  background-repeat: no-repeat;
  background-color: white;
}
.input-box {
  margin-left: 3rem;
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
  height: 60px;
  flex-shrink: 0;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  /* margin-right: 0.5em; */
}
.notification {
  position: fixed;
  border-radius: 10px;
  background-color: #d5dbd9;
  top: 50px;
  margin-right: 1rem;
  right: 7px;
  overflow: auto;
  height: 268px;
  z-index: 10;
}
::-webkit-scrollbar {
  width: 5px;
}
i {
  margin-left: 1rem;
  margin-right: 1rem;
  font-size: 2rem;
  cursor: pointer;
}
.circle {
  border-radius: 75%;
  margin-right: -20px;
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

.aside {
  position: fixed;
  top: 55px;
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
.aside-menu > div,
.userMenu-menu > div {
  margin: 10px;
}
.aside-menu > div:nth-child(1) {
  width: 250px;
  height: 50px;
  border-radius: 3px;
  background-color: rgb(143, 182, 204);
  color: white;
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
  border: 1.5px solid #333;
  border-radius: 50%;
  background-color: rgb(144, 153, 240);
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  margin-right: 0.5em;
}
.mypage-text {
  cursor: pointer;
  font-weight: bolder;
}
.userMenu {
  position: fixed;
  top: 55px;
  right: 0rem;
  z-index: 10;
}
.userMenu-menu {
  width: 200px;
  border: 1px solid black;
  background-color: white;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-right: 1rem;
}
.userMenu-menu > div {
  height: 40px;
  background-color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-right: 1rem;
}
.userMenu-menu > div:nth-child(1) {
  border-radius: 3px;
  color: rgb(143, 182, 204);
  display: flex;
  align-items: center;
  justify-content: center;
}
.userMenu-menu > div:nth-child(2) {
  border-radius: 3px;
  color: rgb(96, 196, 121);
  display: flex;
  align-items: center;
  justify-content: center;
}
.userMenu-menu > div:nth-child(3) {
  border-radius: 3px;
  color: rgb(204, 93, 65);
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 600px) {
  .input-box {
    margin-left: 0;
   }
}
@media (max-width: 414px) {
  .search-bar {
    width: 150px;
  }
  .article,
  .grade {
    display: none;
  }
   .input-box {
    margin-left: 0;
   }
}
@media (max-width: 376px) {
  .search-bar {
    width: 155px;
  }
  i {
    font-size: 20px;
  }
  .input-box {
  margin-left: 0;
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
  .input-box {
  margin-left: 0;
}
}
</style>
