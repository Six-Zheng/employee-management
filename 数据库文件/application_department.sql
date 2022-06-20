create table department
(
    id          int auto_increment comment '表记录ID'
        primary key,
    version     int                     not null comment '版本号',
    create_time datetime                not null comment '创建时间',
    update_time datetime                not null comment '更新时间',
    is_deleted  int                     not null comment '逻辑删除（0：未删除；1：已删除）',
    name        varchar(255)            not null comment '部门名称',
    description varchar(255) default '' not null comment '部门描述'
);

INSERT INTO application.department (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2022-01-14 14:15:57', '2022-02-16 13:35:53', 0, '默认部门', '新员工部门');
INSERT INTO application.department (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2021-12-25 12:01:21', '2022-03-03 06:38:40', 0, '管理部门', '负责公司的人员招聘以及管理');
INSERT INTO application.department (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2021-12-25 12:34:20', '2022-03-06 10:02:21', 0, '策划部门', '负责公司软件业务的开发');
INSERT INTO application.department (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2022-01-13 20:07:23', '2022-04-04 00:40:14', 0, '市场部门', '负责公司的市场调研以及分析');
INSERT INTO application.department (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2022-01-16 00:34:32', '2022-03-06 10:03:53', 0, '测试部门', '负责与开发部门协调进行业务的调试');
INSERT INTO application.department (version, create_time, update_time, is_deleted, name, description) VALUES (1, '2022-04-07 20:23:15', '2022-04-07 20:23:15', 0, '开发部门', '负责产品开发');