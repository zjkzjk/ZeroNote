/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : zeronote

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 08/02/2018 19:27:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `note_id` int(20) NOT NULL AUTO_INCREMENT,
  `notec_id` int(20) NOT NULL,
  `user_id` int(20) NOT NULL,
  `notet_id` int(2) NOT NULL,
  `note_title` varchar(500) COLLATE utf8mb4_bin NOT NULL,
  `note_body` text COLLATE utf8mb4_bin,
  `createtime` date NOT NULL,
  `updatetime` date NOT NULL,
  `note_tag` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `ifshare` bit(4) NOT NULL DEFAULT b'0',
  `location` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`note_id`),
  KEY `user_id` (`user_id`),
  KEY `notet_id` (`notet_id`),
  KEY `notec_id` (`notec_id`),
  CONSTRAINT `notec_id` FOREIGN KEY (`notec_id`) REFERENCES `note_class` (`notec_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `notet_id` FOREIGN KEY (`notet_id`) REFERENCES `note_type` (`notet_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user_infro` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for note_class
-- ----------------------------
DROP TABLE IF EXISTS `note_class`;
CREATE TABLE `note_class` (
  `notec_id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) NOT NULL,
  `notec_name` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `notec_desc` text COLLATE utf8mb4_bin,
  `createtime` date NOT NULL,
  `pic` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `updatedate` date NOT NULL,
  `body` int(13) NOT NULL,
  PRIMARY KEY (`notec_id`),
  KEY `id` (`user_id`),
  CONSTRAINT `id` FOREIGN KEY (`user_id`) REFERENCES `user_infro` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for note_type
-- ----------------------------
DROP TABLE IF EXISTS `note_type`;
CREATE TABLE `note_type` (
  `notet_id` int(2) NOT NULL AUTO_INCREMENT,
  `notet_name` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `notet_desc` text COLLATE utf8mb4_bin,
  PRIMARY KEY (`notet_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for user_infro
-- ----------------------------
DROP TABLE IF EXISTS `user_infro`;
CREATE TABLE `user_infro` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `pic` varchar(100) NOT NULL,
  `birth` date DEFAULT NULL,
  `mobile` varchar(13) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
