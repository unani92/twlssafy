<template>
  <div>
    <div class="progressbar">
      <div class="percent" :style="`width:${40}%`"/>
    </div>
    <div class="numbers">
      <span>{{ left }}</span>
      <span>{{ right }}</span>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ProgressBar",
    props: {
      calGrade: {
        type: String && Number,
        required: true
      },
      score: {
        type: String && Number,
        required: true
      }
    },
    computed: {
      width() {
        return Math.round(this.score/(this.right+1))
      }
    },
    data() {
      if (this.calGrade === 0) {
        return { left: 0, right:9 }
      } else if (this.calGrade === 1) {
        return  { left: 0, right:9 }
      } else if (this.calGrade === 2) {
        return { left: 10, right: 49 }
      } else if (this.calGrade === 3) {
        return { left: 50, right: 149 }
      } else if (this.calGrade === 4) {
        return { left: 150, right: 299 }
      } else {
        return { left: 300, right: 599 }
      }
    }
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