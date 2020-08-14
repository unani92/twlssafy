<template>
  <div class="comment-create">
    <div class="comment">
      <span style="font-size: 15px; font-weight: bold"
        >{{ commentList.length }} 개의 댓글</span
      >
    </div>
    <div>
      <textarea
        v-model="content"
        placeholder="댓글을 작성해 주세요"
        class="create"
      />
      <div>
        <button class="createBtn" @click="submitComment">
          <i class="fas fa-pencil-alt">댓글 달기</i>
        </button>
      </div>
    </div>

    <div v-if="commentList.length && toggle">
      <CommentDetail
        v-for="(comment, i) in commentList"
        :comment="comment"
        :key="i"
        @commentDelete="removeDelete"
      />
    </div>
  </div>
</template>

<script>
import { createComment } from '../../api';
import CommentDetail from './CommentDetail';
import Swal from 'sweetalert2/dist/sweetalert2.js'
import 'sweetalert2/src/sweetalert2.scss'

export default {
  name: 'CommentCreate',
  props: {
    commentList: Array,
    article: Object,
    commentNickname: Array,
    commentImg: Array,
  },
  components: {
    CommentDetail,
  },
  computed: {
    isLogin() {
      return this.$store.state.username !== "";
    },
  },
  data() {
    return {
      content: null,
      toggle: true,
    };
  },
  methods: {
    submitComment() {
      if (this.isLogin) {
        const email = this.$store.state.username;
        const content = this.content;
        const articleid = String(this.article.articleid);
        const params = {
          email,
          content,
          articleid,
        };

        if(!content || !content.trim()) {
          Swal.fire({
            text:"내용을 입력해주세요.",
            icon:"error",
            closeOnClickOutside:true,
            confirmButtonText:"<span style='text-align:center; margin-left:-12px; position:relative; top:-8px;'>OK</span>",
            confirmButtonColor:"#e6837a",
          });
          return;
        }
        const id_token = this.$store.state.id_token;
        createComment(params, id_token)
          .then((res) => {
            console.log(res.data.object);
            const now = new Date();
            const comment = { ...res.data.object, now };
            this.commentList = [comment, ...this.commentList];
            this.content = null;
            console.log(this.content)
          })
          .catch((err) => console.log(err));
    }
    else {
      Swal.fire({
        text:"로그인이 필요한 서비스입니다.",
        icon:"error",
        closeOnClickOutside:true,
        showCancelButton:true,
        confirmButtonText:"<span style='text-align:center; margin-left:-12px; position:relative; top:-8px;'>OK</span>",
        confirmButtonColor:"#e6837a",
        cancelButtonText:"<span style='text-align:center; margin-left:-12px; position:relative; top:-8px;'>NO</span>",
        cancelButtonColor:"#e6837a",
        }).then((result) => {
          if(result.value){
            this.$router.push({
            name: "Login",
            query: { redirect: `${this.article.articleid}` },
          });
          } else {
            Swal.close();
          }
        });
    }
    },
    removeDelete(id) {
      this.commentList = this.commentList.filter((comment) => {
        return Number(comment.commentid) !== Number(id);
      });
    },
    readComment() {
      if (this.toggle) this.toggle = false;
      else this.toggle = true;
    },
  },
};
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
  float: left;
  margin-right: 2%;
}
.create:focus {
  outline: none !important;
}

.createBtn {
  width: 100%;
  height: 70px;
  color: #94adff;
  background-color: #eeeeee00;
  font-weight: bold;
  font-size: 15px;
  margin-bottom: 1rem;
}
</style>
