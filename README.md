# DevMingle
DevMingle은 위치기반 개발자 커뮤니티 앱입니다. 개발자들은 DevMingle을 사용하여 주변 지역의 다른 개발자들과 연락하고 지식을 공유할 수 있습니다. 이 앱은 개발자들이 자신의 기술을 향상시키고 더욱 창의적인 프로젝트를 만들 수 있도록 돕는 것을 목표로 합니다.

## 1. 프로젝트 디렉터리 구조
```bash
.
├── build
│   ├── classes
│   │   └── java
│   │       └── main
│   ├── generated
│   │   └── sources
│   │       ├── annotationProcessor
│   │       │   └── java
│   │       │       └── main
│   │       └── headers
│   │           └── java
│   │               └── main
│   ├── libs
│   │   └── dev-talk-0.0.1-SNAPSHOT.jar
│   ├── resolvedMainClassName
│   ├── resources
│   │   └── main
│   │       ├── application-database.yml
│   │       ├── application-dev.yml
│   │       ├── application-local.yml
│   │       ├── application-prod.yml
│   │       ├── application-valid.yml
│   │       ├── application.yml
│   │       └── db
│   │           └── migration
│   │               └── V1__init_db.sql
│   └── tmp
│       ├── bootJar
│       │   └── MANIFEST.MF
│       └── compileJava
│           └── previous-compilation-data.bin
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── devtalk
│   │   │               ├── DevTalkApplication.java
│   │   │               ├── controller
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── BaseController.java
│   │   │               │   ├── CommonController.java
│   │   │               │   ├── PostController.java
│   │   │               │   └── FollowController.java
│   │   │               ├── dto
│   │   │               │   ├── commons
│   │   │               │   |   └── PreSignedUrlDto.java
│   │   │               │   ├── follows
│   │   │               │   |   ├── TargetUserInfoDto.java
│   │   │               │   |   ├── FollowAddDto.java
│   │   │               │   |   └── FollowInfoDto.java
│   │   │               │   ├── posts
│   │   │               │   |   ├── PostListDto.java
│   │   │               │   |   └── PostListInfoDto.java
│   │   │               │   ├── users
│   │   │               │   |   ├── ChangePwdDto.java
│   │   │               │   |   ├── LoginDto.java
│   │   │               │   |   ├── MypageDto.java
│   │   │               │   |   ├── SignupUserProfiles.java
│   │   │               │   |   ├── SignupUserProfilesResponse.java
│   │   │               │   |   └── UserProfilesResponse.java
│   │   │               │   ├── ApiResponse.java
│   │   │               │   └── ErrorResponse.java
│   │   │               ├── entity
│   │   │               │   ├── BaseTimeEntity.java
│   │   │               │   ├── Categories.java
│   │   │               │   ├── DeletedEntity.java
│   │   │               │   ├── Fllows.java
│   │   │               │   ├── Images.java
│   │   │               │   ├── LoginUser.java
│   │   │               │   ├── Posts.java
│   │   │               │   ├── UserProfiles.java
│   │   │               │   └── Users.java
│   │   │               ├── enums
│   │   │               │   ├── ImageType.java
│   │   │               │   └── OrderType.java
│   │   │               ├── exception
│   │   │               │   ├── ApiResultStatus.java
│   │   │               │   ├── AuthException.java
│   │   │               │   ├── BadApiRequestException.java
│   │   │               │   ├── BadRequestException.java
│   │   │               │   ├── BusinessException.java
│   │   │               │   ├── CommonException.java
│   │   │               │   ├── ForbiddenException.java
│   │   │               │   ├── FollowException.java
│   │   │               │   ├── ForbiddenException.java
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   └── UnauthorizedException.java
│   │   │               ├── repository
│   │   │               │   ├── FollowRepository.java
│   │   │               │   ├── PostsJpaRepository.java
│   │   │               │   ├── PostsRepository.java
│   │   │               │   ├── PostsRepositoryImpl.java
│   │   │               │   ├── UserProfileRepository.java
│   │   │               │   └── UsersRepository.java
│   │   │               ├── security
│   │   │               │   ├── CustomEntryPoint.java
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── resolver
│   │   │               │   └── ApiResponseResolver.java
│   │   │               ├── security
│   │   │               │   ├── jwt
|   │   │               │   |   ├──TokenFilter.java
|   │   │               │   |   └──TokenProvider.java
│   │   │               │   ├── ColumnEncryptor.java
│   │   │               │   └── PermitUrlProperties.java
│   │   │               ├── service
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── FollowService.java
│   │   │               │   ├── PostService.java
│   │   │               │   ├── PostServiceImpl.java
│   │   │               │   ├── S3Service.java
│   │   │               │   └── UserService.java
│   │   │               └── util
│   │   │                   ├── MailSender.java
│   │   │                   ├── PasswordGenerator.java
│   │   │                   ├── RedisUtil.java
│   │   │                   └── TxidGenerator.java
│   │   └── resources
│   │       ├── application-auth.yml
│   │       ├── application-datasource.yml
│   │       ├── application-dev.yml
│   │       ├── application-local.yml
│   │       ├── application-prod.yml
│   │       ├── application.yml
│   │       └── db
│   │           └── migration
│   │               └── V1__init_db.sql
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── devtalk
│                       └── DevTalkApplicationTests.java
├── build.gradle
├── docker-compose.local.yml
├── docker-compose.yml
├── Dockerfile
├── gradlew
├── gradlew.bat
├── HELP.md
├── README.md
├── settings.gradle
├── start-server.sh
└── stop-server.sh
```

## 2. 서버 구성
## 2-1. 개발 환경
- **OS**: Alpine Linux 3.16
- **Language**: openjdk 17
- **Framework**: SpringBoot 3.1.3
- **build & lib management**: Gradle 8.2.1
- **DB**: PostgreSQL 15.3
- **ORM**: Spring Boot JPA
- **Etc**: lombok, jjwt, flyway

## 2-2. Endpoint
- 로컬: http://127.0.0.1:8080
  <br></br>

## 3. 애플리케이션 실행 방법
- **실행 전 `openjdk-17`, `docker`, `docker-compose` 가 설치되어 있어야 합니다.**

## 3-1. 프로젝트 루트 경로에 `.env` 생성
```env
# 활성화 할 프로파일
SPRING_PROFILE: local

# DB 유저 정보
DB_PASSWORD: yourpassword1234

# JWT 설정
JWT_SECRET: yoursecretkey1234
JWT_EXPIRE: 600
```

## 3-2. 프로젝트 루트 경로에서 build & run 진행
### 1. start-server.sh 사용하여 build, run 한꺼번에 실행하기
```bash
./start-server.sh
```
### 2. start-server.sh 사용하지 않고 실행하기
1. build jar
```bash
./gradlew clean bootJar
```
2. docker compose up
```bash
docker compose -f docker-compose.yml up -d
```

## 3-3. Endpoint 호출 방법
### Health Check URL 을 통해 서버가 정상적으로 실행되었는지 확인
- 로컬
```bash
curl -X GET http://127.0.0.1:8080/actuator/health
```
<br></br>

## 4. [데이터베이스 테이블 구조](https://dbdiagram.io/d/64efb9fc02bd1c4a5eb48232)
![위치기반 개발자 커뮤니티](https://github.com/wtd-onbd-be-pj-team-4/dev-talk/assets/72455719/d70ea4fe-3939-45bd-9d64-c4021db6e2a3)
<br></br>

## 5. 구현 방법

## 6. API 명세
### 6-x. 에러 정의

## 7. 클라우드 환경 구조

