# Proyecto - Segunda Entrega
#
# Bases de Datos, Segundo cuatrimestre 2019
# Universidad Nacional del Sur
# 
# Autores: Didriksen Ian, Fernandez Felipe
# 
# Script para la creacion de la base de datos, usuarios y vistas.

use datos_vista;

# ------------------------------------------------------------------------
#Creacion la base de datos.

CREATE database vuelos;

# ------------------------------------------------------------------------
#Seleccion de la base de datos sobre la que se va a trabajar.

USE vuelos;

# ------------------------------------------------------------------------
#Creacion de las tablas.

CREATE TABLE clases (
  nombre VARCHAR(20) NOT NULL,
  porcentaje DECIMAL (2,2) UNSIGNED NOT NULL,

  CONSTRAINT pk_clases 
  PRIMARY KEY (nombre)

) ENGINE=InnoDB;


CREATE TABLE comodidades (
  codigo SMALLINT unsigned NOT NULL,
  descripcion TEXT NOT NULL,

  CONSTRAINT pk_comodidades
  PRIMARY KEY (codigo)

) ENGINE=InnoDB;


CREATE TABLE pasajeros (
  doc_tipo VARCHAR(5) NOT NULL,
  doc_nro INT unsigned NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  direccion VARCHAR(50) NOT NULL,
  telefono VARCHAR(30) NOT NULL,
  nacionalidad VARCHAR(50) NOT NULL,
  
  CONSTRAINT pk_pasajeros
  PRIMARY KEY (doc_tipo, doc_nro)
  
) ENGINE=InnoDB;


CREATE TABLE modelos_avion (
  modelo VARCHAR(50) NOT NULL,
  fabricante VARCHAR(50) NOT NULL,
  cabinas SMALLINT unsigned NOT NULL,
  cant_asientos SMALLINT unsigned NOT NULL,

  CONSTRAINT pk_modelosavion
  PRIMARY KEY (modelo)

) ENGINE=InnoDB;


CREATE TABLE ubicaciones (
  pais VARCHAR(50) NOT NULL,
  estado VARCHAR(50) NOT NULL,
  ciudad VARCHAR(50) NOT NULL,
  huso SMALLINT NOT NULL,

  CONSTRAINT chk_ubicaciones_huso
  CHECK (huso BETWEEN -12 AND 12),

  CONSTRAINT pk_ubicaciones
  PRIMARY KEY (pais, estado, ciudad)
  
) ENGINE=InnoDB;


CREATE TABLE empleados (
  legajo INT unsigned AUTO_INCREMENT NOT NULL,
  password VARCHAR(32) NOT NULL,
  doc_tipo VARCHAR(5) NOT NULL,
  doc_nro INT unsigned NOT NULL,
  apellido VARCHAR(50) NOT NULL, 
  nombre VARCHAR(50) NOT NULL,
  direccion VARCHAR(50) NOT NULL,
  telefono VARCHAR(30) NOT NULL,
   
  CONSTRAINT pk_empleados
  PRIMARY KEY (legajo)
   
) ENGINE=InnoDB; 


CREATE TABLE aeropuertos (
  codigo VARCHAR(50) NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  telefono VARCHAR(30) NOT NULL,
  direccion VARCHAR(50) NOT NULL,
  pais VARCHAR(50) NOT NULL,
  estado VARCHAR(50) NOT NULL,
  ciudad VARCHAR(50) NOT NULL,

  CONSTRAINT pk_aeropuertos 
  PRIMARY KEY (codigo),

  FOREIGN KEY (pais, estado, ciudad) REFERENCES ubicaciones (pais, estado, ciudad) 
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE vuelos_programados (
  numero VARCHAR(50) NOT NULL,
  aeropuerto_salida VARCHAR(50) NOT NULL,
  aeropuerto_llegada VARCHAR(50) NOT NULL,
  
  CONSTRAINT pk_vuelosp 
  PRIMARY KEY (numero),

  FOREIGN KEY (aeropuerto_salida) REFERENCES aeropuertos (codigo) 
  ON DELETE RESTRICT ON UPDATE CASCADE,

  FOREIGN KEY (aeropuerto_llegada) REFERENCES aeropuertos (codigo)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE salidas (
  vuelo VARCHAR(50) NOT NULL,
  dia ENUM ('do','lu','ma','mi','ju','vi','sa') NOT NULL,
  hora_sale TIME NOT NULL,  
  hora_llega TIME NOT NULL,   
  modelo_avion VARCHAR(20) NOT NULL,

  CONSTRAINT pk_salidas
  PRIMARY KEY (vuelo, dia),
  
  FOREIGN KEY (vuelo) REFERENCES vuelos_programados(numero)
  ON DELETE RESTRICT ON UPDATE CASCADE,

  FOREIGN KEY (modelo_avion) REFERENCES modelos_avion(modelo)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE instancias_vuelo (
  vuelo VARCHAR(50) NOT NULL,
  dia ENUM ('do','lu','ma','mi','ju','vi','sa') NOT NULL,
  fecha DATE NOT NULL,
  estado VARCHAR(50),

  FOREIGN KEY (vuelo, dia) REFERENCES salidas(vuelo, dia)
  ON DELETE RESTRICT ON UPDATE CASCADE, 
    
  CONSTRAINT pk_instancias
  PRIMARY KEY (vuelo, fecha, dia)

) ENGINE=InnoDB;
 

CREATE TABLE reservas (
  numero INT UNSIGNED AUTO_INCREMENT NOT NULL,
  fecha DATE NOT NULL,
  vencimiento DATE NOT NULL,
  estado VARCHAR(20) NOT NULL,
  doc_tipo VARCHAR(5) NOT NULL,
  doc_nro INT unsigned NOT NULL,
  legajo INT unsigned NOT NULL,
  
  FOREIGN KEY (doc_tipo, doc_nro) REFERENCES pasajeros (doc_tipo, doc_nro),
  #ON DELETE RESTRICT ON UPDATE CASCADE,
  
  FOREIGN KEY (legajo) REFERENCES empleados (legajo),
  #ON DELETE RESTRICT ON UPDATE CASCADE,
  
  CONSTRAINT pk_reservas
  PRIMARY KEY (numero)
  
) ENGINE=InnoDB;


CREATE TABLE brinda (
  vuelo VARCHAR(50) NOT NULL,
  dia ENUM ('do','lu','ma','mi','ju','vi','sa') NOT NULL,
  clase VARCHAR(20) NOT NULL, 
  precio DECIMAL (7,2) UNSIGNED NOT NULL,
  cant_asientos SMALLINT unsigned NOT NULL,
  
  CONSTRAINT pk_brinda
  PRIMARY KEY (vuelo, dia, clase),

  FOREIGN KEY (vuelo, dia) REFERENCES salidas(vuelo, dia)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  
  FOREIGN KEY (clase) REFERENCES clases(nombre)
  ON DELETE RESTRICT ON UPDATE CASCADE  

) ENGINE=InnoDB;


CREATE TABLE posee (
  clase VARCHAR(20) NOT NULL,
  comodidad SMALLINT unsigned NOT NULL,
  
  CONSTRAINT pk_posee
  PRIMARY KEY (clase, comodidad),
  
  FOREIGN KEY (clase) REFERENCES clases(nombre)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  
  FOREIGN KEY (comodidad) REFERENCES comodidades(codigo)
  ON DELETE RESTRICT ON UPDATE CASCADE
  
) ENGINE=InnoDB;

  
CREATE TABLE reserva_vuelo_clase (
  numero INT UNSIGNED AUTO_INCREMENT NOT NULL,
  vuelo VARCHAR(50) NOT NULL,
  fecha_vuelo DATE NOT NULL,
  clase VARCHAR(20) NOT NULL,
  
  CONSTRAINT pk_reserva_vuelo_clase
  PRIMARY KEY (numero, vuelo, fecha_vuelo),
  
  FOREIGN KEY (numero) REFERENCES reservas (numero)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  
  FOREIGN KEY (vuelo, fecha_vuelo) REFERENCES instancias_vuelo (vuelo, fecha)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  
  FOREIGN KEY (clase) REFERENCES clases(nombre)
  ON DELETE RESTRICT ON UPDATE CASCADE
  
) ENGINE=InnoDB;  

# ------------------------------------------------------------------------
# Ceacion de la vista  

CREATE VIEW vuelos_disponibles AS 
SELECT  DISTINCT	vp.numero AS Numero, 
        s.modelo_avion AS Modelo,
		iv.fecha AS Fecha,
		s.dia AS Dia,
   		s.hora_sale AS HoraSalida,
	    s.hora_llega AS HoraLlegada,											
        CAST(IF(s.hora_llega < s.hora_sale, (CAST('24:00:00' AS TIME) - s.hora_sale + s.hora_llega), s.hora_llega - s.hora_sale) AS TIME) AS duracion,							
        asalida.codigo AS ASalida_codigo,
		asalida.nombre AS ASalida_nombre,
		asalida.ciudad AS ASalida_ciudad,
		asalida.estado AS ASalida_estado,
		asalida.pais AS ASalida_pais,
		allegada.codigo AS ALlegada_codigo,
		allegada.nombre AS ALlegada_nombre,
		allegada.ciudad AS ALlegada_ciudad,
		allegada.estado AS ALlegada_estado,
		allegada.pais AS ALlegada_pais,
		b.clase AS NombreClase,
		b.precio AS Precio,										
		b.cant_asientos + ROUND(b.cant_asientos * porcentaje) - (SELECT count(*) FROM reserva_vuelo_clase rvc WHERE b.vuelo = rvc.vuelo AND rvc.fecha_vuelo = iv.fecha AND rvc.clase = b.clase) AS asientos_disponibles
															
FROM vuelos_programados AS vp JOIN aeropuertos AS allegada ON vp.aeropuerto_llegada = allegada.codigo
    JOIN aeropuertos AS asalida ON vp.aeropuerto_salida = asalida.codigo
	JOIN instancias_vuelo AS iv ON vp.numero = iv.vuelo
	JOIN salidas AS s ON vp.numero = s.vuelo
	JOIN brinda AS b ON vp.numero = b.vuelo AND s.dia = b.dia 
	JOIN clases AS c ON b.clase = c.nombre
	LEFT JOIN reserva_vuelo_clase AS rvc ON vp.numero = rvc.vuelo AND iv.fecha = rvc.fecha_vuelo

WHERE iv.fecha > NOW();
    
  
  
# ------------------------------------------------------------------------
# Creacion usuario admin  
  
CREATE USER admin@localhost IDENTIFIED BY 'admin';

GRANT ALL PRIVILEGES ON vuelos.* TO admin@localhost WITH GRANT OPTION;


# ------------------------------------------------------------------------
# Creacion usuario empleado

CREATE USER empleado@'%' IDENTIFIED BY 'empleado';

GRANT SELECT ON vuelos.* TO empleado@'%';

GRANT UPDATE, DELETE, INSERT ON vuelos.reservas TO empleado@'%';

GRANT UPDATE, DELETE, INSERT ON vuelos.pasajeros TO empleado@'%';

GRANT UPDATE, DELETE, INSERT ON vuelos.reserva_vuelo_clase TO empleado@'%';


# ------------------------------------------------------------------------
# Creacion usuario cliente

CREATE USER cliente@'%' IDENTIFIED BY 'cliente';

GRANT SELECT ON vuelos.vuelos_disponibles TO cliente@'%';









