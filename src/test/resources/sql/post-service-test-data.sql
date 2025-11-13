INSERT INTO users (id, email, nickname, address, certification_code, status, last_login_at) VALUES (1, 'kok2@gmail.com', 'kok2', 'Seoul', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ACTIVE', 0);
INSERT INTO posts (id, content, created_at, modified_at, user_id) VALUES (1, 'helloworld', 1678530673958, null, 1);
alter table `posts` alter column id restart with 2;
