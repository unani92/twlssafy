<template>
  <div class="article-create">
    <h1 style="margin: 1rem 0 1rem 0;">게시글 작성</h1>
    <div class="article-title-skills" >
      <div class="title">
        <label style="font-size: 20px; font-weight: bold" for="title">제목</label>
      </div>
      <div>
        <input v-model="content.title" id="title" type="text">
      </div>
    </div>
    <div class="article-title-skills">
      <div>
        <label style="font-size: 20px; font-weight: bold" for="skills">키워드</label>
      </div>
      <div class="keywords">
        <input v-model="skillInput" @input="submitAutoComplete" id="skills" type="text">
        <div class="keyword-badge"
             v-for="keyword in this.content.keywords"
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
      <Editor/>
    </div>
    <button @click="submitArticle">Submit</button>
  </div>
</template>

<script>
  import Editor from '@/components/Editor.vue'
  import skills from "../skills";
  import { createArticle } from "../api";

  export default {
    name: "ArticleCreate",
    components: {
      Editor
    },
    data() {
      return {
        skillInput: null,
        result: null,
        content: {
          title: null,
          keywords: [],
          content: null
        }
      }
    },
    methods: {
      submitAutoComplete() {
        const autocomplete = document.querySelector(".autocomplete")
        if (this.skillInput) {
          autocomplete.classList.remove("disabled")
          this.result = skills.filter(skill => {
            return skill.match(new RegExp(this.skillInput,"i"))
          })
        } else {
          autocomplete.classList.add("disabled")
        }
      },
      searchSkillAdd(event) {
        const autocomplete = document.querySelector(".autocomplete")
        const skill = event.target.innerText
        this.content.keywords.push(skill)
        this.skillInput = null
        autocomplete.classList.toggle("disabled")
      },
      removeStack(event) {
        const selectedSkill = event.target.parentNode.id
        this.content.keywords = this.content.keywords.filter(skill => {
          return skill !== selectedSkill
        })
      },
      submitArticle() {
        const codeMirror = document.querySelector(".CodeMirror-code")
        this.content.content = codeMirror.innerText
        const params = {
          email: this.$store.state.username,
          nickname: this.$store.state.nickname,
          title: this.content.title,
          content: this.content.content,
          keyword: this.content.keywords
        }
        createArticle(params)
          .then(res=> {
            console.log(res.data)
            this.$router.push('/')
          })
          .catch(err => console.log(err))
        console.log(params)
      },
    },
    updated() {
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