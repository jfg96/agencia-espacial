-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema agencia_espacial
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema agencia_espacial
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `agencia_espacial` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `agencia_espacial` ;

-- -----------------------------------------------------
-- Table `agencia_espacial`.`astronauta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `agencia_espacial`.`astronauta` ;

CREATE TABLE IF NOT EXISTS `agencia_espacial`.`astronauta` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre_completo` VARCHAR(150) NOT NULL,
  `nacionalidad` VARCHAR(100) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `especialidad` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `agencia_espacial`.`estacion_seguimiento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `agencia_espacial`.`estacion_seguimiento` ;

CREATE TABLE IF NOT EXISTS `agencia_espacial`.`estacion_seguimiento` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NOT NULL,
  `pais` VARCHAR(100) NOT NULL,
  `ciudad` VARCHAR(100) NOT NULL,
  `latitud` DOUBLE NOT NULL,
  `longitud` DOUBLE NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `agencia_espacial`.`vehiculo_lanzamiento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `agencia_espacial`.`vehiculo_lanzamiento` ;

CREATE TABLE IF NOT EXISTS `agencia_espacial`.`vehiculo_lanzamiento` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `modelo` VARCHAR(100) NOT NULL,
  `capacidad_kg` DOUBLE NOT NULL,
  `pais_fab` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `agencia_espacial`.`mision`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `agencia_espacial`.`mision` ;

CREATE TABLE IF NOT EXISTS `agencia_espacial`.`mision` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NOT NULL,
  `objetivo` TEXT NOT NULL,
  `fecha_lanzamiento` DATE NOT NULL,
  `fecha_fin_prevista` DATE NULL DEFAULT NULL,
  `estado` VARCHAR(50) NOT NULL,
  `tripulada` TINYINT(1) NOT NULL DEFAULT '0',
  `vehiculo_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_mision_nombre` (`nombre` ASC) VISIBLE,
  INDEX `idx_mision_vehiculo` (`vehiculo_id` ASC) VISIBLE,
  CONSTRAINT `fk_mision_vehiculo`
    FOREIGN KEY (`vehiculo_id`)
    REFERENCES `agencia_espacial`.`vehiculo_lanzamiento` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `agencia_espacial`.`satelite`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `agencia_espacial`.`satelite` ;

CREATE TABLE IF NOT EXISTS `agencia_espacial`.`satelite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `tipo` VARCHAR(100) NOT NULL,
  `altitud_orbital` DOUBLE NOT NULL,
  `fecha_orbita` DATE NOT NULL,
  `mision_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_satelite_mision` (`mision_id` ASC) VISIBLE,
  CONSTRAINT `fk_satelite_mision`
    FOREIGN KEY (`mision_id`)
    REFERENCES `agencia_espacial`.`mision` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `agencia_espacial`.`estacion_satelite`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `agencia_espacial`.`estacion_satelite` ;

CREATE TABLE IF NOT EXISTS `agencia_espacial`.`estacion_satelite` (
  `estacion_id` BIGINT NOT NULL,
  `satelite_id` BIGINT NOT NULL,
  PRIMARY KEY (`estacion_id`, `satelite_id`),
  INDEX `idx_es_satelite` (`satelite_id` ASC) VISIBLE,
  INDEX `idx_es_estacion` (`estacion_id` ASC) VISIBLE,
  CONSTRAINT `fk_es_estacion`
    FOREIGN KEY (`estacion_id`)
    REFERENCES `agencia_espacial`.`estacion_seguimiento` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_es_satelite`
    FOREIGN KEY (`satelite_id`)
    REFERENCES `agencia_espacial`.`satelite` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `agencia_espacial`.`participacion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `agencia_espacial`.`participacion` ;

CREATE TABLE IF NOT EXISTS `agencia_espacial`.`participacion` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `astronauta_id` BIGINT NOT NULL,
  `mision_id` BIGINT NOT NULL,
  `rol` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_participacion` (`astronauta_id` ASC, `mision_id` ASC) VISIBLE,
  INDEX `idx_part_astronauta` (`astronauta_id` ASC) VISIBLE,
  INDEX `idx_part_mision` (`mision_id` ASC) VISIBLE,
  CONSTRAINT `fk_part_astronauta`
    FOREIGN KEY (`astronauta_id`)
    REFERENCES `agencia_espacial`.`astronauta` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_part_mision`
    FOREIGN KEY (`mision_id`)
    REFERENCES `agencia_espacial`.`mision` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `agencia_espacial`.`registro_telemetria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `agencia_espacial`.`registro_telemetria` ;

CREATE TABLE IF NOT EXISTS `agencia_espacial`.`registro_telemetria` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `satelite_id` BIGINT NOT NULL,
  `fecha_hora` DATETIME NOT NULL,
  `temperatura` DOUBLE NOT NULL,
  `velocidad` DOUBLE NOT NULL,
  `nivel_bateria` DOUBLE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_telemetria_satelite` (`satelite_id` ASC) VISIBLE,
  CONSTRAINT `fk_telemetria_satelite`
    FOREIGN KEY (`satelite_id`)
    REFERENCES `agencia_espacial`.`satelite` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

USE `agencia_espacial`;

DELIMITER $$

USE `agencia_espacial`$$
DROP TRIGGER IF EXISTS `agencia_espacial`.`trg_astronauta_fecha_insert` $$
USE `agencia_espacial`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `agencia_espacial`.`trg_astronauta_fecha_insert`
BEFORE INSERT ON `agencia_espacial`.`astronauta`
FOR EACH ROW
BEGIN
    IF NEW.fecha_nacimiento >= CURRENT_DATE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'RS-004: La fecha de nacimiento no puede ser presente ni futura.';
    END IF;
END$$


USE `agencia_espacial`$$
DROP TRIGGER IF EXISTS `agencia_espacial`.`trg_astronauta_fecha_update` $$
USE `agencia_espacial`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `agencia_espacial`.`trg_astronauta_fecha_update`
BEFORE UPDATE ON `agencia_espacial`.`astronauta`
FOR EACH ROW
BEGIN
    IF NEW.fecha_nacimiento >= CURRENT_DATE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'RS-004: La fecha de nacimiento no puede ser presente ni futura.';
    END IF;
END$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
