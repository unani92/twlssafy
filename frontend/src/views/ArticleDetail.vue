<template>
  <div class="article-detail">
    <div class="left-sidemenu">
      <ArticleDetailSideMenu :article="article" :sideMenu="sideMenu" />
    </div>
    <div class="article">
      <div class="nickname-keyword">
        <h1 class="title">{{ title }}</h1>
        <div class="keywords" style="margin-bottom : 1rem;">
          <div
            v-for="keyword in keywords"
            :key="keyword"
            class="keyword"
            @click="search(keyword)"
          >#{{ keyword }}</div>
        </div>
        <div class="username-date">
          <!-- @click="userToggle" -->
          <div>
            <span style="margin-right : 4px; cursor:pointer" @click="gotoWriterPage">{{ nickname }}</span>
            <span v-if="isWriter">
              <Router-link
                :to="{
                name: 'ArticleUpdate',
                params: { id, keywords, title, content  }
            }"
              >
                <i
                  class="fas fa-edit"
                  style="cursor: pointer; margin-right : 4px; color : gray"
                  @click="edit"
                ></i>
              </Router-link>
              <i
                class="fas fa-trash-alt"
                style="cursor: pointer; color : gray"
                @click="removeArticle"
              ></i>
            </span>
          </div>
          <span>{{ updatedAt }}</span>
        </div>
      </div>
      <div class="nickname-keyword markdown">
        <div id="viewer" />
      </div>
      <div class="nickname-keyword" v-if="article">
        <ArticleDetailProfile :article="article" :userinfo="userinfo" :articleCount="articleCount" />
      </div>
      <div v-if="article" class="nickname-keyword">
        <div>
          <CommentCreate
            :article="article"
            :commentList="sideMenu.commentList"
            :commentNickname="commentNickname"
            :commentArticleCount="commentArticleCount"
          />
        </div>
      </div>
      <button class="backBtn" @click="goback">뒤로가기</button>
    </div>
  </div>
</template>

<script>
import { fetchArticle, deleteArticle } from "../api";
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import "highlight.js/styles/github.css";
import Viewer from "@toast-ui/editor/dist/toastui-editor-viewer";
import codeSyntaxHightlight from "@toast-ui/editor-plugin-code-syntax-highlight";
import hljs from "highlight.js";
import ArticleDetailSideMenu from "../components/article/ArticleDetailSideMenu";
import ArticleDetailProfile from "../components/article/ArticleDetailProfile";
import CommentCreate from "../components/article/CommentCreate";
export default {
  name: "ArticleDetail",
  components: {
    ArticleDetailSideMenu,
    ArticleDetailProfile,
    CommentCreate,
  },
  computed: {
    followList() {
      return this.$store.state.followList;
    },
  },
  data() {
    return {
      id: this.$route.params.id,
      nickname: null,
      keywords: null,
      title: null,
      article: null,
      content: null,
      updatedAt: null,
      userinfo: null,
      articleCount: 0,
      commentArticleCount: [],
      sideMenu: {
        commentList: null,
        isFollowed: null,
        cntLikes: null,
        cntPin: null,
        isWriter: false,
      },
    };
  },
  methods: {
    gotoWriterPage() {
      this.$router.push({
        name: "Profile",
        params: { nickname: this.article.nickname },
      });
    },
    search(keyword) {
      const params = {
        q: keyword,
        category: "keyword",
      };
      this.$router.push({ name: "ArticleSearchByStack", query: params });
    },
    async getArticle() {
      const articleInfo = await fetchArticle(this.id);
      const {
        article,
        keyword,
        commentList,
        cntLikes,
        cntPin,
        userinfo,
        commentNickname,
        articleCount,
        commentArticleCount,
      } = articleInfo.data.object;
      console.log(articleInfo);
      this.article = article;
      this.sideMenu.isFollowed = !!this.followList.follow.filter(
        (follow) => follow.followemail === this.article.email
      ).length;

      this.keywords = keyword;
      this.nickname = article.nickname;
      this.title = article.title;
      this.content = article.content;
      this.updatedAt = this.$moment(article.updatedat).fromNow();
      this.sideMenu.commentList = commentList;
      this.sideMenu.cntLikes = cntLikes;
      this.sideMenu.cntPin = cntPin;
      this.userinfo = userinfo;
      this.commentNickname = commentNickname;
      this.commentArticleCount = commentArticleCount;
      this.articleCount = articleCount;

      const loginUser = this.$store.state.nickname;
      if (this.article.nickname === loginUser) {
        this.isWriter = true;
      }

      return article.content;
    },
    getViewer() {
      this.getArticle().then((res) => {
        new Viewer({
          el: document.querySelector("#viewer"),
          initialValue: res,
          plugins: [[codeSyntaxHightlight, { hljs }]],
        });
      });
    },
    // userToggle() {
    //   const dropdown = document.querySelector(".dropdown")
    //   const loginUser = this.$store.state.nickname
    //   if (this.article.nickname === loginUser) {
    //     dropdown.classList.toggle("disabled")
    //   }
    // },

    removeArticle() {
      deleteArticle(this.id)
        .then(() => this.$router.push("/"))
        .catch((err) => console.log(err));
    },
    goback() {
      this.$router.go(-1);
    },
  },
  mounted() {
    this.getViewer();
  },
};
</script>

<style scoped>
.article-detail {
  display: flex;
  justify-content: space-around;
  margin-bottom: 2rem;
}
.article {
  padding-top: 75px;
  width: 80%;
  margin-right: 5%;
  /* margin-left: 5%; */
}
.nickname-keyword {
  padding: 1rem;
  margin-bottom: 1rem;
  background-color: white;
  border-radius: 10px;
}
.left-sidemenu {
  display: block;
}
#viewer {
  display: block;
  margin-bottom: 2rem;
}
.title {
  font-size: 30px;
  margin-bottom: 1rem;
}
.username-date {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  clear: both;
}
.username-date > span:nth-child(1) {
  font-weight: bold;
}
.keyword {
  float: left;
  margin-right: 5px;
  margin-bottom: 5px;
  padding: 5px;
  border-radius: 3px;
  color: white;
  background-color: #94adff;
  font-size: 14px;
}
.dropdown {
  padding: 3px;
  border: 1px solid black;
  margin-bottom: 1rem;
  width: 70px;
}
.dropdown > div {
  cursor: pointer;
}
.disabled {
  display: none;
}
@media (max-width: 414px) {
  .article-detail {
    padding: 1rem;
  }
  .article {
    width: 100%;
  }
  .title {
    font-size: 25px;
  }
}
.backBtn {
  border-radius: 3px;
  color: white;
  background-color: #94adff;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 5px;
}
.commentBtn {
  border-radius: 3px;
  color: black;
  background-color: white;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 5px;
}
</style>