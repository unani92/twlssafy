<template>
  <div class="hashtag">
    <h4 style="text-align: center">주요기술</h4>
    <div class="stack" v-for="stack in stacks" :key="stack"># {{ stack }}</div>
  </div>
</template>

<script>
  export default {
    name: "HashTag",
    data() {
      return {
        stacks: ['python','java','javascript','c','c++','frontend','backend','vue','spring']
      }
    }
  }
</script>

<style scoped>
  .hashtag {
    border-radius: 4px;
    background-color: white;
    margin: 3rem;
    position: fixed;
    right: 0;
    padding: 10px;
  }
  .stack {
    font-size: 1rem;
    margin-bottom: 1rem;
    border-radius: 6rem;
    color: white;
    font-weight: bolder;
    background-color: #2c3e50;
    padding: 3px;
  }
</style>