<template>
  <div>

  </div>
</template>

<script>
  import { mapMutations } from "vuex"
  export default {
    name: "Logout",
    methods: {
      ...mapMutations(["clearUser"]),
      logout() {
        this.clearUser();
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