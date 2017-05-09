-- --------------------------------------------------------
-- Host:                         localhost
-- Versión del servidor:         10.1.10-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win32
-- HeidiSQL Versión:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Volcando estructura de base de datos para fem
CREATE DATABASE IF NOT EXISTS `fem` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `fem`;


-- Volcando estructura para tabla fem.fem_amigos
CREATE TABLE IF NOT EXISTS `fem_amigos` (
  `uuid` varchar(36) NOT NULL,
  `to` varchar(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla fem.fem_bans
CREATE TABLE IF NOT EXISTS `fem_bans` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ip` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reason` varchar(2048) COLLATE utf8_unicode_ci NOT NULL,
  `banned_by_uuid` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `banned_by_name` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `removed_by_uuid` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `removed_by_name` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `removed_by_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `time` bigint(20) NOT NULL,
  `until` bigint(20) NOT NULL,
  `silent` bit(1) NOT NULL,
  `ipban` bit(1) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla fem.fem_datos
CREATE TABLE IF NOT EXISTS `fem_datos` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '',
  `uuid` varchar(36) NOT NULL,
  `timeJoin` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `grupo` int(2) unsigned NOT NULL DEFAULT '0',
  `god` bit(1) NOT NULL DEFAULT b'0',
  `lastConnect` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `coins` int(11) NOT NULL DEFAULT '0',
  `ip` varchar(45) NOT NULL DEFAULT '127.0.0.0',
  `nick` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla fem.fem_ignorados
CREATE TABLE IF NOT EXISTS `fem_ignorados` (
  `uuid` varchar(36) NOT NULL,
  `to` varchar(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla fem.fem_settings
CREATE TABLE IF NOT EXISTS `fem_settings` (
  `uuid` varchar(36) NOT NULL,
  `friendRequest` bit(1) NOT NULL DEFAULT b'1' COMMENT 'Aceptar o no solicitudes de amigos',
  `hideMode` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0 - No ver nadie. 1 - Ver amigos. 2 - Ver todos',
  `lang` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-ES 1-FR 2-IT',
  `enableTell` bit(1) DEFAULT b'1' COMMENT '1 True, 0 false'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla fem.fem_stats
CREATE TABLE IF NOT EXISTS `fem_stats` (
  `uuid` varchar(36) NOT NULL,
  `kills_tnt` int(5) DEFAULT '0',
  `kills_gh` int(5) DEFAULT '0',
  `deaths_gh` int(5) DEFAULT '0',
  `deaths_tnt` int(5) DEFAULT '0',
  `jugadas_tnt` int(5) DEFAULT '0',
  `jugadas_dod` int(5) DEFAULT '0',
  `jugadas_gh` int(5) DEFAULT '0',
  `ganadas_gh` int(5) DEFAULT '0',
  `ganadas_dod` int(5) DEFAULT '0',
  `ganadas_tnt` int(5) DEFAULT '0',
  `tntPuestas` int(5) DEFAULT '0',
  `tntQuitadas` int(5) DEFAULT '0',
  `tntExplotadas` int(5) DEFAULT '0',
  `genUpgraded` int(5) DEFAULT '0',
  `gemDestroyed` int(5) DEFAULT '0',
  `gemPlanted` int(5) DEFAULT '0',
  `record_dod` int(5) DEFAULT '0',
  `rondas_dod` int(5) DEFAULT '0',
  `picAcertadas` int(5) DEFAULT '0',
  `picDibujadas` int(5) DEFAULT '0',
  `ganadas_pic` int(5) DEFAULT '0',
  `jugadas_pic` int(5) DEFAULT '0',
  `jugadas_br` int(5) DEFAULT '0',
  `ganadas_br` int(5) DEFAULT '0',
  `kills_br` int(5) DEFAULT '0',
  `deaths_br` int(5) DEFAULT '0',
  `brIntercambios` int(5) unsigned DEFAULT '0',
  `jugadas_lg` int(5) DEFAULT '0',
  `ganadas_lg` int(5) NOT NULL DEFAULT '0',
  `kills_lg` int(5) DEFAULT '0',
  `deaths_lg` int(5) DEFAULT '0',
  `luckyRotos` int(5) DEFAULT '0',
  `timePlayed` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
