<template>
  <div v-if="comment" :id="comment.commentid" class="comment-detail">
    <div class="profile">
      <div class="prifile-detail">
        <img :src="comment.img" style="width : 30px; height : 30px; cursor : pointer" @click="goProfilePage" />
        <!-- <img :src="photo" alt style="width : 30px; height : 30px;" /> -->
        <div class="profile-text">
          <div>
            <div
              :style="{ backgroundImage:'url('+require('@/assets/image/medal-'+calGrade+'.png')+')'}"
              class="grade"
            ></div>
            <span
              style="cursor:pointer; font-weight:bold; font-size:1rem"
              @click="goProfilePage"
            >{{ comment.nickname }}</span>
          </div>
          <span v-if="comment.updatedat == null" style="color: gainsboro">
            {{comment.createdat.replace("T"," ")}}
          </span>
          <span v-else style="color: gainsboro">
            {{comment.updatedat.replace("T"," ")}}
          </span>
        </div>
      </div>
      <div v-if="canEdit" class="user-icons">
        <i @click="commentUpdate" class="far fa-edit editDelete"></i>
        <i @click="commentDelete" class="far fa-trash-alt editDelete"></i>
      </div>
    </div>
    <!-- 댓글 내용을 보여주거나 수정 하는 코드입니다 update하는 아이콘을 누르면 openUpdateInput data값이 수정되고 
    data값을 평가하는 isUpdating값이 바뀝니다. isUpdating이 true가 되면 수정 input이 열립니다-->
    <div v-if="isUpdating && comment">
      <textarea v-model="comment.content" class="update"/>
      <div>
        <button @click="submitComment" class="updateBtn">
          <i class="fas fa-pencil-alt">댓글 수정</i>
        </button>
      </div>
    </div>

    <div v-else class="detail">{{ comment.content }}</div>
  </div>
</template>

<script>
import { deleteComment, updateComment } from "../../api";
import { getGrade } from "@/utils/calcGrade";

export default {
  name: "CommentDetail",
  computed: {
    // photo() {
    //   return `https://api.adorable.io/avatars/75/${this.comment.email}.png`;
    // },
    canEdit() {
      return this.$store.state.username === this.comment.email;
    },
    calGrade() {
      return getGrade(this.comment.score);
    },
    isUpdating() {
      return this.openUpdateInput;
    },
  },
  data() {
    return {
      openUpdateInput: false,
      content: this.comment.content,
    };
  },
  props: {
    comment: Object,
  },
  methods: {
    commentDelete() {
      const commentid = this.comment.commentid;
      deleteComment(commentid)
        .then(() => {
          this.$emit("commentDelete", commentid);
        })
        .catch((err) => console.log(err));
    },
    commentUpdate() {
      this.openUpdateInput = !this.openUpdateInput;
    },
    // 댓글이 수정이 완료되고 나면 댓글 id를 string으로 보내지 않으면 오류 발생
    submitComment() {
      const textarea = document.querySelector(".update")
      const params = {
        commentId: `${this.comment.commentid}`,
        content: textarea.value,
      };
      updateComment(params)
        .then(() => {
          // 수정 input창을 닫고 내용도 수정
          this.openUpdateInput = false;
          this.comment.content = textarea.value;
        })
        .catch((err) => console.log(err));
    },
    goProfilePage() {
      this.$router.push({
        name: "Profile",
        params: { nickname: this.comment.nickname },
      });
    },
  },
};
</script>

<style scoped>
.comment-detail {
  /* margin-top: 1rem; */
  border-top: 1px solid gainsboro;
  border-bottom: 1px solid gainsboro;
}
img {
  border-radius: 50%;
  margin-right: 1rem;
}
.profile {
  display: flex;
  justify-content: space-between;
  /* margin-bottom: 1rem; */
  margin-top: 1rem;
  padding: 1rem 1rem 0 1rem;
}

.grade {
  display: inline-block;
  width: 24px;
  height: 24px;
  flex-shrink: 0;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  margin-right: 0.5em;
}
.prifile-detail {
  display: flex;
}
.profile-text {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  font-size: 12px;
}
.user-icons {
  display: flex;
  align-items: center;
  font-size: 15px;
}
.user-icons > i {
  margin: 5px;
}
.detail {
  font-size: 17px;
  padding: 1rem;
  margin-bottom: 5px;
}
button {
  border-radius: 3px;
  color: white;
  background-color: #94adff;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 5px;
}
.update {
  display: block;
  width: 100%;
  height: 70px;
  border: 1px solid gainsboro;
  font-weight: bold;
  font-size: 1rem !important;
  padding: 1rem;
  float: left;
  margin-right: 2%;
}
.update:focus {
  outline: none !important;
}

.updateBtn {
  width: 100%;
  height: 70px;
  color: #94adff;
  background-color: #eeeeee00;
  font-weight: bold;
  font-size: 15px;
  margin-bottom: 1rem;
}
</style>