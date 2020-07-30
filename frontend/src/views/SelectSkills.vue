<template>
  <div class="selectskills">
    <h1>관심사를 선택해주세요</h1>
    <div class="selectskills-main">
      <h2>Hot skills</h2>
      <div class="hotskills">
        <div
          v-for="skill in hotSkills"
          :key="skill"
          :id="skill"
          class="skill-badge"
        >
          <span>{{ skill }}</span>
          <i @click="addStack" style="cursor:pointer;" class="fas fa-plus"></i>
        </div>
      </div>
    </div>
    <div class="selectskills-search">
      <input v-model="skillInput" @input="submitAutoComplete" type="text" />
      <div class="autocomplete disabled">
        <div
          @click="searchSkillAdd"
          style="cursor: pointer"
          v-for="res in result"
          :key="res"
        >
          {{ res }}
        </div>
      </div>
    </div>
    <div v-if="userSkill.length" class="selectskills-selected">
      <h2>Selected</h2>
      <div class="hotskills">
        <div
          v-for="skill in userSkill"
          :key="skill"
          :id="skill"
          class="totalSkills"
        >
          <span>{{ skill }}</span>
          <i
            @click="removeStack"
            style="cursor:pointer;"
            class="fas fa-times"
          ></i>
        </div>
      </div>
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
      hotSkills: ["Java", "Python", "C++", "Javascript"],
      skillInput: null,
      result: null,
      userSkill: this.$store.state.interestList,
    };
  },
  methods: {
    //
    submitAutoComplete() {
      const autocomplete = document.querySelector(".autocomplete");
      if (this.skillInput) {
        autocomplete.classList.remove("disabled");
        this.result = skills.filter((skill) => {
          return skill.match(new RegExp("^"+this.skillInput, "i"));
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
      console.log(res.data.data);
      if (res.data.data === "success") {
        const updatedUserSkill = this.userSkill;
        const len = updatedUserSkill.length;
        for (let i = 0; i < len; i++) {
          if (selectedSkill === updatedUserSkill[i]) {
            // console.log("selectedSkill");
            updatedUserSkill.splice(i, 1);
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
        updatedUserSkill.push(skill);
        this.$store.commit("setInterestList", updatedUserSkill);
      }
      this.skillInput = null;
      autocomplete.classList.toggle("disabled");
      console.log(this.$store.state.interestList);
    },
    //편집 결과를 반영
    submitUserSkills() {
      const params = {
        email: this.$store.state.username,
        skill: this.userSkill,
      };
      console.log(params);
      selectSkills(params)
        .then((res) => console.log(res))
        .catch((err) => console.log(err));
    },
  },
};
</script>

<style scoped>
.selectskills {
  padding-top: 50px;
}
h1 {
  font-size: 40px;
  text-align: center;
}
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
  border-radius: 3px;
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
  background-color: black;
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
.btn {
  max-width: 530px;
  margin-top: 1rem;
  margin-bottom: 1rem;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  justify-content: center;
  align-items: center;
}
.signup-btn {
  background-color: rgb(204, 93, 65);
  border-radius: 3px;
  color: white;
  font-weight: bolder;
  font-size: 1rem;
  border-style: none;
  outline: none;
  height: 50px;
  cursor: pointer;
  margin: 1rem 10px 1rem 0;
  width: 530px;
}
@media (max-width: 414px) {
  .signup-btn {
    width: 250px;
  }
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
</style>
