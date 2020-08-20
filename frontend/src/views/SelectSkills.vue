<template>
  <div class="selectskills">
    <div class="selectskills-search">
      <h2 style="margin : 20px;">관심 있는 기술을 검색하여 등록해보세요 !</h2>
      <!-- <h4 style="margin-top : 30px;">기술 검색</h4> -->
      <i class="fas fa-search">
        <input v-model="skillInput" @input="submitAutoComplete" type="text" style="margin-bottom : 15px;" />
      </i>
      <div class="autocomplete disabled">
        <div
          @click="searchSkillAdd"
          style="cursor: pointer"
          v-for="(res,i) in result"
          :key="i"
        >{{ res }}</div>
      </div>
    </div>
    <div v-if="userSkill.length" class="selectskills-selected">
      <h2>Selected</h2>
      <div class="hotskills">
        <div v-for="(skill,i) in userSkill" :key="i" :id="skill.name" class="totalSkills">
          <span>{{ skill.name }}</span>
          <i @click="removeStack" style="cursor:pointer;" class="fas fa-times"></i>
        </div>
      </div>
      <button v-if="nowSignUp" @click="$router.push('/')" class="complete-signup">가입완료</button>
    </div>
  </div>
</template>

<script>
import skills from "../skills.js";
import { selectSkills, deleteSkill } from "../api";

export default {
  name: "SelectSkills",
  data() {
    return {
      path: this.$route.path,
      hotSkills: ['C','Java','AI','Vue.js','Python'],
      skillInput: null,
      result: null,
      userSkill: this.$store.state.userSkills,
    };
  },
  computed: {
    nowSignUp() {
      return this.path === "/selectskills";
    },
  },
  methods: {
    //검색
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
    // hotskill에서 더하기 요청
    addStack(event) {
      const skill = event.target.parentNode.id;
      if (!this.userSkill.includes(skill)) {
        this.userSkill.push(skill);
      }
    },
    //selected 영역에서 삭제(BE, FE 모두)
    async removeStack(event) {
      const selectedSkill = event.target.parentNode.id;
      const params = {
        skill: selectedSkill,
      };
      const id_token = this.$store.state.id_token;
      const res = await deleteSkill(params, id_token);
      if (res.data.data === "success") {
        const updatedUserSkill = this.userSkill;
        const len = updatedUserSkill.length;
        for (let i = 0; i < len; i++) {
          if (selectedSkill === updatedUserSkill[i].name) {
            updatedUserSkill.splice(i, 1);
            this.$store.commit("setUserSkills", updatedUserSkill);
            this.$store.commit("setInterestList", updatedUserSkill);
            break;
          }
        }
      }
    },
    // 검색 결과에서 추가
    async searchSkillAdd(event) {
      const autocomplete = document.querySelector(".autocomplete");
      const skill = event.target.innerText;
      const params = {
        skill: [skill],
      };
      const id_token = this.$store.state.id_token;
      const res = await selectSkills(params, id_token);
      const updatedUserSkill = this.userSkill;
      if (res.data.data === "success") {
        const addedSkill = {
          sno: res.data.object[0].sno,
          name: skill,
        };
        updatedUserSkill.push(addedSkill);
        this.$store.commit("setUserSkills", updatedUserSkill);
        this.$store.commit("setInterestList", updatedUserSkill);
      }
      this.skillInput = null;
      autocomplete.classList.toggle("disabled");
    },
    //편집 결과를 반영
    submitUserSkills() {
      const params = {
        email: this.$store.state.username,
        skill: this.userSkill,
      };
      selectSkills(params)
    },
  },
};
</script>

<style scoped>
.selectskills {
  padding-top: 135px;
}
/* h1 {
  font-size: 40px;
  text-align: center;
} */
input {
  margin-top: 2rem;
  text-align: center;
  font-size: 1rem;
  border: 0;
  outline: 0;
  background: transparent;
  border-bottom: 3px solid black;
}
.selectskills-search,
.selectskills-main,
.selectskills-selected {
  max-width: 530px;
  margin-top: 1rem;
  margin-bottom: 1rem;
  margin-left: auto;
  margin-right: auto;
  padding-bottom: 1rem;
  border-radius: 10px;
  background-color: white;
  text-align: left;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.autocomplete {
  border: 1px solid black;
  width: 300px;
  height: 150px;
  overflow-y: auto;
}
.skill-badge {
  color: white;
  font-weight: bold;
  background-color: rgba(77, 127, 219, 0.815);
  width: 100px;
  height: 40px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin: 6px;
}
.hotskills {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-flow: row wrap;
}
.disabled {
  display: none;
}
.totalSkills {
  display: inline-block;
  border: 1px solid #9fa3af;
  padding: 6px 15px;
  border-radius: 25px;
  min-width: 65px;
  font-weight: 600;
  margin: 0.5rem 0.25rem;
  text-align: center;
}
.complete-signup {
  width: 50%;
  margin: 5px;
  border-style: none;
  background-color: #0095f6;
  font-size: 1rem;
  padding: 5px;
  border-radius: 5px;
  color: white;
}
.complete-signup:hover {
  box-shadow: 0px 1px 5px #0095f6;
  color: #fff;
  /* transform: translateY(-7px); */
  animation-duration: 1s ease-in;
}
</style>
