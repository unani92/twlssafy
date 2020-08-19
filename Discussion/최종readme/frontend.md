<img src="https://user-images.githubusercontent.com/50816217/90447701-d0688280-e11e-11ea-8afa-b6d10f9005aa.png">



#### 3.1 architecture

![](https://github.com/c9-youngchan/django-tutorial/blob/master/profile%20diagram.jpg?raw=true)

###### vue2DropZone은 서드파티 라이브러리

`MyArticleCardList`에서 백엔드 서버로 요청을 보내어 개인 정보와 해당 닉네임 유저가 작성한 글들을 모두 가져온다. 그리고 각 컴포넌트에 필요한 정보들을 `props`와 `$emit`을 이용해 전달한다.

<img src="https://user-images.githubusercontent.com/50816217/90379115-40422300-e0b5-11ea-8c02-1386d48f0e0b.PNG"  width="auto" height="500" style="display:inline">

![](https://user-images.githubusercontent.com/50816217/90435759-6c3bc380-e10a-11ea-84df-49699aa49102.PNG)

![](https://user-images.githubusercontent.com/50816217/90435761-6d6cf080-e10a-11ea-9089-084faa5e4726.PNG)


<img src="https://user-images.githubusercontent.com/53211781/90412174-bb243180-e0e7-11ea-9d97-a8c6c70727b4.png" alt="image" width="340px" height="340px"/><img src="https://user-images.githubusercontent.com/53211781/90412451-16562400-e0e8-11ea-98d0-95caf56d297a.png" alt="image" width=350px />

( 사진 좌: 게시글 작성 | 우: 드래그 & 드랍 이미지 삽입 )