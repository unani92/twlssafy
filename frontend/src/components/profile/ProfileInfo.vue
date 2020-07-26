<template>
  <div class="profile-info">
    <section class="about-area" id="about">
      <div class="picture">
        <img src="https://i.pravatar.cc/400?u=정윤환" />
      </div>
      <div class="text">
        <div class="description">{{this.$route.params.nickname}}</div>
        <div class="intro">{{userInfo.info}}</div>
        <ul>
          <li>
            <div class="info">
              <i class="far fa-calendar-alt"></i>
              <span>&nbsp;&nbsp;From&nbsp;</span>
              <span>&nbsp;{{userInfo.createdate}}</span>
            </div>
          </li>

          <li>
            <div class="info">
              <i class="fas fa-envelope"></i>
              {{userInfo.email}}
            </div>
          </li>
          <li>
            <!-- <div class="info">
              <i class="fas fa-phone"></i> +82 10-1234-5678
            </div>-->
            <div class="skills">
              <span>HTML</span>
              <span>CSS</span>
              <span>Javascript</span>
              <span class="more">➕</span>
            </div>
          </li>
        </ul>
        <div class="sns">
          <a>
            <i class="fas fa-pencil-alt"></i>
            <br />
            <span>100 TILs</span>
          </a>
          <a>
            <i class="fas fa-eye"></i>
            <br />
            <span>22 Followers</span>
          </a>
          <a>
            <i class="fas fa-assistive-listening-systems"></i>
            <br />
            <span>33 Followings</span>
          </a>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  props: ["userInfo"],
};
</script>

<style>
ul {
  list-style-type: none;
}
.profile-info {
  max-width: 850px;
  margin: auto;
}
/* SECTION CONTAINER */
.section-container {
  max-width: 1000px;
  margin: 0 auto;
}

/* SECTION */
section {
  padding: 10px 0;
}

/* ABOUT AREA */
.about-area > .picture {
  display: block;
  text-align: center;
  padding: 20px;
}

@media (min-width: 992px) {
  .about-area > .picture {
    width: 40%;
    display: inline-block;
  }
}

.about-area > .picture > img {
  max-width: 300px;
  max-height: 300px;
  border-radius: 50%;
}
.name {
  font-size: 25px;
  /* text-align: center; */
  padding: 15px 0;
}

.description {
  font-size: 14px;
  font-family: "Noto Sans KR", sans-serif;
}

.about-area > .text {
  display: block;
  width: 96%;
  padding: 30px;
  background-color: white;
  word-break: break-all;
  margin: auto;
  border-radius: 25px;
}

@media (min-width: 992px) {
  .about-area > .text {
    display: inline-block;
    width: 60%;
  }
}

.about-area > .text * {
  font-size: 16px;
}

.about-area > .text > .intro {
  padding: 20px 0;
  font-family: "Noto Sans KR", sans-serif;
  letter-spacing: 1.5px;
}

.about-area > .text > ul {
  padding: 20px 0;
}

.about-area > .text > ul > li {
  height: 40px;
}

.about-area > .text > ul > li > .info {
  color: #777;
}

.about-area > .text > ul > li > .info > i {
  color: royalblue;
}

.about-area > .text > .sns {
  text-align: right;
  display: flex;
  /* justify-content: space-between; */
}
.more:hover {
  cursor: pointer;
  background-color: lightblue;
}
@media (max-width: 414px) {
  .about-area > .text > .sns > a {
    /* text-align: center; */
    margin: 0 auto;
  }
  .about-area > .text > .sns > a > span {
    font-size: 12px;
  }
}

@media (min-width: 992px) {
  .about-area > .text > .sns {
    text-align: left;
  }
}

.about-area > .text > .sns > a {
  padding: 1rem;
  text-align: center;
  border-radius: 5px;
}

.about-area > .text > .sns > a:hover {
  background-color: #f0f0f0;
  cursor: pointer;
}

.skills span {
  display: inline-block;
  border: 1px solid #9fa3af;
  padding: 6px 15px;
  border-radius: 25px;
  min-width: 65px;
  /* font-weight: 600; */
  color: #9fa3af;
  margin: 0 1%;
  text-align: center;
}
</style>