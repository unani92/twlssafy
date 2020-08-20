<template>
  <div class="wrapper">
    <h1 class="title">환영합니다.</h1>

    <form @submit.prevent="submitForm">
      <div class="form">
        <!-- email(email)
          0. 형식 검증
          1. 중복 확인
          2. 메일 인증 요청 및 인증 코드 입력
        -->
        <div class="inputfield logo" style></div>
        <div style="text-align: center;">
          회원가입을 하고 배운 지식을 친구들과 공유해보세요
        </div>
        <div class="inputfield">
          <GoogleLogin />
        </div>
        <p class="join-warning guide-text">{{ logMessage.email }}</p>
        <div class="inputfield">
          <input
            class="input"
            id="email"
            type="email"
            placeholder="email"
            v-model="email"
            @focusout="emailDoubleCheck"
          />

          <!-- <button :disabled="!isemailValid">
            이메일 인증 받기
          </button>-->
        </div>
        <p class="guide-text">{{ logMessage.validationNumber }}</p>
        <div v-if="confirmedEmail" style="margin-bottom:1rem;">
          <div class="emailValidation-form inputfield">
            <input
              class="input"
              type="text"
              v-focus
              ref="validationCode"
              placeholder="메일로 받은 인증코드"
              @focusout="checkValidationCode"
              v-model="validationNumber"
            />
          </div>
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
        <!-- password
        형식 검증-->
        <p class="join-warning guide-text">{{ logMessage.password }}</p>
        <div class="inputfield">
          <input
            class="input"
            id="password"
            type="password"
            placeholder="password"
            v-model="password"
            @focusout="checkPasswordForm"
          />
        </div>

        <!-- password confirm OK -->
        <p class="join-warning guide-text">{{ logMessage.passwordConfirm }}</p>
        <div class="inputfield">
          <input
            class="input"
            id="passwordConfirm"
            type="password"
            placeholder="password confirm"
            v-model="passwordConfirm"
            @focusout="checkPasswordConfirm"
          />
        </div>

        <div class="inputfield">
          <input
            class="input"
            id="info"
            type="text"
            placeholder="한 줄 소개 부탁해요"
            v-model="info"
          />
        </div>
        <p class="join-warning guide-text">{{ logMessage.info }}</p>

        <div class="inputfield">
          <input
            class="input"
            id="github"
            type="text"
            placeholder="깃허브 주소를 입력해주세요"
            v-model="github"
          />
        </div>
        <div class = "policyCheck">
          <input type="checkbox" id="checkbox" v-model="checked">
          
          <a class="terms-modal atag" id ="tou" data-toggle="modal">
            이용약관
          </a>        
          및  
          <a class="privacy-modal atag" data-toggle="modal">
            개인정보처리방침
          </a>
          에 동의합니다.
        </div>
        <!-- 버튼 활성화 OK -->
        <div class="inputfield">
          <button
            type="submit"
            class="btn"
            :disabled="
              !(
                checkValidationCode &&
                isSamePassword &&
                nicknameDoubleCheck &&
                isPasswordValid &&
                checked
              )
            "
          >
            회원 가입
          </button>
        </div>
      </div>
    </form>
    <p class="log"></p>


    <div id="privacyModal" class="modal">
      <!-- Modal content -->
      <div class="modal-content">
        <div class="close" style="text-align : right; cursor : pointer">X</div>
        <privacy-policy/>
      </div>
    </div>
    <div id="termsModal" class="modal">
      <!-- Modal content -->
      <div class="modal-content">
        <div class="close" style="text-align : right; cursor : pointer">X</div>
        <terms-of-use/>
      </div>
    </div>

  </div>
</template>

<script>
import GoogleLogin from '../components/GoogleLogin';
import { validateEmail, validatePassword } from '@/utils/validation';
import { checkEmail, checkNickname, registerUser } from '@/api/index';
import PrivacyPolicy from '@/components/policies/PrivacyPolicy.vue';
import TermsOfUse from '@/components/policies/TermsOfUse.vue';

// import SelectSkills from "../views/SelectSkills.vue";

export default {
  data() {
    return {
      email: '',
      validationNumber: '',
      validationNumberFromBE: '',
      nickname: '',
      password: '',
      passwordConfirm: '',
      info: '',
      github: '',
      logMessage: {
        email: '',
        nickname: '',
        validationNumber: '',
        password: '',
        passwordConfirm: '',
        info: '',
      },
      confirmedEmail: false,
      nicknameDoubleCheck: false,
      checked : false,
    };
  },

  components: {
    GoogleLogin,
    PrivacyPolicy,
    TermsOfUse
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
    attachModal() {
      const privacyModal = document.getElementById("privacyModal");
      const btn = document.querySelector(".privacy-modal");
      const span = document.getElementsByClassName("close")[0];
      btn.onclick = function () {
        privacyModal.style.display = "block";
      };
      span.onclick = function () {
        privacyModal.style.display = "none";
      };


      const termsModal = document.getElementById("termsModal");
      const tbtn = document.querySelector(".terms-modal");
      const tspan = document.getElementsByClassName("close")[1];
      tbtn.onclick = function () {
        termsModal.style.display = "block";
      };
      tspan.onclick = function () {
        termsModal.style.display = "none";
      };

       window.onclick = function (event) {
        if (
          event.target === privacyModal ||
          event.target === termsModal
        ) {
          privacyModal.style.display = "none";
          termsModal.style.display = "none";
          // followingModal.style.display = "none";
        }
      };
    },




    async emailDoubleCheck() {
      if (this.isemailValid) {
        this.logMessage.email = '인증을 완료해주세요';
        const params = {
          email: this.email,
        };
        const res = await checkEmail(params);
        if (res.data.status) {
          this.logMessage.validationNumber =
            '인증번호를 발송했습니다. \n인증번호가 오지 않으면 입력하신 정보가 정확한지 확인하여 주세요. \n이미 가입된 메일은 가상전화번호는 인증번호를 받을 수 없습니다.';
          this.validationNumberFromBE = res.data.object.code;
          this.confirmedEmail = true;
        } else {
          this.logMessage.email = '이미 가입된 메일입니다.';
        }
      } else {
        this.logMessage.email = '유효한 이메일을 입력해주세요';
      }
    },

    checkValidationCode() {
      if (this.validationNumber === this.validationNumberFromBE) {
        this.logMessage.validationNumber = '인증 성공';
        this.logMessage.email = '';
      }
    },
    checkPasswordForm() {
      if (!this.isPasswordValid) {
        this.logMessage.password =
          '영문자 대문자, 소문자, 특수문자, 숫자를 하나이상 포함한 8자';
      } else {
        this.logMessage.password = '';
      }
    },
    checkPasswordConfirm() {
      if (!this.isSamePassword) {
        this.logMessage.passwordConfirm = '일치하지 않습니다';
      } else {
        this.logMessage.passwordConfirm = '';
      }
    },

    async nicknameCheck() {
      if (this.nickname === '') {
        this.logMessage.nickname = '닉네임을 입력해주세요';
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
      this.logMessage.nickname = res.data.data;
    },

    async submitForm() {
      const userData = {
        email: this.email,
        password: this.password,
        nickname: this.nickname,
        info: this.info,
        github: this.github,
      };

      const { data } = await registerUser(userData);
      // 토큰 보내주세요.... 셀렉스킬에 넘겨줄예정
      this.$store.commit('setUsername', userData.email);
      this.$store.commit('setNickname', userData.nickname);
      this.$store.commit('setToken', data.data);
      this.$router.push('/selectskills');
    },
    initForm() {
      this.email = '';
      this.password = '';
      this.nickname = '';
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
  mounted() {
    try {
     this.attachModal();
    } catch (e) {
      return
    }
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Montserrat:400,700&display=swap');
.atag {
  text-decoration: underline;
  color : skyblue;
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Montserrat', sans-serif;
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
  content: '';
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
.wrapper .form .inputfield .check input[type='checkbox'] {
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
  content: '';
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
.wrapper .form .inputfield .check input[type='checkbox']:checked ~ .checkmark {
  background: #e6837a;
}

.wrapper
  .form
  .inputfield
  .check
  input[type='checkbox']:checked
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

.modal {
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  padding-top: 30px; /* Location of the box */
  left: 0;
  top: 0;
  width: 110%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
  font-size: 12px;
}
/* Modal Content */
.modal-content {
  background-color: #fefefe;
  margin: 10%;
  padding: 20px;
  border: 1px solid #888;
  border-radius: 15px;
  width: 70%;
}

/* The Close Button */
.close {
  font-size: 18px;
  font-weight: bold;
  color: rgb(114, 114, 114);

}

.close:hover,
.close:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

.policyCheck{
  font-size: 13px;
  margin : 10px 0 15px 0;
}

@media (max-width: 500px) {
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
  .modal {
  padding-top: 100px; /* Location of the box */
  }
}
@media (min-width: 501px) and (max-width: 768px) {
 .modal {
  padding-top: 70px; /* Location of the box */
  }
}
</style>
