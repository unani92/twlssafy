<template>
  <div class="home-nav">
      <span>
        POST
      </span>
      <div class="post-nav">
        <span>
          Latest
        </span>
          <span>
          Follow
        </span>
          <span>
          Pinned
        </span>
      </div>
    </div>
</template>

<script>
  export default {
    name: "HomeNav"
  }
</script>

<style scoped>
  .home-nav {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 20px;
    font-weight: bold;
    position: fixed;
  }
  .home-nav > span {
    margin: 1rem;
  }
  .post-nav > span {
    margin: 1rem;
    cursor: pointer;
  }
  .post-nav > span:hover {
    color: blueviolet;
  }
</style>