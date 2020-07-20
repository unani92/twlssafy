<template>
  <div class="github">
    <i class="fab fa-github"></i>
    <span>Github 로그인</span>
  </div>
</template>

<script>
  export default {
    name: "GithubLogin"
  }
</script>

<style scoped>
  .github {
    border-radius: 3px;
    background-color: black;
    width: 250px;
    height: 50px;
    color: white;
    display: flex;
    justify-content: space-around;
    align-items: center;
    font-weight: bolder;
    margin: 1rem;
  }
  i {
    color: white;
    font-size: 2rem;
  }
</style>