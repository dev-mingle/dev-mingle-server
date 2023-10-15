CREATE TABLE chat_rooms
(
    id                   BIGSERIAL   NOT NULL PRIMARY KEY,
    name                 varchar(50) NOT NULL UNIQUE,
    user_count           INT,
    admin_user           BIGSERIAL   NOT NULL,
    created_at           TIMESTAMP,                   -- 생성일자
    updated_at           TIMESTAMP                    -- 수정일자
);

ALTER TABLE chat_rooms
    ADD CONSTRAINT fk_chat_rooms_user_profiles FOREIGN KEY (admin_user) REFERENCES user_profiles (id);

CREATE TABLE chat_members
(
    id                   BIGSERIAL   NOT NULL PRIMARY KEY,
    user_profiles_id     BIGSERIAL   NOT NULL,
    chat_rooms_id        BIGSERIAL   NOT NULL
);


ALTER TABLE chat_members
    ADD CONSTRAINT fk_chat_members_chat_rooms FOREIGN KEY (chat_rooms_id) REFERENCES chat_rooms (id),
    ADD CONSTRAINT fk_chat_members_user_profiles FOREIGN KEY (user_profiles_id) REFERENCES user_profiles (id);