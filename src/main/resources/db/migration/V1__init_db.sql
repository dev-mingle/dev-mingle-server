CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- uuidv4 생성을 위한 extension

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_profiles;
DROP TABLE IF EXISTS user_settings;
DROP TABLE IF EXISTS user_policies;
DROP TABLE IF EXISTS user_login_histories;
DROP TABLE IF EXISTS follows;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS vendor_request_histories;


-- 유저 기본 정보
CREATE TABLE users
(
    id                  BIGSERIAL    NOT NULL PRIMARY KEY,
    email               VARCHAR(50)  NOT NULL UNIQUE, -- 이메일(로그인 ID)
    password            VARCHAR(50),                  -- 비밀번호(bcrypt) / 소셜o -> null
    provider            VARCHAR(20)  NOT NULL,        -- 로그인 제공자: EMAIL(소셜x) / GOOGLE / GITHUB
    provider_id         VARCHAR(255) NOT NULL,        -- 유저 식별 id / 소셜x -> UUIDv4, 소셜o -> 로그인 제공자로부터 부여받은 id
    role                VARCHAR(20)  NOT NULL,        -- 권한: ADMIN(관리자) / USER(일반유저)
    is_verified         BOOLEAN DEFAULT false,        -- 이메일 인증 여부 / 소셜x -> 인증링크 여부에따라 true/false, 소셜o -> true
    is_blocked          BOOLEAN DEFAULT false,        -- 차단된 유저 여부 true/false
    is_inactivated      BOOLEAN DEFAULT false,        -- 휴면 계정 여부 true/false
    password_changed_at TIMESTAMP,                    -- 비밀번호 변경일자
    created_at          TIMESTAMP,                    -- 생성일자
    updated_at          TIMESTAMP,                    -- 수정일자
    is_deleted          BOOLEAN DEFAULT false,        -- 삭제여부 true/false
    deleted_at          TIMESTAMP                     -- 삭제일자
);


-- 유저 프로필 정보
CREATE TABLE user_profiles
(
    id         BIGSERIAL   NOT NULL PRIMARY KEY,
    user_id    BIGINT      NOT NULL,  -- users PK, 유저 정보
    image_id   BIGINT,                -- images PK, 프로필 이미지
    nickname   VARCHAR(20) NOT NULL,  -- 닉네임
    city       VARCHAR(20),           -- 도시
    state      VARCHAR(20),           -- 시/군/구
    street     VARCHAR(20),           -- 동/읍/면
    latitude   DOUBLE PRECISION,      -- 주소 설정 시점 위도
    longitude  DOUBLE PRECISION,      -- 주소 설정 시점 경도
    introduce  TEXT,                  -- 자기소개
    url        TEXT,                  -- 프로필에 표시할 URL
    url_name   VARCHAR(20),           -- 프로필에 표시할 URL 별칭
    created_at TIMESTAMP,             -- 생성일자
    updated_at TIMESTAMP,             -- 수정일자
    is_deleted BOOLEAN DEFAULT false, -- 삭제여부 true/false
    deleted_at TIMESTAMP              -- 삭제일자
);
ALTER TABLE user_profiles
    ADD CONSTRAINT fk_user_profiles_users FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT fk_user_profiles_images FOREIGN KEY (image_id) REFERENCES images (id);


-- 유저 설정 정보
CREATE TABLE user_settings
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    user_id     BIGINT    NOT NULL,    -- users PK, 유저 정보
    post_push   BOOLEAN DEFAULT true,  -- 게시글 관련 푸시 알림 여부 true / false
    chat_push   BOOLEAN DEFAULT true,  -- 채팅 관련 푸시 알림 여부 true / false
    follow_push BOOLEAN DEFAULT true,  -- 팔로우 관련 푸시 알림 여부 true / false
    event_push  BOOLEAN DEFAULT true,  -- 이벤트 관련 푸시 알림 여부 true / false
    notice_push BOOLEAN DEFAULT true,  -- 공지사항 관련 푸시 알림 여부 true / false
    created_at  TIMESTAMP,             -- 생성일자
    updated_at  TIMESTAMP,             -- 수정일자
    is_deleted  BOOLEAN DEFAULT false, -- 삭제여부 true/false
    deleted_at  TIMESTAMP              -- 삭제일자
);
ALTER TABLE user_settings
    ADD CONSTRAINT fk_user_setting_users FOREIGN KEY (user_id) REFERENCES users (id);


-- 유저 정책 정보
CREATE TABLE user_policies
(
    id                  BIGSERIAL   NOT NULL PRIMARY KEY,
    user_id             BIGINT      NOT NULL,  -- users PK, 유저 정보
    privacy_policy_ver  VARCHAR(10) NOT NULL,  -- 개인정보 처리방침 버전
    service_policy_ver  VARCHAR(10) NOT NULL,  -- 서비스 이용약관 버전
    location_policy_ver VARCHAR(10) NOT NULL,  -- 위치기반 서비스 이용약관 버전
    created_at          TIMESTAMP,             -- 생성일자
    updated_at          TIMESTAMP,             -- 수정일자
    is_deleted          BOOLEAN DEFAULT false, -- 삭제여부 true/false
    deleted_at          TIMESTAMP              -- 삭제일자
);
ALTER TABLE user_policies
    ADD CONSTRAINT fk_user_policies_users FOREIGN KEY (user_id) REFERENCES users (id);


-- 유저 로그인 히스토리
CREATE TABLE user_login_histories
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    txid       uuid         NOT NULL UNIQUE, -- 요청 트랜잭션 id(UUIDv4)
    user_id    BIGINT       NOT NULL,        -- users PK, 접속 유저 정보
    user_agent VARCHAR(255) NOT NULL,        -- 접속 기기 정보
    public_ip  VARCHAR(20)  NOT NULL,        -- 접속 IP
    created_at TIMESTAMP,                    -- 생성일자
    updated_at TIMESTAMP                     -- 수정일자
);
ALTER TABLE user_login_histories
    ADD CONSTRAINT fk_user_login_histories_users FOREIGN KEY (user_id) REFERENCES users (id);


-- 팔로우 정보
CREATE TABLE follows
(
    id                     BIGSERIAL NOT NULL PRIMARY KEY,
    user_profile_id        BIGINT    NOT NULL,    -- user_profiles PK, 팔로우 요청 유저 정보
    target_user_profile_id BIGINT    NOT NULL,    -- user_profiles PK, 팔로우 대상 유저 정보
    created_at             TIMESTAMP,             -- 생성일자
    updated_at             TIMESTAMP,             -- 수정일자
    is_deleted             BOOLEAN DEFAULT false, -- 삭제여부 true/false
    deleted_at             TIMESTAMP              -- 삭제일자
);
ALTER TABLE follows
    ADD CONSTRAINT fk_follows_user_profiles FOREIGN KEY (user_profile_id) REFERENCES user_profiles (id),
    ADD CONSTRAINT fk_follows_user_profiles FOREIGN KEY (target_user_profile_id) REFERENCES user_profiles (id);


-- 카테고리 정보
CREATE TABLE categories
(
    id         BIGSERIAL   NOT NULL PRIMARY KEY,
    name       VARCHAR(30) NOT NULL,  -- 카테고리 이름
    creator    varchar(20),           -- 카테고리 생성자
    created_at TIMESTAMP,             -- 생성일자
    updated_at TIMESTAMP,             -- 수정일자
    is_deleted BOOLEAN DEFAULT false, -- 삭제여부 true/false
    deleted_at TIMESTAMP              -- 삭제일자
);


-- 게시글 정보
CREATE TABLE posts
(
    id              BIGSERIAL   NOT NULL PRIMARY KEY,
    user_profile_id BIGINT      NOT NULL,  -- user_profiles PK, 작성 유저 정보
    category_id     BIGINT      NOT NULL,  -- categories PK, 카테고리 정보
    title           VARCHAR(50) NOT NULL,  -- 게시글 제목
    contents        TEXT        NOT NULL,  -- 게시글 내용
    latitude        DOUBLE PRECISION,      -- 게시글 작성 시점 위도
    longitude       DOUBLE PRECISION,      -- 게시글 작성 시점 경도
    display_radius  DOUBLE PRECISION,      -- 게시글 표시 반경(km) / 위치기반x: 500km(전국)
    hits            INTEGER DEFAULT 0,     -- 조회수
    likes           INTEGER DEFAULT 0,     -- 좋아요 수
    has_chat        BOOLEAN DEFAULT false, -- 채팅방을 가진 게시글인지 여부 true / false
    created_at      TIMESTAMP,             -- 생성일자
    updated_at      TIMESTAMP,             -- 수정일자
    is_deleted      BOOLEAN DEFAULT false, -- 삭제 여부 true/false
    deleted_at      TIMESTAMP              -- 삭제일자
);
ALTER TABLE posts
    ADD CONSTRAINT fk_posts_user_profiles FOREIGN KEY (user_profile_id) REFERENCES user_profiles (id),
    ADD CONSTRAINT fk_posts_categories FOREIGN KEY (category_id) REFERENCES categories (id);


-- 좋아요 정보
CREATE TABLE likes
(
    id              BIGSERIAL   NOT NULL PRIMARY KEY,
    user_profile_id BIGINT      NOT NULL, -- user_profiles PK, 좋아요 주체 유저 정보
    type            VARCHAR(20) NOT NULL, -- 좋아요 대상 Entity 타입: Posts / ...
    reference_id    BIGINT      NOT NULL  -- 좋아요 대상 Entity PK
);
ALTER TABLE likes
    ADD CONSTRAINT fk_likes_user_profiles FOREIGN KEY (user_profile_id) REFERENCES user_profiles (id);


-- 이미지 정보
CREATE TABLE images
(
    id           BIGSERIAL   NOT NULL PRIMARY KEY,
    url          TEXT        NOT NULL,  -- 이미지 URL
    type         VARCHAR(20) NOT NULL,  -- 이미지 대상 Entity 타입: UserProfiles / Posts / Chats / ...
    reference_id BIGINT      NOT NULL,  -- 이미지 대상 Entity PK
    created_at   TIMESTAMP,             -- 생성일자
    updated_at   TIMESTAMP,             -- 수정일자
    is_deleted   BOOLEAN DEFAULT false, -- 삭제 여부 true/false
    deleted_at   TIMESTAMP              -- 삭제일자
);


-- 외부 API 호출 내역 정보
CREATE TABLE vendor_request_histories
(
    id                   BIGSERIAL   NOT NULL PRIMARY KEY,
    txid                 uuid        NOT NULL UNIQUE, -- 요청 트랜잭션 id(UUIDv4)
    end_point            TEXT        NOT NULL,        -- 요청 end point
    request_header       TEXT        NOT NULL,        -- 요청 header
    request_method       TEXT        NOT NULL,        -- 요청 method
    request_body         TEXT,                        -- 요청 body
    response_header      TEXT        NOT NULL,        -- 응답 header
    response_status_code VARCHAR(10) NOT NULL,        -- 응답 상태 코드
    response_body        TEXT,                        -- 응답 body
    created_at           TIMESTAMP,                   -- 생성일자
    updated_at           TIMESTAMP                    -- 수정일자
);