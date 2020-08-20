<template>
  <div class="card-body">
    <div class="blog-card">
      <div class="inner-part">
        <label for="imgTap" class="img" @click=" $router.push({name: 'ArticleDetail', params: { id: article.articleid },})" >
          <img v-if="article.imgurl != null" class="img-1" :src="article.imgurl" />
          <img v-else-if="article.articleid%4==0" class="img-1" src="@/assets/image/preview0.png"/>
          <img v-else-if="article.articleid%4==1" class="img-1" src="@/assets/image/preview1.png"/>
          <img v-else-if="article.articleid%4==2" class="img-1" src="@/assets/image/preview2.png"/>
          <img v-else class="img-1" src="@/assets/image/preview3.png"/>
        </label>
        <p>
        </p>
        <div class="content">
          <div style="margin-bottom : 10px;">
            <div
              class="title"
              style="cursor:pointer;"
              @click="
                $router.push({
                  name: 'ArticleDetail',
                  params: { id: article.articleid },
                })
              ">
              {{ article.title }}
            </div>
          </div>
          <div style="height : 20px; overflow : hidden; margin-bottom : 10px">
            <span class="keywords" v-for="k in keywords" :key="k" >
              <span id="keyword" @click="search(k)">#{{ k }} </span>
            </span>
          </div>
          <div class="text"
          @click="
                $router.push({
                  name: 'ArticleDetail',
                  params: { id: article.articleid },
                })
              "
          >{{ article.preview }}</div>
          <div class="createdat-text">
            {{ this.$moment(article.createdat).fromNow() }}
          </div>
          <div class="nicknamePinLikes">
            <div style="float : left; ">{{ article.nickname }}</div>
            <div class="btns">
              <button @click="pin" class="firstBtn" style="background-color:white; font-size:16px; width:4%; margin : 3%;">
                <i v-if="isPinned" class="fas fa-bookmark" style="color : hotpink"> {{pinCnt}}</i>
                <i v-else class="far fa-bookmark" style="color : hotpink"> {{pinCnt}}</i>
              </button>
              <button @click="like" style="background-color:white; font-size:16px; width: 4%;margin : 3%;">
                <i v-if="isliked" class="fas fa-heart" style="color : hotpink;"> {{likesCnt}}</i>
                <i v-else class="far fa-heart" style="color : hotpink;"> {{likesCnt}}</i>
                
              </button>
              <button style="background-color:white;font-size:16px; width:4%; margin : 3%;"
              @click="
                $router.push({
                  name: 'ArticleDetail',
                  params: { id: article.articleid },
                })" >
                <i v-if="this.commentCnt>0" class="fas fa-comments" style="color : hotpink;"> {{commentCnt}}</i>
                <i v-else class="far fa-comments" style="color : hotpink;"> {{commentCnt}}</i>

              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { likeArticle, pinArticle } from "@/api/index.js";
import Swal from 'sweetalert2/dist/sweetalert2.js'
import 'sweetalert2/src/sweetalert2.scss'

export default {
  methods: {
    search(k){
      const params = {
        q: k,
        category: "keyword",
      };
      this.$router.push({name: "Dummy", params:{params}})
    },
    async like() {
      if (this.isLogin) {
        const params = {
          article_id: this.article.articleid,
        };
        const token = this.$store.state.id_token;
        const { data } = await likeArticle(params,token);
        //프런트에서 스토어 값 갱신
        const likeList = this.$store.state.likeList;
        if (data.data === "like 설정") {
          likeList.push(this.article);
          this.likesCnt++;
        } else {
          //좋아요 목록에서 삭제 로직
          // const idx = likeList.indexOf(this.article);
          const len = likeList.length;
          for (let i = 0; i < len; i++) {
            if (this.article.articleid === likeList[i].articleid) {
              likeList.splice(i, 1);
            }
          }
          this.likesCnt--
        }
        this.$store.commit("setLikeList", likeList);
      } else {
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
              this.$router.push("/login");
            } else {
              Swal.close();
            }
          });
      }
    },
    async pin() {
      if (this.isLogin) {
        const params = {
          article_id: this.article.articleid,
        };
        const token = this.$store.state.id_token;
        const { data } = await pinArticle(params,token);
        //프런트에서 스토어 값 갱신
        const pinList = this.$store.state.pinList;
        if (data.data === "pin 설정") {
          pinList.push(this.article);
          this.pinCnt++;
        } else {
          //좋아요 목록에서 삭제 로직
          const len = pinList.length;
          for (let i = 0; i < len; i++) {
            if (this.article.articleid === pinList[i].articleid) {
              pinList.splice(i, 1);
            }
          }
        this.pinCnt--
        }
        this.$store.commit("setPinList", pinList);
      } else {
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
              this.$router.push("/login");
            } else {
              Swal.close();
            }
          });
      }
    },
  },
  computed: {
    isLogin() {
      return this.$store.state.username !== "";
    },
    isliked() {
      const likeList = this.$store.state.likeList;
      const len = likeList.length;
      for (let i = 0; i < len; i++) {
        if (this.article.articleid === likeList[i].articleid) {
          return true;
        }
      }
      return false;
    },
    isPinned() {
      const pinList = this.$store.state.pinList;
      const len = pinList.length;
      for (let i = 0; i < len; i++) {
        if (this.article.articleid === pinList[i].articleid) {
          return true;
        }
      }
      return false;
    },
  },
  // },
  props: {
    article: {
      type: Object,
      required: true,
    },
    keywords: {
      type: Array,
      required: true,
    },
    pinCnt:{
      type : Number,
    },
    commentCnt: {
      type : Number,
    },
    likesCnt : {
      type : Number,
    },
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css?family=Fira+Sans:400,500,600,700,800");
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.card-body {
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: "Fira Sans", sans-serif;
  margin-bottom: 1rem;
  width: 100%;
}
.blog-card {
  height: 370px;
  width: 96%;
  max-width: 850px;
  margin: auto;
  border-radius: 25px;
  background: white;
}
.inner-part {
  display: flex;
  height: 360px;
  align-items: center;
  justify-content: center;
  padding: 0 25px;
}
.inner-part .img {
  height: 260px;
  width: 260px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 20px;
  /* box-shadow: 2px 3px 15px rgba(252, 56, 56, 0.1); */
}
.img img {
  height: 100%;
  width: 100%;
  object-fit: cover;
  cursor: pointer;
  transition: 0.6s;
}
.content {
  padding: 0 20px 0 0;
  width: 530px;
  margin-left: 30px;
  transition: 0.6s;
}
.title {
  font-size: 25px;
  font-weight: 700;
  color: #0d0925;
  margin-top: 10px;
  height: 62px;
  overflow: hidden;
}
.content .text {
  color: #4e4a67;
  font-size: 16px;
  margin-bottom: 10px;
  line-height: 1.5em;
  text-align: justify;
  height: 90px;
  overflow: hidden;
  word-break:break-all;
}
button {
  border: none;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  letter-spacing: 1px;
  border-radius: 50px;
  display: inline-flex;
  font-size: 10px;
  text-transform: uppercase;
  color: white;
  margin: 1px;
  width: 25px;
  height: 25px;
  cursor: pointer;
  outline: none;
}

.keywords {
  color: #7b7992;
  margin-bottom: 0;
  font-size: 15px;
  font-weight: 500;
}
.createdat-text {
  font-size: 14px;
  text-align: right;
  margin-bottom: 10px;
}
.btns {
  text-align: right;
}
#keyword{
  cursor:pointer; 
  color : royalblue;
}
@media (max-width: 768px) {
  .inner-part {
    display: block;
    height: 200px;
    padding: 20px;
  }
  .blog-card {
    height: 450px;
  }
  .content {
    margin: 0%;
    width: 100%;
    font-size: 16px;
    height: 70px;
  }
  .content .text {
    font-size: 16px;
    height : 50px;
  }
  .title {
    font-size: 20px;
    margin-bottom: 10px;
    margin-top: 0px;
    height : 50px;
  }

  button {
    margin : 5%;
  }
}
</style>