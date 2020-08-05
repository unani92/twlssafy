<template>
  <div class="detail-profile">
    <img :src="photo" alt @click="gotoWriterPage" />
    <div style="margin-left : 15px;">
      <div @click="gotoWriterPage" class="writer-page">
        <div
          :style="{ backgroundImage:'url('+require('@/assets/image/medal-'+grade+'.png')+')'}"
          class="grade"
        ></div>
        <span style="font-weight:bold; font-size:1.25rem">{{ article.nickname }}</span>
      </div>
      <span id="info">{{userinfo}}</span>
    </div>
  </div>
</template>

<script>
import { getGrade } from "@/utils/calcGrade";

export default {
  name: "ArticleDetailProfile",
  props: {
    article: Object,
    userinfo: String,
    articleCount: Number,
  },
  data() {
    return {
      grade: getGrade(this.articleCount),
    };
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
    console.log(this.userinfo);
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

.grade {
  display: inline-block;
  width: 24px;
  height: 24px;
  flex-shrink: 0;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  margin-right: 0.5em;
}
img {
  border-radius: 50%;
  width: 45px;
  height: 45ax;
}
.writer-page {
  cursor: pointer;
}
h2 {
  font-size: 18px;
  margin: 3px 0 2px 0;
}
#info {
  font-size: 14px;
}
</style>
