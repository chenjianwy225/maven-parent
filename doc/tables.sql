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
  `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',
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
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '审核状态(0:申请,1:通过,2:驳回)',
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
  KEY `fk_comment_parent` (`parentId`),
  CONSTRAINT `fk_comment_article` FOREIGN KEY (`articleId`) REFERENCES `f_article` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parentId`) REFERENCES `f_article_comment` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`userId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论表';

/*
 * 表名：f_article_point(文章点赞表)
 * 创建人：chenjian
 * 创建时间：2019-07-24
 */
DROP TABLE IF EXISTS `f_article_point`;
CREATE TABLE `f_article_point` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `userId` varchar(32) NOT NULL COMMENT '用户编号',
  `articleId` varchar(32) NOT NULL COMMENT '文章编号',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) DEFAULT b'0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`keyId`),
  KEY `fk_point_user` (`userId`),
  KEY `fk_point_article` (`articleId`),
  CONSTRAINT `fk_point_article` FOREIGN KEY (`articleId`) REFERENCES `f_article` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_point_user` FOREIGN KEY (`userId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章点赞表';

/*
 * 表名：f_friend(好友表)
 * 创建人：chenjian
 * 创建时间：2019-07-26
 */
DROP TABLE IF EXISTS `f_friend`;
CREATE TABLE `f_friend` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `userId` varchar(32) NOT NULL COMMENT '用户编号',
  `friendId` varchar(32) NOT NULL COMMENT '好友编号',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`keyId`),
  KEY `fk_friend_user` (`userId`),
  KEY `fk_friend_friend` (`friendId`),
  CONSTRAINT `fk_friend_friend` FOREIGN KEY (`friendId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_friend_user` FOREIGN KEY (`userId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友表';

/*
 * 表名：f_friend_group(好友分组表)
 * 创建人：chenjian
 * 创建时间：2019-07-26
 */
DROP TABLE IF EXISTS `f_friend_group`;
CREATE TABLE `f_friend_group` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `friendId` varchar(32) NOT NULL COMMENT '好友编号',
  `groupId` varchar(32) NOT NULL COMMENT '分组编号',
  PRIMARY KEY (`keyId`),
  KEY `fk_friend_group_group` (`groupId`),
  KEY `fk_friend_group_friend` (`friendId`),
  CONSTRAINT `fk_friend_group_friend` FOREIGN KEY (`friendId`) REFERENCES `f_friend` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_friend_group_group` FOREIGN KEY (`groupId`) REFERENCES `f_group` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友分组表';

/*
 * 表名：f_group(分组表)
 * 创建人：chenjian
 * 创建时间：2019-07-26
 */
DROP TABLE IF EXISTS `f_group`;
CREATE TABLE `f_group` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `userId` varchar(32) NOT NULL COMMENT '用户编号',
  `name` varchar(100) NOT NULL COMMENT '分组名称',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`keyId`),
  KEY `fk_group_user` (`userId`),
  CONSTRAINT `fk_group_user` FOREIGN KEY (`userId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分组表';

/*
 * 表名：f_friend_invite(好友邀请表)
 * 创建人：chenjian
 * 创建时间：2019-07-26
 */
DROP TABLE IF EXISTS `f_friend_invite`;
CREATE TABLE `f_friend_invite` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `sendId` varchar(32) NOT NULL COMMENT '邀请人编号',
  `acceptId` varchar(32) NOT NULL COMMENT '被邀请人编号',
  `content` varchar(100) DEFAULT NULL COMMENT '邀请内容',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`keyId`),
  KEY `fk_invite_sender` (`sendId`),
  KEY `fk_invite_accepter` (`acceptId`),
  CONSTRAINT `fk_invite_accepter` FOREIGN KEY (`acceptId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_invite_sender` FOREIGN KEY (`sendId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友邀请表';

/*
 * 表名：f_game(游戏表)
 * 创建人：chenjian
 * 创建时间：2019-08-14
 */
DROP TABLE IF EXISTS `f_game`;
CREATE TABLE `f_game` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `typeId` varchar(32) NOT NULL COMMENT '分类编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `summary` varchar(255) NOT NULL COMMENT '摘要',
  `content` text NOT NULL COMMENT '内容',
  `picPath` text NOT NULL COMMENT '图片路径(以逗号'',''分隔)',
  `videoPath` text COMMENT '视频路径(以逗号'',''分隔)',
  `urlPath` text NOT NULL COMMENT '下载路径',
  `hits` int(11) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `downloads` int(11) NOT NULL DEFAULT '0' COMMENT '下载量',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`keyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏表';

/*
 * 表名：f_game_type(游戏分类表)
 * 创建人：chenjian
 * 创建时间：2019-08-14
 */
DROP TABLE IF EXISTS `f_game_type`;
CREATE TABLE `f_game_type` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级编号',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`keyId`),
  KEY `fk_game_type` (`parentId`),
  CONSTRAINT `fk_game_type` FOREIGN KEY (`parentId`) REFERENCES `f_game_type` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏分类表';

/*
 * 表名：f_game_comment(游戏评论表)
 * 创建人：chenjian
 * 创建时间：2019-08-14
 */
DROP TABLE IF EXISTS `f_game_comment`;
CREATE TABLE `f_game_comment` (
  `keyId` varchar(32) NOT NULL COMMENT '编号(主键)',
  `userId` varchar(32) NOT NULL COMMENT '用户编号',
  `gameId` varchar(32) NOT NULL COMMENT '文章编号',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级编号',
  `content` text NOT NULL COMMENT '内容',
  `picPath` text COMMENT '图片路径(以逗号'',''分隔)',
  `createDate` datetime NOT NULL COMMENT '创建日期',
  `modifyDate` datetime DEFAULT NULL COMMENT '更新日期',
  `deleteStatus` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`keyId`),
  KEY `fk_game_comment` (`gameId`),
  KEY `fk_game_user` (`userId`),
  KEY `fk_game_parent` (`parentId`),
  CONSTRAINT `fk_game_comment` FOREIGN KEY (`gameId`) REFERENCES `f_game` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_game_parent` FOREIGN KEY (`parentId`) REFERENCES `f_game_comment` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_game_user` FOREIGN KEY (`userId`) REFERENCES `f_user` (`keyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏评论表';