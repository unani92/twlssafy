<template>
  <div class="login">
    <h1>반갑습니다</h1>
    <div class="socialogin">
      <GithubLogin/>
      <GoogleLogin/>
    </div>
    <div class="emailogin">
      <input type="email" placeholder="이메일을 입력하세요">
      <input type="password" placeholder="비밀번호를 입력하세요">
      <button class="login-btn">Login</button>
      <button @click="$router.push('/signup')" class="signup-btn">Sign Up</button>
      <Router-link to="/changepwd" style="text-decoration: none; color: black">비밀번호를 잊으셨나요?</Router-link>
    </div>
  </div>
</template>

<script>
  import GithubLogin from "../components/GithubLogin";
  import GoogleLogin from "../components/GoogleLogin";
  export default {
    name: "Login",
    components: {
      GithubLogin,
      GoogleLogin
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
</style>