<template>
  <div class="wrapper">
    <h1 class="title">LOGIN</h1>

    <form @submit.prevent="submitForm">
      <div class="form">
        <div class="inputfield logo"></div>
        <div style="text-align: center;">
          회원가입을 하고 배운 지식을 친구들과 공유해보세요
        </div>
        <p class="join-warning guide-text">{{ logMessage.email }}</p>
        <div class="inputfield">
          <input
            v-model="email"
            v-focus
            type="email"
            class="input"
            placeholder="email"
            @focusout="emailCheckForm"
          />
        </div>

        <!-- password 형식 검증-->
        <p class="join-warning guide-text">{{ logMessage.password }}</p>
        <div class="inputfield">
          <input
            @input="pwdCheckForm"
            v-model="password"
            class="input"
            type="password"
            placeholder="password"
          />
        </div>

        <!-- 버튼 활성화 OK -->
        <div class="inputfield">
          <button type="submit" class="btn" :disabled="changeBtn">
            Login
          </button>
        </div>
      </div>
    </form>
    <div class="form">
      <div class="inputfield"></div>
      <div class="inputfield" style="justify-content:center">
        <Router-link to="/findpwd" class="find-pwd"
          >비밀번호를 잊으셨나요?</Router-link
        >
      </div>
      <div class="inputfield">
        <button @click="goToSignup" class="btn">
          Sign Up
        </button>
      </div>
      <div class="inputfield" style="justify-content:center">
        <p>소셜 로그인하기</p>
      </div>
      <div class="inputfield">
        <GoogleLogin />
      </div>
    </div>
    <p class="log"></p>
  </div>
</template>

<script>
import GoogleLogin from '../components/GoogleLogin';
import { validateEmail, validatePassword } from '@/utils/validation';
import { login } from '@/api/index';
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      email: '',
      password: '',
      logMessage: {
        email: '',
        password: '',
      },
    };
  },

  components: {
    GoogleLogin,
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
    changeBtn() {
      return this.isemailValid && this.isPasswordValid;
    },
  },
  methods: {
    ...mapActions(['getGoogleUserInfo']),
    emailCheckForm() {
      if (!this.isemailValid) {
        this.logMessage.email = '유효한 이메일을 입력해주세요';
      } else {
        this.logMessage.email = '';
      }
    },
    pwdCheckForm() {
      if (!this.isPasswordValid) {
        this.logMessage.password =
          '영문자 대문자, 소문자, 특수문자, 숫자를 하나이상 포함한 8자';
      } else {
        this.logMessage.password = '';
      }
    },

    async submitForm() {
      const params = {
        email: this.email,
        password: this.password,
      };
      login(params)
        .then((res) => {
          this.getGoogleUserInfo(res.data.object.id_token);
          if (this.$route.query.redirect) {
            const redirect = this.$route.query.redirect;
            this.$router.push({
              name: 'ArticleDetail',
              params: { id: Number(redirect) },
            });
          } else this.$router.push('/');
        })
        .catch(() => (this.logMessage.email = '아이디, 비밀번호를 확인하세요'));
    },
    initForm() {
      this.email = '';
      this.password = '';
    },
    goToSignup() {
      this.$router.push('/signup');
    },
  },
  directives: {
    focus: {
      inserted: function(el) {
        el.focus();
      },
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Montserrat:400,700&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Montserrat', sans-serif;
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
.find-pwd {
  text-decoration: none;
  color: gray;
  font-size: 13px;
}
</style>
