<template>
  <div class="card-body">
    <div class="blog-card">
      <div class="inner-part">
        <label for="imgTap" class="img">
          <img class="img-1" src="https://picsum.photos/300/200" />
        </label>
        <div class="content content-1">
          <span>{{ article.createdat }}</span>
          <div class="title">
            {{ article.articleid }}
          </div>
          <div class="text">
            {{ article.content }}
          </div>
          <button>PIN</button>
          <button>LIKE</button>
        </div>
      </div>
    </div>
  </div>
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
@import url("https://fonts.googleapis.com/css?family=Fira+Sans:400,500,600,700,800");
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.card-body {
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: "Fira Sans", sans-serif;
  margin-bottom: 1rem;
  width: 100%;
}
.blog-card {
  height: 370px;
  width: 95%;
  max-width: 850px;
  margin: auto;
  border-radius: 25px;
  background: white;
}
.inner-part {
  display: flex;
  height: 360px;
  align-items: center;
  justify-content: center;
  padding: 0 25px;
}
.inner-part .img {
  height: 260px;
  width: 260px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 20px;
  /* box-shadow: 2px 3px 15px rgba(252, 56, 56, 0.1); */
}
.img img {
  height: 100%;
  width: 100%;
  object-fit: cover;
  cursor: pointer;
  transition: 0.6s;
}
.content {
  padding: 0 20px 0 35px;
  width: 530px;
  margin-left: 50px;
  transition: 0.6s;
}
.content span {
  display: block;
  color: #7b7992;
  margin-bottom: 15px;
  font-size: 22px;
  font-weight: 500;
}
.content .title {
  font-size: 30px;
  font-weight: 700;
  color: #0d0925;
  margin-bottom: 20px;
}
.content .text {
  color: #4e4a67;
  font-size: 19px;
  margin-bottom: 30px;
  line-height: 1.5em;
  text-align: justify;
}
.content button {
  display: inline-flex;
  padding: 15px 20px;
  border: none;
  font-size: 16px;
  text-transform: uppercase;
  color: #fff0e6;
  font-weight: 600;
  letter-spacing: 1px;
  border-radius: 50px;
  cursor: pointer;
  outline: none;
  border: 1px solid #fd3535;
  background: linear-gradient(147deg, #fe8a39 0%, #fd3838 74%);
}
@media (max-width: 376px) {
  .content {
    display: none;
  }
}
</style>
