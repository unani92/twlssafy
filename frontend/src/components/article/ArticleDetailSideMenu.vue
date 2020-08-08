<template>
  <div class="sidemenu">
    <div class="icon">
      <i @click="clickFollow" class="far fa-user" :class="{pressed : isFollowed}"></i>
      <span class="sideCnt" v-if="isFollowed">-</span>
      <span class="sideCnt" v-else> + </span>
    </div>
    <div class="icon">
      <i @click="clickLike" class="far fa-heart" :class="{pressed : isLiked}"></i>
      <span class="sideCnt">{{ sideMenu.cntLikes }}</span>
    </div>
    <div class="icon">
      <i @click="clickPin" class="far fa-bookmark" :class="{pressed : isPinned}"></i>
      <span class="sideCnt">{{ sideMenu.cntPin }}</span>
    </div>
  </div>
</template>

<script>
  import { likeArticle, pinArticle, requestFollow } from "../../api";
  import { mapState, mapActions } from 'vuex'
  export default {
    name: "ArticleDetailSideMenu",
    computed: {
      ...mapState(["id_token", "likeList", "pinList", "followList"])
    },
    props: {
      sideMenu: Object,
      article: Object
    },
    data() {
      return {
        id: this.$route.params.id,
        isLiked: null,
        isPinned: null,
        isFollowed: null,
      }
    },
    methods: {
      clickLike() {
        const article_id = this.article.articleid
        const email = this.$store.state.username
        if (!email) {
          this.$router.push({ name: 'Login', query: { redirect: `${article_id}` } })
        } else {
         const params = {
          article_id,
        }
        likeArticle(params, this.id_token)
          .then(res => {
            const result = res.data.data
            const likeList = this.likeList
            if (result === "like 취소") {
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
          this.$router.push({ name: 'Login', query: { redirect: `${article_id}` } })
        } else {
          const params = {
            article_id,
          }
          pinArticle(params, this.id_token)
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
        const article_id = this.article.articleid
        const email = this.$store.state.username
        const follow = this.article.email
        if (!email) {
          this.$router.push({ name: 'Login', query: { redirect: `${article_id}` } })
        } else {
         const params = {
            follow
          }
        requestFollow(params, this.id_token)
          .then(res => {
            const result = res.data.data
            const followLists = this.followList.follow
            if (result === "unfollow") {
              const newFollowList = followLists.filter(followList => {
                return followList.followemail !== this.article.email
              })
              this.isFollowed = false
              this.$store.commit("setFollowListByEmail",newFollowList)
            } else {
              const newFollow = {
                email: email,
                followemail: follow
              }
              this.isFollowed = true
              followLists.push(newFollow)
              this.$store.commit("setFollowListByEmail",followLists)
            }
          })
          .catch(err => console.log(err))
       }
      },
      ...mapActions(["getGoogleUserInfo"])
    },
    mounted() {
      if (this.id_token) {
        this.getGoogleUserInfo(this.id_token)
          .then(() => {
            this.isLiked = !!this.likeList.filter(like => Number(like.articleid) === Number(this.id)).length
            this.isPinned = !!this.pinList.filter(pin => Number(pin.articleid) === Number(this.id)).length
            this.isFollowed = !!this.followList.follow.filter(follow => follow.followemail === this.article.email).length
          })
          .catch(err => console.log(err))
      }
    }
  }
</script>

<style scoped>
  .sidemenu {
    border-radius: 35px;
    background-color: white;
    padding: 10px;
    position: fixed;
    width: 80px;    
    right: 91% ;
  }
  .icon {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border-radius: 75px;
    border: 1.5px solid black;
    -moz-border-radius:75px;
    -webkit-border-radius:75px;
    width: 50px;
    height: 50px;
    text-align: center;
    font-size: 24px;
    padding: 8px 5px 5px 5px;
    margin: 8px;
    cursor: pointer;
  }
  .pressed {
    color: crimson;
  }
  @media (max-width: 1024px) {
    .sidemenu {
      bottom: 15px;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      width: 120px;
      height: 48px;
      right : 10%;
      border: 1px solid black;
    }
    .icon {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border: none;
      width: 30px;
      height: 30px;
      text-align: center;
      font-size: 10px;
      padding: 0;
      margin: 1px;
      cursor: pointer;
    }
    .sideCnt {
    margin-top: 0;
    font-size: 11px;
    }
  }
  @media (min-width: 1025px) {
    .sidemenu {
      top: 200px ;
      display: flex;
      justify-content: center;
      flex-direction: column;
      align-items: center;
    }
    .sideCnt {
    margin-top: 2px;
    font-size: 10px;
    }
  }

  .icon {
    width: 40px;
    height : 40px;
    font-size: 17px;
  }

  /* @media (max-width: 1024px) and (min-width: 415px){
    .sidemenu {
      top: 200px ;
      display: flex;
      justify-content: center;
      flex-direction: column;
      align-items: center;
      right : 89%;
    }
    .sideCnt {
    margin-top: 2px;
    font-size: 10px;
    }

    .icon {
    width: 35px;
    height: 35px;
    font-size: 14px;
  }


  
  } */
</style>