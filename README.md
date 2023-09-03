# dev-talk
위치기반 개발자 커뮤니티(임시이름: dev-talk)

## 1. 프로젝트 디렉터리 구조

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

