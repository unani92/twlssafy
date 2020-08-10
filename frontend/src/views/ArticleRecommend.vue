<template>
  <div id="example" style="padding-top: 60px">
    <div v-if="isLoading">
      <LoadingSpinner />
    </div>
    <div v-else style="margin-top: 100px">
      <div id = "intro">
        <h1>HOT & RECOMMEND</h1>
      </div>
      <carousel-3d :controls-visible="true" :width="800" :height="380" :display="5">
        <Slide v-for="(article, index) in articles" :index="index" :key="index">
          <ArticleCard style="margin : 0px"
            :article="article"
            :keywords="keywords[index]"
            :pinCnt="pinCntList[index]"
            :likesCnt="likesCntList[index]"
            :commentCnt="commentCntList[index]"
          />
        </Slide>
      </carousel-3d>
      <div id="outro"><h2>Today We Learned에서 공부한 내용을 매일 가볍게 정리하고 팔로워들과 공유하세요 !</h2></div>
    </div>
  </div>
</template>

<script>
  import { Carousel3d, Slide } from 'vue-carousel-3d'
  import { fetchRecommend } from "../api";
  import { mapState, mapActions } from "vuex";
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
        commentCntList : [],
        isLoading: true,
      }
    },
    computed: {
      ...mapState(["id_token"]),
    },
    components: {
      'carousel-3d': Carousel3d,
      Slide,
      LoadingSpinner,
      ArticleCard,
    },
    methods: {
      ...mapActions(["getGoogleUserInfo"]),
      async fetchData() {
        const { data } = await fetchRecommend(this.id_token)
        this.isLoading = false;
        this.keywords = data.object.keyword;
        this.articles = data.object.articles;
        this.likesCntList = data.object.likesCntList;
        this.pinCntList = data.object.pinCntList;
        this.commentCntList = data.object.commentCntList;
      }
    },
    mounted() {
      if (this.id_token) {
        this.getGoogleUserInfo(this.id_token)
      }
      this.fetchData()
    }
  }
</script>

<style scoped>
  .carousel-3d-slide {
    border: none;
    background-color: transparent;
  }
  .home-nav {
    display: flex;
    height: 60px;
    align-items: center;
    padding: 0 1rem;
  }
  #intro{
    font-family: 'Noto Serif KR', serif;
    font-weight: lighter;
    text-align:center;
    font-size: 13px;
    margin-bottom: 40px;
  }
  #outro{
    font-family: 'Noto Serif KR', serif;
    text-align:center;
    font-size: 12px;
    margin: 3% 0 7% 0;
  }
</style>