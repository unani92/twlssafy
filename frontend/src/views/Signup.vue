<template>
  <div class="signup-body">
    <SignupForm></SignupForm>
  </div>
</template>

<script>
import SignupForm from "@/components/SignupForm.vue";

export default {
  components: {
    SignupForm,
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
