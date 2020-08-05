<template>
  <div class="detail-profile">
    <img :src="photo" alt="" @click="gotoWriterPage" />
    <div style="margin-left : 15px;">
      <h2 @click="gotoWriterPage" class="writer-page">
        {{ article.nickname }}
      </h2>
      <span id = "info">{{userinfo}} </span>
    </div>
  </div>
</template>

<script>
export default {
  name: "ArticleDetailProfile",
  props: {
    article: Object,
    userinfo : String,
  },
  methods: {
    gotoWriterPage() {
      this.$router.push({
        name: "Profile",
        params: { nickname: this.article.nickname },
      });
    },
  },
  computed: {
    photo() {
      return `https://api.adorable.io/avatars/100/${this.article.email}.png`;
    },
  },
  mounted() {
    console.log(this.userinfo)
    console.log(this.article);
  },
};
</script>

<style scoped>
.detail-profile {
  display: flex;
}
.profile {
  margin-left: 2rem;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}
img {
  border-radius: 50%;
  width: 45px;
  height : 45ax;
}
.writer-page {
  cursor: pointer;
}
h2 {
  font-size: 18px;
  margin: 3px 0 2px 0;
}
#info {
  font-size : 14px;
}
</style>
