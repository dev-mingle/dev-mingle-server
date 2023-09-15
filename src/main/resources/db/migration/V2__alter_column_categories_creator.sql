-- 1. 컬럼명 변경: creator -> creator_id
ALTER TABLE categories
    RENAME COLUMN creator TO creator_id;

-- 2. 컬럼 타입 변경: varchar -> bigint
ALTER TABLE categories
    ALTER COLUMN creator_id TYPE BIGINT USING creator_id::BIGINT;

-- 3. 참조키 생성: fk_categories_users
ALTER TABLE categories
    ADD CONSTRAINT fk_categories_users FOREIGN KEY (creator_id) REFERENCES users (id);