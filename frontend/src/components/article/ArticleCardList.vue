<template>
<div>
  <div v-if="isLoading">
<LoadingSpinner></LoadingSpinner>
  </div>
  <div>
    <ArticleCard
      v-for="(article, index) in articles"
      :key="article._id"
      :article="article"
      :keywords="keywords[index]"
    ></ArticleCard>
    <div id="bottomSensor"></div>
  </div>
  </div>
</template>

<script>
import ArticleCard from "@/components/article/AricleCard.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { fetchArticles } from "@/api/index";
import scrollMonitor from "scrollmonitor";

export default {
  components: {
    ArticleCard,
    LoadingSpinner,
  },

  data() {
    return {
      articles: [],
      keywords: [],
      isLoading: true,
      page: 0,
    };
  },
  methods: {
    async fetchData() {
      console.log("ACTION!!!!!!!!!!!!");
      const params = {
        page: this.page++,
      };
      const { data } = await fetchArticles(params);
      this.isLoading = false;
      this.keywords = data.object.keyword;
      this.articles = [...this.articles, ...data.object.article.content];
    },
    addScrollWatcher() {
      const bottomSensor = document.querySelector("#bottomSensor");
      console.log(bottomSensor);
      const watcher = scrollMonitor.create(bottomSensor);
      watcher.enterViewport(() => {
        this.fetchData();
      });
    },
  },
  created() {
    this.fetchData();
  },
  mounted() {
    setTimeout(() => this.addScrollWatcher(), 1000);
  },
};
</script>

<style>
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
