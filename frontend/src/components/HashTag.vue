<template>
  <div class="hashtag">
    <p class="title">👀 Attention 👀</p>
    <div class="stacks">
      <div class="stack" v-for="stack in stacks" :key="stack"># {{ stack }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HashTag",
  data() {
    return {
      stacks: [
        "python",
        "java",
        "javascript",
        "c",
        "c++",
        "frontend",
        "backend",
        "vue",
        "spring",
      ],
    };
  },
};
</script>

<style scoped>
.hashtag {
  position: fixed;
  width: inherit;
  padding-right: 16px;
}
.title {
  padding-bottom: 1rem;
  color: black;
}
.stack {
  font-size: 1rem;
  color: black;
  padding: 1rem;
  border-radius: 1rem;
  font-weight: bolder;
  /* padding: 3px; */
  z-index: -1;
  width: 100%;
  text-transform: uppercase;
}
.stack:hover {
  cursor: pointer;
  background-color: lightgray;
}
</style>
