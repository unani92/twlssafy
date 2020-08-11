<template>
    <div style="text-align : center; font-size: 13px; width: 95%;margin-left:2.5%">
        <hr>
        <div style="margin : 15px;">
            Copyright by Today We Learned, SSAFY 3기 4조. All rights reserved.
            <br>문의사항 : twlssafy@gmail.com
        <span v-if="isLoggedIn" style="margin:10px; cursor:pointer" 
        @click="delAcc"> 회원탈퇴 </span>
        </div>
    </div>
</template>
<script>
import { mapGetters } from "vuex";
import { delAccount } from "../api/index";

export default {
    computed: {
        ...mapGetters(["isLoggedIn"]),
    },
    methods :{
        delAcc(){
            if (confirm("탈퇴 하시겠습니까?")) {
                delAccount(this.$store.state.id_token).then((res) => {
                    if(res.data.status) {
                        this.$router.push({ name: "Logout" });
                        alert("탈퇴 되었습니다!");
                    } else {
                        alert ("회원탈퇴 실패 ! 관리자에게 문의하세요.")
                    }
                });
            }
        }
    }
}
</script>