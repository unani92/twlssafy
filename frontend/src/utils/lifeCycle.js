export function attachModal() {
  if (this.$store.getters.isLoggedIn) {
    const followBtns = [
      ...document.querySelectorAll('.follower-email-container'),
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
          followBtns[i].childNodes[1].childNodes[0].innerHTML = '팔로우 취소';
        }
      }
    }
  }
  //스킬
  const skillModal = document.getElementById('skillModal');
  const btn = document.querySelector('.more');
  const span = document.getElementsByClassName('close2')[0];
  btn.onclick = function() {
    skillModal.style.display = 'block';
  };
  span.onclick = function() {
    skillModal.style.display = 'none';
  };
  // follower modal
  const followerModal = document.getElementById('followersModal');
  const followBtn = document.querySelector('.follower-modal');
  const followSpan = document.getElementsByClassName('close')[0];
  followBtn.onclick = function() {
    followerModal.style.display = 'block';
  };
  followSpan.onclick = function() {
    followerModal.style.display = 'none';
  };

  // following modal
  const followingModal = document.getElementById('followingsModal');
  const followingBtn = document.querySelector('.following-modal');
  const followingSpan = document.getElementsByClassName('close')[1];
  followingBtn.onclick = function() {
    followingModal.style.display = 'block';
  };
  followingSpan.onclick = function() {
    followingModal.style.display = 'none';
  };

  window.onclick = function(event) {
    if (
      event.target === skillModal ||
      event.target === followerModal ||
      event.target === followingModal
    ) {
      skillModal.style.display = 'none';
      followerModal.style.display = 'none';
      followingModal.style.display = 'none';
    }
  };
  const introModal = document.getElementById('introductionModal');
  const introBtn = document.querySelector('.intro-modal');
  const introSpan = document.getElementsByClassName('close2')[1];
  const introSpan2 = document.getElementsByClassName('close2')[2];
  introBtn.onclick = function() {
    introModal.style.display = 'block';
  };
  introSpan.onclick = function() {
    introModal.style.display = 'none';
  };
  introSpan2.onclick = function() {
    introModal.style.display = 'none';
  };
  ///////////////////////////
  ///////////////////////////
  const gitModal = document.getElementById('githubModal');
  const gitBtn = document.querySelector('.git-modal');
  const gitSpan = document.getElementsByClassName('close2')[3];
  const gitSpan2 = document.getElementsByClassName('close2')[4];
  gitBtn.onclick = function() {
    gitModal.style.display = 'block';
  };
  gitSpan.onclick = function() {
    gitModal.style.display = 'none';
  };
  gitSpan2.onclick = function() {
    gitModal.style.display = 'none';
  };
  ///////////////////////////

  window.onclick = function(event) {
    if (
      event.target === skillModal ||
      event.target === followerModal ||
      event.target === followingModal ||
      event.target === introModal
    ) {
      skillModal.style.display = 'none';
      followerModal.style.display = 'none';
      followingModal.style.display = 'none';
      introModal.style.display = 'none';
    }
  };
}
