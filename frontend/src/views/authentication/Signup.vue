<template>
  <div class="signup-body">
    <SignupForm></SignupForm>
    <Footer/>
  </div>
</template>

<script>
import SignupForm from "../../components/authentication/SignupForm";
import Footer from "../../components/navigation/Footer";

export default {
  components: {
    SignupForm,
    Footer
  },
};
</script>

<style>
.signup-body {
  padding-top: 110px;
  max-width: 530px;
  margin-bottom: 1rem;
  margin-left: auto;
  margin-right: auto;
  padding-bottom: 1rem;
  border-radius: 3px;
  text-align: left;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
</style>
