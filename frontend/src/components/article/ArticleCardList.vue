<template>
  <div>
    <vue-headful
      title="TWL: Today We Learned"
    />
    <div v-if="isLoading">
      <LoadingSpinner />
    </div>
    <div v-else>
      <div class="news-index">
        <p>⚡ News ⚡</p>
      </div>
      <br />
      <button @click="goToTop" class="top-btn"><i class="fas fa-angle-double-up"> TOP</i></button>
      <ArticleCard
        v-for="(article, index) in articles"
        :key="article._id"
        :article="article"
        :keywords="keywords[index]"
        :pinCnt="pinCntList[index]"
        :likesCnt="likesCntList[index]"
        :commentCnt="commentCntList[index]"
      />
      <div id="bottomSensor"></div>
    </div>
  </div>
</template>

<script>
import ArticleCard from "@/components/article/ArticleCard.vue";
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
      likesCntList : [],
      pinCntList : [],
      tmpList : [],
      commentCntList : [],
      isLoading: true,
      page: 0,
    };
  },
  methods: {
    async fetchData() {
      const params = {
        page: this.page++,
      };
      const token = this.$store.state.id_token;
      const { data } = await fetchArticles(params, token);
      this.isLoading = false;
      this.keywords = [...this.keywords, ...data.object.keyword];
      this.articles = [...this.articles, ...data.object.article.content];
      this.likesCntList = [...this.likesCntList, ...data.object.likesCntList];
      this.pinCntList = [...this.pinCntList, ...data.object.pinCntList];
      this.commentCntList = [...this.commentCntList, ...data.object.commentCntList];
    },
    addScrollWatcher() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);
      watcher.enterViewport(() => {
        this.fetchData();
      });
    },
    goToTop() {
      const nowScroll = window.scrollY
      scrollBy({top: -nowScroll, behavior:'smooth'})
    }
  },
  created() {
    this.fetchData();
  },
  mounted() {
    setTimeout(() => this.addScrollWatcher(), 1000);
  }
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
.top-btn {
  position: fixed;
  left: 1rem;
  top: 650px;
  width: 60px;
  color: white;
  font-weight: bolder;
  background-color: rgb(144, 153, 240);
  font-size: 1rem;
}

@media (max-width: 414px) {
  .top-btn {
    top: 670px
  }
}

@media (max-width: 375px) {
  .top-btn {
    top: 580px
  }
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
