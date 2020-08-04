<template>
  <div>
    <div v-if="isLoading">
      <LoadingSpinner />
    </div>
    <div>
      <ArticleCard
        v-for="(article, index) in articles"
        :key="article._id"
        :article="article"
        :keywords="keywords[index]"
      />
      <div id="bottomSensor"></div>
    </div>
  </div>
</template>

<script>
import ArticleCard from "@/components/article/AricleCard.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { fetchFollowArticles } from "@/api/index";
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
      const params = {
        page: this.page++,
      };
      const id_token = this.$store.state.id_token;

      const { data } = await fetchFollowArticles(params, id_token);
      console.log(data);
      this.isLoading = false;
      this.keywords = [...this.keywords, ...data.object.keyword];
      this.articles = [...this.articles, ...data.object.article];
    },
    addScrollWatcher() {
      const bottomSensor = document.querySelector("#bottomSensor");
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
