<template>
  <div class="login">
    <LoginForm></LoginForm>
  </div>
</template>

<script>
import LoginForm from '@/components/LoginForm';

export default {
  components: {
    LoginForm,
  },
};
</script>

<style scoped>
.login {
  padding-top: 135px;
  text-align: center;
}
h1 {
  font-size: 45px;
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
  .login-btn,
  .signup-btn,
  input {
    width: 350px;
  }
}
@media (max-width: 414px) {
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
.login-btn:disabled {
  cursor: not-allowed;
  background-color: rgb(237, 155, 104);
}
</style>
