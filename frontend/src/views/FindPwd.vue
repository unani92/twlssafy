<template>
  <!-- 로그인 하지 않은 상태에서 비밀번호를 찾는 컴포넌트 -->
  <!-- 이메일 입력 후 가입된 이메일인지 확인하고 가입되어 있다면 검증메일 발송 후 확인하고 비밀번호를 변경할 수 있는 컴포넌트(ChangePwd)로 이동 -->
  <div class="form">
    <div>
      <h1 id="title">
        가입시 등록한
        <br />이메일을
        <br />입력해 주세요
      </h1>
      <br />

      <input
        @focusout="emailDoubleCheck"
        v-model="email"
        type="email"
        id="emailInput"
        placeholder="가입 시 입력한 이메일을 입력해주세요"
      />

      <div class="validation">
        <div style="margin-bottom:1rem;">
          <div class="emailValidation-form inputfield">
            <input
              class="valInput"
              type="text"
              v-focus
              ref="validationCode"
              placeholder="인증코드를 입력해주세요"
              @focusout="checkValidationCode"
              v-model="validationNumber"
            />
          </div>
        </div>
        <p class="guide-text">{{ logMessage.validationNumber }}</p>
      </div>

      <div class="btns">
        <button class="btn" @click="goback" style="left : 35%; background-color:#b3b3b3;">취소</button>
        <button
          class="btn"
          @click="changePwd"
          style="background-color: royalblue; left : 50%"
          :disabled="confirmedEmail==false"
        >다음</button>
      </div>
    </div>
  </div>
</template>

<script>
import { submitValidationMail } from "../api";

export default {
  name: "ChangePwd",
  data() {
    return {
      email: "",
      validationNumber: "",
      validationNumberFromBE: "not yet",
      info: "",
      logMessage: {
        validationNumber: "",
      },
      confirmedEmail: false,
    };
  },

  methods: {
    async emailDoubleCheck() {
      const email = this.email;
      submitValidationMail(email).then((res) => {
        if (res.data.status) {
          this.logMessage.validationNumber = "인증번호를 발송했습니다.";

          this.validationNumberFromBE = res.data.object.code;
        } else {
          this.logMessage.validationNumber =
            "\n인증번호가 오지 않으면 입력하신 정보가 정확한지 확인하여 주세요.";
        }
      });
    },
    checkValidationCode() {
      if (this.validationNumber === this.validationNumberFromBE) {
        this.logMessage.validationNumber =
          "인증 성공 !\n다음 버튼을 눌러 비밀번호를 변경하세요";
        this.confirmedEmail = true;

        this.logMessage.email = "";
      } else {
        this.logMessage.validationNumber = "인증번호를 확인해주세요";
        this.confirmedEmail = false;
      }
    },
    goback() {
      this.$router.go(-1);
    },
    async changePwd() {
      this.$router.push({ name: "ChangePwd", params: { email: this.email } });
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
#emailInput {
  position: fixed;
  top: 50%;
  left: 25%;
  height: 35px;
  width: 50%;
  border-radius: 6px;
  border: solid 1.5px #b4b4b4;
  margin-bottom: 10%;
  padding: 1%;
}
.valInput {
  position: fixed;
  left: 25%;
  height: 35px;
  width: 50%;
  border-radius: 6px;
  border: solid 1.5px #b4b4b4;
  /* margin-bottom: 10%; */
  padding: 1%;
}

.form {
  text-align: center;
}
#title {
  position: fixed;
  top: 25%;
  left: 25%;
  text-align: left;
}
.btn {
  text-align: center;
  width: 15%;
  height: 35px;
  position: fixed;
  border: none;
  border-radius: 3px;
  color: white;
}

.btns {
  position: fixed;
  text-align: center;
  top: 90%;
}
.validation {
  position: fixed;
  top: 57%;
  /* left : 5%; */
  width: 100%;
}
.guide-text {
  text-align: left;
  position: fixed;
  left: 26%;
  top: 63%;
  font-size: 13px;
}
@media (max-width: 768px) {
  #title {
    position: fixed;
    top: 25%;
    left: 5%;
    text-align: left;
  }
  .guide-text {
    text-align: left;
    position: fixed;
    left: 6%;
    top: 63%;
    font-size: 13px;
  }
  #emailInput {
    width: 90%;
    left: 5%;
  }
  .valInput {
    left: 5%;
    height: 35px;
    width: 90%;
  }
}
</style>