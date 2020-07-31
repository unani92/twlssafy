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
    },

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
    background-color: rgb(249, 249, 249);
    margin: 0 auto;
  }
  /* markdown viewer custom css */
  blockquote {
    border-left-width: 4px !important;
    border-left-style: solid !important;
    border-left-color: #e6837a !important;
    background-color: #d5dbd9;
  }
  blockquote > p {
    color: #0d0925 !important;
    padding: 10px;
  }
  .tui-editor-contents {
    font-size: 1rem !important;
    font-family: 'Nanum Gothic Coding', monospace !important;
  }
  .tui-editor-contents > p {
    font-size: 1rem !important;
  }
  .tui-editor-contents > h1 {
    font-size: 48px !important;
  }
  .tui-editor-contents > h2 {
    font-size: 40px !important;
  }
  .tui-editor-contents > h3 {
    font-size: 32px !important;
  }
  .tui-editor-contents > h4 {
    font-size: 28px !important;
  }
  .tui-editor-contents > h5 {
    font-size: 16px !important;
  }
  blockquote {
    font-size: 1rem;
  }
  pre > code > span {
    font-size: 1rem !important;
  }
</style>
