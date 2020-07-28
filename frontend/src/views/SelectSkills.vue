<template>
  <div class="selectskills">
    <h1>관심사를 선택해주세요</h1>
    <div class="selectskills-main">
      <h2>Hot skills</h2>
      <div class="hotskills">
        <div v-for="skill in hotSkills" :key="skill" :id="skill" class="skill-badge">
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
        >{{ res }}</div>
      </div>
    </div>
    <div v-if="userSkill.length" class="selectskills-selected">
      <h2>Selected</h2>
      <div class="hotskills">
        <div v-for="skill in userSkill" :key="skill" :id="skill" class="skill-badge">
          <span>{{ skill }}</span>
          <i @click="removeStack" style="cursor:pointer;" class="fas fa-times"></i>
        </div>
      </div>
    </div>
    <div class="btn">
      <button @click="submitUserSkills" class="signup-btn">선택완료</button>
      <!-- <button @click="$router.push('/')" class="signup-btn">다음에 등록</button> -->
    </div>
  </div>
</template>

<script>
import skills from "../skills.js";
import { selectSkill } from "../api";

export default {
  name: "SelectSkills",
  data() {
    return {
      hotSkills: ["Python", "Java", "Javascript", "C", "C++"],
      skillInput: null,
      result: null,
      userSkill: [],
    };
  },
  methods: {
    submitAutoComplete() {
      const autocomplete = document.querySelector(".autocomplete");
      if (this.skillInput) {
        autocomplete.classList.remove("disabled");
        this.result = skills.filter((skill) => {
          return skill.match(new RegExp(this.skillInput, "i"));
        });
      } else {
        autocomplete.classList.add("disabled");
      }
    },
    addStack(event) {
      const skill = event.target.parentNode.id;
      if (!this.userSkill.includes(skill)) {
        this.userSkill.push(skill);
      }
    },
    removeStack(event) {
      const selectedSkill = event.target.parentNode.id;
      this.userSkill = this.userSkill.filter((skill) => {
        return skill !== selectedSkill;
      });
    },
    searchSkillAdd(event) {
      const autocomplete = document.querySelector(".autocomplete");
      const skill = event.target.innerText;
      this.userSkill.push(skill);
      this.skillInput = null;
      autocomplete.classList.toggle("disabled");
    },
    submitUserSkills() {
      const params = {
        email: this.$store.state.username,
        skill: this.userSkill,
      };
      selectSkill(params)
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
</style>