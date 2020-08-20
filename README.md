# TWL: Today We Learned
<br>
<center>
<img src="https://user-images.githubusercontent.com/53211781/90404787-d25e2180-e0dd-11ea-8212-521c37540ceb.png" alt="KakaoTalk_20200814_155111341" width="40%" />
</center>
<br>
<br>

## Intro

### :thumbsup: 서비스 소개
<br>

개발자가 되고 싶은 우리들은 매일같이 **TIL**을 만들고 깃허브에 올리지만,

블로깅은 엄두도 내지 못하는 사람들이 있습니다.

우리들의 관심사 는 다르고 매일 공부하는 것도 다릅니다.

그래서 준비했습니다. **꿈나무들의 TIL을 모두 모아볼 수 있는 커뮤니티 TWL** 지금부터 시작합니다.
<br>

#### 배포 주소

http://i3a407.p.ssafy.io/
<br>
<br>

### **:person_frowning: 팀원 소개**


| 이름   | 역할                                                         |
| ------ | ------------------------------------------------------------ |
| 정윤환 | 팀장, firebase storage 연동, 에디터 연동을 통한 게시글 작성 사용자 뷰 구성 |
| 배유진 | AWS 배포, 회원 인증, 게시글 및 추천, 알림 컨트롤러 및 DB 구축 |
| 신영찬 | UCC 제작, 회원가입, 관심사, 마이 페이지, 좋아요, 북마크 기능 구성 |
| 전혜인 | 이메일 인증, 검색, 관심사, 댓글 컨트롤러 및 DB 구축          |



<br>
<br>


## 서비스 주요 기술 소개

<br>

### 0. :key: 사용자 인증

:key: 로그인 / 회원가입

<img src="https://user-images.githubusercontent.com/53211781/90470896-9f587400-e157-11ea-948d-d668c46de574.png" alt="mainLogin" width="300px" />        <img src="https://user-images.githubusercontent.com/53211781/90470985-daf33e00-e157-11ea-9c23-5f41a79e39fe.png" alt="signup" width="300px" />       


이메일 인증을 거쳐 가입이 가능합니다.

소셜 회원의 경우, 이메일을 제외한 정보를 입력하시면 됩니다.

<br>

<br>
<br>


### 1. :pencil2: 마크다운 에디터 CRUD

- 게시글 작성

![image](https://user-images.githubusercontent.com/53211781/90788727-0edd8780-e341-11ea-9a08-2f0c561a78b9.png)

<br>
<br>
<br>


- 게시글 조회

<img src="https://user-images.githubusercontent.com/53211781/90413599-a21c8000-e0e9-11ea-87bb-283380c4d7e9.png" alt="detail" width="250px"/>         <img src="https://user-images.githubusercontent.com/53211781/90413688-c5dfc600-e0e9-11ea-9fb6-6a83193b5d47.jpg" alt="클립보드 복사" width=250px />         <img src="https://user-images.githubusercontent.com/53211781/90414651-07bd3c00-e0eb-11ea-95eb-71bacebe4ae5.jpg" alt="detail_btm" width=250px />

( 좌: <1> 작성자 본인은 수정, 삭제, 마크다운 다운로드 가능 <2> 팔로우, 좋아요, 핀, 게시글 url 복사 )

( 중: url 복사 실제 화면 ) ( 우: 디테일 페이지 하단면, 댓글 작성란 )


<br>
<br>
<br>
<br>


### 2. :mag: 개인 관심사 기반 게시글 큐레이션

- 관심사 선택

  <img src="https://user-images.githubusercontent.com/53211781/90416591-a34fac00-e0ed-11ea-9718-b084516267fb.jpg" alt="select_signup" width="250px"/>       <img src="https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/wikis/uploads/0fad52f684dc1b5311e4b8ded2face2f/mypageSelectSkills.png" width="250px"/>

  ( 사진 좌: 회원가입 페이지 관심사 선택 | 우: 마이페이지 관심사 선택)
  
<br>
<br>
<br>

* 최신글 / 팔로우 / 북마크 모아보기 탭

<img src="https://user-images.githubusercontent.com/53211781/90418241-d430e080-e0ef-11ea-8c27-6e2095532e4a.jpg" alt="follow_pin" width="300px"/>         <img src="https://user-images.githubusercontent.com/53211781/90418471-1c500300-e0f0-11ea-8fdc-8fb258dc58a8.jpg" alt="pinlist" width="300px"/>

<br>
<br>
<br>
<br>


- 관심사, 좋아요 기반 관심글 추천

![image](https://user-images.githubusercontent.com/53211781/90789328-c2467c00-e341-11ea-911b-6c3e70d1b823.png)

<br>
<br>
<br>
<br>


* 키워드 검색 및 기타 검색기능

<img src="https://user-images.githubusercontent.com/53211781/90419097-042cb380-e0f1-11ea-93f8-c77eea705a40.jpg" alt="detail" width="300px"/>        <img src="https://user-images.githubusercontent.com/53211781/90419145-19094700-e0f1-11ea-936a-21b4d4b71ec6.jpg" alt="search" width="300px" />

<br>
<br>
<br>
<br>


### 3. 마이페이지


#### 3.1 캘린더 기능

![image](https://user-images.githubusercontent.com/53211781/90789539-fe79dc80-e341-11ea-9a51-ed5b84099361.png)

날짜를 클릭하면 해당 날짜에 작성한 글들만 모아볼 수 있다. 

<br>
<br>
<br>

#### 3.2 등급

<img src="https://user-images.githubusercontent.com/50816217/90446163-e1fc5b00-e11b-11ea-9af6-41f8cf5de09b.PNG"  width="300px" style="display:inline">

점수 산정 : 글 작성 시 10점, 댓글 작성 시 3점, 자신의 게시글에 좋아요 받을 때 1점

NEW : 10점 미만

BRONZE : 50점 미만

SILVER : 150점 미만

GOLD : 300점 미만

PLATINUM : 600 미만

DIAMOND : 600 이상

<br>
<br>
<br>

#### 3.3 회원 정보 조회 및 수정

<center>
<img src="https://user-images.githubusercontent.com/50816217/90379111-3e785f80-e0b5-11ea-8a46-ff187d4d4553.PNG"  width="300px" style="display:inline">      <img src="https://user-images.githubusercontent.com/50816217/90379113-3fa98c80-e0b5-11ea-8c8c-c62dfb8f6fed.PNG"  width="300px" style="display:inline">      <img src="https://user-images.githubusercontent.com/50816217/90379111-3e785f80-e0b5-11ea-8a46-ff187d4d4553 .PNG"  width="300px" style="display:inline">       <img src="https://user-images.githubusercontent.com/50816217/90379111-3e785f80-e0b5-11ea-8a46-ff187d4d4553 .PNG"  width="300px" style="display:inline">.    <img src="https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/wikis/uploads/305f78db86796bf62c8da13ec3447550/follow.png"  width="300px" style="display:inline">       <img src="https://user-images.githubusercontent.com/50816217/90379111-3e785f80-e0b5-11ea-8a46-ff187d4d4553.PNG "  width="300px" style="display:inline">
</center>


<br>
<br>
<br>
<br>

### 4. 🔔알림
- notification <br/><br/>

![image](https://user-images.githubusercontent.com/53211781/90789698-29fcc700-e342-11ea-85f7-0489b3184139.png).   ![image](https://user-images.githubusercontent.com/53211781/90789750-3d0f9700-e342-11ea-94f8-5d83d9d1c092.png).   ![image](https://user-images.githubusercontent.com/53211781/90789818-4e58a380-e342-11ea-8a7b-f39ee2637a87.png)

( 사진&nbsp;&nbsp;&nbsp;&nbsp; 좌: 알림 &nbsp;&nbsp;  | &nbsp;&nbsp; 중간: 알림 확인  &nbsp;&nbsp; | &nbsp;&nbsp;  우: 읽은 알림 삭제 )


<br>
<br>
<br>
<br>

## :symbols: 사용한 기술
- **backend**
  - Spring
  - maria DB / JPA
- **frontend**
  - Vue.js
  - FireBase
- **deploy**
  - AWS EC2
  - nginx
- **형상관리**
  - Jira
  - GitLab

<br>
<br>
<br>
<br>


## :calendar: 프로젝트 진행 일정
- 총 개발 기간 약 39일 (7/07~8/14)
- **1주차** : 기획 및 설계
- **2주차**: 개발환경 구축 및 데이터베이스 설계
- **3주차 & 4주차**
  - 백엔드 & 프론트엔드 개발
- **5주차**
  - 프론트엔드 개발, 서버 배포
- **6주차** : 오류수정

<center>
<img src="https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/wikis/uploads/cd75fe232f048594725cdc2007cb02cc/%EC%9D%BC%EC%A0%95.png">
</center>

<br>
<br>
<br>
<br>

## 문제 해결 및 문서 정리

### 1. FrontEnd

1-1. [ToastUi Editor](https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/blob/master/Discussion/%EC%B5%9C%EC%A2%85readme/toastui.md)

1-2. [Firebase Storage](https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/blob/master/Discussion/%EC%B5%9C%EC%A2%85readme/firebase.md)

1-3. [Drag & Drop](https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/blob/master/Discussion/%EC%B5%9C%EC%A2%85readme/vue2dropZone.md)

1-4. [AutoComplete](https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/blob/master/Discussion/%EC%B5%9C%EC%A2%85readme/autoComplete.md)

1-5. [myPage component props](https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/blob/master/Discussion/%EC%B5%9C%EC%A2%85readme/frontend.md)

1-6. [file Download](https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/blob/master/Discussion/%EC%B5%9C%EC%A2%85readme/fileDownload.md)

### 2. BackEnd

2-1. <a href="../Discussion/최종readme/Login-JWT-Google.md">로그인</a>

2-2. <a href="../Discussion/최종readme/Notification.md">알림</a>

2-3 [도커 위에 jar 파일 띄우기](https://lab.ssafy.com/s03-webmobile2-sub3/s03p13a407/blob/master/Discussion/%EC%B5%9C%EC%A2%85readme/docker_24hour_dist.md)
