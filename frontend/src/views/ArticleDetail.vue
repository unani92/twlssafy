<template>
  <div class="article-detail">
    <div class="left-sidemenu">
      <ArticleDetailSideMenu :article="article" :sideMenu="sideMenu"/>
    </div>
    <div class="article">
      <h1 class="title">{{ title }}</h1>
      <div class="nickname-keyword">
        <div class="username-date">
          <span @click="userToggle"
                style="cursor: pointer">
            {{ nickname }}
          </span>
          <span>{{ updatedAt }}</span>
        </div>
        <div class="dropdown disabled">
          <div>
            <Router-link
              :to="{
                name: 'ArticleUpdate',
                params: { id, keywords, title, content  }
            }">
              수정하기
            </Router-link>
          </div>
          <div @click="removeArticle">
            삭제하기
          </div>
        </div>
        <div class="keywords">
          <span v-for="keyword in keywords"
                :key="keyword"
                class="keyword">
            # {{ keyword }}
          </span>
        </div>
      </div>
      <div class="nickname-keyword">
        <div id="viewer"/>
      </div>
    </div>
  </div>
</template>

<script>
  import { fetchArticle, deleteArticle } from "../api";
  import '@toast-ui/editor/dist/toastui-editor-viewer.css';
  import 'highlight.js/styles/github.css';
  import Viewer from '@toast-ui/editor/dist/toastui-editor-viewer';
  import ArticleDetailSideMenu from "../components/ArticleDetailSideMenu";
  import codeSyntaxHightlight from '@toast-ui/editor-plugin-code-syntax-highlight';
  import hljs from 'highlight.js';

  export default {
    name: "ArticleDetail",
    components: {
      ArticleDetailSideMenu,
    },
    computed: {
      likeList() {
        return this.$store.state.likeList
      },
      pinList() {
        return this.$store.state.pinList
      }
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
        sideMenu: {
          commentList: null,
          cntLikes: null,
          cntPin: null,
        },
      }
    },
    methods: {
      async getArticle() {
        const articleInfo = await fetchArticle(this.id)
        const { article, keyword, commentList, cntLikes, cntPin } = articleInfo.data.object
        this.article = article
        this.keywords = keyword
        this.nickname = article.nickname
        this.title = article.title
        this.content = article.content
        this.updatedAt = this.$moment(article.updatedat).fromNow()
        this.sideMenu.commentList = commentList
        this.sideMenu.cntLikes = cntLikes
        this.sideMenu.cntPin = cntPin

        return article.content
      },
      getViewer() {
        this.getArticle().then(res => {
          new Viewer({
            el: document.querySelector("#viewer"),
            initialValue: res,
            plugins: [[codeSyntaxHightlight, { hljs }]]
          })
        })
      },
      userToggle(event) {
        const dropdown = document.querySelector(".dropdown")
        const loginUser = this.$store.state.nickname
        if (event.target.innerText === loginUser) {
          dropdown.classList.toggle("disabled")
        }
      },
      removeArticle() {
        deleteArticle(this.id)
          .then(() => this.$router.push('/'))
          .catch(err => console.log(err))
      }
    },
    mounted() {

      this.getViewer()
    }
  }
</script>

<style scoped>
  .article-detail{
    display: flex;
  }
  .article {
    padding-top: 100px;
    width: 80%;
  }
  .nickname-keyword {
    padding: 1rem;
    margin-bottom: 1rem;
    background-color: white;
    border-radius: 10px;
  }
  .left-sidemenu {
    padding-top: 100px;
    margin-left: 1rem;
    width: 10%;
    display: block;
  }
  #viewer {
    display: block;
    margin-bottom: 2rem;
  }
  .title {
    font-size: 60px;
    margin-bottom: 1rem;
  }
  .username-date {
    display: flex;
    justify-content: space-between;
    margin-bottom: 1rem;
  }
  .username-date > span:nth-child(1) {
    font-size: 20px;
    font-weight: bold;
  }
  .keyword {
    margin-right: 5px;
    padding: 3px;
    border-radius: 3px;
    color: white;
    background-color: #e6837a;
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
    .left-sidemenu {
      display: none;
    }
    .article {
      width: 100%;

    }
    .title {
      font-size: 40px;
    }
  }
</style>