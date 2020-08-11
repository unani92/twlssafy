<template>
  <div :id="comment.commentid" class="comment-detail">
    <div class="profile">
      <div class="prifile-detail">
        <img :src="photo" alt style="width : 30px; height : 30px;" />
        <div class="profile-text">
          <div>
            <div
              :style="{ backgroundImage:'url('+require('@/assets/image/medal-'+calGrade+'.png')+')'}"
              class="grade"
            ></div>
            <span
              style="cursor:pointer; font-weight:bold; font-size:1rem"
              @click="goProfilePage"
            >{{ this.nickname }}</span>
          </div>
          <span style="color: gainsboro">{{ this.$moment(this.comment.updatedat).fromNow() }}</span>
        </div>
      </div>
      <div v-if="canEdit" class="user-icons">
        <i @click="commentUpdate" class="far fa-edit editDelete"></i>
        <i @click="commentDelete" class="far fa-trash-alt editDelete"></i>
      </div>
    </div>

    <div v-if="isUpdating">
      <textarea v-model="content" class="update" />
      <div>
        <button @click="submitComment" class="updateBtn">
          <i class="fas fa-pencil-alt">댓글 수정</i>
        </button>
      </div>
    </div>

    <div v-else class="detail">{{ this.comment.content }}</div>
  </div>
</template>

<script>
import { deleteComment, updateComment } from "../../api";
import { getGrade } from "@/utils/calcGrade";

export default {
  name: "CommentDetail",
  computed: {
    photo() {
      return `https://api.adorable.io/avatars/75/${this.comment.email}.png`;
    },
    canEdit() {
      return this.$store.state.username === this.comment.email;
    },
    calGrade() {
      return getGrade(this.commentArticleCount);
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
    nickname: String,
    commentArticleCount: Number,
  },
  methods: {
    commentDelete() {
      const params = this.comment.commentid;
      console.log(params);
      deleteComment(params)
        .then(() => {
          this.$emit("commentDelete", params);
        })
        .catch((err) => console.log(err));
    },
    commentUpdate() {
      this.openUpdateInput = !this.openUpdateInput;
    },
    submitComment() {
      const params = {
        commentId: `${this.comment.commentid}`,
        content: this.content,
      };
      console.log(params);
      updateComment(params)
        .then(() => {
          this.openUpdateInput = false;
          this.comment.content = this.content;
        })
        .catch((err) => console.log(err));
    },
    goProfilePage() {
      this.$router.push({
        name: "Profile",
        params: { nickname: this.nickname },
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