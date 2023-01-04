
test;
CREATE TABLE IF NOT EXISTS `t_region_user`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `sid` VARCHAR(255) NOT NULL,
   `scheme` VARCHAR(16) NOT NULL COMMENT '网络协议',
   `domain` VARCHAR(16) NOT NULL COMMENT '域名',
   `port` INT NOT NULL COMMENT '端口',
   `username` VARCHAR(16) COMMENT '账号',
   `password` VARCHAR(16) COMMENT '密码',
   `role_name` VARCHAR(16) COMMENT '角色名称',
   `region` VARCHAR(64) COMMENT '游戏分区',
   `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
   `last_update_time` TIMESTAMP,
   PRIMARY KEY ( `id` ),
   KEY ( `sid` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- auto-generated definition
create table t_button
(
    id   bigint auto_increment
        primary key,
    name varchar(32) null comment '按钮名称',
    text varchar(32) null comment '按钮文字'
)
    comment '按钮操作';

-- auto-generated definition
create table t_option_resource
(
    id                     bigint             not null
        primary key,
    operatename            varchar(100)       null comment '操作名称',
    refreshfrequency       bigint default 0   null comment '刷新频率,默认值为0，表示固定的NPC',
    priority               int    default 0   null comment '优先级 值越大，越优先操作',
    enablenoviceprotection char   default '0' null comment '新手保护 0否1是',
    lastruntime            datetime           null comment '上一次运行时间',
    worldmap_id            bigint             null,
    deleted                bigint default 0   null comment '是否删除0未删除，删除时间戳',
    constraint fk_option_resource_worldmap_key
        foreign key (worldmap_id) references t_worldmap (id)
)
    comment '资源操作表';

-- auto-generated definition
create table t_region_user
(
    id               int unsigned auto_increment
        primary key,
    sid              varchar(255)                            not null,
    scheme           varchar(16)                             not null comment '网络协议',
    domain           varchar(16)                             not null comment '域名',
    port             int                                     not null comment '端口',
    username         varchar(16)                             null comment '账号',
    password         varchar(16)                             null comment '密码',
    role_name        varchar(16)                             null comment '角色名称',
    region           varchar(64)                             null comment '游戏分区',
    create_time      timestamp default CURRENT_TIMESTAMP     not null,
    last_update_time timestamp default '0000-00-00 00:00:00' not null,
    deleted          bigint    default 0                     null comment '是否删除0未删除，删除时间戳'
)
    comment '用户表' charset = utf8mb4;

create index sid
    on t_region_user (sid);

-- auto-generated definition
create table t_routers
(
    id               bigint auto_increment
        primary key,
    accessvalue      char             not null comment '路径绑定的key的值',
    name             char             null,
    repetition_count int    default 1 null comment '该步骤重复次数 1/不重复',
    sort             int              null comment '顺序',
    world_map_id     bigint           null comment '外键',
    deleted          bigint default 0 null comment '是否删除0未删除，删除时间戳',
    constraint fk_t_routers_t_worldmap
        foreign key (world_map_id) references t_worldmap (id)
)
    comment '路径表';

-- auto-generated definition
create table t_single_option
(
    id                 bigint auto_increment
        primary key,
    option_resource_id bigint      null,
    routers_id         bigint      null,
    button_id          bigint      null,
    action_step_id     bigint      null,
    name               varchar(32) null comment '操作名称',
    type               char        null comment '1.option_resource 2.routers 3.button 4.action_setp'
)
    comment '单个操作表';

-- auto-generated definition
create table t_task
(
    id                  bigint auto_increment
        primary key,
    name                varchar(32)        null comment '任务名称',
    repetion            char   default '0' null comment '可重复?0否1是',
    follow_up_task_id   bigint             null comment '后续任务id',
    follow_up_wait_time bigint default 0   null comment '后续任务等待时间 单位:ms'
)
    comment '任务表';

-- auto-generated definition
create table t_worldmap
(
    id                           bigint auto_increment
        primary key,
    name                         varchar(32)      null comment '名称',
    target_page_unique_link_name varchar(32)      null comment '目标地址唯一路径',
    transferpoint                bigint           null comment '目的地的地图传送点',
    deleted                      bigint default 0 null comment '是否删除0未删除，删除时间戳'
)
    comment '地图位置';

