<template>
  <div id="app">
    <NavBar/>
    <HomeNav id = "homeNav"/>
    <Router-view/>
  </div>
</template>
<script>
  import NavBar from "./components/NavBar";
  import HomeNav from "./components/HomeNav";
  export default {
    components: {
      NavBar,
      HomeNav
    },
    data() {
      return {
        scroll: {
          prev: 0,
          upDown: null
        }
      }
    },
    methods: {
      scrollEvent() {
        const navBar = document.querySelector(".nav-bar")
        const homeNav = document.querySelector(".home-nav")
        if(window.innerWidth<500) return;
        const nowScrollY = window.scrollY
        if (nowScrollY > this.scroll.prev) {
          navBar.classList.add("disabled")
          homeNav.classList.add("disabled")
          this.scroll.prev = nowScrollY
        } else {
          navBar.classList.remove("disabled")
          homeNav.classList.remove("disabled")
          this.scroll.prev = nowScrollY
        }
      },
    },
    mounted() {
      document.addEventListener("scroll", this.scrollEvent)
    }
  }
</script>
<!-- editor custom css -->
<style src="./assets/css/editor-custom.css"></style>
<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }
  body {
    background-color: rgb(249, 249, 249);
    margin: 0 auto;
  }
  #homeNav{
    position:fixed;
    top : 60px;
    height : 60px;
  }
</style>
