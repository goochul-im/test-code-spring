insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values ('1', 'kok2@gmail.com', 'kok2', 'Seoul', 'aaaaaaaaaaaaaa-aaaaaaaaa-aaaaaaaa-aaaaaaaaa', 'ACTIVE', '0');
insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values ('2', 'kok3@gmail.com', 'kok3', 'Seoul', 'aaaaaaaaaaaaaa-aaaaaaaaa-aaaaaaaa-aaaaaaaaa', 'PENDING', '0');
alter table `users` alter column id restart with 3;
