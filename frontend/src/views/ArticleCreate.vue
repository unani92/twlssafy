<template>
  <div class="article-create">
    <h1 style="margin: 1rem 0 1rem 0;">게시글 작성</h1>
    <div class="article-title-skills" >
      <div class="title" style="margin-bottom:32px;">
        <label style="font-size: 20px; font-weight: bold; margin-right:20px" for="title">제목 </label>
        <input v-model="content.title" id="title" type="text">
      </div>
    </div>
    <div class="article-title-skills">
      <div>
        <label style="font-size: 20px; font-weight: bold; margin-right:20px" for="skills">키워드</label>
        <input v-model="skillInput" @input="submitAutoComplete" id="skills" type="text" placeholder="기술을 검색하세요">
        <div class="autocomplete disabled">
          <div @click="searchSkillAdd"
              v-for="res in result"
              :key="res"
              style="cursor: pointer; margin-bottom:6px">
            {{ res }}</div>
        </div>
      </div>
    </div>
      <div class = "keywords">
        <div class="keyword-badge"
             v-for="keyword in this.content.keywords"
             :key="keyword"
             :id="keyword">
          <span>{{ keyword }}</span>
          <i @click="removeStack" style="cursor:pointer; margin-left : 5px;" class="fas fa-times"></i>
        </div>
      </div>
    <div class="editor">
      <Editor @submitContent="submitArticle"/>
    </div>
  </div>
</template>

<script>
  import Editor from '@/components/Editor.vue'
  import skills from "../skills";
  import { createArticle } from "../api";
  import { validateMarkdown } from "@/utils/validation";

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
            return skill.match(new RegExp("^"+this.skillInput,"i"))
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
      submitArticle(data) {
        this.content.content = data
        
        var preview = validateMarkdown(data);
        console.log(preview);
        const params = {
          nickname: this.$store.state.nickname,
          title: this.content.title,
          content: this.content.content,
          keyword: this.content.keywords,
          preview: preview
        }
        const id_token = this.$store.state.id_token
        createArticle(params,id_token)
          .then(res=> {
            this.$router.push({name: "ArticleDetail", params: {id:res.data.object}})
          })
          .catch(err => console.log(err))
        // console.log(this.content.content)
      },
    },
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
    border: 1.5px solid black;
    border-radius: 5px;
    width: 300px;
    height: 150px;
    overflow-y: auto;
    background-color: white;
    z-index: 5;
    padding: 1%;
  }
  .disabled {
    display: none;
  }
  .keyword-badge {
    color: white;
    font-weight: bold;
    background-color: black;
    font-size: small;
    border-radius: 6px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    margin-bottom: 1rem;
    margin-right : 1rem;
    padding: 5px;
    float: left;
  }
  .article-title-skills {
    padding: 10px;
  }
  .title {
    margin-top: 1rem;
  }
  .editor{
    clear:both;
  }
</style>