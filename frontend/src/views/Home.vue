<template>
  <div>

    <div class="home">
      <div>
        <HomeNav class="home-nav" />
      </div>
      <div class="secondary secondary-a ">
        <div class="friends">
          <p>💌 Friends 💌</p>
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
      </div>
      <div class="primary">
        <p>⚡ News ⚡</p>
        <br />
        <ArticleCardList></ArticleCardList>
      </div>
      <div class="secondary secondary-b ">
        <HashTag />
      </div>
    </div>
  </div>
</template>

<script src="https://cdnjs.cloudflare.com/ajax/libs/scrollmonitor/1.2.0/scrollMonitor.js"></script>
<script>
import HomeNav from "../components/HomeNav";
import HashTag from "../components/HashTag";
import ArticleCardList from "@/components/article/ArticleCardList.vue";

export default {
  components: {
    HomeNav,
    HashTag,
    ArticleCardList,
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
.friends {
  position: fixed;
  width: inherit;
}
.primary {
  padding: 1rem;
  padding-top: 0;
  width: 100%;
  height: 100vh;
  /*overflow-y: scroll; /* Add the ability to scroll */
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

.secondary {
  background-color: #eee;
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
