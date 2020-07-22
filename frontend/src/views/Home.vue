<template>
  <div class="home">
    <LoadingSpinner v-if="isLoading"></LoadingSpinner>

    <div v-else>
      <div>
        <HomeNav />
      </div>
      <div>
        <HashTag />
      </div>
      <div class="primary">
        <ArticleCardList :articles="articles"></ArticleCardList>
      </div>
      <div style="height: 10000px"></div>
    </div>
  </div>
</template>

<script>
import HomeNav from "../components/HomeNav";
import HashTag from "../components/HashTag";
import ArticleCardList from "@/components/article/ArticleCardList.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { fetchArticles } from "@/api/index";

export default {
  components: {
    HomeNav,
    HashTag,
    ArticleCardList,
    LoadingSpinner,
  },
  data() {
    return {
      articles: [],
      isLoading: false,
      page: 0,
    };
  },
  methods: {
    async fetchData() {
      const params = {
        page: this.page++,
      };
      this.isLoading = true;
      const { data } = await fetchArticles(params);
      this.isLoading = false;
      this.articles = data.object.article.content;
      console.log(this.articles)
    },
  },
  created() {
    this.fetchData();
  },
};
</script>

<style scoped>
.home {
  padding-top: 60px;
}
.main {
  display: flex;
  justify-content: space-between;
}
</style>
