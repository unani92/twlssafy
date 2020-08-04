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
      this.$router.push({name: "ArticleDetail", params: {id: this.$route.params.id}})
    }
  }
</script>

<style scoped>

</style>