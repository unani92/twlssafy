<template>
  <div class="comment-create">
    <div class="comment">
      <span style="font-size: 20px; font-weight: bold">{{ commentList.length }} 개의 댓글</span>
      <button @click="submitComment">댓글 작성</button>
    </div>
    <div>
      <textarea v-model="content" placeholder="댓글을 작성해 주세요" class="create"/>
    </div>
  </div>
</template>

<script>
  import { createComment } from "../../api";

  export default {
    name: "CommentCreate",
    props: {
      commentList: Array,
      article: Object
    },
    data() {
      return {
        content: null
      }
    },
    methods: {
      submitComment() {
        const email = this.$store.state.username
        const content = this.content
        const articleid = String(this.article.articleid)
        const params = {
          email,
          content,
          articleid
        }
        createComment(params)
          .then(res => console.log(res.data))
          .catch(err => console.log(err))
      }
    }
  }
</script>

<style scoped>
  .comment-create {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  .comment {
    margin-bottom: 1rem;
    display: flex;
    justify-content: space-between;
  }
  button {
    border-radius: 3px;
    color: white;
    background-color: #e6837a;
    border: none;
    outline: none;
    cursor: pointer;
    padding: 5px;
  }
  .create {
    display: block;
    width: 100%;
    height: 70px;
    border: 1px solid gainsboro;
    font-weight: bold;
    font-size: 1rem !important;
    padding: 1rem;
  }
  .create:focus {
    outline: none !important;
  }
</style>