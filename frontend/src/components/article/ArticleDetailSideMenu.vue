<template>
  <div class="sidemenu">
    <div class="icon">
      <i @click="clickFollow" class="far fa-user" :class="{pressed : sideMenu.isFollowed}"></i>
      <span v-if="sideMenu.isFollowed"> - </span>
      <span v-else> + </span>
    </div>
    <div class="icon">
      <i @click="clickLike" class="far fa-heart" :class="{pressed : isLiked}"></i>
      <span style="font-size: 1rem">{{ sideMenu.cntLikes }}</span>
    </div>
    <div class="icon">
      <i @click="clickPin" class="far fa-bookmark" :class="{pressed : isPinned}"></i>
      <span style="font-size: 1rem">{{ sideMenu.cntPin }}</span>
    </div>
  </div>
</template>

<script>
  import { likeArticle, pinArticle, requestFollow } from "../../api";

  export default {
    name: "ArticleDetailSideMenu",
    computed: {
      likeList() {
        return this.$store.state.likeList
      },
      pinList() {
        return this.$store.state.pinList
      },
      followList() {
        return this.$store.state.followList
      }
    },
    props: {
      sideMenu: Object,
      article: Object
    },
    data() {
      return {
        isLiked: null,
        isPinned: null,
      }
    },
    methods: {
      clickLike() {
        const article_id = this.article.articleid
        const email = this.$store.state.username
        if (!email) {
          this.$router.push({name: "Login"})
        } else {
         const params = {
          article_id,
          email
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
      clickPin() {
        const article_id = this.article.articleid
        const email = this.$store.state.username
        if (!email) {
          this.$router.push({name: "Login"})
        } else {
          const params = {
            article_id,
            email
          }
          pinArticle(params)
            .then(res => {
              const result = res.data.data
              const pinList = this.pinList
              if (result === "pin 취소") {
                this.sideMenu.cntPin -= 1
                const newPin = pinList.filter(pin => {
                  return Number(pin.articleid) !== Number(this.article.articleid)
                })
                this.isPinned = false
                this.$store.commit("setPinList",newPin)
              } else  {
                this.sideMenu.cntPin += 1
                pinList.push(this.article)
                this.isPinned = true
                this.$store.commit("setPinList",pinList)
              }
            })
          .catch(err => console.log(err))
        }
      },
      clickFollow() {
        console.log("click")
        const email = this.$store.state.username
        const follow = this.article.email
        const params = {
          email,
          follow
        }
        requestFollow(params)
          .then(res => {
            const result = res.data.data
            const followLists = this.followList
            if (result === "unfollow") {
              const newFollowList = followLists.filter(followList => {
                return followList.followemail !== this.article.email
              })
              this.sideMenu.isFollowed = false
              this.$store.commit("setFollowList",newFollowList)
            } else {
              const newFollow = {
                email: email,
                followemail: follow
              }
              this.sideMenu.isFollowed = true
              followLists.push(newFollow)
              this.$store.commit("setFollowList",followLists)
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
    left: 0;
    width: 10%;
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
  @media (max-width: 414px) {
    .sidemenu {
      bottom: 1px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      height: 75px;
    }
    .icon {
      border-radius: 50px;
      border: 1px solid black;
      -moz-border-radius:50px;
      -webkit-border-radius:50px;
      width: 50px;
      height: 50px;
      font-size: 18px;
    }
  }
  @media (min-width: 415px) {
    .sidemenu {
      left: 2rem ;
      top: 200px ;
    }
  }
</style>