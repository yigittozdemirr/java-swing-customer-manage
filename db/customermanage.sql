-- ============================================
-- Müşteri Yönetim Sistemi - Veritabanı Şeması
-- MySQL 8.0+
-- ============================================

CREATE DATABASE IF NOT EXISTS `customermanage`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `customermanage`;

-- -------------------------------------------
-- Tablo: user (Yönetici / Operatör hesapları)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id`       INT          NOT NULL AUTO_INCREMENT,
  `name`     VARCHAR(255) NOT NULL,
  `mail`     VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------
-- Tablo: customer (Bireysel / Kurumsal müşteriler)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `customer` (
  `id`     INT          NOT NULL AUTO_INCREMENT,
  `name`   VARCHAR(255) NOT NULL,
  `type`   ENUM('PERSON','COMPANY') NOT NULL DEFAULT 'PERSON',
  `phone`  VARCHAR(20)  DEFAULT NULL,
  `mail`   VARCHAR(255) DEFAULT NULL,
  `adress` TEXT         DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------
-- Tablo: product (Ürün kataloğu)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `product` (
  `id`    INT          NOT NULL AUTO_INCREMENT,
  `name`  VARCHAR(255) NOT NULL,
  `code`  VARCHAR(100) DEFAULT NULL,
  `price` INT          NOT NULL DEFAULT 0,
  `stock` INT          NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------
-- Tablo: basket (Geçici sepet - sipariş öncesi)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `basket` (
  `id`         INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`product_id`) REFERENCES `product`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------
-- Tablo: cart (Kesinleşmiş siparişler)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `cart` (
  `id`          INT  NOT NULL AUTO_INCREMENT,
  `customer_id` INT  NOT NULL,
  `product_id`  INT  NOT NULL,
  `price`       INT  NOT NULL DEFAULT 0,
  `note`        TEXT DEFAULT NULL,
  `date`        DATE DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`customer_id`) REFERENCES `customer`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`product_id`)  REFERENCES `product`(`id`)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------
-- Örnek yönetici kullanıcı (varsayılan giriş)
-- -------------------------------------------
INSERT INTO `user` (`name`, `mail`, `password`) VALUES
('Admin', 'admin@patika.dev', '123123');
