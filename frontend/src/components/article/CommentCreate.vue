<template>
  <div class="comment-create">
    <div class="comment">
      <span style="font-size: 20px; font-weight: bold">{{ commentList.length }} 개의 댓글</span>
      <button @click="submitComment">댓글 작성</button>
    </div>
    <div>
      <textarea v-model="content" placeholder="댓글을 작성해 주세요" class="create"/>
    </div>
     <button class="commentBtn" @click="readComment">
          <div v-if="!toggle"> 댓글 보기</div>
          <div v-else> 댓글 닫기</div>
    </button>

    <div v-if="commentList.length && toggle">
      <CommentDetail
          v-for="comment in commentList"
          :comment="comment"
          :key="comment.commentid"
          @commentDelete="removeDelete"
      />
    </div>
  </div>
</template>

<script>
  import { createComment } from "../../api";
  import CommentDetail from "./CommentDetail";
  export default {
    name: "CommentCreate",
    props: {
      commentList: Array,
      article: Object
    },
    components: {
      CommentDetail
    },
    data() {
      return {
        content: null,
        toggle : true
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
        const id_token = this.$store.state.id_token
        createComment(params, id_token)
          .then(res => {
            const now = new Date()
            const comment = { ...res.data.object, now }
            this.commentList = [comment, ...this.commentList]
            this.content = null
          })
          .catch(err => console.log(err))
      },
      removeDelete(id) {
        this.commentList = this.commentList.filter(comment => {
          return Number(comment.commentid) !== Number(id)
        })
      },
      readComment(){
        if(this.toggle)
          this.toggle = false;
        else this.toggle = true;
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
    background-color: #94adff;
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
    margin-bottom: 2rem;
  }
  .create:focus {
    outline: none !important;
  }
</style>