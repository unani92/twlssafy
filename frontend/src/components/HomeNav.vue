<template>
  <div class="home-nav">
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
    <div v-if="$store.getters.isLoggedIn">
      <span>
        <button @click="$router.push('/create')" class="writeBtn">
          <i class="fas fa-pen"></i>
        </button>
      </span>
      <span>
        <button class="writeBtn">
        <i @click="interestToggle" class="fas fa-fire-alt"></i>
        </button>
      </span>
    </div>
    <div class="interest disabled">
      <div class="interest-menu">
        <li class="skill">
          Python
        </li>
        <li class="skill">
          Java
        </li>
        <li class="skill">
          Vuejs
        </li>
        <li class="skill">
          Flutter
        </li>
        <li class="skill">
          AI
        </li>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomeNav",
  methods: {
    interestToggle() {
      const interest = document.querySelector(".interest");
      interest.classList.toggle("disabled");
    },
  },
};
</script>

<style scoped>
.home-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 20px;
  font-weight: bold;
  position: fixed;
  background-color: rgb(244, 248, 255);
  width: 100%;
  z-index: 7;
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
.interest {
  position: absolute;
  top: 70px;
  right: 0;
  z-index: 3;
}
.interest-menu {
  width: 100px;
  border: 1px solid black;
  background-color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-right: 1rem;
  z-index: 2;
}
.interest-menu > li {
  margin: 10px;
  list-style-type: none;
}
.disabled {
  display: none;
}
.writeBtn {
  background-color: rgb(144, 153, 240);
  border-radius: 3px;
  color: white;
  font-weight: bold;
  font-size: 1rem;
  border-style: none;
  outline: none;
  width: 35px;
  height: 35px;
  cursor: pointer;
  margin-right: 10px;
}
</style>
