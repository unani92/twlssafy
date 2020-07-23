<template>
  <div class="article-detail">
    <div id="viewer"/>
  </div>
</template>

<script>
  import { fetchArticle } from "../api";
  import '@toast-ui/editor/dist/toastui-editor-viewer.css'; // Viewer's Style
  import 'highlight.js/styles/github.css'; // code block highlight 스타일
  import Viewer from '@toast-ui/editor/dist/toastui-editor-viewer';
  import codeSyntaxHightlight from '@toast-ui/editor-plugin-code-syntax-highlight';
  import hljs from 'highlight.js';

  export default {
    name: "ArticleDetail",
    data() {
      return {
        id: this.$route.params.id,
        article: null
      }
    },
    methods: {
      async getArticle() {
        const article = await fetchArticle(this.id)
        return article.data.object.article.content
      },
      getViewer() {
        this.getArticle().then(res => {
          const viewer = new Viewer({
          el: document.querySelector("#viewer"),
          initialValue: res,
          plugins: [[codeSyntaxHightlight, { hljs }]]
        })
        console.log(viewer.initialValue)
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
 #viewer {
   padding-top: 100px;
 }
</style>