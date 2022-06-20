create table post
(
    id          int auto_increment comment '表记录ID'
        primary key,
    version     int                     not null comment '版本号',
    create_time datetime                not null comment '创建时间',
    update_time datetime                not null comment '更新时间',
    is_deleted  int                     not null comment '逻辑删除（0：未删除；1：已删除）',
    name        varchar(255)            not null comment '岗位名称',
    description varchar(255) default '' not null comment '岗位描述',
    constraint name
        unique (name)
);

INSERT INTO application.post (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2022-01-16 16:02:30', '2022-01-16 16:02:32', 0, '默认岗位', '新员工岗位');
INSERT INTO application.post (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2021-12-25 16:32:40', '2022-01-16 16:24:02', 0, 'Python初级开发人员', '负责Python简单业务的开发工作');
INSERT INTO application.post (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2021-12-25 16:38:24', '2022-03-06 10:40:29', 0, 'Java高级开发人员', '负责Java复杂业务的开发工作');
INSERT INTO application.post (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2021-12-26 13:05:47', '2022-03-17 02:52:52', 0, '测试初级开发人员', '负责公司业务的测试');
INSERT INTO application.post (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2022-01-01 13:11:35', '2022-01-16 16:43:55', 0, 'Java中级开发人员', '负责Java复杂业务的开发工作');
INSERT INTO application.post (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2021-12-25 16:32:16', '2021-12-25 16:32:16', 0, 'Java初级开发人员', '负责Java简单业务的开发工作');
INSERT INTO application.post (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2022-01-16 16:24:29', '2022-02-16 17:12:18', 0, 'Java测试初级人员', '负责Java开发业务的测试');
INSERT INTO application.post (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2022-03-03 07:00:58', '2022-03-03 07:00:58', 0, 'C#初级开发人员', '负责C#业务的开发');