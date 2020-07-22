<template>
  <div class="login">
    <h1>반갑습니다</h1>
    <div class="socialogin">
      <GithubLogin/>
      <GoogleLogin/>
    </div>
    <div class="emailogin">
      <div class="email">
        <input @focusout="emailCheckForm" v-model="email" type="email" placeholder="이메일을 입력하세요">
        <div class="error-message" v-if="error.email">
          <span>{{error.email}}</span>
        </div>
      </div>
      <div class="password">
        <input @input="pwdCheckForm" v-model="password" type="password" placeholder="비밀번호를 입력하세요">
      </div>
      <div class="error-message" v-if="error.password">
          <span>{{error.password}}</span>
      </div>
      <button @click="login"
              class="login-btn"
              :class="{disabled : !isSubmit}">
        Login</button>
      <button @click="$router.push('/signup')" class="signup-btn">Sign Up</button>
      <Router-link to="/changepwd" style="text-decoration: none; color: black">비밀번호를 잊으셨나요?</Router-link>
    </div>
  </div>
</template>

<script>
  import PV from "password-validator";
  import * as EmailValidator from "email-validator";
  import GithubLogin from "../components/GithubLogin";
  import GoogleLogin from "../components/GoogleLogin";
  import { login } from "../api";

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
          password: false
        },
        isSubmit: false
      }
    },
    components: {
      GithubLogin,
      GoogleLogin
    },
    methods: {
      emailCheckForm() {
        if (EmailValidator.validate(this.email)) {
          this.error.email = false
          this.canLogin.email = true
        } else {
          this.error.email = "이메일 형식이 아닙니다."
          this.canLogin.email = false
        }
      },
      pwdCheckForm() {
        setTimeout(() => {
          if (this.password.length >= 0 && !this.passwordSchema.validate(this.password)) {
            this.error.password = "영문,숫자 포함 8 자리이상이어야 합니다. 2글자 이상 중복은 불가합니다."
            this.canLogin.password = false
        } else {
            this.error.password = false
            this.canLogin.password = true
          }
        }, 1000)
      },
      login() {
        const params = {
          email: this.email,
          password: this.password
        }
        if (this.isSubmit) {
          login(params)
            .then(res => {
              const { data: {data} } = res
              if (data === 'success') {
                const{ email, nickname } = res.data.object
                this.$store.commit("setUsername", email);
                this.$store.commit("setNickname", nickname);
                this.$router.push('/')
              } else if (data === "비밀번호가 일치하지 않습니다.") {
                this.error.password = '비밀번호가 일치하지 않습니다.'
              } else {
                this.error.email = '이메일이 일치하지 않습니다.'
              }
            })
            .catch(err => console.log(err))
        }
      },
      changeBtn() {
      this.isSubmit = this.canLogin.email && this.canLogin.password
      }
    },
    updated() {
      this.changeBtn();
    }
  }
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
  .signup-btn, .login-btn {
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
    margin-bottom: 1rem;
  }
  @media (min-width: 415px) {
    .socialogin {
      display: flex;
      justify-content: center;
    }
    .login-btn, .signup-btn, input {
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
    .login-btn, .signup-btn, input {
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