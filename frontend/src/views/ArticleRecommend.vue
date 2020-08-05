<template>
  <div id="example" style="padding-top: 100px">
    <div v-if="isLoading">
      <LoadingSpinner />
    </div>
    <div v-else>
      <carousel-3d :width="800" :height="400">
        <Slide v-for="(article, index) in articles" :index="index" :key="index">
          <ArticleCard
            :article="article"
            :keywords="keywords[index]"
            :pinCnt="pinCntList[index]"
            :likesCnt="likesCntList[index]"
          />
        </Slide>
      </carousel-3d>
    </div>
  </div>
</template>

<script>
  import { Carousel3d, Slide } from 'vue-carousel-3d'
  import { fetchRecommend } from "../api";
  import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
  import ArticleCard from "@/components/article/AricleCard.vue";
  export default {
    name: "ArticleRecommend",
    data() {
      return {
        articles: [],
        keywords: [],
        likesCntList : [],
        pinCntList : [],
        tmpList : [],
        commentCntList : [],
        isLoading: true,
      }
    },
    components: {
      'carousel-3d': Carousel3d,
      Slide,
      LoadingSpinner,
      ArticleCard
    },
    methods: {
      async fetchData() {
        const id_token = this.$store.state.id_token;
        const { data } = await fetchRecommend(id_token)
        this.isLoading = false;
        this.keywords = data.object.keyword;
        this.articles = data.object.articles;
        this.likesCntList = data.object.likesCntList;
        this.pinCntList = data.object.pinCntList;
        this.commentCntList = data.object.commentCntList;
        console.log(this.articles)
        console.log(this.keywords)
        console.log(this.likesCntList)
        console.log(this.pinCntList)
        console.log(this.commentCntList)
      }
    },
    mounted() {
      this.fetchData();
    }
  }
</script>

<style scoped>
  .hashtag {
    display: none !important;
  }
</style>