<template>
  <div class="profile-info">
    <section class="about-area" id="about">
      <div class="picture">
        <img src="https://i.pravatar.cc/400?u=정윤환" />
      </div>
      <div class="text">
        <div class="description">{{this.$route.params.nickname}}</div>
        <div class="intro">{{userInfo.userInfo.info}}</div>
        <ul>
          <li>
            <div class="info">
              <i class="far fa-calendar-alt"></i>
              <span>&nbsp;&nbsp;From&nbsp;</span>
              <span>&nbsp;{{userInfo.userInfo.createdate}}</span>
            </div>
          </li>

          <li>
            <div class="info">
              <i class="fas fa-envelope"></i>
              {{userInfo.userInfo.email}}
            </div>
          </li>
          <li v-if="userInfo.skills.length!==0">
            <div class="skills">
              <span>#{{userInfo.skills[0][0]}}</span>
              <span>#{{userInfo.skills[1][0]}}</span>
              <span>#{{userInfo.skills[2][0]}}</span>
              <span class="more">➕</span>
            </div>
          </li>
        </ul>
        <div class="sns">
          <a>
            <i class="fas fa-pencil-alt"></i>
            <br />
            <span>{{userInfo.totalArticleCount}} TILs</span>
          </a>
          <a class="follower-modal">
            <i class="fas fa-eye"></i>
            <br />
            <span>{{userInfo.follower.length}} Followers</span>
          </a>
          <a class="following-modal">
            <i class="fas fa-assistive-listening-systems"></i>
            <br />
            <span>{{userInfo.following.length}} Followings</span>
          </a>
        </div>
      </div>
    </section>
    <section v-if="userInfo.totalArticleCount===0" class="no-article">작성한 글이 없습니다.</section>

    <button id="myBtn" style="display:none">Open Modal</button>

    <!-- The SkillModal -->
    <div id="skillModal" class="modal">
      <!-- Modal content -->
      <div class="modal-content">
        <span class="close">&times;</span>
        <span
          v-for="skill in this.userInfo.skills"
          :key="skill[0]"
          class="totalSkills"
        >#{{skill[0]}}</span>
        <SelectSkills v-if="this.$store.state.nickname===this.$route.params.nickname"></SelectSkills>
      </div>
    </div>

    <!-- The followers -->
    <div id="followersModal" class="modal">
      <!-- Modal content -->
      <div class="follow-modal-content">
        <span class="close">&times;</span>
        <br />
        <div
          v-for="follower in this.userInfo.follower"
          :key="follower.email"
          class="follower-email-container"
        >
          <span class="follower-email">{{follower.email}}</span>
          <button class="modal-follow">팔로우</button>
        </div>
        <div v-if="this.userInfo.follower.length===0">팔로우한 친구가 없습니다.</div>
      </div>
    </div>

    <div id="followingsModal" class="modal">
      <!-- Modal content -->
      <div class="follow-modal-content">
        <span class="close">&times;</span>
        <br />
        <div
          v-for="following in this.userInfo.following"
          :key="following.email"
          class="follower-email-container"
        >
          <span class="follower-email">{{following.email}}</span>
          <button class="modal-follow">팔로잉</button>
        </div>
        <div v-if="this.userInfo.following.length===0">팔로잉한 친구가 없습니다.</div>
      </div>
    </div>
  </div>
</template>

<script>
import SelectSkills from "@/views/SelectSkills.vue";
export default {
  // props: ["userInfo"],
  props: {
    userInfo: Object,
  },
  components: {
    SelectSkills,
  },
  data() {
    return {
      skills: this.userInfo.skills,
    };
  },
  updated() {
    //스킬
    const skillModal = document.getElementById("skillModal");
    const btn = document.querySelector(".more");
    const span = document.getElementsByClassName("close")[0];
    btn.onclick = function () {
      skillModal.style.display = "block";
    };
    span.onclick = function () {
      skillModal.style.display = "none";
    };
    // follower modal
    const followerModal = document.getElementById("followersModal");
    const followBtn = document.querySelector(".follower-modal");
    const followSpan = document.getElementsByClassName("close")[1];
    followBtn.onclick = function () {
      followerModal.style.display = "block";
    };
    followSpan.onclick = function () {
      followerModal.style.display = "none";
    };

    // following modal
    const followingModal = document.getElementById("followingsModal");
    const followingBtn = document.querySelector(".following-modal");
    const followingSpan = document.getElementsByClassName("close")[2];
    followingBtn.onclick = function () {
      followingModal.style.display = "block";
    };
    followingSpan.onclick = function () {
      followingModal.style.display = "none";
    };
    window.onclick = function (event) {
      if (
        event.target == skillModal ||
        event.target == followerModal ||
        event.target == followingModal
      ) {
        skillModal.style.display = "none";
        followerModal.style.display = "none";
        followingModal.style.display = "none";
      }
    };
  },
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
.no-article {
  text-align: center;
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

.totalSkills {
  display: inline-block;
  border: 1px solid #9fa3af;
  padding: 6px 15px;
  border-radius: 25px;
  min-width: 65px;
  font-weight: 600;
  margin: 0.5rem 0.25rem;
  text-align: center;
}

.skills span {
  display: inline-block;
  min-width: 30px;
  font-weight: 600;
  margin: 4px 4px;
  text-align: center;
}
/* The Modal (background) */
.modal {
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 1; /* Sit on top */
  padding-top: 100px; /* Location of the box */
  left: 0;
  top: 0;
  width: 100%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0, 0, 0); /* Fallback color */
  background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
  background-color: #fefefe;
  margin: auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}

/* The Close Button */
.close {
  color: #aaaaaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

.follow-modal-content {
  background-color: #fefefe;
  margin: auto;
  padding: 20px;
  border: 1px solid #888;
  width: 40%;
}
.follower-email-container {
  margin: 1rem 1rem;
  display: flex;
  justify-content: space-between;
  line-height: 30px;
}
.follower-email-container > button {
  background-color: #0095f6;
  border: #0095f6;
  border-radius: 4px;
  padding: 5px 9px;
  font-size: 14px;
  color: white;
}
.follower-email-container > button:hover {
  cursor: pointer;
}

.follow-modal-content {
  background-color: #fefefe;
  margin: auto;
  padding: 20px;
  border: 1px solid #888;
  width: 40%;
}
.follower-email-container {
  margin: 1rem 1rem;
  display: flex;
  justify-content: space-between;
  line-height: 30px;
}
.follower-email-container > button {
  background-color: #0095f6;
  border: #0095f6;
  border-radius: 4px;
  padding: 5px 9px;
  font-size: 14px;
  color: white;
}
.follower-email-container > button:hover {
  cursor: pointer;
}
</style>