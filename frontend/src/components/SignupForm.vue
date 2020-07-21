<template>
  <div class="signup-form">
    <h1>환영합니다.</h1>
    <p>회원가입을 하고 배운 지식을 친구들과 공유해보세요</p>
    <form @submit.prevent="submitForm" class="">
      <!-- username(email) 
          0. 형식 검증
          1. 중복 확인
          2. 메일 인증 요청 및 인증 코드 입력
          -->
      <div class="form-input">
        <div class="data-input">
          <label for="email"></label>
          <input
            id="email"
            type="email"
            placeholder="email"
            v-model="email"
            autofocus
            @focusout="emailDoubleCheck"
          />
          <p class="join-warning">{{ logMessage.email }}</p>
          <!-- <button :disabled="!isemailValid">
            이메일 인증 받기
          </button> -->
        </div>
        <div class="data-input">
          <div class="emailValidation-form">
            <label for="validationCode"></label>
            <input
              type="text"
              placeholder="메일로 받은 인증코드"
              :disabled="!confirmedEmail"
              @focusout="checkValidationCode"
              v-model="validationNumber"
            />
            {{ logMessage.validationNumber }}
          </div>
        </div>
        <!-- nickname
        중복확인 -->
        <div class="data-input">
          <label for="nickname"></label>
          <input
            id="nickname"
            type="text"
            v-model="nickname"
            @focusout="nicknameCheck"
            placeholder="nickname"
          />
          <p class="join-warning">{{ logMessage.nickname }}</p>
        </div>
        <!-- password
        형식 검증 -->
        <div class="data-input">
          <label for="password"></label>
          <input
            id="password"
            type="text"
            placeholder="password"
            v-model="password"
          />
          <p class="join-warning">{{ logMessage.password }}</p>
        </div>

        <!-- password confirm OK -->
        <div class="data-input">
          <label for="password comfirm"></label>
          <input
            id="passwordConfirm"
            type="text"
            placeholder="password comfirm"
            v-model="passwordConfirm"
          />
          <p class="join-warning">{{ logMessage.password }}</p>
        </div>
        <!-- 버튼 활성화 OK -->
        <button
          type="submit"
          class="btn data-input"
          :disabled="
            !(checkValidationCode && isSamePassword && nicknameDoubleCheck)
          "
        >
          회원 가입
        </button>
      </div>
    </form>
    <p class="log"></p>
  </div>
</template>

<script>
import { validateEmail, validatePassword } from "@/utils/validation";
import { checkEmail, checkNickname, registerUser } from "@/api/index";

export default {
  data() {
    return {
      email: "",
      validationNumber: "",
      validationNumberFromBE: "",
      nickname: "",
      password: "",
      passwordConfirm: "",
      logMessage: {
        email: "",
        nickname: "",
        validationNumber: "",
        password: "",
        passwordConfirm: "",
      },
      confirmedEmail: false,
      nicknameDoubleCheck: false,
    };
  },
  computed: {
    isemailValid() {
      if (validateEmail(this.email) && this.email) {
        return true;
      } else {
        return false;
      }
    },
    isPasswordValid() {
      if (!this.password) {
        return false;
      }
      return validatePassword(this.password);
    },
    isSamePassword() {
      return this.password === this.passwordConfirm;
    },
  },
  methods: {
    async emailDoubleCheck() {
      if (this.isemailValid) {
        this.logMessage.email = "인증을 완료해주세요";
        const params = {
          username: this.email,
        };
        const res = await checkEmail(params);
        if (res.data.status) {
          this.logMessage.validationNumber =
            "인증번호를 발송했습니다.(유효시간 30분) \n인증번호가 오지 않으면 입력하신 정보가 정확한지 확인하여 주세요. \n이미 가입된 번호이거나, 가상전화번호는 인증번호를 받을 수 없습니다.";

          this.validationNumberFromBE = res.data.object.code;
          this.confirmedEmail = true;
        } else {
          this.logMessage.email = "이미 가입된 메일입니다.";
        }
      } else {
        this.logMessage.email = "유효한 이메일을 입력해주세요";
      }
    },

    checkValidationCode() {
      if (this.validationNumber === this.validationNumberFromBE) {
        this.logMessage.validationNumber = "인증 성공";
        this.logMessage.email = "";
      }
    },

    async nicknameCheck() {
      if (this.nickname === "") {
        this.logMessage.nickname = "닉네임을 입력해주세요";
        return;
      }
      const params = {
        nickname: this.nickname,
      };
      const res = await checkNickname(params);
      if (res.data.status) {
        this.nicknameDoubleCheck = true;
      } else {
        this.usednickname = true;
      }
    },

    async submitForm() {
      const userData = {
        username: this.email,
        password: this.password,
        nickname: this.nickname,
      };
      this.$store.commit("setUsername", userData.username);
      this.$router.push("/selectskills");

      const { data } = await registerUser(userData);
      this.$store.commit("setUsername", data.username);
      // this.$router.push("/selectskills");
    },
    initForm() {
      this.email = "";
      this.password = "";
      this.nickname = "";
    },
  },
};
</script>

<style scoped>
h1 {
  font-size: 45px;
}
input {
  /* margin-top: 2rem; */
  text-align: center;
  font-size: 1rem;
  border: 0;
  outline: 0;
  background: transparent;
  border-bottom: 3px solid black;
  width: 100%;
  margin-top: 1.5rem;
}
button {
  background-color: rgb(204, 93, 65);
  width: 100%;
  border-radius: 3px;
  color: white;
  font-weight: bolder;
  font-size: 1rem;
  border-style: none;
  outline: none;
  height: 50px;
  cursor: pointer;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
.signup-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  margin-left: auto;
  margin-right: auto;
}
.form-input {
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.data-input {
  width: 75%;
}
input:disabled {
  border-bottom: 3px solid lightgrey;
}
.join-warning {
  color: red;
  text-align: start;
}
.btn:disabled {
  background-color: lightgray;
  cursor: not-allowed;
}
p {
  margin: 0;
  padding: 0;
}
@media (min-width: 415px) {
  .socialogin {
    display: flex;
    justify-content: center;
  }
  input {
    width: 100%;
  }
}
</style>
