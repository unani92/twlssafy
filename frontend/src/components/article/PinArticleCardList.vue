<template>
  <div>
    <vue-headful
      title="TWL: Today We Learned"
    />
    <div v-if="isLoading">
      <LoadingSpinner />
    </div>
    <div v-else>
      <div class="news-index" v-if="articles.length">
        <p>⚡ News ⚡</p>
      </div>
      <div class="no-pin" v-else>
        <div class="news-index">
          <h1 class="ment">현재 북마크한 게시글이 없습니다 ! <br> 마음에 드는 게시글을 찜 해보세Yo</h1>
          <img class ="muji" src="https://user-images.githubusercontent.com/53211781/90217074-42994880-de3b-11ea-8d3b-21594cb5ad6e.png" alt="">
        </div>
        <br>
        <br>
        <hr>
        <br>
        <br>
        <div>
          <img style="width: 100%; height: auto" src="https://user-images.githubusercontent.com/53211781/90220314-ed613500-de42-11ea-89b6-e7c522702b3c.png" alt="">
        </div>
        <div>
          <img style="width: 100%; height: auto" src="https://user-images.githubusercontent.com/53211781/90220878-07e7de00-de44-11ea-9bef-bf4a6e34c028.png" alt="">
        </div>
      </div>
      <br />

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
import { fetchPinArticles } from "@/api/index";
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
      const id_token = this.$store.state.id_token;
      const { data } = await fetchPinArticles(params, id_token);
      this.isLoading = false;
      this.keywords = [...this.keywords, ...data.object.keyword];
      this.articles = [...this.articles, ...data.object.article];
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
.news-index {
  max-width: 850px;
  width: 96%;
  display: flex;
  justify-content: left;
  align-items: center;
  margin: auto;
}
.no-pin {
  max-width: 850px;
  width: 100%;
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
.ment {
  font-size: 30px;
}
@media (min-width: 0) and (max-width : 469px){
  .ment {
    font-size: 15px;
  }
   .muji {
    height : 90px;
    width : 90px;
  }
}
@media (min-width: 468px) and (max-width : 615px){
  .ment {
    font-size: 24px;
  }
  .muji {
    height : 150px;
    width : 150px;
  }
}
</style>
