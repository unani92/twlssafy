<template>
  <div>
    <div class="score" :style="`margin-left:${width-3}%`">{{ score }}</div>
    <div class="progressbar">
      <div class="percent" :class="tier" :style="`width:${width}%;`"></div>
    </div>
    <div class="numbers">
      <span style="margin-right: 1rem">{{ left }}</span>
      <span>{{ right+1 }}</span>
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
        return Math.round(((this.score-this.left)/(this.right+1-this.left))*100)
      }
    },
    data() {
      if (this.calGrade === 0) {
        return { left: 0, right:9, tier: 'new' }
      } else if (this.calGrade === 1) {
        return  { left: 0, right:9, tier: 'new' }
      } else if (this.calGrade === 2) {
        return { left: 10, right: 49, tier: 'bronze' }
      } else if (this.calGrade === 3) {
        return { left: 50, right: 149, tier: 'silver' }
      } else if (this.calGrade === 4) {
        return { left: 150, right: 299, tier: 'gold' }
      } else if (this.calGrade === 5) {
        return { left: 300, right: 599, tier: 'platinum' }
      } else {
        return { left: 600, right: 10000, tier: 'diamond' }
      }
    }
  }
</script>

<style scoped>
  .progressbar {
    max-width: 570px;
    margin: 4px auto;
    width: 100%;
    height: 30px;
    background-color: #f0f0f0;
    border-radius: 3px;
  }
  .percent {
    height: 30px;
    border-radius: 3px;
    color:white;
    text-align:right;
    padding: 4px;
    font-weight: bolder;
  }
  .numbers {
    max-width: 570px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
  }
  .new {
    background-color: dimgray;
  }
  .bronze {
    background-color: #C93000
  }
  .silver {
    background-color: silver;
  }
  .gold {
    background-color: gold;
  }
  .platinum {
    background-color: #00CFB7;
  }
  .diamond {
    background-color: #76BAFF;
  }
</style>