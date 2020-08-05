<template>
  <div class="profile-info">
    <section class="about-area" id="about">
      <div class="picture">
        <img src="https://i.pravatar.cc/400?u=정윤환" />
      </div>
      <div class="text">
        <div
          :style="{ backgroundImage:'url('+require('@/assets/image/medal-'+grade+'.png')+')'}"
          class="grade"
        ></div>
        <div class="description follower-email-container" style="margin:0">
          <span class="follower-email">{{ userInfo.userInfo.nickname }}</span>
          <div
            v-show="nickname !== this.$store.state.nickname"
            @click="requestFollow(userInfo.userInfo.email, $event)"
          >
            <button class="followBtn">팔로우</button>
          </div>
        </div>

        <div class="intro">{{ userInfo.userInfo.info }}</div>

        <ul>
          <li>
            <div class="info">
              <i class="far fa-calendar-alt"></i>
              <span>&nbsp;&nbsp;From&nbsp;</span>
              <span>&nbsp;{{ this.$moment(userInfo.userInfo.createdate).format("L") }}</span>
            </div>
          </li>

          <li>
            <div class="info">
              <i class="fas fa-envelope"></i>
              {{ userInfo.userInfo.email }}
            </div>
          </li>
          <li v-if="skills.length !== 0">
            <div class="skills">
              <span v-for="skill in skills" :key="skill.name"># {{ skill.name }}</span>
              <span class="more" data-toggle="modal">➕</span>
            </div>
          </li>
          <li v-else>
            등록된 관심사가 없습니다.
            <span class="more" data-toggle="modal">➕</span>
          </li>
        </ul>
        <div class="sns">
          <a>
            <i class="fas fa-pencil-alt"></i>
            <br />
            <span>{{ userInfo.totalArticleCount }} TILs</span>
          </a>
          <a class="follower-modal" data-toggle="modal">
            <i class="fas fa-eye"></i>
            <br />
            <span>{{ userInfo.follower.follower.length }} Followers</span>
          </a>
          <a class="following-modal" data-toggle="modal">
            <i class="fas fa-assistive-listening-systems"></i>
            <br />
            <span>{{ userInfo.following.follow.length }} Followings</span>
          </a>
        </div>
      </div>

      <div id="calendar" style="text-align : right">
        <Calendar :userInfo="userInfo" />
      </div>
    </section>
    <section v-if="userInfo.totalArticleCount === 0" class="no-article">작성한 글이 없습니다.</section>

    <button id="myBtn" style="display:none">Open Modal</button>

    <!-- The SkillModal -->
    <div id="skillModal" class="modal">
      <!-- Modal content -->
      <div class="modal-content">
        <span class="close">&times;</span>
        <span v-for="skill in skills" :key="skill.name" class="totalSkills"># {{ skill.name }}</span>
        <SelectSkills v-if="this.$store.state.nickname === this.$route.params.nickname"></SelectSkills>
      </div>
    </div>

    <!-- The followers -->
    <div id="followersModal" class="modal">
      <div class="follow-modal-content">
        <span class="close">&times;</span>
        <br />

        <div
          v-for="idx in userInfo.follower.follower.length"
          :key="idx"
          class="follower-email-container"
        >
          <span class="follower-email">
            {{
            userInfo.follower.followerNickname[idx - 1]
            }}
          </span>
          <div
            @click="
              requestFollow(userInfo.follower.follower[idx - 1].email, $event)
            "
          >
            <button class="followBtn">팔로우</button>
          </div>
        </div>
        <div v-if="this.userInfo.follower.follower.length === 0">팔로우한 친구가 없습니다.</div>
      </div>
    </div>

    <div id="followingsModal" class="modal">
      <div class="follow-modal-content">
        <span class="close">&times;</span>
        <br />
        <div
          v-for="idx in userInfo.following.follow.length"
          :key="idx"
          class="follower-email-container"
        >
          <span class="follower-email">
            {{
            userInfo.following.followNickname[idx - 1]
            }}
          </span>
          <div
            @click="
              requestFollow(userInfo.following.follow[idx - 1].email, $event)
            "
          >
            <button class="followBtn">팔로우</button>
          </div>
        </div>
        <div v-if="this.userInfo.following.follow.length === 0">팔로우한 친구가 없습니다.</div>
      </div>
    </div>
  </div>
</template>

<script>
import SelectSkills from "@/views/SelectSkills.vue";
import { requestFollow } from "@/api/index.js";
import { mapState, mapGetters } from "vuex";
import Calendar from "../calendar/Calendar";

export default {
  props: {
    userInfo: Object,
  },
  components: {
    SelectSkills,
    Calendar,
  },
  data() {
    const userSkills = this.$store.state.userSkills;
    let grade = 0;
    if (this.userInfo.totalArticleCount === 0) {
      grade = 1;
    } else if (this.userInfo.totalArticleCount <= 10) {
      grade = 2;
    } else if (this.userInfo.totalArticleCount <= 30) {
      grade = 3;
    } else {
      grade = 4;
    }
    return {
      skills: userSkills,
      nickname: this.$route.params.nickname,
      follower: this.userInfo.follower,
      grade: grade,
    };
  },
  computed: {
    ...mapGetters(["isLoggedIn"]),
    ...mapState(["id_token", "userSkills"]),
  },
  methods: {
    async requestFollow(followWantingTo, e) {
      if (!this.isLoggedIn) {
        console.log("login required");
        if (confirm("팔로우 하시려면 로그인을 해야 합니다")) {
          this.$router.push("/login");
        }
      } else {
        const params = {
          follow: followWantingTo,
        };
        const { data } = await requestFollow(params, this.id_token);
        console.log(data);
        // 프런트에서 처리
        const followList = this.$store.state.followList.followNickname;
        const nicknameOfThisBlog = this.$route.params.nickname;
        if (data.data === "follow") {
          followList.push(nicknameOfThisBlog);
          e.target.innerHTML = "팔로우 취소";

          //로그인한 사용자의 스토어에 this.userInfo.userInfo.email추가하기
        } else {
          e.target.innerHTML = "팔로우";

          const len = followList.length;
          for (let i = 0; i < len; i++) {
            if (nicknameOfThisBlog === followList[i]) {
              followList.splice(i, 1);
              break;
            }
          }
        }
        this.$store.commit("setFollowListByNickname", followList);
      }
    },
    isFollowing(follow) {
      //로그인한 사용자가 어떤 사용자를 팔로잉하고 있는가
      if (this.isLoggedIn) {
        // 로그인한 사용자가 팔로잉하고 있는 모든 사람
        const followList = this.$store.state.followList.followNickname;
        const len = followList.length;
        for (let i = 0; i < len; i++) {
          if (follow === followList[0]) {
            return true;
          }
        }
      }
      return false;
    },
  },
  updated() {
    if (this.$store.getters.isLoggedIn) {
      const followBtns = [
        ...document.querySelectorAll(".follower-email-container"),
      ];
      const len = followBtns.length;
      for (let i = 0; i < len; i++) {
        const followerOfLoginUser = this.$store.state.followList.followNickname;
        const numOfFollowerOFLoginUser = followerOfLoginUser.length;
        for (let j = 0; j < numOfFollowerOFLoginUser; j++) {
          // trim하지 않으면 선택자로 버튼값의 innerHTML값을 가져올 때 양쪽에 공백문자가 삽입되어 비교가 안된다.
          if (
            followBtns[i].childNodes[0].innerHTML.trim() ===
            followerOfLoginUser[j].trim()
          ) {
            followBtns[i].childNodes[1].childNodes[0].innerHTML = "팔로우 취소";
          }
        }
      }
    }
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
  beforeUpdate() {
    this.skills = this.$store.state.userSkills;
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
  padding-top: 70px;
}
.grade {
  width: 40px;
  height: 40px;
  flex-shrink: 0;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100%;
  margin-right: 0.5em;
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
@media (max-width: 992px) {
  .follow-modal-content {
    width: 80%;
  }
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
.followBtn {
  background-color: #0095f6;
  border: #0095f6;
  border-radius: 4px;
  padding: 5px 9px;
  font-size: 14px;
  color: white;
}
.followBtn:hover {
  cursor: pointer;
}
</style>
