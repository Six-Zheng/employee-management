create table role
(
    id          int auto_increment comment '表记录ID'
        primary key,
    version     int                     null comment '版本号',
    create_time datetime                null comment '创建时间',
    update_time datetime                null comment '更新时间',
    is_deleted  int                     null comment '逻辑删除（0：未删除；1：已删除）',
    name        varchar(255)            not null comment '角色名称',
    description varchar(255) default '' not null comment '角色描述',
    tag         varchar(255)            not null comment '角色标识',
    constraint role_name_uindex
        unique (name),
    constraint role_tag_uindex
        unique (tag)
);

INSERT INTO application.role (version, create_time, update_time, is_deleted, name, description, tag) VALUES (1, '2022-03-03 02:57:05', '2022-03-03 02:59:40', 0, '默认角色', '用户默认角色', 'ROLE_DEFAULT');
INSERT INTO application.role (version, create_time, update_time, is_deleted, name, description, tag) VALUES (1, '2022-04-04 04:20:04', '2022-04-04 04:20:04', 0, '管理员', '具有系统所有可操作权限', 'ROLE_ADMIN');
INSERT INTO application.role (version, create_time, update_time, is_deleted, name, description, tag) VALUES (1, '2022-04-04 08:19:54', '2022-04-04 21:21:13', 0, '临时管理员', '临时管理员', 'ROLE_ADMIN_TEMPLATE');