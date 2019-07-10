# TodaysNews
  이 APP은 OpenWeatherMap , News 두 api 를 사용해 사용자가 오늘의 날씨와 뉴스를 볼수 있는
 APP입니다.

#### Authentication
##### 이메일 로그인
이메일 주소와 비밀번호로 사용자를 인증하는 방법
Firebase 인증 SDK를 사용

##### 구글 로그인
Google 계정으로 로그인을해 OAuth 토큰을 받아 사용자를 인증하는 방법


<img width="200px" height="300px" alt="email" src="./app/src/main/res/drawable/emaillogin.png"></img> <img width="200px" height="300px" alt="google" src="./app/src/main/res/drawable/googlelogin.png"></img>



#### Firebase Authentication 유저 관리 페이지

신규회원이 회원가입을 하게 되면 Firebase Authentication에 유저정보가 추가
되고 인증이 된 사용자를 Firebase console 에서 대쉬보드 형태로 볼수 있다.

![firebaseauth](./app/src/main/res/drawable/firebase_auth.png)

#### OpenWeatherMap API
##### API 호출방식
- 사용자 위치기반 날씨 요청 ( Get ) : https://api.openweathermap.org/data/2.5/weather?lat=latitude&lon=logitude&appid=APPID

서버 응답(Json)

![openweatherRes](./app/src/main/res/drawable/openweatherres.png)

  [Sample URL](https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22)

#### News API
  ##### API 호출방식
  - 카테고리별 뉴스 요청 ( Get ) : https://newsapi.org/v2/top-headlines?country=kr&category=category&apiKey=apiKey

  서버 응답(Json)

  ![categoryRes](./app/src/main/res/drawable/newsapicategoryres.png)

  [Sample URL( 카테고리 : business )](https://newsapi.org/v2/top-headlines?country=kr&category=business&apiKey=ec4c02f7e056430bb7cc71878bca7a01)

  - 키워드 검색 요청 ( Get ) : https://newsapi.org/v2/top-headlines?country=kr&q=키워드&apiKey=apiKey

  서버 응답(Json, 검색 키워드 = "북한" )

  ![keywordRes](./app/src/main/res/drawable/newsapikeywordres.png)

  [Sample URL( 키워드 : 북한 )](https://newsapi.org/v2/top-headlines?country=kr&q=%EB%B6%81%ED%95%9C&apiKey=ec4c02f7e056430bb7cc71878bca7a01)


### library  
    - 사용자 인증
         Firebase Google Authentication , Firebase Email Authentication
        (이메일 로그인, 구글 로그인(Firebase와 통합하기 위해 gms-service-auth 라이브러리 추가))

    - API 호출
        Retrofit (OpenWeather API 와 News API 호출하기 위해 사용)

    - 이미지 로딩 라이브러리
        Glide (서버에서 보내주는 이미지를 보여주기 위해 사용 )

    - API
        OpenWeatherAPI ( 날씨 정보 OpenWeatherMap API : https://openweathermap.org/ )
        NewsAPI ( 뉴스 정보 News API : https://newsapi.org/ )

    - Debugging
        Stetho(Facebook) : 네트워크 상태를 쉽게 확인하기 위해 사용


### 어플 시연 영상
[![IMAGE ALT TEXT](https://img.youtube.com/vi/U6TIXF1g58E/0.jpg)](http://www.youtube.com/watch?v=U6TIXF1g58E "어플 시연 영상")
