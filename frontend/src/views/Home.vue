<template>
  <div class="home">
    <div class="main">
      <div>
        <HomeNav/>
      </div>
      <div>
        <HashTag/>
      </div>
    </div>
    <div style="height: 10000px">

    </div>
  </div>
</template>

<script>
  import HomeNav from "../components/HomeNav";
  import HashTag from "../components/HashTag";
  export default {
    name: "Home",
    components: {
      HomeNav,
      HashTag
    }
  }
</script>

<style scoped>
  .home {
    padding-top: 60px;
  }
  .main {
    display: flex;
    justify-content: space-between;
  }
</style>