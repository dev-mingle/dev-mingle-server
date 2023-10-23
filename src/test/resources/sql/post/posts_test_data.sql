CREATE ALIAS IF NOT EXISTS H2GIS_SPATIAL FOR "org.h2gis.functions.factory.H2GISFunctions.load";
CALL H2GIS_SPATIAL();

insert into users (id, email, password, provider, provider_id, role)
values (1, '1234@naver.com', '1234', 'GOOGLE', 'uuid', 'USER');

insert into user_profiles (id, user_id, nickname)
values (1, 1, 'user1');

insert into categories (id, name)
values (1, 'ctegory1'),
       (2, 'ctegory2');

insert into posts (id, user_profile_id, category_id, title, contents, latitude, longitude, created_at, is_deleted)
values (1, 1, 1, '서울대입구', '서울대입구', 37.481445, 126.952688, current_time, false),
       (2, 1, 1, '신림', '신림', 37.484397, 126.929644, TIMESTAMPADD(minute, 1, NOW()), false),
       (3, 1, 1, '신도림', '신도림', 37.511399, 126.891976, TIMESTAMPADD(minute, 2, NOW()), false),
       (4, 1, 1, '낙성대', '낙성대', 37.477255, 126.963437, TIMESTAMPADD(minute, 3, NOW()), false),
       (5, 1, 2, '신림2', '신림', 37.484397, 126.929644, TIMESTAMPADD(minute, 4, NOW()), false),
       (6, 1, 1, '삭제포스트', '삭제테스트', 37.484397, 126.929644, TIMESTAMPADD(minute, 5, NOW()), true);
