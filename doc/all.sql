drop table if exists 'test';
create table `test` (
                        `id` bigint not null comment 'id',
                        `name` varchar(50) not null comment '名称',
                        `password` int comment '密码',
                        primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `test` (id, name, password) values (2,'测试','password');

drop table if exists 'demo';
create table `demo` (
                        `id` bigint not null comment 'id',
                        `name` varchar(50) not null comment '名称',
                        primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';