# TWL: Today We Learned

<img src="https://user-images.githubusercontent.com/53211781/90404787-d25e2180-e0dd-11ea-8212-521c37540ceb.png" alt="KakaoTalk_20200814_155111341" style="zoom:50%;" />

## Intro

### :thumbsup:  서비스 소개

개발자가 되고 싶은 우리들은 매일같이 **TIL**을 만들고 깃허브에 올리지만,

블로깅은 엄두도 내지 못하는 사람들이 있습니다. 

우리들의 관심사 는 다르고 매일 공부하는 것도 다릅니다.

그래서 준비했습니다. **꿈나무들의 TIL을 모두 모아볼 수 있는 커뮤니티 TWL** 지금부터 시작합니다.

#### 배포 주소

http://i3a407.p.ssafy.io/



### **:person_frowning: 팀원 소개**

#### Back-End

배유진 - JWT / Google Login, 게시글 관리, 서버 배포

전혜인 - 

#### Front-End

신영찬

정윤환



### **:symbols: 사용한 기술**



## 서비스 주요 기술 소개

### 0. :key:  사용자 인증

#### - 구글 로그인

#### - 이메일 로그인



### 1.  :pencil2:마크다운 에디터 CRUD

- 게시글 작성

<img src="https://user-images.githubusercontent.com/53211781/90412174-bb243180-e0e7-11ea-9d97-a8c6c70727b4.png" alt="image" width="340px" height="340px"/><img src="https://user-images.githubusercontent.com/53211781/90412451-16562400-e0e8-11ea-98d0-95caf56d297a.png" alt="image" width=350px />

  ( 사진  좌: 게시글 작성  |  우: 드래그 & 드랍 이미지 삽입 )



- 게시글 조회

<img src="https://user-images.githubusercontent.com/53211781/90413599-a21c8000-e0e9-11ea-87bb-283380c4d7e9.png" alt="detail" width=200px/>       !<img src="https://user-images.githubusercontent.com/53211781/90413688-c5dfc600-e0e9-11ea-9fb6-6a83193b5d47.jpg" alt="클립보드 복사" width=200 />        <img src="https://user-images.githubusercontent.com/53211781/90414651-07bd3c00-e0eb-11ea-95eb-71bacebe4ae5.jpg" alt="detail_btm" width=200 />

( 좌: <1> 작성자 본인은 수정, 삭제, 마크다운 다운로드 가능 <2> 팔로우, 좋아요, 핀, 게시글 url 복사 )  

( 중: url 복사 실제 화면 ) ( 우: 디테일 페이지 하단면, 댓글 작성란 )



### 2.   :mag:개인 관심사 기반 게시글 큐레이션

- 관심사 선택

  <img src="https://user-images.githubusercontent.com/53211781/90416591-a34fac00-e0ed-11ea-9718-b084516267fb.jpg" alt="select_signup" width=200px/>         <img src="https://user-images.githubusercontent.com/53211781/90416596-a480d900-e0ed-11ea-8acf-a6e7075ba3d7.jpg" alt="select_mypage" width=200px/>

    ( 사진  좌: 회원가입 페이지 관심사 선택  |  우: 마이페이지 관심사 선택)

  

- 최신글 / 팔로우 / 북마크 모아보기 탭 

<img src="https://user-images.githubusercontent.com/53211781/90418241-d430e080-e0ef-11ea-8c27-6e2095532e4a.jpg" alt="follow_pin" width=200/>           <img src="https://user-images.githubusercontent.com/53211781/90418471-1c500300-e0f0-11ea-8fdc-8fb258dc58a8.jpg" alt="pinlist" width=200px/>



- 관심사, 좋아요 기반 관심글 추천

  ![image](https://user-images.githubusercontent.com/53211781/90417461-b1ea9300-e0ee-11ea-9ecd-e81e7cf196b2.png)

  

- 키워드 검색 및 기타 검색기능

<img src="https://user-images.githubusercontent.com/53211781/90419097-042cb380-e0f1-11ea-93f8-c77eea705a40.jpg" alt="detail" width=200/>        <img src="https://user-images.githubusercontent.com/53211781/90419145-19094700-e0f1-11ea-936a-21b4d4b71ec6.jpg" alt="search" width=200 />



### 3.  🧍마이페이지

 - 달력
 - 활동 점수
 - 팔로워 조회
 - 회원정보 수정

### 4. 🔔알림



## 문제 해결 및 문서 정리



### 1. FrontEnd

1-1. ToastUi Editor

1-2. Firebase Storage

1-3. Drag & Drop

1-4. AutoComplete

1-5. Social Login

### 2. BackEnd