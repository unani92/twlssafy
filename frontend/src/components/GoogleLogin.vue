<template>
  <div class="google">
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

         // console.log(googleUser);
 
            // This only gets the user information: id, name, imageUrl and email
        const id_token = googleUser.wc.id_token;
        axios.post('http://localhost:8080/googlelogin',{ id_token },{headers: {id_token}})
          .then((res) =>{
            const { email } = res.data.object
            console.log(res.data)
          if (res.data.data === "failed") {
            this.$router.push({ name: "SocialSignup", params: {email, id_token} })
          } else {
            axios.post('http://localhost:8080/account/googleInfo',null,{headers:{id_token}})
              .then(res => {
                const { email, followList, id_token, likesList, notification, pinList } = res.data.object
                this.$store.commit("setUsername", email)
                this.$store.commit("setFollowList", followList.follow)
                this.$store.commit("setToken", id_token)
                this.$store.commit("setLikeList", likesList)
                this.$store.commit("setNotificationlist", notification)
                this.$store.commit("setPinList", pinList)
              })
            }
        })
          .catch(err => console.log(err))
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
