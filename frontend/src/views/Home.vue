<template>
  <div>
    <LoadingSpinner v-if="isLoading"></LoadingSpinner>

    <div v-else class="home">
      <div>
        <HomeNav class="home-nav" />
      </div>
      <div class="secondary secondary-a ">
        <li class="user-item friend-item">
          <figure
            class="user-photo"
            style="background-image: url('https://i.pravatar.cc/100?u=배유진');"
          ></figure>
          <p class="user-name">
            배유진
          </p>
        </li>
        <li class="user-item friend-item">
          <figure
            class="user-photo"
            style="background-image: url('https://i.pravatar.cc/100?u=전해인');"
          ></figure>
          <p class="user-name">
            전혜인
          </p>
        </li>
        <li class="user-item friend-item">
          <figure
            class="user-photo"
            style="background-image: url('https://i.pravatar.cc/100?u=신영찬');"
          ></figure>
          <p class="user-name">
            신영찬
          </p>
        </li>
      </div>
      <div class="primary">
        <br>
        <br>
        <ArticleCardList :articles="articles" :keywords="keywords"></ArticleCardList>
      </div>
      <div class="secondary secondary-b ">
        <HashTag />
      </div>
    </div>
    <div id="bottomSensor"></div>
  </div>
</template>

<script src="https://cdnjs.cloudflare.com/ajax/libs/scrollmonitor/1.2.0/scrollMonitor.js"></script>
<script>
import HomeNav from "../components/HomeNav";
import HashTag from "../components/HashTag";
import ArticleCardList from "@/components/article/ArticleCardList.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { fetchArticles } from "@/api/index";
import scrollMonitor from "scrollmonitor";

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
      keywords:[],
      isLoading: false,
      page: 0,
    };
  },
  methods: {
    async fetchData() {
      console.log("ACTION!!!!!!!!!!!!");
      const params = {
        page: this.page++,
      };
      this.isLoading = true;
      const { data } = await fetchArticles(params);
      this.isLoading = false;
      this.keywords = data.object.keyword;
      this.articles = [...this.articles, ...data.object.article.content];
    },
    addScrollWatcher() {
      // const bottomSensor = document.querySelector(
      // ".card-body:nth-last-child(3)"
      // );
      const bottomSensor = document.querySelector("#bottomSensor");
      console.log(bottomSensor);
      const watcher = scrollMonitor.create(bottomSensor);
      watcher.enterViewport(() => {
        this.fetchData();
      });
    },
  },
  created() {
    this.fetchData();
  },
  mounted() {
    setTimeout(() => this.addScrollWatcher(), 1000);
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
.primary {
  padding: 1rem;
  padding-top: 0;
  width: 100%;
  height: 100vh;
  overflow-y: scroll; /* Add the ability to scroll */
}
.secondary-a {
  background: lightgray;
}
.secondary-b {
  /* background: #666666; */
  color: lightgray;
  width: 300px;
}
.secondary {
  padding: 1rem;
  padding-top: 70px;
}
.secondary-a {
  display: none;
}
@media (max-width: 414px) {
  .home-nav {
    font-size: 0.75rem;
  }
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
    display: block;
  }
  .secondary-b {
    order: 3;
  }
  .primary {
    order: 2;
    width: 60%;
    padding-top: 70px;
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

/* Hide scrollbar for Chrome, Safari and Opera */
.primary::-webkit-scrollbar {
  /* display: none; */
  opacity: 0.5;
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

.user-item {
  margin: 1rem 0;
  display: flex;
}

.user-photo {
  width: 50px;
  height: 50px;
  flex-shrink: 0;
  border: 2px solid #333;
  border-radius: 50%;
  background-color: rgb(144, 153, 240);
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  margin-right: 0.5em;
}
.friend-item {
  align-items: center;
}
.user-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
