<template>
  <div class="login">
    <h1>반갑습니다</h1>
    <div class="socialogin">
      <GithubLogin />
      <GoogleLogin />
    </div>
    <div class="emailogin">
      <div class="email">
        <input
          @focusout="emailCheckForm"
          v-model="email"
          type="email"
          placeholder="이메일을 입력하세요"
        />
        <div class="error-message" v-if="error.email">
          <span>{{ error.email }}</span>
        </div>
      </div>
      <div class="password">
        <input
          @input="pwdCheckForm"
          v-model="password"
          type="password"
          placeholder="비밀번호를 입력하세요"
        />
      </div>
      <div class="error-message" v-if="error.password">
        <span>{{ error.password }}</span>
      </div>
      <button @click="login" class="login-btn" :class="{ disabled: !isSubmit }">
        Login
      </button>
      <Router-link to="/findpwd" style="text-decoration: none; color: gray; font-size : 13px; margin-top : 5px;"
        >비밀번호를 잊으셨나요?</Router-link>
      <button @click="$router.push('/signup')" class="signup-btn" style="margin-bottom : 1rem;">
        Sign Up
      </button>
    </div>
  </div>
</template>

<script>
import PV from "password-validator";
import * as EmailValidator from "email-validator";
import GithubLogin from "../components/GithubLogin";
import GoogleLogin from "../components/GoogleLogin";
import { login } from "../api";
import { mapActions } from 'vuex'

export default {
  name: "Login",
  created() {
    this.passwordSchema
      .is()
      .min(8)
      .is()
      .max(100)
      .has()
      .digits()
      .has()
      .letters();
  },
  data() {
    return {
      email: null,
      password: null,
      passwordSchema: new PV(),
      error: {
        email: null,
        password: null,
      },
      canLogin: {
        email: false,
        password: false,
      },
      isSubmit: false,
    };
  },
  components: {
    GithubLogin,
    GoogleLogin,
  },
  methods: {
    emailCheckForm() {
      if (EmailValidator.validate(this.email)) {
        this.error.email = false;
        this.canLogin.email = true;
      } else {
        this.error.email = "이메일 형식이 아닙니다.";
        this.canLogin.email = false;
      }
    },
    pwdCheckForm() {
      setTimeout(() => {
        if (
          this.password.length >= 0 &&
          !this.passwordSchema.validate(this.password)
        ) {
          this.error.password =
            "영문,숫자 포함 8 자리이상이어야 합니다. 2글자 이상 중복은 불가합니다.";
          this.canLogin.password = false;
        } else {
          this.error.password = false;
          this.canLogin.password = true;
        }
      }, 1000);
    },
    ...mapActions(["getGoogleUserInfo"]),
    login() {
      const params = {
        email: this.email,
        password: this.password,
      };
      if (this.isSubmit) {
        login(params)
          .then(res => {
            this.getGoogleUserInfo(res.data.object.id_token)
            if (this.$route.query.redirect) {
                const redirect = this.$route.query.redirect;
                this.$router.push({
                  name: "ArticleDetail",
                  params: { id: Number(redirect) },
                });
              } else this.$router.push("/");
        })
          .catch(err => alert("Error : " , err))
      } else {
        this.error.email = "아이디, 비밀번호를 확인하세요"
      }
    },
    changeBtn() {
      this.isSubmit = this.canLogin.email && this.canLogin.password;
    },
  },
  updated() {
    this.changeBtn();
  },
};
</script>

<style scoped>
.login {
  padding-top: 100px;
  text-align: center;
}
h1 {
  font-size: 45px;
}
.emailogin {
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
input {
  margin-top: 2rem;
  text-align: center;
  font-size: 1rem;
  border: 0;
  outline: 0;
  background: transparent;
  border-bottom: 3px solid black;
}
.signup-btn,
.login-btn {
  background-color: rgb(204, 93, 65);
  border-radius: 3px;
  color: white;
  font-weight: bolder;
  font-size: 1rem;
  border-style: none;
  outline: none;
  height: 50px;
  cursor: pointer;
  margin-top: 1rem;
  /* margin-bottom: 1rem; */
}
@media (min-width: 415px) {
  .socialogin {
    display: flex;
    justify-content: center;
  }
  .login-btn,
  .signup-btn,
  input {
    width: 350px;
  }
}
@media (max-width: 414px) {
  .socialogin {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  .login-btn,
  .signup-btn,
  input {
    width: 200px;
  }
}
.error-message {
  color: crimson;
  margin: 3px;
  text-align: center;
}
.disabled {
  cursor: default;
  background-color: rgb(237, 155, 104);
}
</style>
