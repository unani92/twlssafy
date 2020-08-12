<template>
  <div style="padding-top: 150px">
    <div class="progressbar">
      <div class="percent" :style="`width:${score/right}%`"/>
    </div>
    <div class="numbers">
      <span>{{ left }}</span>
      <span>{{ right }}</span>
    </div>
  </div>
</template>

<script>
  import { getGrade } from '@/utils/calcGrade';
  import { mapState } from 'vuex'
  export default {
    name: "ProgressBar",
    computed: {
      ...mapState(["score"]),
      grade() {
        return getGrade(this.score)
      },
    },
    data() {
      if (this.grade === 0) {
        return { left: 0, right: 0 }
      } else if (this.grade === 1) {
        return { left: 10, right: 50 }
      } else if (this.grade === 2) {
        return { left: 51, right: 150 }
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