/** test user*/
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `loginId` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  `createUser` varchar(255) DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT NULL,
  `updateUser` varchar(255) DEFAULT NULL,
  `status` int(10) unsigned DEFAULT NULL,
  `version` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/** user table */
CREATE TABLE `base_client_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `who` varchar(100) DEFAULT NULL,
  `when` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `what` varchar(50) DEFAULT NULL,
  `ipFrom` varchar(50) DEFAULT NULL,
  `deviceType` varchar(50) DEFAULT NULL,
  `platformInfo` varchar(20) DEFAULT NULL,
  `userAgent` varchar(200) DEFAULT NULL,
  `misc` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `base_file` (
  `id` varchar(32) NOT NULL,
  `reqId` varchar(255) NOT NULL,
  `reqType` varchar(255) NOT NULL,
  `fileOriginalName` varchar(255) NOT NULL,
  `fileUniqueName` varchar(255) NOT NULL,
  `subdirectory` varchar(255) DEFAULT NULL,
  `createUser` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateUser` varchar(50) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `base_file_generation` (
  `id` varchar(32) NOT NULL,
  `reqId` varchar(255) DEFAULT NULL,
  `reqType` varchar(255) DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `fileUniqueName` varchar(255) DEFAULT NULL,
  `subdirectory` varchar(255) DEFAULT NULL,
  `fileType` varchar(10) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `progress` float DEFAULT NULL,
  `expirationTime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `createUser` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateUser` varchar(50) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `base_sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(50) NOT NULL,
  `val` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `isActive` smallint(2) NOT NULL,
  `createUser` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateUser` varchar(50) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `base_sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `parentId` int(11) NOT NULL DEFAULT '0',
  `leaf` smallint(6) NOT NULL DEFAULT '0',
  `menuUrl` varchar(100) DEFAULT NULL,
  `orderIndex` smallint(6) NOT NULL DEFAULT '0',
  `icon` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `isActive` smallint(2) NOT NULL DEFAULT '1',
  `isForall` char(1) DEFAULT 'n',
  `moduleCategory` varchar(50) DEFAULT 'sys',
  `mobileApplicable` smallint(2) DEFAULT '0',
  `menuCode` varchar(20) DEFAULT 'na',
  `createUser` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateUser` varchar(50) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `base_sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'active',
  `remarks` varchar(255) DEFAULT NULL,
  `category` varchar(20) NOT NULL DEFAULT 'sys',
  `createUser` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateUser` varchar(50) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `base_sys_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleId` int(11) NOT NULL,
  `menuId` int(11) NOT NULL,
  `createUser` varchar(50) DEFAULT 'admin',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_menu` (`roleId`,`menuId`),
  KEY `menu_idx` (`menuId`)
  /**
   * CONSTRAINT `role` FOREIGN KEY (`roleId`) REFERENCES `base_sys_role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   * CONSTRAINT `menu` FOREIGN KEY (`menuId`) REFERENCES `base_sys_menu` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
   * */
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `base_sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginId` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `lastLogon` datetime DEFAULT NULL,
  `type` varchar(10) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'active',
  `failedTries` smallint(6) NOT NULL DEFAULT '0',
  `remarks` varchar(255) DEFAULT NULL,
  `createUser` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateUser` varchar(50) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginId` (`loginId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `base_sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  `createUser` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_idx` (`userId`),
  KEY `role_idx` (`roleId`)
  /**
   *  CONSTRAINT `fk_user` FOREIGN KEY (`userId`) REFERENCES `base_sys_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   *  CONSTRAINT `fk_role` FOREIGN KEY (`roleId`) REFERENCES `base_sys_role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
   * */
) ENGINE=InnoDB DEFAULT CHARSET=utf8;