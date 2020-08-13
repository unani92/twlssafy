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
<style>
  @import url('https://fonts.googleapis.com/css2?family=Nanum+Gothic&family=Nanum+Gothic+Coding&family=Noto+Sans+KR:wght@300&display=swap');
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
  /* .disabled {
    display: none;
  } */
  /* markdown viewer custom css */
  blockquote {
    border-left-width: 4px !important;
    border-left-style: solid !important;
    border-left-color: #e6837a !important;
    background-color: #eeeeee;
  }
  blockquote > p {
    color: #0d0925 !important;
    padding: 10px;
  }
  .tui-editor-contents p {
    font-size: 1rem !important;
    font-family: 'Nanum Gothic Coding', monospace !important;
    /*font-family: 'Nanum Gothic', sans-serif;*/
  }
  .tui-editor-contents > p {
    font-size: 1rem !important;
    word-break: break-all !important;
  }
  .tui-editor-contents pre {
    margin: 2px 0 8px;
    padding: 18px;
    background-color: #f5f7f8;
    font-size: 1rem;
  }
  .tui-editor-contents > h1 {
    font-size: 48px !important;
    padding: 2rem 0 2rem 0 !important;
    margin: 1rem 0 1rem 0 !important;
    font-family: 'Nanum Gothic', sans-serif;
  }
  .tui-editor-contents > h2 {
    font-size: 40px !important;
    padding: 2rem 0 2rem 0 !important;
    margin: 1rem 0 1rem 0 !important;
    font-family: 'Nanum Gothic', sans-serif;
  }
  .tui-editor-contents > h3 {
    font-size: 32px !important;
    padding: 1rem 0 1rem 0 !important;
    font-family: 'Nanum Gothic', sans-serif;
  }
  .tui-editor-contents > h4 {
    font-size: 28px !important;
    padding: 1rem 0 1rem 0 !important;
    font-family: 'Nanum Gothic', sans-serif;
  }
  .tui-editor-contents > h5 {
    font-size: 16px !important;
    padding: 1rem 0 1rem 0 !important;
    font-family: 'Nanum Gothic', sans-serif;
  }
  blockquote {
    font-size: 1rem;
  }
  pre > code > span {
    font-size: 1rem !important;
  }
  #homeNav{
    position:fixed;
    top : 60px;
    height : 60px;
  }
</style>
