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
      userinfo: {},
    };
  },
  methods: {
    setUserInfo(userInfo) {
      this.userinfo = userInfo;
    },
  },
};
</script>
<style>
  .profile{
    padding-top : 60px;
  }
</style>
