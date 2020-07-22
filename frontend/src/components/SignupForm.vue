<template>
  <div class="signup-form">
    <h1>환영합니다.</h1>
    <p>회원가입을 하고 배운 지식을 친구들과 공유해보세요</p>
    <form @submit.prevent="submitForm" class="">
      <!-- email(email) 
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
            @focusout="emailDoubleCheck"
          />
          <p class="join-warning guide-text">{{ logMessage.email }}</p>
          <!-- <button :disabled="!isemailValid">
            이메일 인증 받기
          </button> -->
        </div>
        <div v-if="confirmedEmail" class="data-input">
          <div class="emailValidation-form">
            <label for="validationCode"></label>
            <input
              type="text"
              v-focus
              ref="validationCode"
              placeholder="메일로 받은 인증코드"
              @focusout="checkValidationCode"
              v-model="validationNumber"
            />
            <p class="guide-text">{{ logMessage.validationNumber }}</p>
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
          <p class="join-warning guide-text">{{ logMessage.nickname }}</p>
        </div>
        <!-- password
        형식 검증 -->
        <div class="data-input">
          <label for="password"></label>
          <input
            id="password"
            type="password"
            placeholder="password"
            v-model="password"
            @focusout="checkPasswordForm"
          />
          <p class="join-warning guide-text">{{ logMessage.password }}</p>
        </div>

        <!-- password confirm OK -->
        <div class="data-input">
          <label for="password comfirm"></label>
          <input
            id="passwordConfirm"
            type="password"
            placeholder="password comfirm"
            v-model="passwordConfirm"
            @focusout="checkPasswordConfirm"
          />
          <p class="join-warning guide-text">
            {{ logMessage.passwordConfirm }}
          </p>
        </div>

        <div class="data-input">
          <label for="info"></label>
          <input
            id="info"
            type="text"
            placeholder="한 줄 소개 부탁해요"
            v-model="info"
          />
          <p class="join-warning guide-text">
            {{ logMessage.info }}
          </p>
        </div>
        <!-- 버튼 활성화 OK -->
        <button
          type="submit"
          class="btn data-input"
          :disabled="
            !(
              checkValidationCode &&
              isSamePassword &&
              nicknameDoubleCheck &&
              isPasswordValid
            )
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
      info: "",
      logMessage: {
        email: "",
        nickname: "",
        validationNumber: "",
        password: "",
        passwordConfirm: "",
        info: "",
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
      return this.password && this.password === this.passwordConfirm;
    },
  },
  methods: {
    async emailDoubleCheck() {
      if (this.isemailValid) {
        this.logMessage.email = "인증을 완료해주세요";
        const params = {
          email: this.email,
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
    checkPasswordForm() {
      if (!this.isPasswordValid) {
        this.logMessage.password =
          "영문자 대문자, 소문자, 특수문자, 숫자를 하나이상 포함한 8자";
      } else {
        this.logMessage.password = "";
      }
    },
    checkPasswordConfirm() {
      if (!this.isSamePassword) {
        this.logMessage.passwordConfirm = "일치하지 않습니다";
      } else {
        this.logMessage.passwordConfirm = "";
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
      console.log(res.data);
      if (res.data.status) {
        this.nicknameDoubleCheck = true;
      } else {
        this.usednickname = true;
      }
      this.logMessage.nickname = res.data.data;
    },

    async submitForm() {
      const userData = {
        email: this.email,
        password: this.password,
        nickname: this.nickname,
        info: this.info,
      };

      const { data } = await registerUser(userData);
      console.log(data);
      this.$store.commit("setUsername", userData.email);
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
      inserted: function(el) {
        el.focus();
      },
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
.guide-text {
  font-size: 0.75rem;
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
