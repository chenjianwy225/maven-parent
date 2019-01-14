/*
 * 表名：f_user(用户基本信息表)
 * 创建人：chenjian
 * 创建时间：2019-01-04
 */
DROP TABLE IF EXISTS `f_user`;
CREATE TABLE `f_user` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `userName` varchar(50) NOT NULL COMMENT '用户名',
  `userPassword` varchar(50) NOT NULL COMMENT '用户密码',
  `mobile` varchar(11) NOT NULL COMMENT '手机号码',
  `nickName` varchar(50) NOT NULL COMMENT '昵称',
  `trueName` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `sex` char(1) NOT NULL COMMENT '性别(1:男,2:女,3:保密)',
  `photo` varchar(255) DEFAULT NULL COMMENT '头像',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) NOT NULL COMMENT '是否删除(0:否,1:是)',
  `isReal` char(1) NOT NULL COMMENT '是否实名认证(0:否,1:是,2:待审核)',
  PRIMARY KEY (`keyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基本信息表';

/*
 * 表名：f_user_info(用户详细信息表)
 * 创建人：chenjian
 * 创建时间：2019-01-14
 */
DROP TABLE IF EXISTS `f_user_info`;
CREATE TABLE `f_user_info` (
  `userId` varchar(32) NOT NULL COMMENT '用户编号',
  `idType` char(1) DEFAULT NULL COMMENT '证件类型(1:身份证,2:军官证)',
  `idNo` varchar(50) DEFAULT NULL COMMENT '证件号码',
  `idiograph` varchar(200) DEFAULT NULL COMMENT '个性签名',
  `qqNo` varchar(50) DEFAULT NULL COMMENT 'QQ号码',
  `weiXin` varchar(50) DEFAULT NULL COMMENT '微信号码',
  `weiBo` varchar(50) DEFAULT NULL COMMENT '微博号码',
  `occupation` varchar(50) DEFAULT NULL COMMENT '职业',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `constellation` varchar(50) DEFAULT NULL COMMENT '星座',
  `degree` varchar(20) DEFAULT NULL COMMENT '学位',
  `school` varchar(100) DEFAULT NULL COMMENT '毕业学校',
  `company` varchar(100) DEFAULT NULL COMMENT '公司(单位)名称',
  `address` varchar(200) DEFAULT NULL COMMENT '住址',
  PRIMARY KEY (`userId`),
  CONSTRAINT `fk_user` FOREIGN KEY (`userId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户详细信息表';