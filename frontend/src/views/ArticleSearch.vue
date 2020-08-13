<template>
  <div class="article-search">
    <div class="search">
      <div class="option">
        <input type="radio" name="category" value="keyword" v-model="category" /> 키워드
        <input type="radio" name="category" value="title" v-model="category" /> 제목
        <input type="radio" name="category" value="content" v-model="category" /> 내용
        <input type="radio" name="category" value="nickname" v-model="category" /> 닉네임
      </div>
      <div class="searchbar">
        <input
          @keypress.enter="fetchSearchData"
          type="text"
          v-model="input"
          @input="submitAutoComplete"
        />
        <i class="fas fa-search" @click="fetchSearchData"></i>
      </div>
      <div class="autocomplete disabled">
        <div
          @click="searchSkillAdd"
          class="autocomplete-cursor"
          v-for="res in result"
          :key="res"
        >{{ res }}</div>
      </div>
    </div>
    <p class="search-result-guide">{{searchResult}}</p>
    <div v-if="searchArticles" style="margin-top:1rem">
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
    <scroll />
  </div>
</template>

<script>
import ArticleSearchCard from "../components/article/ArticleSearchCard";
import { searchArticle } from "../api";
import scrollMonitor from "scrollmonitor";
import skills from "../skills.js";

export default {
  name: "ArticleSearch",
  components: {
    ArticleSearchCard,
  },
  data() {
    return {
      searchArticles: [],
      keywords: [],
      commentCntList: [],
      pinCntList: [],
      likesCntList: [],
      isLoading: true,
      page: 0,
      input: null,
      q: null,
      category: "keyword",
      searchResult: "",
    };
  },
  methods: {
    submitAutoComplete() {
      const autocomplete = document.querySelector(".autocomplete");
      if (this.input) {
        autocomplete.classList.remove("disabled");
        this.result = skills.filter((skill) => {
          return skill.match(new RegExp("^" + this.input, "i"));
        });
      } else {
        autocomplete.classList.add("disabled");
      }
    },
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
            object: {
              article,
              keyword,
              likesCntList,
              commentCntList,
              pinCntList,
            },
          },
        } = await searchArticle(params);
        console.log(article);
        console.log(keyword);
        this.keywords = [...this.keywords, ...keyword];
        this.searchArticles = [...this.searchArticles, ...article];
        this.likesCntList = [...this.likesCntList, ...likesCntList];
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
            object: {
              article,
              keyword,
              likesCntList,
              commentCntList,
              pinCntList,
            },
          },
        } = await searchArticle(params);
        console.log(article);
        console.log(keyword);
        this.keywords = [...this.keywords, ...keyword];
        this.searchArticles = [...this.searchArticles, ...article];
        this.likesCntList = [...this.likesCntList, ...likesCntList];
        this.commentCntList = [...this.commentCntList, ...commentCntList];
        this.pinCntList = [...this.pinCntList, ...pinCntList];
        console.log(this.pinCntList);
      }
    },

    async searchSkillAdd(event) {
      const skill = event.target.innerText;
      try {
        this.searchArticles = [];
        const params = {
          page: 0,
          q: skill,
          category: "keyword",
        };
        const res = await searchArticle(params);
        console.log(res);
        const {
          data: {
            object: {
              article,
              keyword,
              likesCntList,
              commentCntList,
              pinCntList,
            },
          },
        } = await searchArticle(params);
        this.keywords = [...this.keywords, ...keyword];
        this.searchArticles = [...this.searchArticles, ...article];
        this.likesCntList = [...this.likesCntList, ...likesCntList];
        this.commentCntList = [...this.commentCntList, ...commentCntList];
        this.pinCntList = [...this.pinCntList, ...pinCntList];
        this.searchResult = "";
      } catch (error) {
        this.searchResult = `"${skill}"의 검색 결과가 없습니다.`;
        console.error(error);
      } finally {
        this.input = skill;
        const autocomplete = document.querySelector(".autocomplete");
        autocomplete.classList.toggle("disabled");
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
  updated() {
    //autocomplete가 켜져 있을 때 esc 누르면 닫힘
    document.onkeydown = function (evt) {
      evt = evt || window.event;
      if (evt.keyCode == 27) {
        const autocomplete = document.querySelector(".autocomplete");
        autocomplete.classList.toggle("disabled");
      }
    };
  },
};
</script>

<style scoped>
.article-search {
  padding-top: 135px;
}
.search {
  text-align: center;
  left: 5%;
  width: 100%;
}

.option {
  margin: 1rem;
}
.searchbar {
  width: 50%;
  margin: 1rem 1rem 0 25%;
}
.autocomplete {
  width: 50%;
  position: absolute;
  left: 25%;
}
.autocomplete-cursor:hover {
  cursor: pointer;
  background-color: rgb(228, 228, 228);
  background: radial-gradient(rgba(0, 0, 0, 0.05), transparent 70%);
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
  box-shadow: 0 1px 6px 0 rgba(32, 33, 36, 0.28);
}
.search-result-guide {
  text-align: center;
  margin-top: 1rem;
}
@media (max-width: 768px) {
  .searchbar {
    width: 70%;
    margin: 1rem 1rem 0 15%;
  }
  .searchbar > i {
    right: 15%;
  }
  .searchbar > input {
    padding-left: 10px;
  }
  .autocomplete {
    width: 70%;
    left: 15%;
  }
}
.autocomplete {
  box-shadow: 0 1px 6px 0 rgba(32, 33, 36, 0.28);
  border-top: none;
  max-height: 150px;
  overflow-y: auto;
  margin: 0 auto;
  padding: 0;
  background: white;
  /* background: linear-gradient(white, rgba(0, 0, 0)),
    radial-gradient(at top, rgba(0, 0, 0, 0.5), transparent 70%);
  background-repeat: no-repeat;
  background-attachment: local, scroll; */
}
.disabled {
  display: none;
}
.totalSkills {
  display: inline-block;
  border: 1px solid #9fa3af;
  padding: 6px 15px;
  border-radius: 25px;
  min-width: 65px;
  font-weight: 600;
  margin: 0.5rem 0.25rem;
  text-align: center;
}
</style>
