<template>
  <div class="article-search">
    <div class="search">
      <div class="option">
        <select v-model="category" name="category" id="category">
          <option value>분류</option>
          <option value="nickname">닉네임</option>
          <option value="title">제목</option>
          <option value="content">내용</option>
          <option value="keyword">키워드</option>
        </select>
      </div>
      <div class="searchbar">
        <i class="fas fa-search"></i>
        <input @keypress.enter="fetchSearchData" type="text" v-model="input" />
      </div>
    </div>
    <div v-if="searchArticles">
      <ArticleSearchCard
        v-for="(article,index) in searchArticles"
        :key="article.articleid"
        :article="article"
        :keywords="keywords[index]"
      />
      <div id="bottomSensor"></div>
    </div>
  </div>
</template>

<script>
import ArticleSearchCard from "../components/article/ArticleSearchCard";
import { searchArticle } from "../api";
import scrollMonitor from "scrollmonitor";

export default {
  name: "ArticleSearch",
  components: {
    ArticleSearchCard,
  },
  data() {
    return {
      searchArticles: [],
      keywords: [],
      isLoading: true,
      page: 0,
      input: null,
      q: null,
      category: null,
    };
  },
  methods: {
    async fetchSearchData() {
      if (this.q === this.input) {
        const params = {
          page: this.page++,
          q: this.q || this.$route.query.q,
          category: this.category || this.$route.query.category,
        };
        const res = await searchArticle(params);
        console.log(res);
        const {
          data: {
            object: { article, keyword },
          },
        } = await searchArticle(params);
        console.log(article);
        console.log(keyword);
        this.keywords = [...this.keywords, ...keyword];
        this.searchArticles = [...this.searchArticles, ...article];
      } else {
        this.q = this.input;
        this.page = 0;
        this.searchArticles = [];
        const params = {
          page: this.page++,
          q: this.q,
          category: this.category,
        };
        const res = await searchArticle(params);
        console.log(res);
        const {
          data: {
            object: { article, keyword },
          },
        } = await searchArticle(params);
        console.log(article);
        console.log(keyword);
        this.keywords = [...this.keywords, ...keyword];
        this.searchArticles = [...this.searchArticles, ...article];
      }
    },
    addScrollWatcher() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);
      watcher.enterViewport(() => {
        this.fetchSearchData();
      });
    },
  },
  mounted() {
    setTimeout(() => this.addScrollWatcher(), 1000);
  },
};
</script>

<style scoped>
.article-search {
  padding-top: 100px;
  max-width: 720px;
  margin-left: auto;
  margin-right: auto;
}
.search {
  display: flex;
  width: 100%;
  padding: 1rem;
}
.option {
  width: 30%;
}
.option > select {
  text-align: center;
  width: 90%;
  height: 60px;
  font-size: 1rem;
  padding-left: 20px;
  padding-right: 10px;
}
.searchbar {
  width: 70%;
}
.searchbar > i {
  position: absolute;
  z-index: 100000;
  cursor: pointer;
}
i {
  padding: 20px;
  width: 70px;
  text-align: left;
  font-size: larger;
}
.searchbar > input {
  width: 100%;
  height: 60px;
  text-align: center;
  font-size: larger;
  z-index: 5;
}
</style>
