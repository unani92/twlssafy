<template>
  <div class="hashtag">
    <p class="title">👀 Attention 👀</p>
    <div class="stacks">
      <!-- <div class="stack" v-for="stack in stacks" :key="stack"># {{ stack }}</div> -->
      <div class="stack" v-for="h in hashTag" :key="h" @click="searchByStack(h)">#{{h}}</div>
    </div>
  </div>
</template>

<script>
// import { fetchHashTags, searchArticle } from "@/api/index";
import { fetchHashTags } from "@/api/index";

export default {
  name: "HashTag",
  data() {
    return {
      hashTag: [],
    };
  },
  methods: {
    async fetchData() {
      const { data } = await fetchHashTags();
      this.hashTag = data.object;
    },
    async searchByStack(skill) {
      const params = {
        q: skill,
        category: "keyword",
      };
      // const { data } = await searchArticle(params);
      // console.log(data);
      this.$router.push({ name: "ArticleSearchByStack", query: params });
    },
  },
  created() {
    this.fetchData();
  },
};
</script>

<style scoped>
.hashtag {
  position: fixed;
  width: inherit;
  padding-right: 16px;
}
.title {
  padding-bottom: 1rem;
  color: black;
}
.stack {
  font-size: 1rem;
  color: black;
  padding: 1rem;
  border-radius: 1rem;
  font-weight: bolder;
  /* padding: 3px; */
  z-index: -1;
  width: 100%;
  text-transform: uppercase;
}
.stack:hover {
  cursor: pointer;
  background-color: lightgray;
}
</style>
