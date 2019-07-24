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
  `sex` char(1) NOT NULL DEFAULT '3' COMMENT '性别(1:男,2:女,3:保密)',
  `photo` varchar(255) DEFAULT NULL COMMENT '头像',
  `isReal` char(1) NOT NULL DEFAULT '0' COMMENT '是否实名认证(0:否,1:是,2:待审核)',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除(0:否,1:是)',
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

/*
 * 表名：f_article(文章信息表)
 * 创建人：chenjian
 * 创建时间：2019-07-23
 */
DROP TABLE IF EXISTS `f_article`;
CREATE TABLE `f_article` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `typeId` varchar(32) NOT NULL COMMENT '分类编号',
  `userId` varchar(32) DEFAULT NULL COMMENT '用户编号',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `summary` varchar(255) NOT NULL COMMENT '摘要',
  `content` text COMMENT '内容',
  `picPath` text COMMENT '图片路径(以逗号'',''分隔)',
  `audioPath` text COMMENT '音频路径(以逗号'',''分隔)',
  `videoPath` text COMMENT '视频路径(以逗号'',''分隔)',
  `hits` int(11) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`keyId`),
  KEY `fk_article_type` (`typeId`),
  KEY `fk_article_user` (`userId`),
  CONSTRAINT `fk_article_type` FOREIGN KEY (`typeId`) REFERENCES `f_article_type` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_article_user` FOREIGN KEY (`userId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章信息表';

/*
 * 表名：f_article_type(文章分类表)
 * 创建人：chenjian
 * 创建时间：2019-07-23
 */
DROP TABLE IF EXISTS `f_article_type`;
CREATE TABLE `f_article_type` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级编号',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`keyId`),
  KEY `fk_type_type` (`parentId`),
  CONSTRAINT `fk_type_type` FOREIGN KEY (`parentId`) REFERENCES `f_article_type` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

/*
 * 表名：f_comment(文章评论表)
 * 创建人：chenjian
 * 创建时间：2019-07-24
 */
DROP TABLE IF EXISTS `f_article_comment`;
CREATE TABLE `f_article_comment` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `userId` varchar(32) NOT NULL COMMENT '用户编号',
  `articleId` varchar(32) NOT NULL COMMENT '文章编号',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级编号',
  `content` text NOT NULL COMMENT '内容',
  `picPath` text COMMENT '图片路径(以逗号'',''分隔)',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`keyId`),
  KEY `fk_comment_article` (`articleId`) USING BTREE,
  KEY `fk_comment_user` (`userId`),
  CONSTRAINT `fk_comment_article` FOREIGN KEY (`articleId`) REFERENCES `f_article` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`userId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论表';