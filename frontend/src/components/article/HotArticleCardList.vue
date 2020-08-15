<template>
  <div>
    <div v-if="isLoading">
      <LoadingSpinner />
    </div>
    <div v-else>
      <div class="news-index">
        <p>⚡ News ⚡</p>
      </div>
      <br />
      <carousel-3d>
        <Slide v-for="(slide, i) in articles.length" :index="i" :key="i">
          <img src="https://placehold.it/360x270">
        </Slide>
      </carousel-3d>
    </div>
  </div>
</template>

<script>
// import ArticleCard from "@/components/article/ArticleCard.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { Carousel3d, Slide } from 'vue-carousel-3d'
import { fetchHotArticles } from "@/api/index";

export default {
  components: {
    // ArticleCard,
    LoadingSpinner,
    'carousel-3d': Carousel3d,
    Slide
  },
  data() {
    return {
      articles: [],
      keywords: [],
      isLoading: true,
      page: 0,
      slides: 6
    };
  },
  methods: {
    async fetchData() {
      const params = {
        page: this.page++,
      };
      const id_token = this.$store.state.id_token;

      const { data } = await fetchHotArticles(params, id_token);
      this.isLoading = false;
      this.keywords = [...this.keywords, ...data.object.keyword];
      this.articles = [...this.articles, ...data.object.article];
    },
  },
  created() {
    this.fetchData();
  },
  mounted() {

  },
};
</script>

<style>
.news-index {
  max-width: 850px;
  width: 96%;
  display: flex;
  justify-content: left;
  margin: auto;
}
#bottomSensor {
  height: 10px;
}

::-webkit-scrollbar {
  -webkit-appearance: none;
}
::-webkit-scrollbar {
  width: 10px;
}

::-webkit-scrollbar-track {
  background: #eee;
  border: 1px solid #eee;
}

::-webkit-scrollbar-thumb {
  background: rgb(144, 153, 240);
  border: 1px solid #eee;
  height: 100px;
  border-radius: 5px;
}
</style>
