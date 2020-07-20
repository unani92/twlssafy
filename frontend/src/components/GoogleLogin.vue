<template>
  <div class="google">
    <i class="fab fa-google"></i>
    <span>google 로그인</span>
  </div>
</template>

<script>
  export default {
    name: "GoogleLogin"
  }
</script>

<style scoped>
  .google {
    border-radius: 3px;
    background-color: deepskyblue;
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