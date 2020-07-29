<template>
  <div>

  </div>
</template>

<script>
  export default {
    name: "Logout",
    methods: {
      logout() {
        this.$store.commit("setToken",null)
        this.$cookies.remove("id_token")
        this.$router.push('/')
      }
    },
    created() {
      this.logout()
    }
  }
</script>

<style scoped>

</style>