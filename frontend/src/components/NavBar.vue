<template>
  <div>
    <div class="nav-bar">
      <div class="logo-searchbar">
        <div class="logo">
          <Router-link class="logo-text" to="/">
            TWL
          </Router-link>
        </div>
        <input class="search-bar" type="text" />
      </div>
      <div class="article-icon">
        <div class="article">
          <button @click="$router.push('/create')" class="btn">write a post</button>
        </div>
        <div class="icon">
          <i class="far fa-bell" />
          <i @click="asideBarToggle" class="fas fa-bars"></i>
        </div>
      </div>
    </div>
    <div class="aside disabled">
      <div class="aside-menu">
        <GoogleLogin />
        <GithubLogin />
        <div @click="goToEmailLogin" class="emailogin-text">
          Email Login
        </div>
        <div @click="goToSignup" class="signup-text">
          Signup
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GoogleLogin from "./GoogleLogin";
import GithubLogin from "./GithubLogin";
export default {
  name: "NavBar",
  components: {
    GoogleLogin,
    GithubLogin,
  },
  methods: {
    asideBarToggle() {
      const aside = document.querySelector(".aside");
      aside.classList.toggle("disabled");
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
.logo-text {
  text-decoration: none;
  color: white;
}
.search-bar {
  margin-left: 1rem;
  width: 350px;
  height: 30px;
}
.article-icon {
  display: flex;
  justify-content: center;
  align-items: center;
}
.btn {
  background-color: rgb(144, 153, 240);
  border-radius: 3px;
  color: white;
  font-weight: bold;
  font-size: 1rem;
  border-style: none;
  outline: none;
  width: 110px;
  height: 35px;
  cursor: pointer;
}
.btn:hover {
  background-color: rgb(15, 35, 219);
  transition: 2s;
}
i {
  margin-left: 1rem;
  margin-right: 1rem;
  font-size: 2rem;
  cursor: pointer;
}
i:hover {
  border-radius: 50%;
  background-color: rgb(215, 218, 219);
  transition: 1s;
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
  padding-top: 70px;
  position: absolute;
  top: 0;
  right: 0;
  z-index: 3;
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
.aside-menu > div {
  margin: 10px;
}
.aside-menu > div:nth-child(3) {
  width: 250px;
  height: 50px;
  border-radius: 3px;
  background-color: rgb(204, 93, 65);
  display: flex;
  align-items: center;
  justify-content: center;
}
.aside-menu > div:nth-child(4) {
  width: 250px;
  height: 50px;
  border-radius: 3px;
  background-color: lightgray;
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
</style>
