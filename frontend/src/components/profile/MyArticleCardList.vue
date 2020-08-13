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

      <ArticleCard
        v-for="(article, index) in articles"
        :key="article._id"
        :article="article"
        :keywords="keywords[index]"
        :commentCnt="commentCntList[index]"
        :likesCnt="likesCntList[index]"
        :pinCnt="pinCntList[index]"
      />
      <div id="bottomSensor"></div>
    </div>
  </div>
</template>

<script>
import ArticleCard from "@/components/article/AricleCard.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { fetchMyArticles } from "@/api/index";
import { mapState, mapActions } from "vuex";
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
      likesCntList: [],
      pinCntList: [],
      commentCntList: [],
      isLoading: true,
      page: 0,
      userInfo: {},
    };
  },
  computed: {
    ...mapState(["id_token"]),
  },
  methods: {
    ...mapActions(["getGoogleUserInfo"]),
    async fetchData() {
      const params = {
        page: this.page++,
        nickname: this.$route.params.nickname,
      };
      const { data } = await fetchMyArticles(params);
      if (this.$route.path === `/account/${this.$route.params.nickname}`) {
        // MyArticleCardList 컴포넌트가 화면에서 없어져도 스크롤 모니터가 남아서 현재 요청을 보내려고 함
        // 임시로 요청을 막고 있지만 현재 컴포넌트가 완전히 없어지지 않아 다른 이슈나 에러가 발생할 가능성이 있음
        console.log(this.$route.path);
        this.isLoading = false;
        this.keywords = [...this.keywords, ...data.object.keyword];
        this.articles = [...this.articles, ...data.object.article.content];
        this.likesCntList = [...this.likesCntList, ...data.object.likesCntList];
        this.commentCntList = [
          ...this.commentCntList,
          ...data.object.commentCntList,
        ];
        this.pinCntList = [...this.pinCntList, ...data.object.pinCntList];
        this.userInfo = data.object.user;

        this.$emit("setUserInfo", {
          userInfo: this.userInfo,
          following: data.object.followList,
          follower: data.object.followerList,
          totalArticleCount: data.object.totalArticleCount,
        });
        this.$store.commit("setUserSkills", data.object.interestList);
      }
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
    if (this.id_token) {
      this.getGoogleUserInfo(this.id_token);
    }
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
