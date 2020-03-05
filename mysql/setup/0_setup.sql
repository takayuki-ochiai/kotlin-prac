CREATE SCHEMA IF NOT EXISTS `kotlin_test` DEFAULT CHARACTER SET utf8 ;
USE `kotlin_test`;

DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `adgroups`;
CREATE TABLE `adgroups` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `click_price` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `ads`;
CREATE TABLE `ads`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adgroup_id` int(10) unsigned NOT NULL,
  `title` varchar(255) DEFAULT '',
  `landing_page_url` varchar(1024) NOT NULL DEFAULT '',
  `image_url` varchar(1024) NOT NULL DEFAULT '',
  `advertising_subject` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `adspots`;
CREATE TABLE `adspots`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  `floor_price` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `ad_adspot_reports`;
CREATE TABLE `ad_adspot_reports`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ad_id` int(10) unsigned NOT NULL,
  `adspot_id` int(10) unsigned NOT NULL,
  `summary_on` date NOT NULL,
  `imps` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `prices` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `white_lists`;
CREATE TABLE `white_lists` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ad_id` int(11) NOT NULL,
  `adspot_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `adgroup_block_domains`;
CREATE TABLE `adgroup_block_domains` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adgroup_id` int(10) unsigned NOT NULL,
  `domain_url` varchar(1024) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `adspot_block_domains`;
CREATE TABLE `adspot_block_domains` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adspot_id` int(10) unsigned NOT NULL,
  `domain_url` varchar(1024) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `adspot_block_categories`;
CREATE TABLE `adspot_block_categories` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adspot_id` int(10) unsigned NOT NULL,
  `category_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
