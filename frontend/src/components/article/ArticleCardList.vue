<template>
  <div>
    <ArticleCard
      v-for="article in articles"
      :key="article._id"
      :article="article"
    ></ArticleCard>
  </div>
</template>

<script>
import ArticleCard from "@/components/article/AricleCard.vue";

export default {
  components: {
    ArticleCard,
  },
  props: {
    articles: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style></style>
