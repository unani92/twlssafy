**Login**

<br/>
<br/>


          □ JWT
   
          □ Google Login 


<br/>
<br/>


 1. JWT

<br/>

 1-1>. JWT 토큰 생성
 
![createJWT](uploads/6937eaabea69f70022d9ad7abdff2a3e/createJWT.png)

RS256 방식으로 생성

<br/>

 1-2>. JWT 토큰 Decoding

 ![decode](uploads/328148d83100ffc58e7ba59983c08a86/decode.png)

Base64로 Decoding

<br/>
<br/>

---



2. [Google Login](https://www.npmjs.com/package/vue-google-login)

`npm i vue-google-login`

 1-1>.

  ![googlelogin](uploads/04ee4bb660d5dc96b2ad3bcf4c8998ed/googlelogin.png)


Google에서 발급된 토큰 Decoding 후, 회원 정보를 담을 새로운 Token 발급

<br/>
<br/>