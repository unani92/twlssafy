<template>
  <div class="form">
    <div>
    <h1 id="title">
      비밀번호를<br>변경해주세요</h1>
    <br>

    <p id="p1">새로운 비밀번호 입력</p>
       <input
         class="pwd"
         id="pwd1"
         type="password"
         placeholder="비밀번호를 입력해주세요"
         v-model="pwd1"
         @focusout="checkPasswordForm"
       />

    <p class="guide-text" id="validpwd">{{ validPwd }}</p>

    <p id="p2"> 비밀번호 확인</p>
    <input class="pwd"
          type="password"
          id = "pwd2"
          v-model="pwd2"
          placeholder="위와 동일한 비밀번호를 입력해주세요"
          @focusout="checkPwd"
        />
    <p class="guide-text" id="msg">{{ msg }}</p>


    <div class="btns">
        <button class="btn" @click="goback" 
        style="left : 35%; background-color:#b3b3b3;">
          취소</button>
        <button class="btn" @click="changePwd"
        style="background-color: royalblue; left : 50%" 
        :disabled = "btnEnabled==false"
        >
        다음</button>
    </div>
  </div>

  </div>
</template>

<script>
import { validatePassword } from "@/utils/validation";
import { submitChangePwd } from "../api";
import Swal from 'sweetalert2/dist/sweetalert2.js'
import 'sweetalert2/src/sweetalert2.scss'

export default {
    name: "ChangePwd",
    data() {
    return {
      pwd1 : "",
      pwd2 : "",
      msg : " ",
      validPwd : '',
      passwordConfirm : false,
      btnEnabled : false,
    };
  },
  props: {
    email: String,
  },
  methods: {
    async checkPwd(){
      if(this.pwd1 !== this.pwd2){
        this.msg="비밀번호 불일치"
      } else this.msg = ""

      if(this.msg === "" && this.isPasswordValid){
        this.btnEnabled=true;  
      }
    },
    goback(){
      this.$router.go(-1)
    },
    async changePwd(){
      submitChangePwd(this.email, this.pwd1)
        .then(()=>{
          Swal.fire({
            text:"비밀번호 변경완료 ! 로그인 해주세요",
            icon:"error",
            closeOnClickOutside:true,
            confirmButtonText:"<span style='text-align:center; margin-left:-12px; position:relative; top:-8px;'>OK</span>",
            confirmButtonColor:"#e6837a",
          });
          this.$router.push("/")
        })
        .catch(err =>
          alert ("Error : " , err)
        );
      },
     checkPasswordForm() {
      if (!this.isPasswordValid) {
        this.validPwd =
          "비밀번호는 영문자 대문자, 소문자, 특수문자, 숫자를 포함한 8자 이상으로 설정해주세요";
      } else {
        this.validPwd = "";
      }
    },
   },
  computed : {
    isPasswordValid() {
      if (!this.pwd1) {
        return false;
      }
      return validatePassword(this.pwd1);
    },
    isSamePassword() {
      return this.pwd1 && this.pwd2 === this.passwordConfirm;
    },
  },
  directives: {
    focus: {
      // directive definition
      inserted: function (el) {
        el.focus();
      },
    },
  },
  };
</script>

<style scoped>
  #pwd1 {
    position: fixed;
    top : 45%;
    left : 25%;
    height: 35px;
    width: 50%;
    border-radius: 6px;
    border: solid 1.5px #b4b4b4;
    margin-bottom: 12%;
  }
  #pwd2{
    position: fixed;
    top : 58%;
    width: 50%;;
  }
  .pwd {
    position : fixed;
    left : 25%;
    height: 35px;
    width: 50%;
    border-radius: 6px;
    border: solid 1.5px #b4b4b4;
    padding : 1%;
  }
  
  .form {
    text-align: center;
  }
  #title {
    position: fixed;
    top : 16%;
    left :42%;
    text-align: left;
  }
  .btn {
    text-align: center;
    width: 15%;
    height : 35px;
    position: fixed;
    border: none;
    border-radius: 3px;
    color : white;
  }
  p {
    position: fixed;
    text-align: center;    
    left : 26%;
    font-size: 13px;
  }
  #p1{
    top : 42%;
    font-weight: bold;
  }
  #p2 {
    font-weight: bold;
    top : 55%;
  }
  #validpwd {
    top : 50%;
  }
  .btns {
    position: fixed;
    text-align: center;    
    top : 90%;
  }
  .guide-text {
    text-align: left;
    position: fixed;
    left : 26%;
    top : 63%;
    font-size: 13px;
    color : red;
  }
  @media (max-width: 768px) {
  #title {
    position: fixed;
    top : 16%;
    left : 5%;
    text-align: left;
  }
  .guide-text {
    text-align: left;
    position: fixed;
    left : 6%;
    top : 63%;
    font-size: 13px;
  }
    #pwd1, #pwd2 {
      width: 90%;
      left : 5%;
    }
    #p1, #p2 {
      left : 5%;
    }
}
</style>