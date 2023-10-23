-- 컬럼 추가: version
ALTER TABLE posts
    ADD COLUMN version BIGINT default 0;