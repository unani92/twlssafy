<template>
  <div class="notices">
    <div class="notice" :class="{disabled : noti.ready === 1}" v-if="noti.type === 'like'">
      <i style="color: crimson" class="far fa-heart"></i>
      <div class="unread" :id="noti.notificationid" style="cursor: pointer" @click="goToPage">
        <p>{{ noti.other }} 님이</p>
        <p>{{ noti.content.slice(0,9) }}... 글을 좋아합니다.</p>
      </div>
      <i class="fas fa-times"></i>
    </div>
    <div class="notice" :class="{disabled : noti.ready === 1}" v-else-if="noti.type === 'pin'">
      <i style="color: crimson" class="far fa-bookmark"></i>
      <div :id="noti.notificationid" style="cursor: pointer" @click="goToPage">
        <p>{{ noti.other }} 님이</p>
        <p>{{ noti.content.slice(0,9) }}... 글을 찜했습니다.</p>
      </div>
      <i class="fas fa-times"></i>
    </div>
    <div class="notice" :class="{disabled : noti.ready === 1}" v-else>
      <i style="color: crimson" class="far fa-user"></i>
      <div>
        <p>{{ noti.other }} 님이</p>
        <p>{{ noti.content.slice(0,9) }}... 당신을 팔로우합니다.</p>
      </div>
      <i class="fas fa-times"></i>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
  export default {
    name: "Notification",
    props: {
      noti: Object
    },
    methods: {
      goToPage(event) {
        const notificationId = event.target.parentNode.id
        console.log(notificationId)
        axios.get(`http://localhost:8080/notification/${notificationId}`)
          .then(res => {
            this.noti.ready = 1
            this.$router.push({name: 'ArticleDetail', params: {id: this.noti.articleid}})
            if(res.data.status)
              this.$store.state.notificationCnt--;
            }
          )
          .catch(err => {
            console.log(err)
            }
          )
      }
    }
  }
</script>

<style scoped>
  .notices {
    text-align: right;
    width: 300px;
    border-radius: 5px;
    border: gainsboro 1px solid;
    background-color: ghostwhite;
  }
  .notice {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem;
  }
  i {
    font-size: 20px;
  }
  .disabled {
    background-color: gainsboro;
    color: #9fa3af;
    cursor: none;
  }
</style>