<template>
  <div class="article-list-body">
    <ArticleCard
      v-for="(article,index) in articles"
      :key="article._id"
      :article="article"
      :keywords="keywords[index]"
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
      type: Array,
      required: true,
    },
    keywords : {
      type : Array,
      required: true,
    }
  },
};
</script>

<style></style>
