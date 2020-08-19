![docker.png](https://firebasestorage.googleapis.com/v0/b/twl-image-storage.appspot.com/o/docker.png?alt=media&token=ecf2155b-913c-4aa1-9ad0-a24bb36d9add)

세상 쉽고 간단하게 spring boot 서버를 ec2에 24시간 배포할 수 있는 방법을 포스팅하려 한다. 이 포스팅은 빌드된 jar 파일이 있으며 ec2 인스턴스와 해당 인스턴스에 도커 및 nginx를 설치했다는 전제 하에서 진행하려 한다. 

## 1. 인스턴스 실행 및 jar 파일 전송

윈도우의 경우 mobax, puttyGen을 통해 ec2에 접속할 수 있지만 맥의 경우 mabax 프로그램이 제공되지 않는다. *따라서* 터미널을 통해 접속 및 파일 전송을 할 수 있다. 

#### EC2 인스턴스 접속

```bash
$ ssh -i /path/key-pair.pem ec2-user@address
```

인스턴스 접속을 위해 사전 발급받은 pem키의 위치와 유저의 컴퓨터 이름, 주소를 정확하게 적어주면 터미널을 통한 접속이 가능하다. 

#### jar 파일 전송

```bash
$ scp -i /path/key-pair.pem /path/file.txt ec2-user@address:~
```

jar 파일을 인스턴스 홈에 전송하라는 명령어이다. 

![스크린샷 2020-08-12 오후 10.12.35.png](https://firebasestorage.googleapis.com/v0/b/twl-image-storage.appspot.com/o/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202020-08-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2010.12.35.png?alt=media&token=9737bd82-5764-4dd2-93f0-73c1571ba966)

실행 결과는 다음과 같다. (도커파일은 현재 없다고 생각하자)


## 2. 도커 파일 생성하기

도커의 이미지를 생성(빌드)하고 실행하기에 앞서 구체적으로 어떻게 할지를 정의한 도커 파일을 생성해야 한다. 편의를 위해 홈 화면에 도커파일을 그대로 생성해보고자 한다. 커멘드 라인이기 때문에 `vi ./Dockerfile`  명령어를 통해 도커파일을 (생성)실행할 수 있다. 

```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /home/ubuntu

COPY webcuration-0.0.1-SNAPSHOT.jar .

CMD java -jar webcuration-0.0.1-SNAPSHOT.jar
```

>  jdk11 환경에서 시작한다는 의미이다. 
>  빌드, 러닝이 실행될 위치(파일이 있는 위치)는 홈이다. 
>  목표하는 파일을 복사해서 러닝이 시작되면 해당 파일을 실행하시오... 라는 의미이다. 


## 3. 도커 빌드

```bash
$ sudo docker build --tag app:0.1 ./
```

app:0.1 이라는 태그를 가질 도커 이미지를 빌드한다는 의미이다. ` ./ ` 는 홈 디렉터리에 있는 도커파일을 찾아서 빌드를 수행하라는 의미이다. 특정 도커파일을 빌드하려 하면 도커파일의 경로를 적어주면 된다. 
도커 관리자의 경우 sudo를 붙일 필요가 없지만 권한 문제로 인해 도커 빌드 에러가 발생하면 마법의 명령어 `sudo` ~~갓도~~ 를 입력해 권한 문제를 해결한다. 

![스크린샷 2020-08-12 오후 10.32.43.png](https://firebasestorage.googleapis.com/v0/b/twl-image-storage.appspot.com/o/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202020-08-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2010.32.43.png?alt=media&token=0eddabff-2539-4a82-9438-15beefb59cda)

## 도커 러닝

```bash
$ docker run -p 8081:8081 app:0.1 
```

포트 번호는 상황에 따라 유동적으로 바꾸면 된다. 다만 해당 케이스는 팀원들 간에 백앤드 서버의 포트를 8081로 하기로 사전에 이야기가 되어 있었기 때문에 그렇게 한 것이다. 
따라서 포트번호, 레포명:태그명을  그대로 적어주면 컨테이너 위에 올라가고   `docker stop [container name]`로 멈추기 전까지 백앤드 서버가 실행된다. 도커 위에 컨테이너가 올라가고 서버가 실행되면 인스턴스를 종료해도 서버가 종료되지 않는다. 

참고로 컨테이너를 조회하려면 `docker ps` 를 통해 현재 돌아가고 있는지 여부를 확인할 수 있다. 
![스크린샷 2020-08-12 오후 10.38.29.png](https://firebasestorage.googleapis.com/v0/b/twl-image-storage.appspot.com/o/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202020-08-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2010.38.29.png?alt=media&token=8824a637-c8dc-4730-97b8-5e66b8a56456)

