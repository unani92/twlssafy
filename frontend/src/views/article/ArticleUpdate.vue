<template>
  <div class="article-create">
    <!-- <h1 style="margin: 1rem 0 1rem 0;">게시글 수정</h1> -->

    <div class="article-title-skills">
      <div>
        <label style="padding-left : 5px;">제목</label>
        <input v-model="article.title" id="title" type="text">
      </div>
    </div>

    <div class="ispublic">
      <label class="box-radio-input">
        <input type="radio"
          name="cp_item"
          value=1
          v-model="ispublic"
        >
        <span>전체공개</span>
      </label>
      <label class="box-radio-input">
        <input type="radio"
          name="cp_item"
          value=3
          v-model="ispublic"
        >
        <span>비공개</span>
      </label>
      <div>
        <span v-if="ispublic === '1'">해당 글은 전체 공개됩니다</span>
        <span v-else>해당 글은 마이페이지에서 조회 가능합니다.</span>
      </div>
    </div>

   <div class="article-title-skills">
      <div>
        <input
          class="skills"
          v-model="skillInput"
          @input="submitAutoComplete"
          id="skills"
          type="text"
          placeholder="기술을 검색하세요"
        />
        <div class="autocomplete disabled">
          <div
            @click="searchSkillAdd"
            v-for="res in result"
            :key="res"
            style="cursor: pointer; margin-bottom:6px;"
          >{{ res }}</div>
        </div>
      </div>
    </div>
    <div class="keywords">
      <div
        class="keyword-badge"
        v-for="keyword in this.article.keywords"
        :key="keyword"
        :id="keyword"
      >
        <span>{{ keyword }} </span>
        <i @click="removeStack" style="cursor:pointer; margin-left : 5px;" class="fas fa-times"></i>
      </div>
    </div>
    <div class="editor">
      <div class="upload-progress">
        <div class="upload-percent" :style="`width:${uploadValue}%;`"></div>
      </div>
      <div id="editor"/>
    </div>
    <button id="submitUpdate">Update</button>
  </div>
</template>


<script>
  import Editor from '@toast-ui/editor';
  import skills from "../../skills";
  import { updateArticle } from "../../api";
  import codeSyntaxHightlight from "@toast-ui/editor-plugin-code-syntax-highlight";
  import hljs from "highlight.js";
  import { validateMarkdown } from "@/utils/validation";
  import firebase from 'firebase'

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
      },
      ispublic : {
        type : Number
      }
    },
    data() {
      return {
        skillInput: null,
        result: null,
        article: {
          title: this.title,
          keywords: this.keywords,
          content: this.article,
          ispublic: this.ispublic,
        },
        imageData: null,
        picture: null,
        uploadValue: 0
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
          preview: preview,
          ispublic : this.ispublic,
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
        hooks: {
          addImageBlobHook: (blob, callback) => {
            const date = new Date()
            this.imageData = blob
            const storageRef = firebase.storage().ref(`${this.imageData.name}_${date.getTime()}`).put(this.imageData);
            storageRef.on(`state_changed`, snapshot => {
              this.uploadValue = (snapshot.bytesTransferred/snapshot.totalBytes)*100;
            },
              error => {console.log(error.message)},
              () => {
                this.uploadValue = 0
                storageRef.snapshot.ref.getDownloadURL()
                  .then(url => callback(url, `${this.imageData.name}_${date.getTime()}`))

              }
            )
          }
        }
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
  padding: 140px 2rem 0 2rem;
}
.article-title-skills {
  background-color: white;
  margin-bottom: 1rem;
}
#title {
  width: 100%;
  height: 40px;
  background: transparent;
  border: 0;
  outline: 0;
  font-size: 30px;
  padding : 5px;
}
.box-radio-input input[type="radio"]{
    display:none;
}
.box-radio-input input[type="radio"] + span{
    display:inline-block;
    background:none;
    border:1px solid #dfdfdf;
    padding:0px 10px;
    text-align:center;
    height:35px;
    line-height:33px;
    font-weight:500;
    cursor:pointer;
}
.box-radio-input input[type="radio"]:checked + span{
    border:1px solid #e6837a;
    background: #e6837a;
    border-radius: 4px;
    color:#fff;
}
.ispublic {
  margin-top: 1rem;
  margin-bottom: 1rem;
  width: 95%;
  margin-left: 2.5%;
  background-color: white;
  padding: 1rem;
}
.skills {
  margin-top: 10px;
  width: 200px;
  text-align: center;
  border: 0;
  outline: 0;
  background: transparent;
  border-bottom: 3px solid black;
  padding : 10px;

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
  /* margin-right: 1rem; */
  padding: 8px;
  float: left;
  margin-left : 2.5%;
}
.article-title-skills {
  padding: 10px;
  width : 95%;
  margin-left: 2.5%;
}
.editor {
  width : 95%;
  margin-left: 2.5%;
  clear: both;
}
.upload-progress {
  width: 100%;
  height: 20px;
  }
.upload-percent {
  background-color: #e6837a;
  height: 20px;
}
div {
  border-radius: 10px;
}
#submitUpdate{

  width : 100px;
  margin : 25px 0 25px 2.5%;
  background-color: pink;
}
</style>