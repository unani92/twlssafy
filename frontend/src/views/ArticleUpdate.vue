<template>
  <div class="article-create">
    <h1 style="margin: 1rem 0 1rem 0;">게시글 수정</h1>
    <div class="article-title-skills" >
      <div class="title">
        <label style="font-size: 20px; font-weight: bold" for="title">제목</label>
      </div>
      <div>
        <input v-model="article.title" id="title" type="text">
      </div>
    </div>
    <div class="article-title-skills">
      <div>
        <label style="font-size: 20px; font-weight: bold" for="skills">키워드</label>
      </div>
      <div class="keywords">
        <input v-model="skillInput" @input="submitAutoComplete" id="skills" type="text">
        <div class="keyword-badge"
             v-for="keyword in this.article.keywords"
             :key="keyword"
             :id="keyword">
          <span>{{ keyword }}</span>
          <i @click="removeStack" style="cursor:pointer;" class="fas fa-times"></i>
        </div>
      </div>
      <div class="autocomplete disabled">
        <div @click="searchSkillAdd"
             v-for="res in result"
             :key="res"
             style="cursor: pointer">
          {{ res }}</div>
      </div>
    </div>
    <div class="editor">
      <div id="editor"/>
    </div>
    <button id="submitUpdate">Submit</button>
  </div>
</template>


<script>
  import Editor from '@toast-ui/editor';
  import skills from "../skills";
  import { updateArticle } from "../api";
  import codeSyntaxHightlight from "@toast-ui/editor-plugin-code-syntax-highlight";
  import hljs from "highlight.js";
  import { validateMarkdown } from "@/utils/validation";

  export default {
    name: "ArticleUpdate",
    props: {
      id: {
        type: String && Number,
      },
      keywords: {
        type: Array
      },
      title: {
        type: String
      },
      content: {
        type: String
      }
    },
    data() {
      return {
        skillInput: null,
        result: null,
        article: {
          title: this.title,
          keywords: this.keywords,
          content: this.article
        }
      }
    },
    methods: {
      submitAutoComplete() {
        const autocomplete = document.querySelector(".autocomplete")
        if (this.skillInput) {
          autocomplete.classList.remove("disabled")
          this.result = skills.filter(skill => {
            return skill.match(new RegExp("^"+this.skillInput,"i"))
          })
        } else {
          autocomplete.classList.add("disabled")
        }
      },
      searchSkillAdd(event) {
        const autocomplete = document.querySelector(".autocomplete")
        const skill = event.target.innerText
        this.article.keywords.push(skill)
        this.skillInput = null
        autocomplete.classList.toggle("disabled")
      },
      removeStack(event) {
        const selectedSkill = event.target.parentNode.id
        this.article.keywords = this.article.keywords.filter(skill => {
          return skill !== selectedSkill
        })
      },
      submitArticle(data) {
        this.article.content = data
        var preview = validateMarkdown(data)
        const params = {
          articleId: String(this.id),
          title: this.article.title,
          content: this.article.content,
          keyword: this.article.keywords,
          preview: preview
        }
        const id_token = this.$store.state.id_token
        updateArticle(params, id_token)
          .then(res=> {
            console.log(res)
            this.$router.push({name: "ArticleDetail", params: {id:this.id}})
          })
          .catch(err => console.log(err))
        console.log(params)
      },
    },
    mounted() {
      const editor = new Editor({
        el: document.querySelector("#editor"),
        initialEditType: "markdown",
        initialValue: this.content,
        previewStyle: "vertical",
        height: "500px",
        plugins: [[codeSyntaxHightlight, { hljs }]],

      })
      const btn = document.querySelector('#submitUpdate')
      btn.addEventListener('click', () => {
        const editContent = editor.getMarkdown()
        this.submitArticle(editContent)
      })
    }
  }
</script>

<style scoped>
  .article-create {
    padding: 60px 2rem 0 2rem;
  }
  .article-title-skills {
    background-color: white;
    margin-bottom: 1rem;
  }
  input {
    margin-top: 10px;
    width: 200px;
    text-align: center;
    font-size: 1rem;
    border: 0;
    outline: 0;
    background: transparent;
    border-bottom: 3px solid black;
  }
  .autocomplete {
    position: absolute;
    border: 1px solid black;
    width: 300px;
    height: 150px;
    overflow-y: auto;
    background-color: white;
    z-index: 5;
  }
  .disabled {
    display: none;
  }
  .keywords {
    display: flex;
    margin-bottom: 1rem;
  }
  .keyword-badge {
    color: white;
    font-weight: bold;
    background-color: black;
    font-size: small;
    width: 60px;
    height: 20px;
    border-radius: 6px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    margin: 6px;
  }
  .article-title-skills {
    padding: 10px;
  }
  .title {
    margin-top: 1rem;
  }
  button {
    margin: 1rem 0 1rem 0;
    width: 100px;
    height: 30px;
    border-radius: 3px;
    border-style: none;
    cursor: pointer;
    background-color: #e6837a;
    color: white;
  }
</style>