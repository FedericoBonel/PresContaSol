CREATE TABLE `docmnt_opciones`
(
    `nombre` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
    PRIMARY KEY (`nombre`) USING BTREE
) COLLATE = 'utf8_general_ci'
  ENGINE = InnoDB
;



CREATE TABLE `presentacion`
(
    `identificador`  VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
    `fecha_creacion` DATE         NOT NULL,
    `apertura`       INT(11)      NOT NULL,
    `convocatoria`   VARCHAR(50)  NOT NULL COLLATE 'utf8_general_ci',
    `autor`          VARCHAR(50)  NOT NULL COLLATE 'utf8_general_ci',
    `municipio`      VARCHAR(50)  NOT NULL COLLATE 'utf8_general_ci',
    PRIMARY KEY (`identificador`) USING BTREE,
    INDEX `FK_presentacion_convocatoria` (`convocatoria`) USING BTREE,
    INDEX `FK_presentacion_municipio` (`municipio`) USING BTREE,
    INDEX `FK_presentacion_usuario` (`autor`) USING BTREE,
    CONSTRAINT `FK_presentacion_convocatoria` FOREIGN KEY (`convocatoria`) REFERENCES `solprescontrol`.`convocatoria` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `FK_presentacion_municipio` FOREIGN KEY (`municipio`) REFERENCES `solprescontrol`.`municipio` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `FK_presentacion_usuario` FOREIGN KEY (`autor`) REFERENCES `solprescontrol`.`usuario` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE
) COLLATE = 'utf8_general_ci'
  ENGINE = InnoDB
;

CREATE TABLE `docmnt_prsntcion`
(
    `nombre`       VARCHAR(50)  NULL DEFAULT NULL COLLATE 'utf8_general_ci',
    `presentacion` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
    UNIQUE INDEX `asignacion` (`nombre`, `presentacion`) USING BTREE,
    INDEX `FK__presentacion` (`presentacion`) USING BTREE,
    CONSTRAINT `FK__presentacion` FOREIGN KEY (`presentacion`) REFERENCES `solprescontrol`.`presentacion` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE
) COLLATE = 'utf8_general_ci'
  ENGINE = InnoDB
;

