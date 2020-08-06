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
        console.log(params.params)
        this.$router.push({ name: "ArticleSearchByStack", query: params.params })
      } else this.$router.push('/')
    },
    // mounted() {
    //   console.log(this.$route)
    // }
  }
</script>

<style scoped>

</style>