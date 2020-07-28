<template>
  <div class="google">
    <!-- <i class="fab fa-google"></i> -->
    <!-- <a href="http://localhost:8080/oauth2/authorization/google">Google 로그인</a> -->
    <GoogleLogin class="big-button" :renderParams="renderParams" :params="params" :onSuccess="onSuccess" :onFailure="onFailure"> Google Login</GoogleLogin>
    <!-- <button class="big-button" type="button" value="google logout" @click="GoogleSignout()"/> -->
  </div>
</template>

<script>
import GoogleLogin from 'vue-google-login';
import axios from 'axios';
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
      onSuccess(googleUser){
        // this.googleAccessToken = googleUser.Zi
        // console.log(googleUser.w3.ig)

         console.log(googleUser);
 
            // This only gets the user information: id, name, imageUrl and email
            const id_token = googleUser.wc.id_token;
        axios.post('http://localhost:8080/googlelogin',{ id_token })
          .then(res =>{
          console.log(res);
        }).catch(err =>{
          console.log(err);
          }
        )
           
      },
      onFailure(err){
        console.log("fail -> " + err);
            },
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
