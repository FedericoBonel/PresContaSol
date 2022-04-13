CREATE TABLE `convocatoria`
(
    `identificador`  VARCHAR(100)  NOT NULL COLLATE 'utf8mb3_general_ci',
    `fecha_apertura` DATE          NOT NULL,
    `fecha_cierre`   DATE          NOT NULL,
    `descripcion`    VARCHAR(2000) NOT NULL COLLATE 'utf8mb3_general_ci',
    PRIMARY KEY (`identificador`) USING BTREE
) COLLATE='utf8mb3_general_ci'
    ENGINE=InnoDB
;

CREATE TABLE `docmnt_cnvctria`
(
    `nombre`       VARCHAR(50)  NOT NULL COLLATE 'utf8mb3_general_ci',
    `convocatoria` VARCHAR(100) NOT NULL COLLATE 'utf8mb3_general_ci',
    UNIQUE INDEX `Index 1` (`nombre`, `convocatoria`) USING BTREE,
    INDEX          `FK__convocatoria` (`convocatoria`) USING BTREE,
    CONSTRAINT `FK__convocatoria` FOREIGN KEY (`convocatoria`) REFERENCES `solprescontrol`.`convocatoria` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `FK__docmnt_opciones` FOREIGN KEY (`nombre`) REFERENCES `solprescontrol`.`docmnt_opciones` (`nombre`) ON UPDATE CASCADE ON DELETE CASCADE
) COLLATE='utf8mb3_general_ci'
    ENGINE=InnoDB
;

CREATE TABLE `docmnt_opciones`
(
    `nombre` VARCHAR(50) NOT NULL COLLATE 'utf8mb3_general_ci',
    PRIMARY KEY (`nombre`) USING BTREE
) COLLATE='latin1_general_ci'
    ENGINE=InnoDB
;

CREATE TABLE `docmnt_prsntcion`
(
    `nombre`       VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
    `presentacion` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
    UNIQUE INDEX `asignacion` (`nombre`, `presentacion`) USING BTREE,
    INDEX          `FK__presentacion` (`presentacion`) USING BTREE,
    CONSTRAINT `FK__presentacion` FOREIGN KEY (`presentacion`) REFERENCES `solprescontrol`.`presentacion` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE
) COLLATE='utf8mb3_general_ci'
    ENGINE=InnoDB
;

CREATE TABLE `municipio`
(
    `identificador` VARCHAR(30) NOT NULL COLLATE 'utf8mb3_general_ci',
    `nombre`        VARCHAR(30) NOT NULL COLLATE 'utf8mb3_general_ci',
    `categoria`     INT(11) NOT NULL,
    `supervisor`    VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
    `representante` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
    PRIMARY KEY (`identificador`) USING BTREE,
    UNIQUE INDEX `Index 2` (`representante`) USING BTREE,
    INDEX           `FK_municipio_usuario` (`supervisor`) USING BTREE,
    CONSTRAINT `FK_municipio_usuario` FOREIGN KEY (`supervisor`) REFERENCES `solprescontrol`.`usuario` (`identificador`) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT `FK_municipio_usuario_2` FOREIGN KEY (`representante`) REFERENCES `solprescontrol`.`usuario` (`identificador`) ON UPDATE CASCADE ON DELETE SET NULL
) COLLATE='utf8mb3_general_ci'
    ENGINE=InnoDB
;

CREATE TABLE `presentacion`
(
    `identificador`  VARCHAR(100) NOT NULL COLLATE 'utf8mb3_general_ci',
    `fecha_creacion` DATE         NOT NULL,
    `apertura`       INT(11) NOT NULL,
    `convocatoria`   VARCHAR(50)  NOT NULL COLLATE 'utf8mb3_general_ci',
    `autor`          VARCHAR(50)  NOT NULL COLLATE 'utf8mb3_general_ci',
    `municipio`      VARCHAR(50)  NOT NULL COLLATE 'utf8mb3_general_ci',
    PRIMARY KEY (`identificador`) USING BTREE,
    INDEX            `FK_presentacion_convocatoria` (`convocatoria`) USING BTREE,
    INDEX            `FK_presentacion_municipio` (`municipio`) USING BTREE,
    INDEX            `FK_presentacion_usuario` (`autor`) USING BTREE,
    CONSTRAINT `FK_presentacion_convocatoria` FOREIGN KEY (`convocatoria`) REFERENCES `solprescontrol`.`convocatoria` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `FK_presentacion_municipio` FOREIGN KEY (`municipio`) REFERENCES `solprescontrol`.`municipio` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `FK_presentacion_usuario` FOREIGN KEY (`autor`) REFERENCES `solprescontrol`.`usuario` (`identificador`) ON UPDATE CASCADE ON DELETE CASCADE
) COLLATE='utf8mb3_general_ci'
    ENGINE=InnoDB
;

CREATE TABLE `rol`
(
    `nombre` VARCHAR(20) NOT NULL COLLATE 'utf8mb3_general_ci',
    PRIMARY KEY (`nombre`) USING BTREE
) COLLATE='utf8mb3_general_ci'
    ENGINE=InnoDB
;

CREATE TABLE `usuario`
(
    `identificador` VARCHAR(10) NOT NULL COLLATE 'utf8mb3_general_ci',
    `nombre`        VARCHAR(50) NOT NULL COLLATE 'utf8mb3_general_ci',
    `clave`         VARCHAR(8)  NOT NULL COLLATE 'utf8mb3_general_ci',
    `rol`           VARCHAR(50) NOT NULL COLLATE 'utf8mb3_general_ci',
    PRIMARY KEY (`identificador`) USING BTREE,
    INDEX           `FK__rol` (`rol`) USING BTREE,
    CONSTRAINT `FK__rol` FOREIGN KEY (`rol`) REFERENCES `solprescontrol`.`rol` (`nombre`) ON UPDATE CASCADE ON DELETE CASCADE
) COLLATE='utf8mb3_general_ci'
    ENGINE=InnoDB
;
