-- 1. 컬럼 삭제: point
ALTER TABLE posts
    DROP COLUMN point;

-- 2. 컬럼 추가: latitude
ALTER TABLE posts
    ADD COLUMN latitude DOUBLE PRECISION;

-- 3. 컬럼 추가: longitude
ALTER TABLE posts
    ADD COLUMN longitude DOUBLE PRECISION;
