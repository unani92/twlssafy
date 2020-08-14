<template>
  <div
    style="text-align : center; font-size: 13px; width: 95%;margin-left:2.5%"
  >
    <hr />
    <div style="margin : 15px;">
      Copyright by Today We Learned, SSAFY 3기 7조. All rights reserved.
      <br />문의사항 : twlssafy@gmail.com
      <span
        v-if="isLoggedIn"
        style="margin:10px; cursor:pointer"
        @click="delAcc"
      >
        회원탈퇴
      </span>
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex';
import { delAccount } from '../api/index';
import Swal from 'sweetalert2/dist/sweetalert2.js'
import 'sweetalert2/src/sweetalert2.scss'

export default {
  computed: {
    ...mapGetters(['isLoggedIn']),
  },
  methods: {
    delAcc() {
      Swal.fire({
        text:"탈퇴 하시겠습니까?",
        icon:"question",
        closeOnClickOutside:true,
        showCancelButton:true,
        confirmButtonText:"<span style='text-align:center; margin-left:-12px; position:relative; top:-8px;'>OK</span>",
        confirmButtonColor:"#e6837a",
        cancelButtonText:"<span style='text-align:center; margin-left:-12px; position:relative; top:-8px;'>NO</span>",
        cancelButtonColor:"#e6837a",
        }).then((result) => {
          if(result.value){
            delAccount(this.$store.state.id_token).then((res) => {
              if (res.data.status) {
                this.$router.push({ name: 'Logout' });
                Swal.fire({
                  text:"탈퇴 되었습니다.",
                  icon:"success",
                  closeOnClickOutside:true,
                  confirmButtonText:"<span style='text-align:center; margin-left:-12px; position:relative; top:-8px;'>OK</span>",
                  confirmButtonColor:"#e6837a",
                });
              } else {
                Swal.fire({
                  text:"회원탈퇴 실패 ! 관리자에게 문의하세요.",
                  icon:"error",
                  closeOnClickOutside:true,
                  confirmButtonText:"<span style='text-align:center; margin-left:-12px; position:relative; top:-8px;'>OK</span>",
                  confirmButtonColor:"#e6837a",
                });
              }
            });
          } else {
            Swal.close();
          }
        });
      
    },
  },
};
</script>
