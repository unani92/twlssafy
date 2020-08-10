<template>
  <div>

  </div>
</template>

<script>
  export default {
    name: "Dummy",
    props: {
      id: Number && String
    },
    methods: {

    },
    created() {
      // this.$router.push({ name: "ArticleSearchByStack", query: params })
      const params = this.$route.params
      if (params.id !== undefined) {
        this.$router.push({ name: "ArticleDetail", params: {id: params.id} })
      } else if (params.params !== undefined) {
        this.$router.push({ name: "ArticleSearchByStack", query: params.params })
      } else if(params.userInfo !== undefined){
        this.$router
        .push({
          name: "Profile",
          params: { nickname: params.userInfo.userInfo.nickname },
        })
      } else if(params.data !== undefined){
        this.$router.push({ name: "CalendarCardList", params : {data : params.data}})
      } else if(params.following !== undefined){
        this.$router
        .push({
          name: "Profile",
          params: { nickname : params.following },
        })
      }else this.$router.push('/')
    },
  }
</script>

<style>
* {

font-family: 'Montserrat', sans-serif;
}
</style>