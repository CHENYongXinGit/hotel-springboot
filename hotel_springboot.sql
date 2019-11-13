/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50727
Source Host           : localhost:3306
Source Database       : hotel_springboot

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2019-11-13 18:46:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`username`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名' ,
`password`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码' ,
`created`  bigint(20) NULL DEFAULT NULL COMMENT '创建时间' ,
`updated`  bigint(20) NULL DEFAULT NULL COMMENT '修改时间' ,
`status`  int(2) NOT NULL DEFAULT 0 COMMENT '状态' ,
`phone`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号' ,
`photo`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像' ,
`remarks`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=5

;

-- ----------------------------
-- Records of admin
-- ----------------------------
BEGIN;
INSERT INTO `admin` VALUES ('1', '糖果', '9b9d50466c3878f3ab847cc16cdcd0ce', '1571811964616', '1571820799339', '0', '18319157026', 'http://tangguo.cn-gd.ufileos.com/a4a220ea-4f04-49a1-b49d-3c91b6151903.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=lhNR1HUYl581wGBW1qfLGbqi5SE%3D&Expires=1574406985', '管理员'), ('2', '张三', '6ba23264cbfc1025e480d270376b3311', '1571811964616', '1572610623728', '0', '13229852398', 'http://tangguo.cn-gd.ufileos.com/46bf376e-7932-4b20-a8d4-2d9f6e1506be.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=%2BCZueMhN3RyhK1DIA%2BTKoXCBGeY%3D&Expires=1575202623', '员工a'), ('3', '小欣', 'b0f4c436f7544dc81063fd3af92754bd', '1572314767838', '1572577929796', '1', '13255555555', null, ''), ('4', '张晓雪', 'f90c777993e7e4a3d1a4efb075027b35', '1572314982360', '1572314982360', '0', '18333333333', null, '测试');
COMMIT;

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
`admin_id`  bigint(20) NULL DEFAULT NULL ,
`role_id`  bigint(20) NULL DEFAULT NULL 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
BEGIN;
INSERT INTO `admin_role` VALUES ('4', '4'), ('2', '1');
COMMIT;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
`c_id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`c_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`c_sex`  int(1) NULL DEFAULT NULL ,
`c_phone`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`c_identity`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`c_member`  int(1) NULL DEFAULT 0 ,
`c_password`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`c_created`  bigint(20) NULL DEFAULT NULL ,
`c_updated`  bigint(20) NULL DEFAULT NULL ,
PRIMARY KEY (`c_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=3

;

-- ----------------------------
-- Records of customer
-- ----------------------------
BEGIN;
INSERT INTO `customer` VALUES ('1', '张思德', '2', '13727766732', '440902199802222122', '1', '123456', '1572761607207', '1572761607207'), ('2', '周生生', '1', '18322222222', '440322222222222222', '0', '123456', '1573131640133', '1573131640133');
COMMIT;

-- ----------------------------
-- Table structure for customer_order
-- ----------------------------
DROP TABLE IF EXISTS `customer_order`;
CREATE TABLE `customer_order` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`room_number`  bigint(20) NULL DEFAULT NULL ,
`name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sex`  int(1) NULL DEFAULT 1 ,
`member`  int(1) NULL DEFAULT 0 ,
`identity`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`days`  int(10) NULL DEFAULT NULL ,
`price`  double(10,0) NULL DEFAULT NULL ,
`state`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`start_time`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`end_time`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`phone`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=9

;

-- ----------------------------
-- Records of customer_order
-- ----------------------------
BEGIN;
INSERT INTO `customer_order` VALUES ('1', '202', '张思德', '2', '1', '440902199802222122', '1', '118', '已入住', '2019-11-04 20:54:53', null, '13727766732'), ('2', '203', '张珊珊', '2', '0', '440902199801212212', '2', '258', '已入住', '2019-11-05 20:54:53', null, null), ('4', '101', '果果', '2', '1', '440902199802222122', '3', '354', '已完成', '2019-11-15 20:54:53', '2019-11-05 10:43:52', null), ('5', '102', '黄海峰', '1', '0', '440902199802222122', '1', '129', '已退订', '2019-11-15 20:54:53', '2019-11-05 10:01:19', null), ('6', '102', '周生生', '1', '0', '440322222222222222', '1', '129', '已退订', '2019-11-08 09:55:48', '2019-11-08 13:38:47', '18322222222'), ('7', '101', '张思德', '2', '1', '440902199802222122', '1', '118', '已退订', '2019-11-08 09:55:48', '2019-11-08 15:46:39', '13727766732'), ('8', '102', '张思德', '2', '1', '440902199802222122', '6', '708', '已预订', '2019-11-10 00:00:00', null, '13727766732');
COMMIT;

-- ----------------------------
-- Table structure for hotel_room
-- ----------------------------
DROP TABLE IF EXISTS `hotel_room`;
CREATE TABLE `hotel_room` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`room_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`room_number`  bigint(20) NULL DEFAULT NULL ,
`bed_type`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`broadband`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`standard_price`  double(10,0) NULL DEFAULT NULL ,
`member_price`  double(10,0) NULL DEFAULT NULL ,
`room_window`  int(10) NULL DEFAULT 0 ,
`room_area`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`room_status`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`room_type`  bigint(20) NULL DEFAULT NULL ,
`photo`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`created`  bigint(20) NULL DEFAULT NULL ,
`updated`  bigint(20) NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
FOREIGN KEY (`room_type`) REFERENCES `hotel_room_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `room_type` (`room_type`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=16

;

-- ----------------------------
-- Records of hotel_room
-- ----------------------------
BEGIN;
INSERT INTO `hotel_room` VALUES ('1', '经济舒适单人间', '101', '一张单人床', '免费WiFi', '129', '118', '1', '20平方', '空闲', '1', 'http://tangguo.cn-gd.ufileos.com/dcef2f1c-7ec7-4495-87e8-b2c85e8f2d67.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=4ObfPSqi4WRk92wHyWIBVwas6Fc%3D&Expires=1575353607', '1572613562181', '1573115609204'), ('2', '经济舒适单人间', '102', '一张单人床', '免费WiFi', '129', '118', '0', '20平方', '预订', '1', 'http://tangguo.cn-gd.ufileos.com/dcef2f1c-7ec7-4495-87e8-b2c85e8f2d67.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=4ObfPSqi4WRk92wHyWIBVwas6Fc%3D&Expires=1575353607', '1572613802427', '1572613802427'), ('3', '商务双床房', '103', '两张单人床', '免费WiFi', '299', '268', '0', '50平方', '空闲', '4', 'http://tangguo.cn-gd.ufileos.com/44d54921-e7aa-4415-99f9-96d1d35458d4.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=snhTXo85oHNdQDqZHptCVQ%2FHRjo%3D&Expires=1575536618', '1572680443518', '1572944618164'), ('4', '商务双床房', '104', '两张单人床', '免费WiFi', '299', '268', '1', '50平方', '空闲', '4', 'http://tangguo.cn-gd.ufileos.com/44d54921-e7aa-4415-99f9-96d1d35458d4.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=snhTXo85oHNdQDqZHptCVQ%2FHRjo%3D&Expires=1575536618', '1572680458796', '1572944367907'), ('5', '普通标准房', '105', '一张单人床', '免费WiFi', '99', '88', '0', '20平方', '空闲', '3', 'http://tangguo.cn-gd.ufileos.com/654ec590-ea8a-47d5-b68d-02e5c3f55751.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=%2BzN8%2F90GANb10ro92m4LGlHz%2Fco%3D&Expires=1575536888', '1572685491588', '1572944888184'), ('6', '豪华总统套房', '106', '一张大床', '免费WiFi', '399', '350', '1', '80平方', '空闲', '7', 'http://tangguo.cn-gd.ufileos.com/f7b247aa-17fb-4009-954b-76e662fe85a5.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=QRB15e0SoT%2BYLfaXL8wTCRqkBMg%3D&Expires=1575536769', '1572685797331', '1572944415223'), ('7', '豪华总统套房', '107', '一张大床', '免费WiFi', '399', '350', '1', '80平方', '空闲', '7', 'http://tangguo.cn-gd.ufileos.com/f7b247aa-17fb-4009-954b-76e662fe85a5.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=QRB15e0SoT%2BYLfaXL8wTCRqkBMg%3D&Expires=1575536769', '1572685797331', '1572944427440'), ('8', '经济舒适单人间', '201', '一张单人床', '免费WiFi', '129', '118', '0', '20平方', '空闲', '1', 'http://tangguo.cn-gd.ufileos.com/a279e565-85b3-43d3-a25f-e5a4f666794f.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=9HFENQYUWOCvpnl2ktW9UeE3EAg%3D&Expires=1575353549', '1572761549307', '1572761549307'), ('9', '经济舒适单人间', '202', '一张单人床', '免费WiFi', '129', '118', '0', '20平方', '入住', '1', 'http://tangguo.cn-gd.ufileos.com/35cec6f5-8d32-4f51-9314-174885818394.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=u8UTLF1zoHKsG66MscHdg5Mot6E%3D&Expires=1575353559', '1572761559591', '1572761559591'), ('10', '经济舒适单人间', '203', '一张单人床', '免费WiFi', '129', '118', '0', '20平方', '入住', '1', 'http://tangguo.cn-gd.ufileos.com/35b4ebd5-b888-4da4-b104-b15fba7baa12.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=uEiu1Brb1UVXunwg9bW6UKUdZNk%3D&Expires=1575353566', '1572761566184', '1572761566184'), ('11', '经济舒适单人间', '204', '一张单人床', '免费WiFi', '129', '118', '1', '20平方', '空闲', '1', 'http://tangguo.cn-gd.ufileos.com/620f424b-9145-4bb9-b300-7fd988b18a40.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=GdiVaNadxfv0%2FG7YC7vGgdHn%2BmY%3D&Expires=1575353582', '1572761582704', '1572761582704'), ('12', '经济舒适单人间', '205', '一张单人床', '免费WiFi', '129', '118', '1', '20平方', '空闲', '1', 'http://tangguo.cn-gd.ufileos.com/58f270b2-ecb3-4779-b4ff-1c56220fadf9.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=08VFeBL86Li5%2BKaFfgrR0pEqSv4%3D&Expires=1575353601', '1572761601067', '1572761601067'), ('13', '经济舒适单人间', '206', '一张单人床', '免费WiFi', '129', '118', '1', '20平方', '空闲', '1', 'http://tangguo.cn-gd.ufileos.com/dcef2f1c-7ec7-4495-87e8-b2c85e8f2d67.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=4ObfPSqi4WRk92wHyWIBVwas6Fc%3D&Expires=1575353607', '1572761607207', '1572761607207'), ('14', '商务双床房', '108', '两张单人床', '免费WiFi', '299', '268', '1', '50平方', '空闲', '4', 'http://tangguo.cn-gd.ufileos.com/0d52916a-a808-4a7f-95a5-5f08e9b45d5e.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=k0nWW2pwSCJZPSRxwUouIRbjvhw%3D&Expires=1575543195', '1572951195015', '1572951195015'), ('15', '商务双床房', '109', '两张单人床', '免费WiFi', '299', '268', '1', '50平方', '空闲', '4', 'http://tangguo.cn-gd.ufileos.com/63c57837-6aad-4453-9b42-67e617f5a057.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=uR3NoUsf80xD1VCPT%2Fe5N3H8u0E%3D&Expires=1575543338', '1572951338972', '1572951338972');
COMMIT;

-- ----------------------------
-- Table structure for hotel_room_type
-- ----------------------------
DROP TABLE IF EXISTS `hotel_room_type`;
CREATE TABLE `hotel_room_type` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`created`  bigint(20) NULL DEFAULT NULL ,
`remarks`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`photo`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=8

;

-- ----------------------------
-- Records of hotel_room_type
-- ----------------------------
BEGIN;
INSERT INTO `hotel_room_type` VALUES ('1', '单人间', '1572314982360', '糖果酒店位于 Ternes 区，提供了一个距离凯旋门 545 码（500 米）的休息之所。我们在中国的酒店经过全面翻新，提供干净并极具设计感的客房和 2 间带露台的普通套房。我们在自助早餐中供应本地产品，以及美食店供应小吃。探索我们的图书馆空间，在来访期间休闲放松', 'http://tangguo.cn-gd.ufileos.com/8e16d5ad-dda4-4ab9-83b1-7ff57e711791.jpeg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=8brdzZ8B9GrFaKHMIOSTicBr7Go%3D&Expires=1575621116'), ('2', '双人间', '1572593551483', '糖果酒店位于 Ternes 区，提供了一个距离凯旋门 545 码（500 米）的休息之所。我们在中国的酒店经过全面翻新，提供干净并极具设计感的客房和 2 间带露台的普通套房。我们在自助早餐中供应本地产品，以及美食店供应小吃。探索我们的图书馆空间，在来访期间休闲放松', 'http://tangguo.cn-gd.ufileos.com/62ef79c9-0aa8-4e1e-afbe-4dd84488baba.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=JLkSl21N7%2FFfyIMLxMENlEp%2BjHQ%3D&Expires=1575621096'), ('3', '标准间', '1572594130693', '糖果酒店位于 Ternes 区，提供了一个距离凯旋门 545 码（500 米）的休息之所。我们在中国的酒店经过全面翻新，提供干净并极具设计感的客房和 2 间带露台的普通套房。我们在自助早餐中供应本地产品，以及美食店供应小吃。探索我们的图书馆空间，在来访期间休闲放松', 'http://tangguo.cn-gd.ufileos.com/90ef27d2-e78b-4d34-8ea4-ca8e3ccb3d47.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=I3LmHkA2%2FPn1ie7AnNCXn%2ByKIag%3D&Expires=1575621137'), ('4', '商务间', '1572944270027', '糖果酒店位于 Ternes 区，提供了一个距离凯旋门 545 码（500 米）的休息之所。我们在中国的酒店经过全面翻新，提供干净并极具设计感的客房和 2 间带露台的普通套房。我们在自助早餐中供应本地产品，以及美食店供应小吃。探索我们的图书馆空间，在来访期间休闲放松', 'http://tangguo.cn-gd.ufileos.com/1b64cb36-e99d-4cda-9582-5f655650ac22.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=gpYryqOsSHJtIdUxyceeL8%2F0%2BAo%3D&Expires=1575621151'), ('5', '豪华间', '1572944284040', '糖果酒店位于 Ternes 区，提供了一个距离凯旋门 545 码（500 米）的休息之所。我们在中国的酒店经过全面翻新，提供干净并极具设计感的客房和 2 间带露台的普通套房。我们在自助早餐中供应本地产品，以及美食店供应小吃。探索我们的图书馆空间，在来访期间休闲放松', 'http://tangguo.cn-gd.ufileos.com/3fb43a9f-33c8-442e-9d71-33f3db20b51e.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=ZylmS%2FkLZKJCyCcBrbBn69aJ3YA%3D&Expires=1575621169'), ('6', '普通套间', '1572944304812', '糖果酒店位于 Ternes 区，提供了一个距离凯旋门 545 码（500 米）的休息之所。我们在中国的酒店经过全面翻新，提供干净并极具设计感的客房和 2 间带露台的普通套房。我们在自助早餐中供应本地产品，以及美食店供应小吃。探索我们的图书馆空间，在来访期间休闲放松', 'http://tangguo.cn-gd.ufileos.com/143543ec-c968-4447-8318-fdc4f74ee5dd.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=2BU9C41RSJJrtAvA7VXDtl7ng3c%3D&Expires=1575621263'), ('7', '总统套间', '1572944320976', '糖果酒店位于 Ternes 区，提供了一个距离凯旋门 545 码（500 米）的休息之所。我们在中国的酒店经过全面翻新，提供干净并极具设计感的客房和 2 间带露台的普通套房。我们在自助早餐中供应本地产品，以及美食店供应小吃。探索我们的图书馆空间，在来访期间休闲放松', 'http://tangguo.cn-gd.ufileos.com/1e8dd1be-5c2b-468c-b615-49250159d918.jpg?UCloudPublicKey=TOKEN_b0092c12-e7a7-4d68-afdc-72170d62928a&Signature=ieugJiF6yM2i03Op1UyZZ2OKKm8%3D&Expires=1575621224');
COMMIT;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`resource`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=19

;

-- ----------------------------
-- Records of permission
-- ----------------------------
BEGIN;
INSERT INTO `permission` VALUES ('1', '房间列表', 'room:list'), ('2', '房间添加', 'room:add'), ('3', '房间编辑', 'room:edit'), ('4', '房间删除', 'room:del'), ('5', '预订列表', 'order:list'), ('6', '入住列表', 'checkin:list'), ('7', '完成列表', 'completed:list'), ('8', '入住', 'customer:checkin'), ('9', '退房', 'customer:checkout'), ('10', '退订', 'customer:unsubscribe'), ('11', '删除订单', 'order:del'), ('12', '员工列表', 'user:list'), ('13', '员工添加', 'user:add'), ('14', '员工删除', 'user:del'), ('15', '员工编辑', 'user:edit'), ('16', '会员列表', 'member:list'), ('17', '会员编辑', 'member:edit'), ('18', '会员删除', 'member:del');
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sn`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=5

;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('1', '顾客管理', 'customerMgr'), ('2', '会员管理', 'memberMgr'), ('3', '房间管理', 'roomMgr'), ('4', '员工管理', 'staffMgr');
COMMIT;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
`role_id`  bigint(20) NULL DEFAULT NULL ,
`permission_id`  bigint(20) NULL DEFAULT NULL 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
BEGIN;
INSERT INTO `role_permission` VALUES ('3', '1'), ('3', '2'), ('3', '3'), ('3', '4'), ('4', '12'), ('4', '13'), ('4', '14'), ('4', '15'), ('1', '5'), ('1', '6'), ('1', '7'), ('1', '8'), ('1', '9'), ('1', '10'), ('2', '16'), ('2', '17');
COMMIT;

-- ----------------------------
-- Auto increment value for admin
-- ----------------------------
ALTER TABLE `admin` AUTO_INCREMENT=5;

-- ----------------------------
-- Auto increment value for customer
-- ----------------------------
ALTER TABLE `customer` AUTO_INCREMENT=3;

-- ----------------------------
-- Auto increment value for customer_order
-- ----------------------------
ALTER TABLE `customer_order` AUTO_INCREMENT=9;

-- ----------------------------
-- Auto increment value for hotel_room
-- ----------------------------
ALTER TABLE `hotel_room` AUTO_INCREMENT=16;

-- ----------------------------
-- Auto increment value for hotel_room_type
-- ----------------------------
ALTER TABLE `hotel_room_type` AUTO_INCREMENT=8;

-- ----------------------------
-- Auto increment value for permission
-- ----------------------------
ALTER TABLE `permission` AUTO_INCREMENT=19;

-- ----------------------------
-- Auto increment value for role
-- ----------------------------
ALTER TABLE `role` AUTO_INCREMENT=5;
