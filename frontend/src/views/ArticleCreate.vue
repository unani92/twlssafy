<template>
  <div class="article-create">
    <!-- <h3 style="margin: 1rem 0 1rem 0;">게시글 작성</h3> -->
    <div class="article-title-skills">
      <div>
        <input v-model="content.title" id="title" type="text" placeholder="제목을 입력하세요" />
      </div>
    </div>
    <div class="ispublic">
      <label class="box-radio-input">
        <input type="radio"
          name="cp_item"
          value=1
          v-model="content.ispublic"
          checked="checked"
        >
        <span>전체공개</span>
      </label>
      <label class="box-radio-input">
        <input type="radio"
          name="cp_item"
          value=3
          v-model="content.ispublic"
        >
        <span>비공개</span>
      </label>
      <div>
        <span v-if="content.ispublic === '1'">해당 글은 전체 공개됩니다</span>
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
        v-for="keyword in this.content.keywords"
        :key="keyword"
        :id="keyword"
      >
        <span>{{ keyword }} </span>
        <i @click="removeStack" style="cursor:pointer; margin-left : 5px;" class="fas fa-times"></i>
      </div>
    </div>
    <div class="editor">
      <Editor @submitContent="submitArticle" />
    </div>
  </div>
</template>

<script>
import Editor from "@/components/Editor.vue";
import skills from "../skills";
import { createArticle } from "../api";
import { validateMarkdown } from "@/utils/validation";

export default {
  name: "ArticleCreate",
  components: {
    Editor,
  },
  data() {
    return {
      skillInput: null,
      result: null,
      content: {
        title: null,
        keywords: [],
        content: null,
        ispublic: "1"
      },
    };
  },
  methods: {
    submitAutoComplete() {
      const autocomplete = document.querySelector(".autocomplete");
      if (this.skillInput) {
        autocomplete.classList.remove("disabled");
        this.result = skills.filter((skill) => {
          return skill.match(new RegExp("^" + this.skillInput, "i"));
        });
      } else {
        autocomplete.classList.add("disabled");
      }
    },
    searchSkillAdd(event) {
      const autocomplete = document.querySelector(".autocomplete");
      const skill = event.target.innerText;
      this.content.keywords.push(skill);
      this.skillInput = null;
      autocomplete.classList.toggle("disabled");
    },
    removeStack(event) {
      const selectedSkill = event.target.parentNode.id;
      this.content.keywords = this.content.keywords.filter((skill) => {
        return skill !== selectedSkill;
      });
    },
    submitArticle(data) {
      this.content.content = data;

      var preview = validateMarkdown(data);
      const params = {
        nickname: this.$store.state.nickname,
        title: this.content.title,
        content: this.content.content,
        keyword: this.content.keywords,
        preview: preview,
        ispublic: this.content.ispublic
      };
      const id_token = this.$store.state.id_token;
      createArticle(params, id_token)
        .then((res) => {
          this.$router.push({
            name: "ArticleDetail",
            params: { id: res.data.object.articleId },
          });
        })
        .catch((err) => console.log(err));
    },
  },
};
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
div {
  border-radius: 10px;
}
</style>