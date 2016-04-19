create table user(
  id                    bigint auto_increment primary key,
  login_name          varchar(32) comment 'label: 登录名, searchable: eq, validate: length(min=5, max=32)',
  salt                  varchar(64) comment 'label: 用于加密的盐',
  login_password      varchar(64) comment 'label: 加密后的密码',
  token               varchar(225) comment 'label: api认证token',
	user_type            varchar(2) comment 'label: 用户类型:0管理员，1普通用户',
  name                 varchar(50) comment 'label: 姓名, searchable: like',
  sex                  varchar(2) comment 'label: 性别',
  mobile               varchar(20) comment 'label: 手机',
	qq             		varchar(20) comment 'label: QQ',
  email                 varchar(100) comment 'label: 邮箱, searchable: like, validate: email',
  province             varchar(6) comment 'label: 省份',
  city                 varchar(6) comment 'label: 城市',
  district             varchar(6) comment 'label: 区县',
  address_info         varchar(1000) comment 'label: 地址',
	status               varchar(2) comment 'label: 用户状态 1正常，0删除，2试用，3禁用',
  creator_id           bigint comment '创建人id',
  create_date          timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' comment '创建时间',
  modifier_id          bigint comment '修改人id',
  update_date          timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' comment '修改时间',
  unique key uk_login_name(login_name)
) engine=innodb default charset=utf8 comment '用户表';

insert into user set name = '管理员',
  login_name = 'admin', salt='27ad28b980affe8c453e4f65fadcba45624b6a01', login_password='a96cdd2daff763cbab9028f2730d83a6cd5ddb93',
  user_type = '0',
  status = '1',
  create_date = now();