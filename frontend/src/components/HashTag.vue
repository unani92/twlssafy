<template>
  <div class="hashtag">
    <p class="title">👀 Attention 👀</p>
    <div class="stacks">
      <div class="stack" v-for="stack in stacks" :key="stack">
        # {{ stack }}
      </div>
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
  border-radius: 4px;
  position: fixed;
  width: inherit;
  margin-right: 100px;
  padding-right: 100px;
}
.title {
  padding-bottom: 1rem;
  color: black;
}
/* .stacks {
  background-color: white;
} */
.stack {
  font-size: 1rem;
  color: black;
  margin-bottom: 1rem;
  border-radius: 6rem;
  font-weight: bolder;
  padding: 3px;
  z-index: -1;
  width: 100%;
  text-transform: uppercase;
}
.stack:hover {
  cursor: pointer;
  border: 1px black solid;
}
</style>
