<template>
  <div id="example" style="padding-top: 100px">
    <carousel-3d>
      <Slide v-for="(slide, i) in slides" :index="i" :key="i">
        <img src="https://placehold.it/360x270">
      </Slide>
    </carousel-3d>
  </div>
</template>

<script>
  import { Carousel3d, Slide } from 'vue-carousel-3d'
  export default {
    name: "ArticleRecommend",
    data() {
      return {
        slides: 7
      }
    },
    components: {
      'carousel-3d': Carousel3d,
      Slide
    }

  }
</script>

<style scoped>

</style>