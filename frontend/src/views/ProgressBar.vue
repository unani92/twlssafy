<template>
  <div>
    <div class="progressbar">
      <div class="percent" :style="`width:${70}%`"/>
    </div>
    <div class="numbers">
      <span>0</span>
      <span>100</span>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ProgressBar",
    computed: {

    },
  }
</script>

<style scoped>
  .progressbar {
    max-width: 570px;
    margin: 0 auto;
    width: 100%;
    height: 20px;
    background-color: #e0e0e0;
    border-radius: 3px;
  }
  .percent {
    /*width: 70%;*/
    height: 20px;
    background-color: darkred;
    border-radius: 3px;
  }
  .numbers {
    max-width: 570px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
  }
</style>