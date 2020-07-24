<template>
  <div class="sidemenu">
    <div class="icon">
      <i class="far fa-user"></i>
    </div>
    <div class="icon">
      <i class="far fa-heart"></i>
    </div>
    <div class="icon">
      <i class="far fa-bookmark"></i>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ArticleDetailSideMenu"
  }
</script>

<style scoped>
  .sidemenu {
    border-radius: 5px;
    background-color: white;
    padding: 10px;
    position: fixed;
  }
  .icon {
    border-radius: 75px;
    border: 1px solid black;
    -moz-border-radius:75px;
    -webkit-border-radius:75px;
    width: 70px;
    height: 70px;
    text-align: center;
    font-size: 2rem;
    padding: 5px;
    margin-bottom: 5px;
    cursor: pointer;
  }
</style>