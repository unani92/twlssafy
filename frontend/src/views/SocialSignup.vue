<template>
  <div class="wrapper">
    <h1 class="title">환영합니다.</h1>
      <div class="form">
        <div class="inputfield logo" style></div>
        <div style="text-align: center;">회원가입을 하고 배운 지식을 친구들과 공유해보세요</div>
        <p class="join-warning guide-text">{{ logMessage.email }}</p>
        <div class="inputfield">
          <input v-model="email" class="input" type="email" readonly/>
        </div>
        <!-- nickname
        중복확인-->
        <p class="join-warning guide-text">{{ logMessage.nickname }}</p>
        <div class="inputfield">
          <input
            class="input"
            id="nickname"
            type="text"
            v-model="nickname"
            @focusout="nicknameCheck"
            placeholder="nickname"
          />
        </div>

        <div class="inputfield">
          <input class="input" id="info" type="text" placeholder="한 줄 소개 부탁해요" v-model="info" />
        </div>
        <p class="join-warning guide-text">{{ logMessage.info }}</p>
        <!-- 버튼 활성화 OK -->
        <div class="inputfield">
          <button
            type="submit"
            class="btn"
            :disabled="
              !(
                nicknameDoubleCheck
              )
            "
          >회원 가입</button>
        </div>
      </div>
    <p class="log"></p>
  </div>
</template>

<script>

import { checkNickname, registerUser } from "@/api/index";
// import SelectSkills from "../views/SelectSkills.vue";
// import { mapState, mapMutations } from 'vuex'

export default {
  data() {
    return {
      nickname: "",
      info: "",
      logMessage: {
        nickname: "",
      },
      nicknameDoubleCheck: false,
    };
  },
  props: {
    email: String
  },
  methods: {
    async nicknameCheck() {
      if (this.nickname === "") {
        this.logMessage.nickname = "닉네임을 입력해주세요";
        return;
      }
      const params = {
        nickname: this.nickname,
      };
      const res = await checkNickname(params);
      console.log(res.data);
      if (res.data.status) {
        this.nicknameDoubleCheck = true;
      } else {
        alert("이미 있는 닉네임")
      }
      this.logMessage.nickname = res.data.data;
    },

    async submitForm() {
      const userData = {
        email: this.email,
        nickname: this.nickname,
        info: this.info,
      };

      const { data } = await registerUser(userData);
      console.log(data);
      this.$store.commit("setUsername", userData.email);
      this.$store.commit("setNickname", userData.nickname);
      this.$router.push("/selectskills");
    },
    initForm() {
      this.email = "";
      this.password = "";
      this.nickname = "";
    },
  },
  directives: {
    focus: {
      // directive definition
      inserted: function (el) {
        el.focus();
      },
    },
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css?family=Montserrat:400,700&display=swap");

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Montserrat", sans-serif;
}
body {
  background: #e6837a;
  padding: 0 10px;
}
.wrapper {
  max-width: 500px;
  width: 100%;
  background: #fff;
  margin: 20px auto;
  box-shadow: 1px 1px 2px rgba(0, 0, 0, 0.125);
  padding: 30px;
}

.wrapper .title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 25px;
  color: #e6837a;
  text-transform: uppercase;
  text-align: center;
}

.wrapper .form {
  width: 100%;
}

.wrapper .form .inputfield {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.wrapper .form .inputfield label {
  width: 200px;
  color: #757575;
  margin-right: 10px;
  font-size: 14px;
}

.wrapper .form .inputfield .input,
.wrapper .form .inputfield .textarea {
  width: 100%;
  outline: none;
  border: 1px solid #d5dbd9;
  font-size: 15px;
  padding: 8px 10px;
  border-radius: 3px;
  transition: all 0.3s ease;
}

.wrapper .form .inputfield .textarea {
  width: 100%;
  height: 125px;
  resize: none;
}

.wrapper .form .inputfield .custom_select {
  position: relative;
  width: 100%;
  height: 37px;
}

.wrapper .form .inputfield .custom_select:before {
  content: "";
  position: absolute;
  top: 12px;
  right: 10px;
  border: 8px solid;
  border-color: #d5dbd9 transparent transparent transparent;
  pointer-events: none;
}

.wrapper .form .inputfield .custom_select select {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  outline: none;
  width: 100%;
  height: 100%;
  border: 0px;
  padding: 8px 10px;
  font-size: 15px;
  border: 1px solid #d5dbd9;
  border-radius: 3px;
}

.wrapper .form .inputfield .input:focus,
.wrapper .form .inputfield .textarea:focus,
.wrapper .form .inputfield .custom_select select:focus {
  border: 1px solid #e6837a;
}

.wrapper .form .inputfield p {
  font-size: 14px;
  color: #757575;
}
.wrapper .form .inputfield .check {
  width: 15px;
  height: 15px;
  position: relative;
  display: block;
  cursor: pointer;
}
.wrapper .form .inputfield .check input[type="checkbox"] {
  position: absolute;
  top: 0;
  left: 0;
  opacity: 0;
}
.wrapper .form .inputfield .check .checkmark {
  width: 15px;
  height: 15px;
  border: 1px solid #e6837a;
  display: block;
  position: relative;
}
.wrapper .form .inputfield .check .checkmark:before {
  content: "";
  position: absolute;
  top: 1px;
  left: 2px;
  width: 5px;
  height: 2px;
  border: 2px solid;
  border-color: transparent transparent #fff #fff;
  transform: rotate(-45deg);
  display: none;
}
.wrapper .form .inputfield .check input[type="checkbox"]:checked ~ .checkmark {
  background: #e6837a;
}

.wrapper
  .form
  .inputfield
  .check
  input[type="checkbox"]:checked
  ~ .checkmark:before {
  display: block;
}

.wrapper .form .inputfield .btn {
  width: 100%;
  padding: 8px 10px;
  font-size: 15px;
  border: 0px;
  background: #e6837a;
  color: #fff;
  cursor: pointer;
  border-radius: 3px;
  outline: none;
}

.wrapper .form .inputfield .btn:hover {
  background: #e6837a;
}

.wrapper .form .inputfield:last-child {
  margin-bottom: 0;
}
.join-warning {
  color: red;
}
.guide-text {
  font-size: 0.75rem;
}
@media (max-width: 420px) {
  .wrapper .form .inputfield {
    flex-direction: column;
    align-items: flex-start;
  }
  .wrapper .form .inputfield label {
    margin-bottom: 5px;
  }
  .wrapper .form .inputfield.terms {
    flex-direction: row;
  }
}
</style>