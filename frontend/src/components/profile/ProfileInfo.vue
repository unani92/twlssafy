<template>
  <div class="profile-info">
    <section class="about-area" id="about">
      <div class="picture">
        <img v-if="userInfo.userInfo.img" :src="userInfo.userInfo.img" />
        <img v-else src="https://i.pravatar.cc/400?u=정윤환" />
        <i style="font-size: 2rem; margin-top: 2rem; cursor: pointer" class="far fa-plus-square"></i>
      </div>
      <div>
        <vue2-dropzone
          ref="imgDropZone"
          id="customdropzone"
          :options="dropzoneOptions"
          @vdropzone-complete="afterComplete"
        />
        <div v-if="images" class="image-div">
          <div v-if="images">
            <img :src="images" class="image" />
          </div>
        </div>
      </div>
      <div class="text">
        <div
          :style="{
            backgroundImage:
              'url(' + require('@/assets/image/medal-' + calGrade + '.png') + ')',
          }"
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
              <span>
                &nbsp;{{
                this.$moment(userInfo.userInfo.createdate).format('L')
                }}
              </span>
            </div>
          </li>

          <li style>
            <div class="info">
              <i class="fas fa-envelope"></i>
              {{ userInfo.userInfo.email }}
            </div>
          </li>
          <li v-if="userInfo.userInfo.github != null">
            <div class="info">
              <i class="fab fa-github"></i>
              {{ userInfo.userInfo.github }}
            </div>
          </li>
          <!-- <li style="dec" v-if="skills.length !== 0"></li> -->
        </ul>
        <li v-if="skills.length !== 0">
          <div class="skills">
            <span v-for="skill in skills" :key="skill.name">#{{ skill.name }}</span>
            <button :disabled="!isMypage" class="more" data-toggle="modal">
              <i class="far fa-plus-square"></i>
            </button>
          </div>
        </li>
        <li v-else>
          등록된 관심사가 없습니다.
          <span :disabled="!isMypage" class="more" data-toggle="modal">
            <i class="far fa-plus-square"></i>
          </span>
        </li>
        <div class="sns">
          <a style="background-color : white">
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
          <div id="calendar">
            <Calendar :userInfo="userInfo" />
          </div>
        </div>
      </div>
    </section>
    <section v-if="userInfo.totalArticleCount === 0" class="no-article">작성한 글이 없습니다.</section>

    <button id="myBtn" style="display:none">Open Modal</button>

    <!-- The SkillModal -->
    <div id="skillModal" class="modal">
      <!-- Modal content -->
      <div class="modal-content">
        <SelectSkills v-if="this.$store.state.nickname === this.$route.params.nickname"></SelectSkills>
        <div class="close2" style="text-align : center; cursor : pointer">완료</div>
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
          <span
            @click="goUserPage(userInfo.follower.followerNickname[idx - 1])"
            class="follower-email"
          >{{ userInfo.follower.followerNickname[idx - 1] }}</span>
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
          <span
            @click="goUserPage(userInfo.following.followNickname[idx - 1])"
            class="follower-email"
          >{{ userInfo.following.followNickname[idx - 1] }}</span>
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
import { requestFollow, changeImg } from "@/api/index.js";
import { mapState, mapGetters } from "vuex";
import Calendar from "../calendar/Calendar";
import { getGrade } from "@/utils/calcGrade";
// drag and drop
import firebase from 'firebase';
import vue2Dropzone from "vue2-dropzone"
import 'vue2-dropzone/dist/vue2Dropzone.min.css'
let uuid = require("uuid");

export default {
  props: {
    userInfo: Object,
  },
  components: {
    SelectSkills,
    Calendar,
    vue2Dropzone
  },
  data() {
    const userSkills = this.$store.state.userSkills;
    return {
      skills: userSkills,
      nickname: this.$route.params.nickname,
      follower: this.userInfo.follower,
      grade: 0,
      dropzoneOptions: {
        url: "https://httpbin.org/post",
        thumbnailWidth: 150,
        thumbnailHeight: 150,
        addRemoveLinks: false,
        acceptedFiles: ".jpg, .jpeg, .png",
        dictDefaultMessage: `<p class='text-default'><i class='fa fa-cloud-upload mr-2'></i> + </p>
          <p class="form-text">Allowed Files: .jpg, .jpeg, .png</p>
          `
      },
      images: null
    };
  },
  computed: {
    ...mapGetters(["isLoggedIn"]),
    ...mapState(["id_token", "userSkills"]),
    isMypage() {
      if (this.userInfo.userInfo.email == this.$store.state.username)
        return true;
      else return false;
    },
    calGrade() {
      return getGrade(this.userInfo.totalArticleCount);
    },
  },
  methods: {
    async afterComplete(upload) {
      let imageName = uuid.v1()
      const div = document.querySelector(".picture")
      const imgField = div.querySelector("img")
      this.isLoading = true
      try {
        let file = upload
        const metaData = {
          contentType: "image/png"
        }
        const storageRef = firebase.storage().ref();
        const imageRef = storageRef.child(`images/${imageName}.png`);
        await imageRef.put(file, metaData);
        const downloadURL = await imageRef.getDownloadURL();
        this.images = downloadURL
        imgField.src = this.images
        // 백앤드에 이미지 수정 요청
        changeImg(downloadURL, this.id_token)
          .then(res => console.log(res))
          .catch(err => console.log(err))
      } catch (error) {
        console.log(error);
      }
      this.$refs.imgDropZone.removeFile(upload);
    },
    goUserPage(following) {
      this.$router.push({ name: "Dummy", params: { following: following } });
    },

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
    const skillModal2 = document.getElementById("skillModal");
    const btn2 = document.querySelector(".more");
    const span2 = document.getElementsByClassName("close2")[0];
    btn2.onclick = function () {
      skillModal2.style.display = "block";
    };
    span2.onclick = function () {
      skillModal2.style.display = "none";
    };
    // follower modal
    const followerModal = document.getElementById("followersModal");
    const followBtn = document.querySelector(".follower-modal");
    const followSpan = document.getElementsByClassName("close")[0];
    followBtn.onclick = function () {
      followerModal.style.display = "block";
    };
    followSpan.onclick = function () {
      followerModal.style.display = "none";
    };

    // following modal
    const followingModal = document.getElementById("followingsModal");
    const followingBtn = document.querySelector(".following-modal");
    const followingSpan = document.getElementsByClassName("close")[1];
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
.image-div {
  display: flex;
  margin: 25px;
}
.image {
  max-width: 250px;
  margin: 15px;
}
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
  padding: 10px;

  /*display: flex;*/
  /*justify-content: space-between;*/
  /*flex-direction: column;*/
  /*align-items: center;*/
}

li {
  list-style-type: none;
}

@media (min-width: 992px) {
  .about-area > .picture {
    width: 40%;
    display: inline-block;
  }
}

.about-area > .picture > img {
  width: 300px;
  height: 300px;
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
.more {
  color: black;
  background: transparent;
}
.more:hover {
  cursor: pointer;
}
.more:disabled {
  opacity: 0;
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
  margin: 10%;
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
  margin-top: 10%;
  padding: 20px;
  border: 1px solid #888;
  width: 40%;
}
@media (max-width: 992px) {
  .follow-modal-content {
    width: 80%;
  }
  .skills span {
    margin: 0.5% 1% 0.5% 0.5%;
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
  width: 100%;
}
.followBtn:hover {
  cursor: pointer;
}
#calendar {
  margin-left: 1%;
}
</style>
