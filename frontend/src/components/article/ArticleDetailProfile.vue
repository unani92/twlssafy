<template>
  <div class="detail-profile">
    <img :src="photo" alt="" />
    <div class="profile">
      <h2 @click="gotoWriterPage" class="writer-page">
        {{ article.nickname }}
      </h2>
      <span>User First, Mobile Friendly!! </span>
    </div>
  </div>
</template>

<script>
export default {
  name: "ArticleDetailProfile",
  props: {
    article: Object,
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
}
.writer-page {
  cursor: pointer;
}
</style>
