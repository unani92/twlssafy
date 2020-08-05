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
      if (this.$route.params.params !== undefined) {
        const params = this.$route.params.params
        this.$router.push({ name: "ArticleSearchByStack", query: params });
      } else this.$router.push({name: "ArticleDetail", params: {id: this.$route.params.id}})
    },
    // mounted() {
    //   console.log(this.$route)
    // }
  }
</script>

<style scoped>

</style>