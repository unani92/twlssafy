<template>
  <div class="article-search">
    <div class="search">
      <div class="option">
        <!-- <select v-model="category" name="category" id="category">
          <option selected value>분류</option>
          <option value="title">제목</option>
          <option value="content">내용</option>
          <option value="keyword">키워드</option>
          <option value="nickname">닉네임</option>
        </select> -->
        <input type="radio" name="category" value="keyword" v-model="category"> 키워드 
        <input type="radio" name="category" value="title" v-model="category"> 제목 
        <input type="radio" name="category" value="content" v-model="category">  내용 
        <input type="radio" name="category" value="nickname" v-model="category"> 닉네임 
      </div>
      <div class="searchbar">        
        <input @keypress.enter="fetchSearchData" type="text" v-model="input"/>
        <i class="fas fa-search" @click="fetchSearchData"></i>
      </div>
    </div>
    <div v-if="searchArticles">
      <ArticleSearchCard
        v-for="(article,index) in searchArticles"
        :key="article.articleid"
        :article="article"
        :keywords="keywords[index]"
        :commentCnt="commentCntList[index]"
        :likesCnt="likesCntList[index]"
        :pinCnt="pinCntList[index]"
      />
      <div id="bottomSensor"></div>
    </div>
    <scroll/>
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
      commentCntList : [],
      pinCntList : [],
      likesCntList : [],
      isLoading: true,
      page: 0,
      input: null,
      q: null,
      category: "keyword",
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
            object: { article, keyword, likesCntList, commentCntList, pinCntList},
          },
        } = await searchArticle(params);
        console.log(article);
        console.log(keyword);
        this.keywords = [...this.keywords, ...keyword];
        this.searchArticles = [...this.searchArticles, ...article];
        this.likesCntList = [...this.likesCntList, ...likesCntList]
        this.commentCntList = [...this.commentCntList, ...commentCntList];
        this.pinCntList = [...this.pinCntList, ...pinCntList];
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
        console.log(`else: ${res}`);
        const {
          data: {
            object: { article, keyword, likesCntList, commentCntList, pinCntList},
          },
        } = await searchArticle(params);
        console.log(article);
        console.log(keyword);
        this.keywords = [...this.keywords, ...keyword];
        this.searchArticles = [...this.searchArticles, ...article];
        this.likesCntList = [...this.likesCntList, ...likesCntList]
        this.commentCntList = [...this.commentCntList, ...commentCntList];
        this.pinCntList = [...this.pinCntList, ...pinCntList];
        console.log(this.pinCntList)
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
  padding-top: 135px;
}
.search {
  text-align: center;
  left : 5%;
  width: 100%;
}
  .option {
    margin : 1rem;
  }
.searchbar {
  width: 50%;
  margin: 1rem 1rem 1rem 25%;
}
.searchbar > i {
  position: absolute;
  right: 25%;
  /* z-index: 100000; */
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
  font-size: larger;
  z-index: 5;
  padding-left: 20px;
  border: none;
  border-radius: 1%;
  outline: none;
}
@media (max-width: 768px){
  .searchbar {
   width: 70%;
   margin: 1rem 1rem 1rem 15%;
  }
  .searchbar > i {
  right: 15%;
}
.searchbar > input {
    padding-left: 10px;

}
}

</style>
