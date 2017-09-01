/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.5.40 : Database - company
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`company` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `company`;

/*Table structure for table `dept` */

DROP TABLE IF EXISTS `dept`;

CREATE TABLE `dept` (
  `d_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `d_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`d_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `dept` */

insert  into `dept`(`d_id`,`d_name`) values ('8a8484c55e036efa015e036efcf10002','1'),('8a8484c55e037363015e037365220001','2'),('8a8484c55e0386ac015e0386add30002','4'),('8a8484c55e039819015e03981ad90001','3'),('8a8484c55e039968015e039969ef0001','5'),('8a8484c55e0399a9015e0399aaf30002','6'),('8a8484c55e0399f7015e0399f91c0002','7'),('8a8484c55e039a37015e039a38dc0001','8'),('d001','武当派'),('d004','华山派');

/*Table structure for table `emp` */

DROP TABLE IF EXISTS `emp`;

CREATE TABLE `emp` (
  `e_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `e_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `e_age` int(4) DEFAULT NULL,
  `e_sex` int(2) DEFAULT NULL,
  `e_time` datetime DEFAULT NULL,
  `d_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `uploadtime` datetime DEFAULT NULL,
  PRIMARY KEY (`e_id`),
  KEY `FK188C8E4ABE104` (`d_id`),
  CONSTRAINT `FK188C8E4ABE104` FOREIGN KEY (`d_id`) REFERENCES `dept` (`d_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `emp` */

insert  into `emp`(`e_id`,`e_name`,`e_age`,`e_sex`,`e_time`,`d_id`,`url`,`uploadtime`) values ('001','王五',14,2,'2017-08-24 07:07:28','8a8484c55e037363015e037365220001',NULL,NULL),('40289f875e10fc51015e10fd5def0003','李四',13,1,'2000-10-10 00:00:00','8a8484c55e037363015e037365220001',NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
