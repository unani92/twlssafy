<template>
  <li>
    <div class="title">
      {{ article.title }}
    </div>
    <div class="thumbnail"></div>
    <div class="contents">
      {{ article.contents }}
    </div>
    <div class="time">
      {{ article.createdAt }}
    </div>
  </li>
</template>

<script>
export default {
  props: {
    article: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style>
.thumbnail {
  background-image: url("https://picsum.photos/300/200");
  width: 300px;
  height: 200px;
}
</style>
