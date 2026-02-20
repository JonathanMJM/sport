/* 
 * 体育馆预约与人流监测系统数据库初始化脚本
 */
DROP DATABASE IF EXISTS gym_reservation_system;
CREATE DATABASE gym_reservation_system DEFAULT CHARSET utf8mb4;
USE gym_reservation_system;

-- 1. 系统用户表
-- role: admin(管理员), coach(教练), student(学员)
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '登录账号',
  `password` varchar(100) DEFAULT '123456' COMMENT '加密密码',
  `name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `role` varchar(20) NOT NULL COMMENT '角色',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
  `status` varchar(20) DEFAULT 'NORMAL' COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) COMMENT='用户表';

-- 初始化用户数据
INSERT INTO `sys_user` (id, username, password, name, role, status) VALUES 
(1, 'admin', '123456', '系统管理员', 'admin', 'NORMAL'),
(2, 'coach1', '123456', '张健身', 'coach', 'NORMAL'),
(3, 'student1', '123456', '李爱动', 'student', 'NORMAL');

-- 2. 场馆资源表 (Venue)
-- 原 Course 表改造，用于场地预约
CREATE TABLE `gym_venue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '场馆名称',
  `type` varchar(50) DEFAULT NULL COMMENT '类型: 篮球, 羽毛球, 游泳',
  `location` varchar(200) DEFAULT NULL COMMENT '位置',
  `capacity` int DEFAULT 50 COMMENT '最大容纳人数(安全阈值)',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '每小时或每场次价格',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片链接',
  `status` varchar(20) DEFAULT 'OPEN' COMMENT 'OPEN:开放, CLOSED:关闭, MAINTENANCE:维护',
  `description` varchar(500) DEFAULT NULL,
  `open_time` varchar(20) DEFAULT '08:00',
  `close_time` varchar(20) DEFAULT '22:00',
  PRIMARY KEY (`id`)
);

INSERT INTO `gym_venue` (id, name, type, location, capacity, price, status, description) VALUES
(1, '中心篮球场', '篮球', 'A馆1层', 50, 100.00, 'OPEN', '标准木地板篮球场'),
(2, 'VIP羽毛球馆', '羽毛球', 'A馆2层', 20, 50.00, 'OPEN', '专业防滑地胶');

-- 3. 器材表 (Equipment) - 新增
CREATE TABLE `gym_equipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `total_stock` int DEFAULT 0 COMMENT '总库存',
  `available_stock` int DEFAULT 0 COMMENT '可用库存',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '租赁单价',
  `image_url` varchar(255) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'NORMAL', 
  PRIMARY KEY (`id`)
);

INSERT INTO `gym_equipment` (id, name, total_stock, available_stock, price) VALUES
(1, '篮球', 20, 20, 5.00),
(2, '羽毛球拍', 30, 30, 10.00);

-- 4. 私教课程表 (Course) - 保留，用于教练发布课程
CREATE TABLE `gym_course` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `coach_id` bigint(20) DEFAULT NULL,
  `coach_name` varchar(50) DEFAULT NULL,
  `venue_id` bigint(20) DEFAULT NULL COMMENT '关联场馆',
  `time_slot` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT '0.00',
  `max_students` int DEFAULT 10,
  `enrolled` int DEFAULT 0,
  `status` varchar(20) DEFAULT 'PUBLISHED',
  `description` varchar(500) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `gym_course` (id, name, coach_id, coach_name, venue_id, price, max_students, description) VALUES
(1, '体能强化训练', 2, '张健身', 1, 200.00, 10, '高强度间歇训练');

-- 5. 预约/订单表 (Reservation)
-- 统一管理场地预约和课程报名
CREATE TABLE `gym_reservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `type` varchar(20) DEFAULT 'VENUE' COMMENT 'VENUE:场地, COURSE:课程',
  `target_id` bigint(20) NOT NULL COMMENT '场地ID或课程ID',
  `target_name` varchar(100) DEFAULT NULL COMMENT '冗余名称',
  `start_time` datetime DEFAULT NULL COMMENT '预约开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '预约结束时间',
  `price` decimal(10,2) DEFAULT '0.00',
  `status` varchar(20) DEFAULT 'UNPAID' COMMENT 'UNPAID, PAID, CANCELLED, COMPLETED',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

-- 6. 签到与人流监测表 (CheckIn)
CREATE TABLE `gym_checkin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `reservation_id` bigint(20) DEFAULT NULL,
  `venue_id` bigint(20) DEFAULT NULL,
  `check_in_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `check_out_time` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT 'CHECKED_IN' COMMENT 'CHECKED_IN:在场, LEFT:已离场',
  PRIMARY KEY (`id`)
);

-- 7. 器材租赁记录 (EquipmentRental)
CREATE TABLE `gym_equipment_rental` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `equipment_id` bigint(20) NOT NULL,
  `rent_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `return_time` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT 'RENTED' COMMENT 'RENTED:租用中, RETURNED:已归还',
  PRIMARY KEY (`id`)
);

-- 8. 评价表 (Evaluation)
CREATE TABLE `gym_evaluation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `type` varchar(20) DEFAULT 'COACH' COMMENT 'COACH, VENUE',
  `score` int DEFAULT 5,
  `comment` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
