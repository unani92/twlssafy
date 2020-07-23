<template>
  <div>
    <LoadingSpinner v-if="isLoading"></LoadingSpinner>

    <div v-else class="home">
      <div>
        <HomeNav class="home-nav" />
      </div>
      <!-- <div class="secondary secondary-a ">
        <Friends />
      </div> -->
      <div class="primary">
        <ArticleCardList :articles="articles"></ArticleCardList>
      </div>
      <div class="secondary secondary-b ">
        <HashTag />
      </div>
    </div>
  </div>
</template>

<script>
import HomeNav from "../components/HomeNav";
import HashTag from "../components/HashTag";
import ArticleCardList from "@/components/article/ArticleCardList.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { fetchArticles } from "@/api/index";

export default {
  components: {
    HomeNav,
    HashTag,
    ArticleCardList,
    LoadingSpinner,
  },
  data() {
    return {
      articles: [],
      isLoading: false,
      page: 0,
    };
  },
  methods: {
    async fetchData() {
      const params = {
        page: this.page++,
      };
      this.isLoading = true;
      const { data } = await fetchArticles(params);
      this.isLoading = false;
      this.articles = data.object.article.content;
      console.log(this.articles);
    },
  },
  created() {
    this.fetchData();
  },
};
</script>

<style scoped>
.home-nav {
  display: flex;
  height: 60px;
  align-items: center;
  padding: 0 1rem;
}

.home {
  padding-top: 60px;
}
/* .main {
  display: flex;
  justify-content: space-between;
} */
.primary {
  padding: 1rem;
  padding-top: 70px;
}
.secondary-a {
  background: white;
}
.secondary-b {
  /* background: #666666; */
  color: white;
  width: 300px;
}
.secondary {
  padding: 1rem;
  padding-top: 70px;
}
@media (min-width: 1024px) {
  .home {
    display: flex;
    flex-wrap: wrap;
  }
  .header {
    width: 100%;
  }
  .nav {
    width: 100%;
  }
  .secondary {
    width: 20%;
  }
  .secondary-a {
    order: 1;
  }
  .secondary-b {
    order: 3;
  }
  .primary {
    order: 2;
    width: 60%;
  }
  .footer {
    order: 4;
    width: 100%;
    background-color: lightgray;
  }
}
@media (min-width: 1440px) {
  .secondary {
    width: 300px;
  }
  .primary {
    /* width: calc(100% - 600px); */
    /* flex-grow:1 */
    flex: 1 1 auto;
    /* fixed된 형태로 배치되고 싶다면 width를 사용하기를 추천한다. 줄바꿈이 상관 없다면 -예를 들면, 해쉬태그- flex로 유연하게 하는 것이 좋음 */
  }
}
/* scroll */
.primary {
  background-color: #eee;
  width: 60%;
  height: 100vh;
  overflow-y: scroll; /* Add the ability to scroll */
}

/* Hide scrollbar for Chrome, Safari and Opera */
.primary::-webkit-scrollbar {
  display: none;
}

/* Hide scrollbar for IE, Edge and Firefox */
.primary {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}

.secondary {
  background-color: #eee;
  /* width: 60%; */
  height: 100%;
  overflow-y: scroll; /* Add the ability to scroll */
}

/* Hide scrollbar for Chrome, Safari and Opera */
.secondary::-webkit-scrollbar {
  display: none;
}

/* Hide scrollbar for IE, Edge and Firefox */
.secondary {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
</style>
