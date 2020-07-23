<template>
  <div class="card-body">
    <div class="blog-card">
      <div class="inner-part">
        <label for="imgTap" class="img">
          <img class="img-1" src="https://picsum.photos/300/200" />
        </label>
        <p><br></p>
        <div class="content content-1">
          
          <div class="title">
            {{ article.title }}
          </div>
          <div class="text">
            {{ article.content }}
          </div>
          <div style="font-size : 14px; text-align : right; margin-bottom : 10px;">{{ article.createdat }}</div>
          <div class = "nicknamePinLikes">
            <div style="float : left; ">
              {{article.nickname}}
            </div>
            <div class = "btns" style="text-align : right;">
              <button class="firstBtn"><i class="far fa-bookmark"></i></button>
              <!-- <button class="firstBtn"><i class="fas fa-bookmark"></i></button> -->
              <!-- <button><i class="far fa-heart"></i></button> -->
              <button><i class="fas fa-heart"></i></button>
            </div>
          </div>
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
  margin-bottom: 30px;
  margin-top: 10px;
}
.content .text {
  color: #4e4a67;
  font-size: 19px;
  margin-bottom: 30px;
  line-height: 1.5em;
  text-align: justify;
  height : 80px;
}
.content button {
  display: inline-flex;
  padding: 5px 10px;
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
@media (max-width: 630px) {
  .inner-part{
    display:block;
    height : 200px;
    padding: 20px;
  }
  .blog-card{
    height : 450px;
  }
  .content {
    margin: 0%;
    width : 100%;

  }
  .text {
    height : 40px;
  }
  .content .title {
  margin-bottom: 10px;
  margin-top: 0px;
  }

  .content button {
  display: inline-block;
  }
 
}
@media (min-width: 1024px) {

}
</style>
