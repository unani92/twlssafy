<template>
  <div :id="comment.commentid" class="comment-detail">
    <div class="profile">
      <div class="prifile-detail">
        <img :src="photo" alt="">
        <div class="profile-text">
          <h3>{{ this.comment.email }}</h3>
          <span style="color: gainsboro">{{ this.$moment(this.comment.updatedat).fromNow() }}</span>
        </div>
      </div>
      <div v-if="canEdit" class="user-icons">
        <i class="far fa-edit"></i>
        <i @click="commentDelete" class="far fa-trash-alt"></i>
      </div>
    </div>
    <div class="detail">
      {{ this.comment.content }}
    </div>
  </div>
</template>

<script>
  import { deleteComment } from "../../api";

  export default {
    name: "CommentDetail",
    computed: {
      photo() {
        return `https://api.adorable.io/avatars/75/${this.comment.email}.png`
      },
      canEdit() {
        return this.$store.state.username === this.comment.email
      }
    },
    props: {
      comment: Object
    },
    methods: {
      commentDelete() {
        const params = this.comment.commentid
        console.log(params)
        deleteComment(params)
          .then(() => {
            this.$emit("commentDelete",params)
        })
          .catch(err => console.log(err))
      }
    },
  }
</script>

<style scoped>
  .comment-detail {
    margin-top: 1rem;
    border-top: 1px solid gainsboro;
    border-bottom: 1px solid gainsboro;
    padding: 1rem;
  }
  img {
    border-radius: 50%;
    margin-right: 1rem;
  }
  .profile {
    display: flex;
    justify-content: space-between;
    margin-bottom: 1rem;
  }
  .prifile-detail {
    display: flex;
  }
  .profile-text {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
  }
  .user-icons {
    display: flex;
    align-items: center;
    font-size: 24px;
  }
  .user-icons > i {
    margin: 10px;
  }
  .detail {
    font-size: 20px;
    padding: 1rem;
  }
</style>