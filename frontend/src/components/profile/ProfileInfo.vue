<template>
  <div class="profile-info">
    <vue-headful
      :title="`@${userInfo.userInfo.nickname} 님의 페이지`"
    />
    <section class="about-area" id="about">
      <div class="picture">
        <img v-if="userInfo.userInfo.img" :src="userInfo.userInfo.img" />
        <img v-else src="https://i.pravatar.cc/400?u=정윤환" />
        <div>
          <div v-if="this.userInfo.userInfo.email === this.$store.state.username">
            <i @click="toggleDropZone" class="far fa-plus-square dropzone-icon"></i>
            <!--            <i-->
            <!--              v-else-->
            <!--              @click="toggleDropZone"-->
            <!--              class="far fa-minus-square dropzone-icon"-->
            <!--            ></i>-->
          </div>
        </div>
      </div>
      <div class="text">
        <div style="display: flex; justify-content: space-between">
          <div
            :style="{
              backgroundImage:
                'url(' +
                require('@/assets/image/medal-' + calGrade + '.png') +
                ')',
            }"
            class="grade"
          />
          <div
            :style="{
              backgroundImage:
                'url(' +
                require('@/assets/image/medal-' + (calGrade + 1) + '.png') +
                ')',
            }"
            class="grade"
          />
        </div>
        <div v-if="userInfo">
          <ProgressBar v-if="userInfo" :calGrade="calGrade" :score="userInfo.userInfo.score" />
        </div>
        <div class="description follower-email-container" style="margin:0">
          <span class="follower-email">{{ userInfo.userInfo.nickname }}</span>
          <div
            v-show="
              this.userInfo.userInfo.nickname !== this.$store.state.nickname
            "
            @click="requestFollow(userInfo.userInfo.email, $event)"
          >
            <button class="followBtn">팔로우</button>
          </div>
        </div>

        <div class="intro">
          {{ userInfo.userInfo.info }}
          <a
            v-if="isMypage"
            :disabled="!isMypage"
            class="intro-modal"
            data-toggle="modal"
          >
            <i style="color : gray; font-size : 12px" class="far fa-edit"></i>
          </a>
        </div>

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
          <li>
            <div class="info">
              <i class="fab fa-github"></i>
              {{ userInfo.userInfo.github }}
              <a
                v-if="isMypage"
                class="git-modal"
                data-toggle="modal"
              >
                <i style="color : gray; font-size : 12px" class="far fa-edit"></i>
              </a>
            </div>
          </li>
          <!-- <li style="dec" v-if="skills.length !== 0"></li> -->
        </ul>
        <li v-if="skills.length !== 0">
          <div class="skills">
            <span
              v-for="skill in skills"
              :key="skill.name"
              style="cursor : pointer"
              @click="searchByStack(skill.name)"
            >#{{ skill.name }}</span>
            <button :disabled="!isMypage" class="more" data-toggle="modal">
              <i class="far fa-plus-square"></i>
            </button>
          </div>
        </li>
        <li v-else>
          등록된 관심사가 없습니다.
          <button :disabled="!isMypage" class="more" data-toggle="modal">
            <i class="far fa-plus-square"></i>
          </button>
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
        <SelectSkills v-if="this.$store.state.nickname === this.userInfo.userInfo.nickname"></SelectSkills>
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

    <!-- 한줄 소개 수정  -->
    <div id="introductionModal" class="modal">
      <div class="intro-modal-content">
        <div style="margin-bottom : 20px;">한줄 소개 수정</div>
        <input class="editInput" style="padding-right : 20px;" v-model="intro" />
        <div style="margin-top : 20px;">
          <button
            @click="modiIntro"
            class="close2 editBtn"
            style="text-align : center; cursor : pointer"
          >완료</button>
          <button class="close2 cancelBtn" style="text-align : center; cursor : pointer">취소</button>
        </div>
      </div>
    </div>
    <!-- github  수정  -->
    <div id="githubModal" style="text-align : center" class="modal">
      <div class="git-modal-content">
        <div style="margin-bottom : 20px;">Git Hub 수정</div>
        <input class="editInput" style="padding-right : 20px;" v-model="github" />
        <div style="margin-top : 20px;">
          <button
            @click="modiGit"
            class="close2 editBtn"
            style="text-align : center; cursor : pointer"
          >완료</button>
          <button class="close2 cancelBtn" style="text-align : center; cursor : pointer">취소</button>
        </div>
      </div>
    </div>

    <!--    드래그앤드랍      -->
    <div v-if="dropzoneOptions" class="dropZone dropZoneDisabled">
      <vue2-dropzone
        ref="imgDropZone"
        id="customdropzone"
        :options="dropzoneOptions"
        @vdropzone-complete="afterComplete"
      />
    </div>
  </div>
</template>

<script>
import SelectSkills from "@/views/SelectSkills.vue";
import {
  requestFollow,
  changeImg,
  modifyIntro,
  modifyGit,
} from "@/api/index.js";
import { mapState, mapGetters } from "vuex";
import Calendar from "../calendar/Calendar";
import { getGrade } from "@/utils/calcGrade";
// drag and drop
import firebase from "firebase";
import vue2Dropzone from "vue2-dropzone";
import "vue2-dropzone/dist/vue2Dropzone.min.css";

// progressbar
import ProgressBar from "../../views/ProgressBar";
let uuid = require("uuid");

export default {
  props: {
    userInfo: Object,
  },
  components: {
    SelectSkills,
    Calendar,
    vue2Dropzone,
    ProgressBar,
  },
  data() {
    const userSkills = this.$store.state.userSkills;
    return {
      skills: userSkills,
      openDropZone: false,
      images: null,
    };
  },
  computed: {
    ...mapGetters(["isLoggedIn"]),
    ...mapState(["id_token", "userSkills"]),
    isMypage() {
      return this.userInfo.userInfo.email === this.$store.state.username;
    },
    calGrade() {
      return getGrade(this.userInfo.userInfo.score);
    },
    dropzoneOptions() {
      return {
        url: "https://httpbin.org/post",
        thumbnailWidth: 150,
        thumbnailHeight: 150,
        addRemoveLinks: false,
        acceptedFiles: ".jpg, .jpeg, .png",
        dictDefaultMessage: `<p class='text-default'><i class="far fa-plus-square dropzone-icon"></i></p>
          <p class="form-text">프로필 이미지를 변경합니다</p>
          `,
      };
    },
    nickname() {
      return this.userInfo.userInfo.nickname;
    },
    follower() {
      return this.userInfo.follower;
    },
    intro() {
      return this.userInfo.userInfo.info;
    },
    github() {
      return this.userInfo.userInfo.github;
    },
  },
  methods: {
    //DOM에 modal 부착하기
    attachModal() {
      if (this.$store.getters.isLoggedIn) {
        const followBtns = [
          ...document.querySelectorAll(".follower-email-container"),
        ];
        const len = followBtns.length;
        for (let i = 0; i < len; i++) {
          const followerOfLoginUser = this.$store.state.followList
            .followNickname;
          const numOfFollowerOFLoginUser = followerOfLoginUser.length;
          for (let j = 0; j < numOfFollowerOFLoginUser; j++) {
            // trim하지 않으면 선택자로 버튼값의 innerHTML값을 가져올 때 양쪽에 공백문자가 삽입되어 비교가 안된다.
            if (
              followBtns[i].childNodes[0].innerHTML.trim() ===
              followerOfLoginUser[j].trim()
            ) {
              followBtns[i].childNodes[1].childNodes[0].innerHTML =
                "팔로우 취소";
            }
          }
        }
      }
      //스킬
      const skillModal = document.getElementById("skillModal");
      const btn = document.querySelector(".more");
      const span = document.getElementsByClassName("close2")[0];
      // btn.addEventListener("click", () => {
      //   skillModal.style.display = "block";
      // });
      btn.onclick = function () {
        skillModal.style.display = "block";
      };
      span.onclick = function () {
        skillModal.style.display = "none";
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
          event.target === skillModal ||
          event.target === followerModal ||
          event.target === followingModal
        ) {
          skillModal.style.display = "none";
          followerModal.style.display = "none";
          followingModal.style.display = "none";
        }
      };
      const introModal = document.getElementById("introductionModal");
      const introBtn = document.querySelector(".intro-modal");
      const introSpan = document.getElementsByClassName("close2")[1];
      const introSpan2 = document.getElementsByClassName("close2")[2];
      introBtn.onclick = function () {
        introModal.style.display = "block";
      };
      introSpan.onclick = function () {
        introModal.style.display = "none";
      };
      introSpan2.onclick = function () {
        introModal.style.display = "none";
      };
      ///////////////////////////
      ///////////////////////////
      const gitModal = document.getElementById("githubModal");
      const gitBtn = document.querySelector(".git-modal");
      const gitSpan = document.getElementsByClassName("close2")[3];
      const gitSpan2 = document.getElementsByClassName("close2")[4];
      gitBtn.onclick = function () {
        gitModal.style.display = "block";
      };
      gitSpan.onclick = function () {
        gitModal.style.display = "none";
      };
      gitSpan2.onclick = function () {
        gitModal.style.display = "none";
      };
      ///////////////////////////

      window.onclick = function (event) {
        if (
          event.target === skillModal ||
          event.target === followerModal ||
          event.target === followingModal ||
          event.target === introModal
        ) {
          skillModal.style.display = "none";
          followerModal.style.display = "none";
          followingModal.style.display = "none";
          introModal.style.display = "none";
        }
      };
    },
    async searchByStack(skill) {
      const params = {
        q: skill,
        category: "keyword",
      };
      this.$router.push({ name: "ArticleSearchByStack", query: params });
    },
    modiGit() {
      const params = {
        github: this.github,
      };
      const token = this.id_token;
      modifyGit(params, token)
        .then(() => {
          this.userInfo.userInfo.github = this.github;
        })
        .catch((err) => console.log(err));
    },
    modiIntro() {
      const params = {
        intro: this.intro,
      };
      const token = this.id_token;
      modifyIntro(params, token)
        .then(() => {
          this.userInfo.userInfo.info = this.intro;
        })
        .catch((err) => console.log(err));
    },

    // 유저 이미지 변경하기
    toggleDropZone() {
      if (this.userInfo.userInfo.email === this.$store.state.username) {
        const dropZone = document.querySelector(".dropZone");
        dropZone.classList.toggle("dropZoneDisabled");
        this.openDropZone = !this.openDropZone;
      } else console.log(false);
    },
    async afterComplete(upload) {
      let imageName = uuid.v1();
      const div = document.querySelector(".picture");
      const imgField = div.querySelector("img");
      this.isLoading = true;
      try {
        let file = upload;
        const metaData = {
          contentType: "image/png",
        };
        const storageRef = firebase.storage().ref();
        const imageRef = storageRef.child(`images/${imageName}.png`);
        await imageRef.put(file, metaData);
        const downloadURL = await imageRef.getDownloadURL();
        this.images = downloadURL;
        imgField.src = this.images;
        // 백앤드에 이미지 수정 요청
        changeImg(downloadURL, this.id_token)
          .then((res) => console.log(res))
          .catch((err) => console.log(err));
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
        if (confirm("팔로우 하시려면 로그인을 해야 합니다")) {
          this.$router.push("/login");
        }
      } else {
        const params = {
          follow: followWantingTo,
        };
        const { data } = await requestFollow(params, this.id_token);
        // 프런트에서 처리
        const followList = this.$store.state.followList.followNickname;
        const nicknameOfThisBlog = this.userInfo.userInfo.nickname;
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
    this.attachModal();
  },
  beforeUpdate() {
    this.skills = this.$store.state.userSkills;
  },
  mounted() {
    this.attachModal();
  },
};
</script>
<style scoped src="@/assets/css/ProfileInfo.css"></style>
