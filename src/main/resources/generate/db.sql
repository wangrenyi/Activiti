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
) ENGINE=InnoDB DEFAULT CHARSET=utf8