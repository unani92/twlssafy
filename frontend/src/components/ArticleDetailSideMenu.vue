<template>
  <div class="sidemenu">
    <div class="icon">
      <i class="far fa-user"></i>
      <span>{{ isLiked }}</span>
    </div>
    <div class="icon">
      <i @click="clickLike" class="far fa-heart" :class="{pressed : isLiked}"></i>
      <span style="font-size: 1rem">{{ sideMenu.cntLikes }}</span>
    </div>
    <div class="icon">
      <i class="far fa-bookmark"></i>
      <span style="font-size: 1rem">{{ sideMenu.cntPin }}</span>
    </div>
  </div>
</template>

<script>
  import { likeArticle } from "../api";

  export default {
    name: "ArticleDetailSideMenu",
    computed: {
      likeList() {
        return this.$store.state.likeList
      },
      pinList() {
        return this.$store.state.pinList
      }
    },
    props: {
      sideMenu: Object,
      article: Object
    },
    data() {
      return {
        isLiked: null,
        isPinned: null
      }
    },
    methods: {
      clickLike() {
        const params = {
          article_id: this.article.articleid,
          email: this.$store.state.username
        }
        likeArticle(params)
          .then(res => {
            const result = res.data.data
            const likeList = this.likeList
            if (result === "likes 취소") {
              this.sideMenu.cntLikes -= 1
              const newLike = likeList.filter(like => {
                return Number(like.articleid) !== Number(this.article.articleid)
              })
              this.isLiked = false
              this.$store.commit("setLikeList",newLike)
            } else {
              this.sideMenu.cntLikes += 1
              likeList.push(this.article)
              this.isLiked = true
              this.$store.commit("setLikeList",likeList)
            }
          })
          .catch(err => console.log(err))
      }
    },
    mounted() {
      const id = this.$route.params.id
      this.isLiked = !!this.likeList.filter(like => like.articleid === id).length
      this.isPinned = !!this.pinList.filter(pin => pin.articleid === id).length
    }
  }
</script>

<style scoped>
  .sidemenu {
    border-radius: 5px;
    background-color: white;
    padding: 10px;
    position: fixed;
  }
  .icon {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border-radius: 75px;
    border: 1px solid black;
    -moz-border-radius:75px;
    -webkit-border-radius:75px;
    width: 70px;
    height: 70px;
    text-align: center;
    font-size: 26px;
    padding: 5px;
    margin-bottom: 5px;
    cursor: pointer;
  }
  .pressed {
    color: crimson;
  }
</style>