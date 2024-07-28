--
-- Host: localhost    Database: sj_GBlog
-- ------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sj_article`
--

DROP TABLE IF EXISTS `sj_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sj_article` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(256) DEFAULT NULL COMMENT 'title',
  `content` longtext COMMENT 'content',
  `summary` varchar(1024) DEFAULT NULL COMMENT 'summary',
  `category_id` bigint DEFAULT NULL COMMENT 'categoryId',
  `thumbnail` varchar(256) DEFAULT NULL COMMENT 'thumbnail',
  `is_top` char(1) DEFAULT '0' COMMENT 'Top Comment (0N, 1Y)',
  `status` char(1) DEFAULT '1' COMMENT 'Status (0Published, 1Draft)',
  `view_count` bigint DEFAULT '0' COMMENT 'Views',
  `is_comment` char(1) DEFAULT '1' COMMENT 'Allow Comments (0N, 1Y)',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT 'Delete Status (0Not Deleted, 1Deleted)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Article Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sj_article`
--

LOCK TABLES `sj_article` WRITE;
/*!40000 ALTER TABLE `sj_article` DISABLE KEYS */;
INSERT INTO `sj_article` VALUES (1,'My Top Open World Games','# My most favorite open word games since 2010! \n\nContents for Testing\n','When can we expect the Elder Scroll 6 out?',2,'image.jpg','1','0',174,'0',1,'2024-07-22 05:10:01',1,'2024-07-22 07:10:01',0),(3,'We need more games like...?','# Lets vote for your top games.\n\nTesting Contents...\n\n','Testing Article',5,'image.jpg','1','0',5,'0',1,'2024-07-23 08:28:38',1,'2024-07-23 08:28:40',0);
/*!40000 ALTER TABLE `sj_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sj_article_tag`
--

DROP TABLE IF EXISTS `sj_article_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sj_article_tag` (
  `article_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'articleId',
  `tag_id` bigint NOT NULL DEFAULT '0' COMMENT 'tagId',
  PRIMARY KEY (`article_id`,`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Article Tag Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sj_article_tag`
--

LOCK TABLES `sj_article_tag` WRITE;
/*!40000 ALTER TABLE `sj_article_tag` DISABLE KEYS */;
INSERT INTO `sj_article_tag` VALUES (1,3),(2,3),(3,3);
/*!40000 ALTER TABLE `sj_article_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sj_category`
--

DROP TABLE IF EXISTS `sj_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sj_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT 'Name',
  `pid` bigint DEFAULT '-1' COMMENT 'Parent Id,default -1',
  `description` varchar(512) DEFAULT NULL COMMENT 'Description',
  `status` char(1) DEFAULT '0' COMMENT 'Status 0:Normal,1:Stopped',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT 'Delete status (0Not deleted, 1Deleted)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Category Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sj_category`
--

LOCK TABLES `sj_category` WRITE;
/*!40000 ALTER TABLE `sj_category` DISABLE KEYS */;
INSERT INTO `sj_category` VALUES (1,'Switch',-1,'description_placeholder','0',1,'2024-07-22 06:45:55',1,'2024-07-22 06:45:59',0),(2,'PS5',-1,'description_placeholder','0',1,'2024-07-22 07:45:55',1,'2024-07-22 07:45:55',0),(3,'XBox',-1,'Description_placeholder','0',1,'2024-07-22 07:45:55',1,'2024-07-22 07:45:55',0),(4,'Steam',-1,'description_placeholder','0',1,'2024-07-22 07:45:55',1,'2024-07-22 07:45:55',0),(5,'Mobile',-1,'description_placeholder','0',1,'2024-07-22 07:45:55',1,'2024-07-22 07:45:55',0),(6,'Other',-1,'description_placeholder','0',1,'2024-07-22 07:45:55',1,'2024-07-22 07:45:55',0);
/*!40000 ALTER TABLE `sj_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sj_comment`
--

DROP TABLE IF EXISTS `sj_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sj_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` char(1) DEFAULT '0' COMMENT 'type (0代表文章评论,1代表Link评论)',
  `article_id` bigint DEFAULT NULL COMMENT 'articleId',
  `root_id` bigint DEFAULT '-1' COMMENT 'rootId',
  `content` varchar(512) DEFAULT NULL COMMENT 'content',
  `to_comment_user_id` bigint DEFAULT '-1' COMMENT 'targetUserId',
  `to_comment_id` bigint DEFAULT '-1' COMMENT 'targetId',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT 'Delete Status (0Not deleted,1Deleted)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Comment Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sj_comment`
--

LOCK TABLES `sj_comment` WRITE;
/*!40000 ALTER TABLE `sj_comment` DISABLE KEYS */;
INSERT INTO `sj_comment` VALUES (1,'0',2,-1,'Test Time',-1,-1,2,'2024-07-24 08:45:13',2,'2024-07-24 08:45:13',0),(2,'0',2,1,'Test Time',2,1,2,'2024-07-24 16:46:35',2,'2024-07-24 16:46:35',0),(3,'0',3,-1,'Test Time2',-1,-1,2,'2024-07-24 16:46:53',2,'2024-07-24 16:46:53',0),(4,'0',2,-1,'1',-1,-1,2,'2024-11-27 14:29:00',2,'2024-11-27 14:29:00',0),(5,'0',2,4,'1',2,4,2,'2024-11-27 14:29:07',2,'2024-11-27 14:29:07',0);
/*!40000 ALTER TABLE `sj_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sj_link`
--

DROP TABLE IF EXISTS `sj_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sj_link` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `logo` varchar(256) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL COMMENT 'Website url',
  `status` char(1) DEFAULT '2' COMMENT 'Reviewing Status (0Passed,1Failed,2UnderReview)',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT 'Delete Status (0Not deleted,1Deleted)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Link Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sj_link`
--

LOCK TABLES `sj_link` WRITE;
/*!40000 ALTER TABLE `sj_link` DISABLE KEYS */;
INSERT INTO `sj_link` VALUES (1,'Chrome','chrome_logo_img','description','chrome_url','0',1,'2024-08-11 06:50:11',1,'2024-08-11 06:51:37',0),(2,'Safari','safari_img','description','safari_url','0',1,'2024-08-11 06:53:46',1,'2024-08-11 06:59:50',0),(3,'Nintendo','nintendo_img','description','nintendo_url','0',1,'2024-08-11 07:01:54',1,'2024-08-11 07:01:54',0),(4,'Play Station','play_station_img','description','play_station_url','0',1,'2024-08-11 07:04:01',1,'2024-08-11 07:04:01',0);
/*!40000 ALTER TABLE `sj_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sj_tag`
--

DROP TABLE IF EXISTS `sj_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sj_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT 'Name',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT 'Delete Status (0Not deleted,1Deleted)',
  `remark` varchar(500) DEFAULT NULL COMMENT 'Note',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Tag Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sj_tag`
--

LOCK TABLES `sj_tag` WRITE;
/*!40000 ALTER TABLE `sj_tag` DISABLE KEYS */;
INSERT INTO `sj_tag` VALUES (1,'Game Experience',1,'2024-08-11 07:05:51',1,'2024-08-11 07:05:51',0,'description'),(2,'New Game Anticipation',1,'2024-08-11 07:06:08',1,'2024-08-11 07:06:08',0,'description'),(3,'Voting and Discussion',1,'2024-08-11 07:07:54',1,'2024-08-11 07:07:54',0,'description'),(4,'Game News',1,'2024-08-11 07:08:04',1,'2024-08-11 07:08:04',0,'description'),(5,'Events and Competitions',1,'2024-08-11 07:08:18',1,'2024-08-11 07:08:18',0,'description'),(6,'Products',1,'2024-08-11 07:08:26',1,'2024-08-11 07:08:26',0,'description'),(7,'Looking for advices',1,'2024-08-11 07:09:06',1,'2024-08-11 07:09:06',0,'description'),(8,'Machine Related',1,'2024-08-11 07:09:20',1,'2024-08-11 07:09:20',0,'description'),(9,'Career in games',1,'2024-08-11 07:10:09',1,'2024-08-11 07:10:09',0,'description'),(10,'Looking for game-mates',1,'2024-08-11 07:10:32',1,'2024-08-11 07:10:32',0,'description');
/*!40000 ALTER TABLE `sj_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'MenuID',
  `menu_name` varchar(50) NOT NULL COMMENT 'Menu Name',
  `parent_id` bigint DEFAULT '0' COMMENT 'Parent Menu ID',
  `order_num` int DEFAULT '0' COMMENT 'Show Order',
  `path` varchar(200) DEFAULT '' COMMENT 'Path',
  `component` varchar(255) DEFAULT NULL COMMENT 'Components',
  `is_frame` int DEFAULT '1' COMMENT 'Frame Status (0Y, 1N)',
  `menu_type` char(1) DEFAULT '' COMMENT 'Menu Type (M menu C Menu F button)',
  `visible` char(1) DEFAULT '0' COMMENT 'Menu Status (0show 1hide)',
  `status` char(1) DEFAULT '0' COMMENT 'Menu Status (0normal 1stopped)',
  `perms` varchar(100) DEFAULT NULL COMMENT 'Permissions',
  `icon` varchar(100) DEFAULT '#' COMMENT 'Menu icons',
  `create_by` bigint DEFAULT NULL COMMENT 'Creater',
  `create_time` datetime DEFAULT NULL COMMENT 'Created time',
  `update_by` bigint DEFAULT NULL COMMENT 'Editor',
  `update_time` datetime DEFAULT NULL COMMENT 'Updated time',
  `remark` varchar(500) DEFAULT '' COMMENT 'Note',
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2030 DEFAULT CHARSET=utf8mb3 COMMENT='Menu authorization table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'System Management',0,1,'system',NULL,1,'M','0','0','','system',0,'2021-11-12 10:46:19',0,NULL,'System Management Table','0'),(100,'User Management',1,1,'user','system/user/index',1,'C','0','0','system:user:list','user',0,'2021-11-12 10:46:19',1,'2024-08-10 06:17:35','User Management Menu','0'),(101,'Character Management',1,2,'role','system/role/index',1,'C','0','0','system:role:list','peoples',0,'2021-11-12 10:46:19',0,NULL,'Character Management Menu','0'),(102,'MenuManagement',1,3,'menu','system/menu/index',1,'C','0','0','system:menu:list','tree-table',0,'2021-11-12 10:46:19',0,NULL,'Menu Management Menu','0'),(1001,'User Lookup',100,1,'','',1,'F','0','0','system:user:query','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1002,'User Addon',100,2,'','',1,'F','0','0','system:user:add','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1003,'User Edits',100,3,'','',1,'F','0','0','system:user:edit','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1004,'User Deletes',100,4,'','',1,'F','0','0','system:user:remove','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1005,'User Exports',100,5,'','',1,'F','0','0','system:user:export','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1006,'User Imports',100,6,'','',1,'F','0','0','system:user:import','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1007,'Reset Password',100,7,'','',1,'F','0','0','system:user:resetPwd','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1008,'Character Loopup',101,1,'','',1,'F','0','0','system:role:query','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1009,'Character Addon',101,2,'','',1,'F','0','0','system:role:add','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1010,'Character Edits',101,3,'','',1,'F','0','0','system:role:edit','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1011,'Character Delete',101,4,'','',1,'F','0','0','system:role:remove','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1012,'CharacterExport',101,5,'','',1,'F','0','0','system:role:export','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1013,'Menu Lookup',102,1,'','',1,'F','0','0','system:menu:query','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1014,'Menu Addon',102,2,'','',1,'F','0','0','system:menu:add','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1015,'Menu Edits',102,3,'','',1,'F','0','0','system:menu:edit','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(1016,'Menu Delete',102,4,'','',1,'F','0','0','system:menu:remove','#',0,'2021-11-12 10:46:19',0,NULL,'','0'),(2017,'Contents Management',0,4,'content',NULL,1,'M','0','0',NULL,'table',NULL,'2024-01-08 02:44:38',1,'2024-07-31 12:34:23','','0'),(2024,'Category Management',2017,1,'category','content/category/index',1,'C','0','0','content:category:list','example',NULL,'2024-01-08 02:51:45',NULL,'2024-01-08 02:51:45','','0'),(2024,'Article Management',2024,0,'article','content/article/index',1,'C','0','0','content:article:list','build',NULL,'2024-01-08 02:53:10',NULL,'2024-01-08 02:53:10','','0'),(2021,'Tag Management',2017,6,'tag','content/tag/index',1,'C','0','0','content:tag:index','button',NULL,'2024-01-08 02:55:37',NULL,'2024-01-08 02:55:50','','0'),(2022,'Link Management',2017,4,'link','content/link/index',1,'C','0','0','content:link:list','404',NULL,'2024-01-08 02:56:50',NULL,'2024-01-08 02:56:50','','0'),(2023,'Write Blog',0,0,'write','content/article/write/index',1,'C','0','0','content:article:writer','build',NULL,'2024-01-08 03:39:58',1,'2024-07-31 22:07:05','','0'),(2024,'Link Addon',2022,0,'',NULL,1,'F','0','0','content:link:add','#',NULL,'2024-01-16 07:59:17',NULL,'2024-01-16 07:59:17','','0'),(2025,'Link Edits',2022,1,'',NULL,1,'F','0','0','content:link:edit','#',NULL,'2024-01-16 07:59:44',NULL,'2024-01-16 07:59:44','','0'),(2026,'Link Delete',2022,1,'',NULL,1,'F','0','0','content:link:remove','#',NULL,'2024-01-16 08:00:05',NULL,'2024-01-16 08:00:05','','0'),(2027,'Link Lookup',2022,2,'',NULL,1,'F','0','0','content:link:query','#',NULL,'2024-01-16 08:04:09',NULL,'2024-01-16 08:04:09','','0'),(2028,'Export Category',2018,1,'',NULL,1,'F','0','0','content:category:export','#',NULL,'2024-01-21 07:06:59',NULL,'2024-01-21 07:06:59','','0');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'CharacterID',
  `role_name` varchar(30) NOT NULL COMMENT 'Character Name',
  `role_key` varchar(100) NOT NULL COMMENT 'Character key',
  `role_sort` int NOT NULL COMMENT 'Showing Order',
  `status` char(1) NOT NULL COMMENT 'CharacterStatus (0normal 1stopped)',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Delete Status (0exist 1delete)',
  `create_by` bigint DEFAULT NULL COMMENT 'Creater',
  `create_time` datetime DEFAULT NULL COMMENT 'Created time',
  `update_by` bigint DEFAULT NULL COMMENT 'Editor',
  `update_time` datetime DEFAULT NULL COMMENT 'Updated time',
  `remark` varchar(500) DEFAULT NULL COMMENT 'Note',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COMMENT='Character Info Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'Super Manager','admin',1,'0','0',0,'2024-07-12 07:46:19',0,NULL,'Super Manager'),(2,'Normal Character','common',2,'0','0',0,'2024-08-10 19:46:19',NULL,'2024-08-10 07:01:35','Normal Character'),(3,'Developer','system:build:test',3,'0','0',1,'2024-08-10 08:12:03',1,'2024-08-10 09:05:47','Developer');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT 'CharacterID',
  `menu_id` bigint NOT NULL COMMENT 'MenuID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='Character and Menu Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (0,0),(2,1),(2,100),(2,101),(2,102),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,2017),(2,2018),(2,2019),(2,2020),(2,2021),(2,2022),(2,2023),(2,2024),(2,2025),(2,2026),(2,2027),(2,2028);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT 'Username',
  `nick_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT 'Nickname',
  `password` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT 'Password',
  `type` char(1) DEFAULT '0' COMMENT 'User type：0normal User,1manager',
  `status` char(1) DEFAULT '0' COMMENT 'Account Status (0normal, 1stopped)',
  `email` varchar(64) DEFAULT NULL COMMENT 'email',
  `phonenumber` varchar(32) DEFAULT NULL COMMENT 'phone',
  `sex` char(1) DEFAULT NULL COMMENT 'User gender (0M,1F,2O)',
  `avatar` varchar(128) DEFAULT NULL COMMENT 'Avatar',
  `create_by` bigint DEFAULT NULL COMMENT 'Creater Userid',
  `create_time` datetime DEFAULT NULL COMMENT 'Created time',
  `update_by` bigint DEFAULT NULL COMMENT 'Editor',
  `update_time` datetime DEFAULT NULL COMMENT 'Updated time',
  `del_flag` int DEFAULT '0' COMMENT 'Delete Status (0Not deleted,1Deleted)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='User Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'huanfqc','Jane','$2a$10$VcIamfDZIvkRP1JJZKYAHOZpsb4Z3LZptJACS9wur9mZoOpTMpsAO','1','0','email@example.com','18888888888','0','user_img',NULL,'2024-07-21 13:07:35',1,'2024-07-22 13:08:04',0),(2,'Lala','TestUser','$2a$10$Cjxu8UwfmUYvgzy7VJexke3suuKNM9bwy8ENHj4UEzBmMZX5p.OBm','0','0','email@example.com','12343473451','0','user_img',NULL,'2024-07-13 13:07:28',NULL,'2024-12-25 13:55:04',0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT 'UserID',
  `role_id` bigint NOT NULL COMMENT 'CharacterID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='User and Character Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

