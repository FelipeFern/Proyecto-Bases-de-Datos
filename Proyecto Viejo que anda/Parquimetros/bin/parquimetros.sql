# Se crea la base de Datos
CREATE DATABASE parquimetros;

# Se selecciona la base de datos sobre la cual se van a hacer modificaciones
USE parquimetros;

#-------------------------------------------------------------------------
# Se crean las tablas para las entidades y relaciones

CREATE TABLE conductores (
 dni INT(11) UNSIGNED NOT NULL, 
 nombre VARCHAR(45) NOT NULL, 
 apellido VARCHAR(45) NOT NULL, 
 direccion VARCHAR(45) NOT NULL, 
 telefono VARCHAR(45) NOT NULL, 
 registro INT(11) UNSIGNED NOT NULL, 
 
 CONSTRAINT pk_conductores 
 PRIMARY KEY (dni)
 ) ENGINE=InnoDB;

 
#
CREATE TABLE automoviles (
 patente CHAR(6) NOT NULL,
 marca VARCHAR(45) NOT NULL, 
 modelo VARCHAR(45) NOT NULL, 
 color VARCHAR(45) NOT NULL, 
 dni INT(11) UNSIGNED NOT NULL, 
 
 CONSTRAINT pk_automoviles 
 PRIMARY KEY (patente),

 FOREIGN KEY (dni) REFERENCES conductores (dni)
 ON DELETE RESTRICT ON UPDATE CASCADE
 
) ENGINE=InnoDB;

#
CREATE TABLE tipos_tarjeta (
 tipo VARCHAR(45) NOT NULL,
 descuento DECIMAL(3,2) UNSIGNED NOT NULL, 
 
 CONSTRAINT pk_tipos_tarjeta
 PRIMARY KEY (tipo)
 ) ENGINE=InnoDB;

#
CREATE TABLE tarjetas (
 id_tarjeta INT(11) UNSIGNED AUTO_INCREMENT NOT NULL, 
 saldo DECIMAL(5,2) NOT NULL, # MIRAR ESTO para poner solo dos digitos
 tipo VARCHAR(45) NOT NULL,
 patente CHAR(6) NOT NULL,

 CONSTRAINT pk_tarjetas	
 PRIMARY KEY (id_tarjeta),

 FOREIGN KEY (tipo) REFERENCES tipos_tarjeta (tipo)
 ON DELETE RESTRICT ON UPDATE CASCADE,
 FOREIGN KEY (patente) REFERENCES automoviles (patente)
 ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

#
CREATE TABLE inspectores ( 
 legajo INT (11) UNSIGNED NOT NULL,
 dni INT(11) UNSIGNED NOT NULL, 
 nombre VARCHAR(45) NOT NULL, 
 apellido VARCHAR(45) NOT NULL, 
 password VARCHAR(32) NOT NULL, #agregar hash md5

 CONSTRAINT pk_inspectores	
 PRIMARY KEY (legajo)
) ENGINE=InnoDB;

#
CREATE TABLE ubicaciones (
 calle VARCHAR(45) NOT NULL,
 altura INT(11) UNSIGNED NOT NULL,  
 tarifa DECIMAL(5,2) UNSIGNED NOT NULL,

 CONSTRAINT pk_ubicaciones	
 PRIMARY KEY (calle,altura)
) ENGINE=InnoDB;


#
CREATE TABLE parquimetros (
 id_parq INT(11) UNSIGNED NOT NULL,
 numero INT(11) UNSIGNED NOT NULL,
 calle VARCHAR(45) NOT NULL,
 altura INT(11) UNSIGNED NOT NULL,

 CONSTRAINT pk_parquimetros	
 PRIMARY KEY (id_parq),

  FOREIGN KEY (calle,altura) REFERENCES ubicaciones (calle,altura)
  ON DELETE RESTRICT ON UPDATE CASCADE
  
) ENGINE=InnoDB;

#
CREATE TABLE accede(
 legajo INT (11) UNSIGNED NOT NULL,
 id_parq INT(11) UNSIGNED NOT NULL,
 fecha DATE NOT NULL,
 hora TIME(2) NOT NULL,

 CONSTRAINT pk_accede	
  PRIMARY KEY (id_parq,fecha,hora),
 
  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_parq) REFERENCES parquimetros (id_parq)
  ON DELETE RESTRICT ON UPDATE CASCADE,
 
  KEY (legajo)
) ENGINE=InnoDB;


#
CREATE TABLE estacionamientos (
 id_tarjeta INT(11) UNSIGNED AUTO_INCREMENT NOT NULL, 
 id_parq INT(11) UNSIGNED NOT NULL,
 fecha_ent DATE NOT NULL,
 hora_ent TIME(2) NOT NULL,
 fecha_sal DATE NULL,
 hora_sal TIME(2) NULL,

 CONSTRAINT pk_estacionamientos	
 PRIMARY KEY (id_parq,fecha_ent,hora_ent),

 FOREIGN KEY (id_tarjeta) REFERENCES tarjetas (id_tarjeta) 
 ON DELETE RESTRICT ON UPDATE CASCADE,
 FOREIGN KEY (id_parq) REFERENCES parquimetros (id_parq)
 ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


#
CREATE TABLE asociado_con(
 id_asociado_con INT(11) UNSIGNED AUTO_INCREMENT NOT NULL, 
 legajo INT (11) UNSIGNED NOT NULL,
 calle VARCHAR(45) NOT NULL,
 altura INT(11) UNSIGNED NOT NULL,  
 dia CHAR(2) NOT NULL,
 turno CHAR(1) NOT NULL,
 

 CONSTRAINT pk_asociado_con	
  PRIMARY KEY (id_asociado_con),
  
  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (calle,altura) REFERENCES ubicaciones (calle,altura)
  ON DELETE RESTRICT ON UPDATE CASCADE
 
) ENGINE=InnoDB;


#
CREATE TABLE multa(
 numero INT(11) UNSIGNED AUTO_INCREMENT NOT NULL,
 fecha DATE NOT NULL,
 hora TIME(2) NOT NULL,
 patente CHAR(6) NOT NULL,
 id_asociado_con INT(11) UNSIGNED NOT NULL,
 
 CONSTRAINT pk_multa
  PRIMARY KEY (numero),
 
  FOREIGN KEY (patente) REFERENCES automoviles (patente)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_asociado_con) REFERENCES asociado_con (id_asociado_con)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


#-------------------------------------------------------------------------
# Se crean las vistas

CREATE VIEW estacionados AS 
SELECT p.calle,p.altura,t.patente 
FROM (parquimetros as p NATURAL JOIN estacionamientos as e) NATURAL JOIN tarjetas as t 
WHERE e.fecha_sal is NULL AND e.hora_sal is NULL;


# Se crean los usuarios y se otorgan sus privilegios
# Usuario admin:
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON parquimetros.* TO 'admin'@'localhost' WITH GRANT OPTION;

# Usuario venta:
CREATE USER 'venta'@'%' IDENTIFIED BY 'venta';
GRANT INSERT ON parquimetros.tarjetas TO 'venta'; 
GRANT SELECT ON parquimetros.tipos_tarjeta TO 'venta';
GRANT SELECT ON parquimetros.automoviles TO 'venta';

# Usuario inspector:
CREATE USER 'inspector'@'%' IDENTIFIED BY 'inspector';
GRANT SELECT ON parquimetros.estacionados TO 'inspector';
GRANT SELECT ON parquimetros.inspectores TO 'inspector';
GRANT SELECT ON parquimetros.parquimetros TO 'inspector';
GRANT SELECT ON parquimetros.asociado_con TO 'inspector';
GRANT SELECT ON parquimetros.accede TO 'inspector'; 
GRANT INSERT ON parquimetros.accede TO 'inspector'; 
GRANT SELECT ON parquimetros.automoviles TO 'inspector';
GRANT INSERT ON parquimetros.multa to 'inspector'; 


