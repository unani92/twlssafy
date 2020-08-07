<template>
  <div class="google">
    <GoogleLogin class="big-button" :renderParams="renderParams" :params="params" :onSuccess="onSuccess" :onFailure="onFailure"> Google Login</GoogleLogin>
  </div>
        <!-- <GoogleLogin :params="params" :logoutButton=true>Logout</GoogleLogin> -->
</template>

<script>
import GoogleLogin from 'vue-google-login';
import http from '../api/http-common.js';
import { mapActions } from 'vuex'
  export default {
    // name: "GoogleLogin",

    data(){
      return {
        params :{
          client_id : '634062607964-elrm78as5396cdodbtf1p6mp6nd0dib4.apps.googleusercontent.com'
        },
        renderParams: {
          width: 250,
          height: 50,
          longtitle: true
        },
        googleAccessToken : '',
      };
    },

    methods : {
      ...mapActions(["getGoogleUserInfo"]),
      onSuccess(googleUser){
        let id_token = googleUser.wc.id_token;
        http.post('/googlelogin',{ id_token },{headers: {id_token}})
          .then((res) =>{
            const { email } = res.data.object
          if (res.data.data === "failed") {
            this.$router.push({ name: "SocialSignup", params: {email, id_token} })
          } else {
              id_token = res.data.object.id_token
              this.getGoogleUserInfo(id_token)
            }
        })
          .catch(err => alert("Error : ",err))
      },
      onFailure(err) { console.log("fail -> " + err) },
    },
    components: {
      GoogleLogin
    }
  }
</script>

<style scoped>
  .google {
    border-radius: 3px;
    background-color: deepskyblue;
    width: 250px;
    height: 50px;
    color: white;
    display: flex;
    justify-content: space-around;
    align-items: center;
    font-weight: bolder;
    margin: 1rem;
  }
  i {
    color: white;
    font-size: 2rem;
  }
</style>
