<template>
  <div style="padding-top: 150px" v-if="!this.$store.getters.isLoggedIn">
    <LoadingSpinner/>
  </div>
</template>

<script>
  import LoadingSpinner from "../../components/common/LoadingSpinner";
  export default {
    name: "LoginLoading",
    components: {
      LoadingSpinner
    },
    props: {
      redirect: {
        type: Number || String,
        required: false
      }
    },
    updated() {
      if (this.$store.getters.isLoggedIn) {
        if (this.redirect) {
          this.$router.push({ name: 'ArticleDetail', params: { id: Number(this.redirect) } })
        } else this.$router.push('/')
      }
    },
  }
</script>

<style scoped>

</style>