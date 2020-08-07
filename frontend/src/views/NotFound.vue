<template>
  <div class="notfound">
    <i class="far fa-frown"></i>
    <span class="notfound-text">404 Not Found</span>
    <div class="buttons">
      <Router-link to="/">
        <button>Go Home</button>
      </Router-link>
    </div>
  </div>
</template>

<script>
  export default {
    name: "NotFound",

  }
</script>

<style scoped>
  .notfound {
    text-align: center;
    display: flex;
    flex-direction: column;
    margin-top: 3rem;
    padding-top: 60px;
  }
  i {
    font-size: 250px;
    margin: 2rem;
  }
  .notfound-text {
    font-size: 2rem;
    font-weight: bolder;
  }
  .buttons {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 2rem;
  }
  button {
    background-color: #020024;
    border: none;
    width: 200px;
    height: 50px;
    color: rgb(255, 255, 255);
    padding: 10px 20px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    margin: 4px 2px;
    cursor: pointer;
    font-size: 30px;
    border-radius: 32px;
  }
  button:hover {
    background-color: #9affe1;
    transition: 1s;
  }
</style>