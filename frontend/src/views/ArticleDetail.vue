<template>
  <div class="article-detail" style="height: 10000px;">
    <div class="left-sidemenu">
      <ArticleDetailSideMenu/>
    </div>
    <div class="article">
      <h1 class="title">{{ title }}</h1>
      <div class="username-date">
        <span>{{ nickname }}</span>
        <span>{{ updatedAt }}</span>
      </div>
      <div class="keywords">
        <span v-for="keyword in keywords"
              :key="keyword"
              class="keyword">
          # {{ keyword }}
        </span>
      </div>
      <div id="viewer"/>
    </div>
  </div>
</template>

<script>
  import { fetchArticle } from "../api";
  import '@toast-ui/editor/dist/toastui-editor-viewer.css'; // Viewer's Style
  import 'highlight.js/styles/github.css'; // code block highlight 스타일
  import Viewer from '@toast-ui/editor/dist/toastui-editor-viewer';
  import ArticleDetailSideMenu from "../components/ArticleDetailSideMenu";
  import codeSyntaxHightlight from '@toast-ui/editor-plugin-code-syntax-highlight';
  import hljs from 'highlight.js';

  export default {
    name: "ArticleDetail",
    components: {
      ArticleDetailSideMenu,
    },
    data() {
      return {
        id: this.$route.params.id,
        keywords: null,
        nickname: null,
        title: null,
        article: null,
        updatedAt: null,
      }
    },
    methods: {
      async getArticle() {
        const articleInfo = await fetchArticle(this.id)
        const { article, keyword } = articleInfo.data.object
        this.keywords = keyword
        this.nickname = article.nickname
        this.title = article.title
        this.article = article.content
        this.updatedAt = article.updatedat
        console.log(articleInfo)
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
      }
    },
    created() {
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
  .left-sidemenu {
    padding-top: 100px;
    margin-left: 1rem;
    width: 10%;
    display: block;
  }
  #viewer {
    display: block;
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
</style>