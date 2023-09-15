-- 1. 컬럼 생성: point geometry(Point, 4326) -- 4326:위도_경도_좌표계
ALTER TABLE posts
    add column point geometry(Point, 4326);

-- 2. 컬럼 삭제: latitude
ALTER TABLE posts
    DROP COLUMN latitude;

-- 3. 컬럼 삭제: longitude
ALTER TABLE posts
    DROP COLUMN longitude;

-- 4. 컬럼 삭제: display_radius
ALTER TABLE posts
    DROP COLUMN display_radius;