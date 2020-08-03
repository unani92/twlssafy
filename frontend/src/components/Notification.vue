<template>
  <div class="notices">
    <div class="notice" v-if="noti.type === 'like'">
      <i style="color: crimson" class="far fa-heart"></i>
      <div :id="noti.notificationid" style="cursor: pointer" @click="goToPage">
        <p>{{ noti.email }} 님이</p>
        <p>{{ noti.content.slice(0,9) }}... 글을 좋아합니다.</p>
      </div>
      <i class="fas fa-times"></i>
    </div>
    <div class="notice" v-else-if="noti.type === 'pin'">
      <i style="color: crimson" class="far fa-bookmark"></i>
      <div>
        <p>{{ noti.email }} 님이</p>
        <p>{{ noti.content.slice(0,9) }}... 글을 찜했습니다.</p>
      </div>
      <i class="fas fa-times"></i>
    </div>
    <div class="notice" v-else>
      <i style="color: crimson" class="far fa-user"></i>
      <div>
        <p>{{ noti.email }} 님이</p>
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
        axios.get(`http://localhost:8080/notification/${notificationId}`, {id_token:this.$store.state.id_token})
          .then(res => console.log(res))
          .catch(err => console.log(err))
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
</style>