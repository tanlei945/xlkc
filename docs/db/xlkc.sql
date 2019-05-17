/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/5/16 16:41:13                           */
/*==============================================================*/


drop table if exists bb_banner;

drop table if exists bb_books;

drop table if exists bb_config;

drop table if exists bb_course;

drop table if exists bb_course_assistant;

drop table if exists bb_course_per_information;

drop table if exists bb_course_refund;

drop table if exists bb_course_type;

drop table if exists bb_deliveryaddress;

drop table if exists bb_homework;

drop table if exists bb_logistics;

drop table if exists bb_order;

drop table if exists bb_posts;

drop table if exists bb_recommend_posts;

drop table if exists bb_region;

drop table if exists bb_user_recharge;

drop table if exists bb_video;

drop table if exists demo;

drop table if exists jeecg_order_customer;

drop table if exists jeecg_order_main;

drop table if exists jeecg_order_ticket;

drop table if exists onl_cgreport_head;

drop table if exists onl_cgreport_item;

drop table if exists onl_cgreport_param;

drop table if exists sys_announcement;

drop table if exists sys_announcement_send;

drop table if exists sys_data_log;

drop table if exists sys_depart;

drop table if exists sys_dict;

drop table if exists sys_dict_item;

drop table if exists sys_log;

drop table if exists sys_permission;

drop table if exists sys_permission_data_rule;

drop table if exists sys_quartz_job;

drop table if exists sys_role;

drop table if exists sys_role_permission;

drop table if exists sys_sms;

drop table if exists sys_sms_template;

drop table if exists sys_user;

drop table if exists sys_user_depart;

drop table if exists sys_user_role;

drop table if exists user;

drop table if exists user_account;

drop table if exists user_account_bill;

drop table if exists user_address;

drop table if exists user_announcement;

drop table if exists user_buke;

drop table if exists user_collect;

drop table if exists user_course;

drop table if exists user_evaluate;

drop table if exists user_feedback;

drop table if exists user_homework;

drop table if exists user_invoice;

drop table if exists user_invoice_title;

drop table if exists user_leave;

drop table if exists user_message;

drop table if exists user_message_send;

drop table if exists user_pay_evdence;

drop table if exists user_posts;

drop table if exists user_response;

drop table if exists user_third;

drop table if exists user_withdraw;

/*==============================================================*/
/* User: benben                                                 */
/*==============================================================*/
create user benben;

/*==============================================================*/
/* User: "benben-boot"                                          */
/*==============================================================*/
create user "benben-boot";

/*==============================================================*/
/* Table: bb_banner                                             */
/*==============================================================*/
create table bb_banner
(
   id                   varchar(32) not null comment '主键id',
   img_url              varchar(64) comment '图片地址',
   img_size             int(32) comment '图片大小 单位：字节',
   del_flag             varchar(1) default '1' comment '是否删除：0-已删除  1-未删除',
   use_flag             varchar(1) default '0' comment '是否有用：0-不启用 1-启用',
   description          varchar(64) comment '描述',
   create_time          datetime comment '上传时间',
   create_by            varchar(1) comment '上传者',
   update_time          datetime comment '更新时间',
   update_by            varchar(32) comment '更新者'
);

alter table bb_banner comment '轮播图';

/*==============================================================*/
/* Table: bb_books                                              */
/*==============================================================*/
create table bb_books
(
   id                   int not null comment '主键ID',
   name                 varchar(20) comment '书籍名字',
   bookintro            varchar(200) comment '书籍简介',
   price                decimal(10,2) comment '书籍价格',
   num                  int comment '购买人数',
   book_comment         varchar(255) comment '书籍详情',
   book_content         varchar(255) comment '书籍内容',
   book_url             varchar(50) comment '书籍图片地址',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人id',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人id',
   primary key (id)
);

alter table bb_books comment '书籍表';

/*==============================================================*/
/* Table: bb_config                                             */
/*==============================================================*/
create table bb_config
(
   id                   varchar(32) not null comment '主键id',
   config_name          varchar(32) comment '变量名',
   config_group         varchar(32) comment '分组',
   title                varchar(32) comment '变量标题',
   description          varchar(100) comment '描述',
   config_type          varchar(12) comment '类型:string,text,int,bool,array,datetime,date,file',
   config_value         text comment '值',
   content              text comment '变量字典数据',
   rule                 varchar(100) comment '验证规则',
   extend               varchar(255) comment '拓展属性',
   create_by            varchar(32) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间'
);

alter table bb_config comment '系统配置表';

/*==============================================================*/
/* Table: bb_course                                             */
/*==============================================================*/
create table bb_course
(
   id                   int not null comment '主键ID',
   course_name          varchar(20) comment '课程名称',
   comment              varchar(200) comment '课程描述',
   price                decimal(10,2) comment '课程价格',
   num                  int comment '课程人数',
   starttime            datetime comment '上课时间',
   endtime              datetime comment '结束时间',
   address              varchar(50) comment '上课地址',
   language             int comment '语种 0/普通话 1/粤语',
   intro                varchar(200) comment '上课简介',
   course_content       varchar(200) comment '课程部分内容',
   picture_url          varchar(50) comment '课程图片地址',
   video_url            varchar(50) comment '课程视频地址',
   course_verify        int comment '课程预约确认 1/确认 0/待定',
   course_refund        int comment '课程退款 0/可以申请退款 1/退款 2/退款完成',
   achieve              int comment '课程完成 1/已完成 0/未完成',
   create_tiem          datetime comment '创建人时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_course comment '课程表';

/*==============================================================*/
/* Table: bb_course_assistant                                   */
/*==============================================================*/
create table bb_course_assistant
(
   id                   int not null comment '主键ID',
   course_id            int comment '课程相关联id',
   language             int comment '0/普通话 1/粤语',
   company              varchar(50) comment '所属公司名称',
   forte                varchar(20) comment '个人长处',
   skill                varchar(50) comment '对学习者的贡献',
   picture              varchar(50) comment '照片存放地址',
   state                char(10) comment '0/申请失败 1/成功',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time           datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_course_assistant comment '课程助教';

/*==============================================================*/
/* Table: bb_course_per_information                             */
/*==============================================================*/
create table bb_course_per_information
(
   id                   int not null comment '主键ID',
   name                 varchar(20) comment '报名人名称',
   phone                varchar(11) comment '联系电话',
   email                varchar(30) comment '联系邮箱',
   address              varchar(50) comment '居住地',
   company              varchar(20) comment '所在公司',
   referrer             varchar(10) comment '推荐人',
   voucher              int comment '凭证确认 1/ 是  0/否',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_course_per_information comment '预约课程个人信息表';

/*==============================================================*/
/* Table: bb_course_refund                                      */
/*==============================================================*/
create table bb_course_refund
(
   id                   int not null comment '主键ID',
   uid                  int comment '用户相关联id',
   course_id            int comment '课程相关id',
   reason               varchar(255) comment '退款原因',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_course_refund comment '课程退款表';

/*==============================================================*/
/* Table: bb_course_type                                        */
/*==============================================================*/
create table bb_course_type
(
   id                   int not null comment '主键ID',
   name                 varchar(20) comment '课程类型名称',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_course_type comment '课程类型表';

/*==============================================================*/
/* Table: bb_deliveryaddress                                    */
/*==============================================================*/
create table bb_deliveryaddress
(
   id                   int not null comment '主键ID',
   name                 varchar(20) comment '收货人姓名',
   phone                varchar(11) comment '联系电话',
   address              varchar(100) comment '收货地址',
   state                tinyint comment '0/不是默认 1/默认地址',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_deliveryaddress comment '收货地址表';

/*==============================================================*/
/* Table: bb_homework                                           */
/*==============================================================*/
create table bb_homework
(
   id                   int not null comment 'ID',
   course_id            int not null comment '课程相关联id',
   group_name           varchar(20) not null comment '小组名称',
   crew                 varchar(50) not null comment '组员',
   word_url             varchar(100) comment '文档路径',
   word_name            varchar(20) comment '文档名称',
   picture_url          varchar(100) comment '图片路径',
   picture_name         varchar(20) comment '图片名称',
   voice_url            varchar(100) comment '音频路径',
   voice_name           varchar(20) comment '音频名称',
   video_url            varchar(100) comment '视频路径',
   video_name           varchar(20) comment '视频名称',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_homework comment '功课表
';

/*==============================================================*/
/* Table: bb_logistics                                          */
/*==============================================================*/
create table bb_logistics
(
   id                   int not null comment '主键ID',
   book_id              int comment '商品id',
   waybill              varchar(11) comment '运单号',
   express              varchar(10) comment '承运快递',
   phone                varchar(11) comment '快递联系方式',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_logistics comment '物流信息表';

/*==============================================================*/
/* Table: bb_order                                              */
/*==============================================================*/
create table bb_order
(
   id                   int not null comment '主键ID',
   address_id           int comment '地址相关联id',
   book_id              int comment '书籍相关联id',
   remark               varchar(100) comment '备注',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   uodate_by            int comment '修改人',
   primary key (id)
);

alter table bb_order comment '订单表';

/*==============================================================*/
/* Table: bb_posts                                              */
/*==============================================================*/
create table bb_posts
(
   id                   int not null comment '主键ID',
   course_type_id       int comment '课程分类id',
   course_posts_name    varchar(10) comment '帖子分类名称',
   posts_name           varchar(20) comment '帖子标题',
   comment              varchar(255) comment '帖子内容',
   picture_url          varchar(50) comment '图片地址',
   liknum               int comment '点赞次数',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_posts comment '帖子表';

/*==============================================================*/
/* Table: bb_recommend_posts                                    */
/*==============================================================*/
create table bb_recommend_posts
(
   id                   int not null comment '主键ID',
   name                 varchar(20) comment '帖子名称',
   comment              varchar(300) comment '帖子内容',
   picture_url          varchar(50) comment '图片地址',
   likenum              int comment '点赞次数',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table bb_recommend_posts comment '管理员推荐帖子';

/*==============================================================*/
/* Table: bb_region                                             */
/*==============================================================*/
create table bb_region
(
   id                   varchar(32) not null comment '主键id',
   name                 varchar(32) comment '省份名称',
   parent_id            varchar(32) comment '父ID',
   short_name           varchar(32) comment '简称',
   level_type           varchar(1) comment '行政层级',
   city_code            varchar(10) comment '行政编码',
   zip_code             varchar(10) comment '邮政编码',
   merger_name          varchar(100) comment '描述',
   lng                  double(10,6) comment '经度',
   lat                  double(10,6) comment '纬度',
   pinyin               varchar(20) comment '简拼',
   status               varchar(1) default '1',
   create_by            varchar(32) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   primary key (id)
);

alter table bb_region comment '行政区域表';

/*==============================================================*/
/* Table: bb_user_recharge                                      */
/*==============================================================*/
create table bb_user_recharge
(
   id                   varchar(32) comment '主键id',
   user_id              varchar(32) comment '用户id',
   recharge_money       decimal(10,2) comment '充值金额',
   status               varchar(1) comment '0-失败 1-成功',
   recharge_type        varchar(1) comment '1：支付宝 2：微信',
   order_no             varchar(50) comment '第三方订单号',
   create_time          datetime comment '创建时间',
   create_by            varchar(32) comment '创建人',
   update_time          datetime comment '更新时间',
   update_by            varchar(32) comment '编辑人'
);

alter table bb_user_recharge comment '用户充值';

/*==============================================================*/
/* Table: bb_video                                              */
/*==============================================================*/
create table bb_video
(
   id                   int not null comment '主键ID',
   name                 varchar(20) comment '视频名称',
   video_url            varchar(60) comment '视频地址',
   video_type           varchar(20) comment '视频类型',
   video_class          int comment '视频 1/收费视频 0/ 免费视频',
   invitecode           varchar(10) comment '邀请码',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改',
   primary key (id)
);

alter table bb_video comment '学习园地视频';

/*==============================================================*/
/* Table: demo                                                  */
/*==============================================================*/
create table demo
(
   id                   varchar(50) not null comment '主键ID',
   name                 varchar(30) comment '姓名',
   key_word             varchar(255) comment '关键词',
   punch_time           datetime comment '打卡时间',
   salary_money         decimal(10,3) comment '工资',
   bonus_money          double(10,2) comment '奖金',
   sex                  varchar(2) comment '性别 {男:1,女:2}',
   age                  int(11) comment '年龄',
   birthday             date comment '生日',
   email                varchar(50) comment '邮箱',
   content              varchar(1000) comment '个人简介',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '修改人',
   update_time          datetime comment '修改时间',
   primary key (id)
);

alter table demo comment 'demo';

/*==============================================================*/
/* Table: jeecg_order_customer                                  */
/*==============================================================*/
create table jeecg_order_customer
(
   id                   varchar(32) not null comment '主键',
   name                 varchar(100) not null comment '客户名',
   sex                  varchar(4) comment '性别',
   idcard               varchar(18) comment '身份证号码',
   idcard_pic           varchar(500) comment '身份证扫描件',
   telphone             varchar(32) comment '电话1',
   order_id             varchar(32) not null comment '外键',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '修改人',
   update_time          datetime comment '修改时间',
   primary key (id)
);

alter table jeecg_order_customer comment 'jeecg_order_customer';

/*==============================================================*/
/* Table: jeecg_order_main                                      */
/*==============================================================*/
create table jeecg_order_main
(
   id                   varchar(32) not null comment '主键',
   order_code           varchar(50) comment '订单号',
   ctype                varchar(500) comment '订单类型',
   order_date           datetime comment '订单日期',
   order_money          double(10,3) comment '订单金额',
   content              varchar(500) comment '订单备注',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '修改人',
   update_time          datetime comment '修改时间',
   primary key (id)
);

alter table jeecg_order_main comment 'jeecg_order_main';

/*==============================================================*/
/* Table: jeecg_order_ticket                                    */
/*==============================================================*/
create table jeecg_order_ticket
(
   id                   varchar(32) not null comment '主键',
   ticket_code          varchar(100) not null comment '航班号',
   tickect_date         datetime comment '航班时间',
   order_id             varchar(32) not null comment '外键',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '修改人',
   update_time          datetime comment '修改时间',
   primary key (id)
);

alter table jeecg_order_ticket comment 'jeecg_order_ticket';

/*==============================================================*/
/* Table: onl_cgreport_head                                     */
/*==============================================================*/
create table onl_cgreport_head
(
   id                   varchar(36) not null,
   code                 varchar(100) not null comment '报表编码',
   name                 varchar(100) not null comment '报表名字',
   cgr_sql              varchar(1000) not null comment '报表SQL',
   return_val_field     varchar(100) comment '返回值字段',
   return_txt_field     varchar(100) comment '返回文本字段',
   return_type          varchar(2) default '1' comment '返回类型，单选或多选',
   db_source            varchar(100) comment '动态数据源',
   content              varchar(1000) comment '描述',
   update_time          datetime comment '修改时间',
   update_by            varchar(32) comment '修改人id',
   create_time          datetime comment '创建时间',
   create_by            varchar(32) comment '创建人id',
   primary key (id),
   key index_onlinereport_code (code)
);

alter table onl_cgreport_head comment 'onl_cgreport_head';

/*==============================================================*/
/* Table: onl_cgreport_item                                     */
/*==============================================================*/
create table onl_cgreport_item
(
   id                   varchar(36) not null,
   cgrhead_id           varchar(36) not null comment '报表ID',
   field_name           varchar(36) not null comment '字段名字',
   field_txt            varchar(300) comment '字段文本',
   field_width          int(3),
   field_type           varchar(10) comment '字段类型',
   search_mode          varchar(10) comment '查询模式',
   is_order             int(2) default 0 comment '是否排序  0否,1是',
   is_search            int(2) default 0 comment '是否查询  0否,1是',
   dict_code            varchar(36) comment '字典CODE',
   field_href           varchar(120) comment '字段跳转URL',
   is_show              int(2) default 1 comment '是否显示  0否,1显示',
   order_num            int(11) comment '排序',
   replace_val          varchar(200) comment '取值表达式',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '修改人',
   update_time          datetime comment '修改时间',
   primary key (id)
);

alter table onl_cgreport_item comment 'onl_cgreport_item';

/*==============================================================*/
/* Table: onl_cgreport_param                                    */
/*==============================================================*/
create table onl_cgreport_param
(
   id                   varchar(36) not null,
   cgrhead_id           varchar(36) not null comment '动态报表ID',
   param_name           varchar(32) not null comment '参数字段',
   param_txt            varchar(32) comment '参数文本',
   param_value          varchar(32) comment '参数默认值',
   order_num            int(11) comment '排序',
   create_by            varchar(50) comment '创建人登录名称',
   create_time          datetime comment '创建日期',
   update_by            varchar(50) comment '更新人登录名称',
   update_time          datetime comment '更新日期',
   primary key (id)
);

alter table onl_cgreport_param comment 'onl_cgreport_param';

/*==============================================================*/
/* Table: sys_announcement                                      */
/*==============================================================*/
create table sys_announcement
(
   id                   varchar(32) not null,
   titile               varchar(100) comment '标题',
   msg_content          text comment '内容',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   sender               varchar(100) comment '发布人',
   priority             varchar(255) comment '优先级（L低，M中，H高）',
   msg_type             varchar(10) comment '通告对象类型（USER:指定用户，ALL:全体用户）',
   send_status          varchar(10) comment '发布状态（0未发布，1已发布，2已撤销）',
   send_time            datetime comment '发布时间',
   cancel_time          datetime comment '撤销时间',
   del_flag             varchar(1) comment '删除状态（0，正常，1已删除）',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   user_ids             text comment '指定用户',
   primary key (id)
);

alter table sys_announcement comment '系统通告表';

/*==============================================================*/
/* Table: sys_announcement_send                                 */
/*==============================================================*/
create table sys_announcement_send
(
   id                   varchar(32),
   annt_id              varchar(32) comment '通告ID',
   user_id              varchar(32) comment '用户id',
   read_flag            varchar(10) comment '阅读状态（0未读，1已读）',
   read_time            datetime comment '阅读时间',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间'
);

alter table sys_announcement_send comment '用户通告阅读标记表';

/*==============================================================*/
/* Table: sys_data_log                                          */
/*==============================================================*/
create table sys_data_log
(
   id                   varchar(32) not null comment 'id',
   create_by            varchar(32) comment '创建人登录名称',
   create_time          datetime comment '创建日期',
   update_by            varchar(32) comment '更新人登录名称',
   update_time          datetime comment '更新日期',
   data_table           varchar(32) comment '表名',
   data_id              varchar(32) comment '数据ID',
   data_content         text comment '数据内容',
   data_version         int(11) comment '版本号',
   primary key (id)
);

alter table sys_data_log comment 'sys_data_log';

/*==============================================================*/
/* Table: sys_depart                                            */
/*==============================================================*/
create table sys_depart
(
   id                   varchar(32) not null comment 'ID',
   parent_id            varchar(32) comment '父机构ID',
   depart_name          varchar(100) not null comment '机构/部门名称',
   depart_name_en       varchar(500) comment '英文名',
   depart_name_abbr     varchar(500) comment '缩写',
   depart_order         int(11) default 0 comment '排序',
   description          text comment '描述',
   org_type             varchar(10) comment '机构类型',
   org_code             varchar(64) not null comment '机构编码',
   mobile               varchar(32) comment '手机号',
   fax                  varchar(32) comment '传真',
   address              varchar(100) comment '地址',
   memo                 varchar(500) comment '备注',
   status               varchar(1) comment '状态（1启用，0不启用）',
   del_flag             varchar(1) comment '删除状态（0，正常，1已删除）',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建日期',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新日期',
   primary key (id)
);

alter table sys_depart comment '组织机构表';

/*==============================================================*/
/* Table: sys_dict                                              */
/*==============================================================*/
create table sys_dict
(
   id                   varchar(32) not null,
   dict_name            varchar(100) comment '字典名称',
   dict_code            varchar(100) comment '字典编码',
   description          varchar(255) comment '描述',
   del_flag             int(1) comment '删除状态',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   type                 int(1) unsigned zerofill default 0 comment '字典类型0为string,1为number',
   primary key (id),
   key indextable_dict_code (dict_code)
);

alter table sys_dict comment 'sys_dict';

/*==============================================================*/
/* Table: sys_dict_item                                         */
/*==============================================================*/
create table sys_dict_item
(
   id                   varchar(32) not null,
   dict_id              varchar(32) comment '字典id',
   item_text            varchar(100) comment '字典项文本',
   item_value           varchar(100) comment '字典项值',
   description          varchar(255) comment '描述',
   sort_order           int(10) comment '排序',
   status               int(11) comment '状态（1启用 0不启用）',
   create_by            varchar(32),
   create_time          datetime,
   update_by            varchar(32),
   update_time          datetime,
   primary key (id)
);

alter table sys_dict_item comment 'sys_dict_item';

/*==============================================================*/
/* Table: sys_log                                               */
/*==============================================================*/
create table sys_log
(
   id                   varchar(32) not null,
   log_type             int(2) comment '日志类型（1登录日志，2操作日志）',
   log_content          varchar(1000) comment '日志内容',
   operate_type         int(2) comment '操作类型',
   userid               varchar(32) comment '操作用户账号',
   username             varchar(100) comment '操作用户名称',
   ip                   varchar(100) comment 'IP',
   method               varchar(500) comment '请求java方法',
   request_url          varchar(255) comment '请求路径',
   request_param        varchar(255) comment '请求参数',
   request_type         varchar(10) comment '请求类型',
   cost_time            bigint(20) comment '耗时',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   primary key (id)
);

alter table sys_log comment '系统日志表';

/*==============================================================*/
/* Table: sys_permission                                        */
/*==============================================================*/
create table sys_permission
(
   id                   varchar(32) not null comment '主键id',
   parent_id            varchar(32) comment '父id',
   name                 varchar(100) comment '菜单标题',
   url                  varchar(255) comment '路径',
   component            varchar(255) comment '组件',
   component_name       varchar(100) comment '组件名字',
   redirect             varchar(255) comment '一级菜单跳转地址',
   menu_type            int(11) comment '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
   perms                varchar(255) comment '菜单权限编码',
   sort_no              double(3,2) comment '菜单排序',
   always_show          tinyint(1) comment '聚合子路由: 1是0否',
   icon                 varchar(100) comment '菜单图标',
   is_route             tinyint(1) default 1 comment '是否路由菜单: 0:不是  1:是（默认值1）',
   is_leaf              tinyint(1) comment '是否叶子节点:    1:是   0:不是',
   hidden               int(2) default 0 comment '是否隐藏路由: 0否,1是',
   description          varchar(255) comment '描述',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   del_flag             int(1) default 0 comment '删除状态 0正常 1已删除',
   primary key (id)
);

alter table sys_permission comment '菜单权限表';

/*==============================================================*/
/* Table: sys_permission_data_rule                              */
/*==============================================================*/
create table sys_permission_data_rule
(
   id                   varchar(32) not null comment 'ID',
   permission_id        varchar(32) comment '菜单ID',
   rule_name            varchar(50) comment '规则名称',
   rule_column          varchar(50) comment '字段',
   rule_conditions      varchar(50) comment '条件',
   rule_value           varchar(300) comment '规则值',
   create_time          datetime comment '创建时间',
   create_by            varchar(32),
   update_time          datetime comment '修改时间',
   update_by            varchar(32) comment '修改人',
   primary key (id)
);

alter table sys_permission_data_rule comment 'sys_permission_data_rule';

/*==============================================================*/
/* Table: sys_quartz_job                                        */
/*==============================================================*/
create table sys_quartz_job
(
   id                   varchar(32) not null,
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   del_flag             int(1) comment '删除状态',
   update_by            varchar(32) comment '修改人',
   update_time          datetime comment '修改时间',
   job_class_name       varchar(255) comment '任务类名',
   cron_expression      varchar(255) comment 'cron表达式',
   parameter            varchar(255) comment '参数',
   description          varchar(255) comment '描述',
   status               int(1) comment '状态 0正常 -1停止',
   primary key (id)
);

alter table sys_quartz_job comment 'sys_quartz_job';

/*==============================================================*/
/* Table: sys_role                                              */
/*==============================================================*/
create table sys_role
(
   id                   varchar(32) not null comment '主键id',
   role_name            varchar(200) comment '角色名称',
   role_code            varchar(100) comment '角色编码',
   description          varchar(255) comment '描述',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   primary key (id),
   key index_role_code (role_code)
);

alter table sys_role comment '角色表';

/*==============================================================*/
/* Table: sys_role_permission                                   */
/*==============================================================*/
create table sys_role_permission
(
   id                   varchar(32) not null,
   role_id              varchar(32) comment '角色id',
   permission_id        varchar(32) comment '权限id',
   data_rule_ids        varchar(1000),
   primary key (id)
);

alter table sys_role_permission comment '角色权限表';

/*==============================================================*/
/* Table: sys_sms                                               */
/*==============================================================*/
create table sys_sms
(
   id                   varchar(32) not null comment 'ID',
   es_title             varchar(100) comment '消息标题',
   es_type              varchar(1) comment '发送方式：1短信 2邮件 3微信',
   es_receiver          varchar(50) comment '接收人',
   es_param             varchar(1000) comment '发送所需参数Json格式',
   es_content           longtext comment '推送内容',
   es_send_time         datetime comment '推送时间',
   es_send_status       varchar(1) comment '推送状态 0未推送 1推送成功 2推送失败 -1失败不再发送',
   es_send_num          int(11) comment '发送次数 超过5次不再发送',
   es_result            varchar(255) comment '推送失败原因',
   remark               varchar(500) comment '备注',
   create_by            varchar(32) comment '创建人登录名称',
   create_time          datetime comment '创建日期',
   update_by            varchar(32) comment '更新人登录名称',
   update_time          datetime comment '更新日期',
   primary key (id)
);

alter table sys_sms comment 'sys_sms';

/*==============================================================*/
/* Table: sys_sms_template                                      */
/*==============================================================*/
create table sys_sms_template
(
   id                   varchar(32) not null comment '主键',
   template_name        varchar(50) comment '模板标题',
   template_code        varchar(32) not null comment '模板CODE',
   template_type        varchar(1) not null comment '模板类型：1短信 2邮件 3微信',
   template_content     varchar(1000) not null comment '模板内容',
   template_test_json   varchar(1000) comment '模板测试json',
   create_time          datetime comment '创建日期',
   create_by            varchar(32) comment '创建人登录名称',
   update_time          datetime comment '更新日期',
   update_by            varchar(32) comment '更新人登录名称',
   primary key (id),
   key uniq_templatecode (template_code)
);

alter table sys_sms_template comment 'sys_sms_template';

/*==============================================================*/
/* Table: sys_user                                              */
/*==============================================================*/
create table sys_user
(
   id                   varchar(32) not null comment '主键id',
   username             varchar(100) comment '登录账号',
   realname             varchar(100) comment '真实姓名',
   password             varchar(255) comment '密码',
   salt                 varchar(45) comment 'md5密码盐',
   avatar               varchar(255) comment '头像',
   birthday             datetime comment '生日',
   sex                  int(11) comment '性别（1：男 2：女）',
   email                varchar(45) comment '电子邮件',
   phone                varchar(45) comment '电话',
   status               int(2) comment '状态(1：正常  2：冻结 ）',
   del_flag             varchar(1) comment '删除状态（0，正常，1已删除）',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   longitude            decimal(10,6),
   latitude             decimal(10,6),
   primary key (id),
   key index_user_name (username)
);

alter table sys_user comment '用户表';

/*==============================================================*/
/* Table: sys_user_depart                                       */
/*==============================================================*/
create table sys_user_depart
(
   ID                   varchar(32) not null comment 'id',
   user_id              varchar(32) comment '用户id',
   dep_id               varchar(32) comment '部门id',
   primary key (ID)
);

alter table sys_user_depart comment 'sys_user_depart';

/*==============================================================*/
/* Table: sys_user_role                                         */
/*==============================================================*/
create table sys_user_role
(
   id                   varchar(32) not null comment '主键id',
   user_id              varchar(32) comment '用户id',
   role_id              varchar(32) comment '角色id',
   primary key (id)
);

alter table sys_user_role comment '用户角色表';

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   national varchar(32) not null comment 'ID',
   chinarname           national varchar(32) not null comment '中文姓名',
   englishname          national varchar(32) not null comment '英文姓名',
   nickname             national varchar(50) not null comment '昵称',
   password             national varchar(32) not null comment '密码',
   salt                 national varchar(30) not null comment '密码盐',
   email                national varchar(100) comment '电子邮箱',
   mobile               national varchar(11) not null comment '手机号',
   referrer             varchar(11) not null comment '推荐人',
   address              varchar(100) not null comment '所在地区',
   company               varchar(100) not null comment '所在公司',
   avatar               national varchar(255) not null comment '头像',
   level                tinyint(1) unsigned default 0 comment '等级',
   sex                  tinyint(1) unsigned not null default 0 comment '性别  0/男,1/女',
   birthday             datetime comment '生日',
   bio                  national varchar(100) comment '格言',
   money                decimal(10,2) unsigned not null default 0.00 comment '余额',
   score                int(10) unsigned default 0 comment '积分',
   success_ions         int(10) unsigned default 1 comment '连续登录天数',
   maxsuccess_ions      int(10) unsigned not null default 1 comment '最大连续登录天数',
   prev_time            int(10) unsigned not null default 0 comment '上次登录时间',
   login_time           int(10) unsigned not null default 0 comment '登录时间',
   loginip              national varchar(50) not null comment '登录IP',
   loginfailure         tinyint(1) unsigned not null default 0 comment '失败次数',
   joinip               national varchar(50) not null comment '加入IP',
   join_time            datetime comment '加入时间',
   create_time          datetime comment '创建时间',
   create_by            national varchar(32) comment '创建人',
   update_time          datetime comment '更新时间',
   update_by            national varchar(32) comment '编辑人',
   token                national varchar(50) not null comment 'Token',
   status               tinyint(1) unsigned not null default 1 comment '状态(1：正常  2：冻结 ）',
   del_flag             national varchar(1) not null comment '删除状态（0，正常，1已删除）',
   verification         national varchar(255) not null comment '验证',
   wx_id                national varchar(50) comment '微信id'
);

alter table user comment '用户会员表';

/*==============================================================*/
/* Table: user_account                                          */
/*==============================================================*/
create table user_account
(
   id                   varchar(32) not null comment '主键id',
   vip_flag             varchar(1) comment '0:非会员 1：会员',
   user_id              varchar(32) comment '用户id',
   account_no           varchar(50) comment '账号 支付账号或微信账号或银行卡号',
   status               varchar(1) comment '0-失败 1-成功',
   create_by            varchar(32) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间'
);

alter table user_account comment '用户账户表';

/*==============================================================*/
/* Table: user_account_bill                                     */
/*==============================================================*/
create table user_account_bill
(
   id                   varchar(32) comment '主键id',
   user_id              varchar(32) comment '用户id',
   before_money         decimal(2) comment '操作前金额',
   change_money         decimal(2) comment '变化金额',
   after_mpney          decimal(2) comment '操作后金额',
   sign                 varchar(1) comment '标志符 + -',
   bill_type            varchar(1) comment '1:充值 2：消费',
   create_by            varchar(32) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间'
);

alter table user_account_bill comment '用户账户流水表';

/*==============================================================*/
/* Table: user_address                                          */
/*==============================================================*/
create table user_address
(
   id                   varchar(32) not null comment '主键id',
   user_id              varchar(32) comment '用户id',
   detailed_address     varchar(100) comment '详细地址',
   del_flag             varchar(1) comment '是否删除',
   default_flag         varchar(1) comment '是否默认（1：默认）',
   lng                  double(10,6) comment '经度',
   lat                  double(10,6) comment '纬度',
   provice              varchar(32) comment '省份',
   city                 varchar(32) comment '城市',
   area                 varchar(32) comment '县（区）',
   address_label        varchar(32) comment '标签1-公司 2-家 3-学校',
   reciver_name         varchar(32) comment '收货人姓名',
   reciver_telephone    varchar(16) comment '收货人电话',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间'
);

alter table user_address comment '用户地址表';

/*==============================================================*/
/* Table: user_announcement                                     */
/*==============================================================*/
create table user_announcement
(
   id                   varchar(32) not null,
   titile               varchar(100) comment '标题',
   msg_content          text comment '内容',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   sender               varchar(100) comment '发布人',
   send_status          varchar(10) comment '发布状态（0未发布，1已发布，2已撤销）',
   send_time            datetime comment '发布时间',
   cancel_time          datetime comment '撤销时间',
   img_url              varchar(32) comment '图片',
   del_flag             varchar(1) default '0' comment '删除状态（0，正常，1已删除）',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间'
);

alter table user_announcement comment '用户通告表';

/*==============================================================*/
/* Table: user_buke                                             */
/*==============================================================*/
create table user_buke
(
   id                   int not null comment '主键ID',
   uid                  int comment '用户相关联id',
   course_id            varchar(20) comment '课程相关联id',
   comment              varchar(200) comment '补课内容',
   start_time           datetime comment '补课开始时间',
   end_time             datetime comment '补课结束时间',
   state                int comment '0/可以补课 1/审核中 2/成功',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table user_buke comment '用户补课表';

/*==============================================================*/
/* Table: user_collect                                          */
/*==============================================================*/
create table user_collect
(
   id                   int not null comment '主键ID',
   posts_id             int comment '和帖子相关联id',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table user_collect comment '我的收藏';

/*==============================================================*/
/* Table: user_course                                           */
/*==============================================================*/
create table user_course
(
   id                   int not null comment '主键ID',
   uid                  int comment '用户相关联id',
   course_id            int comment '课程相关联id',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table user_course comment '我的课程';

/*==============================================================*/
/* Table: user_evaluate                                         */
/*==============================================================*/
create table user_evaluate
(
   id                   varchar(32) not null comment '主键id',
   user_id              varchar(32) comment '评论用户id',
   posts_id             varchar(32) comment '帖子相关联iid',
   content              text comment '评论内容',
   evaluate_type        varchar(1) comment '0：评论商家 1：评论骑手',
   img_url              varchar(32) comment '用户上传图片',
   del_flag             varchar(1) default '1' comment '是否被删除 0：已删除 1：未删除',
   create_by            varchar(32) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间'
);

alter table user_evaluate comment '用户评价表';

/*==============================================================*/
/* Table: user_feedback                                         */
/*==============================================================*/
create table user_feedback
(
   id                   int not null comment '主键ID',
   uid                  int comment '用户相关联id',
   question_type        varchar(20) comment '问题类型',
   comment              varchar(255) comment '反馈描述',
   picker_url           varchar(50) comment '反馈图片',
   contact              varchar(20) comment '联系方式',
   create_time          datetime comment '创建人时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table user_feedback comment '用户反馈表';

/*==============================================================*/
/* Table: user_homework                                         */
/*==============================================================*/
create table user_homework
(
   id                   int not null comment '主键ID',
   uid                  int comment '和用户相关联ID',
   hid                  int comment '和功课相关联ID',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人id',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人id',
   primary key (id)
);

alter table user_homework comment '我的功课';

/*==============================================================*/
/* Table: user_invoice                                          */
/*==============================================================*/
create table user_invoice
(
   id                   varchar(32) not null comment '主键id',
   user_id              varchar(32) comment '用户id',
   invoice_titile_id    varchar(64) comment '纳税人名称',
   invoice_type         varchar(1) comment '发票类型（个人:0  公司:1）',
   invoice_money        decimal(2) comment '开票金额',
   invoice_content      varchar(64) comment '发票内容',
   phone                varchar(11) comment '电话',
   mailing_address      varchar(64) comment '邮寄地址',
   paper_flag           varchar(1) comment '0：不是纸质发票 1：是',
   email                varchar(32) comment '邮箱',
   status               varchar(1) comment '0：失败 1 成功',
   create_by            varchar(32) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   primary key (id),
   key index_user_name (user_id)
);

alter table user_invoice comment '用户发票表';

/*==============================================================*/
/* Table: user_invoice_title                                    */
/*==============================================================*/
create table user_invoice_title
(
   id                   varchar(32) not null comment '主键id',
   user_id              varchar(32) comment '用户id',
   tax_name             varchar(64) comment '纳税人名称',
   tax_no               varchar(50) comment '税号',
   invoice_type         varchar(1) comment '发票类型（个人:0  公司:1）',
   company_bank         varchar(64) comment '开户行名称',
   company_address      varchar(100) comment '公司地址',
   bank_account         varchar(50) comment '银行账号',
   telephone            varchar(32) comment '电话号码',
   status               varchar(1) comment '0：失败 1 成功',
   create_by            varchar(32) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   primary key (id),
   key index_user_name (user_id)
);

alter table user_invoice_title comment '用户发票抬头';

/*==============================================================*/
/* Table: user_leave                                            */
/*==============================================================*/
create table user_leave
(
   id                   int not null comment '主键ID',
   uid                  int comment '用户相关联id',
   course_id            varchar(20) comment '课程相关联id',
   comment              varchar(200) comment '请假内容',
   start_time           datetime comment '请假开始时间',
   end_time             datetime comment '请假结束时间',
   state                int comment '0/可以请假 1/审核中 2/成功',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table user_leave comment '用户请假表';

/*==============================================================*/
/* Table: user_message                                          */
/*==============================================================*/
create table user_message
(
   id                   int not null comment '主键id',
   posts_id             int comment '和帖子相关联id',
   name                 varchar(20) comment '消息名称',
   comment              varchar(500) comment '消息的描述',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table user_message comment '用户消息';

/*==============================================================*/
/* Table: user_message_send                                     */
/*==============================================================*/
create table user_message_send
(
   id                   varchar(32),
   message_id           varchar(32) comment '消息ID',
   user_id              varchar(32) comment '用户id',
   read_flag            varchar(10) default '0' comment '阅读状态（0未读，1已读）',
   read_time            datetime comment '阅读时间',
   create_by            varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(32) comment '更新人',
   update_time          datetime comment '更新时间'
);

alter table user_message_send comment '用户消息阅读标记表';

/*==============================================================*/
/* Table: user_pay_evdence                                      */
/*==============================================================*/
create table user_pay_evdence
(
   id                   int not null comment '主键ID',
   uid                  int comment '用户相关联id',
   picture              varchar(50) comment '汇款凭证地址',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table user_pay_evdence comment '用户支付凭证表';

/*==============================================================*/
/* Table: user_posts                                            */
/*==============================================================*/
create table user_posts
(
   id                   int not null comment '主键ID',
   posts_id             int comment '和帖子相关联id',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   uodate_by            int comment '修改人',
   primary key (id)
);

alter table user_posts comment '我的帖子';

/*==============================================================*/
/* Table: user_response                                         */
/*==============================================================*/
create table user_response
(
   id                   int not null comment '主键ID',
   evaluate_id          int comment '评论人id',
   user_id              int comment '回复用户id',
   comment              varchar(255) comment '回复内容',
   create_time          datetime comment '创建时间',
   create_by            int comment '创建人',
   update_time          datetime comment '修改时间',
   update_by            int comment '修改人',
   primary key (id)
);

alter table user_response comment '用户回复表';

/*==============================================================*/
/* Table: user_third                                            */
/*==============================================================*/
create table user_third
(
   id                   varchar(32) not null,
   user_id              varchar(32) comment '用户ID',
   open_id              varchar(32) comment 'OpenId',
   open_type            varchar(1) comment '状态  0/QQ,1/微信 2/微博',
   status               varchar(1) default '0' comment '状态  0/启用,1/未启用,2/已删除',
   create_time          datetime comment '创建时间',
   create_by            varchar(32) comment '创建人',
   update_time          datetime comment '更新时间',
   update_by            varchar(32) comment '编辑人'
);

alter table user_third comment '用户三方表';

/*==============================================================*/
/* Table: user_withdraw                                         */
/*==============================================================*/
create table user_withdraw
(
   id                   varchar(32) comment '主键id',
   user_id              varchar(32) comment '用户id',
   money                decimal(10,2) comment '充值金额',
   status               varchar(1) default '0' comment '0-未审核 1-审核未通过 2-审核已通过',
   withdraw_type        varchar(1) comment '1：支付宝 2：微信',
   order_no             varchar(50) comment '第三方订单号',
   create_time          datetime comment '创建时间',
   create_by            varchar(32) comment '创建人',
   update_time          datetime comment '更新时间',
   update_by            varchar(32) comment '编辑人'
);

alter table user_withdraw comment '用户提现';

