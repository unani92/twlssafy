<template>
  <div id="app">
    <NavBar/>
    <Router-view/>
  </div>
</template>
<script>
  import NavBar from "./components/NavBar";
  export default {
    components: {
      NavBar,
    }
  }
</script>
<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }
  body {
    background-color: rgb(237, 239, 242);
    margin: 0 auto;
  }
  blockquote {
    border-left-width: 4px !important;
    border-left-style: solid !important;
    border-left-color: crimson !important;
    background-color: #d5dbd9;
  }
  blockquote > p {
    color: #0d0925 !important;
  }
</style>
