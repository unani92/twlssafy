<template>
  <div class="google">
    <GoogleLogin
      class="big-button"
      :renderParams="renderParams"
      :params="params"
      :onSuccess="onSuccess"
      :onFailure="onFailure"
      >Google Login</GoogleLogin
    >
  </div>
</template>

<script>
import GoogleLogin from 'vue-google-login';
import { socialLogin } from '../../api';
import { mapActions } from 'vuex';
export default {
  // name: "GoogleLogin",

  data() {
    return {
      params: {
        client_id:
          '634062607964-elrm78as5396cdodbtf1p6mp6nd0dib4.apps.googleusercontent.com',
      },
      renderParams: {
        width: 250,
        height: 50,
        longtitle: true,
      },
      googleAccessToken: '',
    };
  },

  methods: {
    ...mapActions(['getGoogleUserInfo']),
    onSuccess(googleUser) {
      let id_token = googleUser.wc.id_token;
      const redirect = this.$route.query.redirect || this.$route.params.id
      this.$router.push({ name: 'LoginLoading', params: {redirect: redirect} })
      socialLogin(id_token)
        .then((res) => {
          const { email } = res.data.object;
          if (res.data.data === 'failed') {
            this.$router.push({
              name: 'SocialSignup',
              params: { email, id_token },
            });
          } else {
            id_token = res.data.object.id_token;
            this.getGoogleUserInfo(id_token);
          }
        })
        .catch((err) => alert('Error : ', err));
    },
    onFailure(err) {
      console.log('fail -> ' + err);
    },
  },
  components: {
    GoogleLogin,
  },
};
</script>

<style scoped>
.google {
  width: 100%;
  display: flex;
  justify-content: center;
  margin: 10px 0 0;
}
i {
  color: white;
  font-size: 2rem;
}
</style>
