<template>
  <div class="profile">
    <ProfileInfo :userInfo="userinfo"></ProfileInfo>
    <MyArticleCardList @setUserInfo="setUserInfo"></MyArticleCardList>
  </div>
</template>

<script>
import ProfileInfo from "@/components/profile/ProfileInfo.vue";
import MyArticleCardList from "@/components/profile/MyArticleCardList.vue";

export default {
  components: {
    ProfileInfo,
    MyArticleCardList,
  },
  data() {
    return {
      userinfo: "",
    };
  },
  methods: {
    setUserInfo(userInfo) {
      // userInfo: this.userInfo, skills:data.object.keywords, following:data.object.followList,
      this.userinfo = userInfo;
    },
  },
  destroyed() {
    this.$store.commit("setUserSkills", []);
  },
};
</script>

<style>
</style>
