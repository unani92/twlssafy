<template>
  <div class="home-nav">
    <div class="post-nav">
      <span>
        <span v-if="($store.state.prev=='/')" class="bar-text now" v-on:click="goToHome()">홈</span>
        <span v-else class="bar-text" v-on:click="goToHome()">홈</span>
      </span>
      <span>
        <span v-if="($store.state.prev=='/latest')" class="bar-text now" v-on:click="goToLatest()">최신</span>
        <span v-else class="bar-text" v-on:click="goToLatest()">최신</span>
      </span>
      <span>
        <span v-if="($store.state.prev=='/follow')" class="bar-text now" v-on:click="goToFollow()">팔로우</span>
        <span v-else class="bar-text" v-on:click="goToFollow()">팔로우</span>
      </span>
      <span>
        <span v-if="($store.state.prev=='/pin')" class="bar-text now" v-on:click="goToPin()">북마크</span>
        <span v-else class="bar-text" v-on:click="goToPin()">북마크</span>
      </span>
    </div>
    <div v-if="$store.getters.isLoggedIn">
      <span>
        <button @click="$router.push('/create')" class="writeBtn">
          <i class="fas fa-pen"></i>
        </button>
      </span>
      <span>
        <!-- <button class="writeBtn">
          <i @click="interestToggle" class="fas fa-fire-alt"></i>
        </button> -->
      </span>
    </div>
    <div class="interest disabled">
      <div class="interest-menu">
        <li v-for="h in hashTag" :key="h">#{{h}}</li>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { fetchHashTags } from "@/api/index";
export default {
  name: "HomeNav",
  computed: {
    ...mapGetters(["isLoggedIn"]),
  },
  data() {
    return {
      hashTag: [],
      scroll: {
        prev: 0,
        upDown: null
      },
      path: null,
      prevTab: this.$store.state.prev,
    };
  },
  methods: {
    interestToggle() {
      const interest = document.querySelector(".interest");
      interest.classList.toggle("disabled");
    },
    async fetchData() {
      const { data } = await fetchHashTags();
      this.hashTag = data.object;
      
    },
    scrollEvent() {
      const homeBar = document.querySelector(".home-nav")
      if(window.innerWidth < 500) {
        return;
      }
      if (homeBar) {
        const nowScrollY = window.scrollY
        if(window.innerWidth > 400) {
          if (nowScrollY > this.scroll.prev) {
              homeBar.classList.add("disabled")
              this.scroll.prev = nowScrollY
            } else {
              homeBar.classList.remove("disabled")
              this.scroll.prev = nowScrollY
            }
          } else {
            homeBar.classList.remove("disabled")
            this.scroll.prev = nowScrollY
          }
        }
      },
      goToHome() {
        this.$store.commit('setPrev','/');
        this.$router.push('/')
      },
      goToLatest() {
        this.$store.commit('setPrev','/latest');
        this.$router.push('/latest')
      },
      goToFollow() {
        this.$store.commit('setPrev','/follow');
        this.$router.push('/follow')
      },
      goToPin() {
        this.$store.commit('setPrev','/pin');
        this.$router.push('/pin')
      }
  },
  mounted() {
    this.path = this.$router.currentRoute.path;
    if(window.innerWidth>400){
      document.addEventListener("scroll", this.scrollEvent)
    } 
  },
  created() {
    this.fetchData();
  },
};
</script>

<style scoped>
.home-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 20px;
  font-weight: bold;
  position: fixed;
  background-color: rgb(244, 248, 255);
  width: 100%;
  z-index: 7;
}
.home-nav > span {
  margin: 1rem;
}
.post-nav > span {
  margin: 1rem;
  cursor: pointer;
}
.bar-text{
  color: lightslategrey;
}
.post-nav > span:hover {
  color: blueviolet;
}
.post-nav > span > a {
  text-decoration: none;
}
.post-nav > span > a:hover {
  color: blueviolet;
}
.router-link-active, .now {
  color: blueviolet;
  font-size: 23px;
}
.interest {
  position: absolute;
  top: 70px;
  right: 0;
  z-index: 3;
}
.interest-menu {
  width: 300px;
  border: 1px solid black;
  background-color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-right: 1rem;
  z-index: 2;
}
.interest-menu > li {
  margin: 10px;
  list-style-type: none;
}
.disabled {
  display: none !important;
}
.writeBtn {
  background-color: rgb(144, 153, 240);
  border-radius: 3px;
  color: white;
  font-weight: bold;
  font-size: 1rem;
  border-style: none;
  outline: none;
  width: 35px;
  height: 35px;
  cursor: pointer;
  margin-right: 10px;
}
@media (max-width: 500px){
  span {
    margin: 0;
    font-size: 13px;
  }
  .router-link-active {
    font-size: 15px;
  }
  .writeBtn {
  font-size : 14px;
  width: 24px;
  height: 24px;

  }
}
</style>
