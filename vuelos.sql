# Proyecto - Segunda Entrega
#
# Bases de Datos, Segundo cuatrimestre 2019
# Universidad Nacional del Sur
# 
# Autores: Didriksen Ian, Fernandez Felipe
# 
# Script para la creacion de la base de datos, usuarios y vistas.

use datos;

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
  
   CONSTRAINT pk_instancias
  PRIMARY KEY (vuelo, fecha),

  FOREIGN KEY (vuelo, dia) REFERENCES salidas(vuelo, dia)
  ON DELETE RESTRICT ON UPDATE CASCADE
    
 

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

CREATE TABLE asientos_reservados (
	vuelo VARCHAR(50) NOT NULL,
	fecha DATE NOT NULL,
	clase VARCHAR(20) NOT NULL,
	cantidad INT UNSIGNED NOT NULL,

	CONSTRAINT pk_asientos_reservados
	PRIMARY KEY (vuelo, fecha, clase),
		
	FOREIGN KEY (vuelo, fecha) REFERENCES instancias_vuelo(vuelo, fecha)
	ON DELETE RESTRICT ON UPDATE CASCADE, 
	
	FOREIGN KEY (clase) REFERENCES clases (nombre)
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
	JOIN salidas AS s ON vp.numero = s.vuelo and s.dia = iv. dia 
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





#-------------------------------------------




DELIMITER !
 
 CREATE FUNCTION dia(fecha DATE) RETURNS CHAR(2)
 DETERMINISTIC
 BEGIN
   DECLARE i INT;   
   SELECT DAYOFWEEK(fecha) INTO i;
   CASE i
		WHEN 1 THEN RETURN 'Do';
		WHEN 2 THEN RETURN 'Lu';
		WHEN 3 THEN RETURN 'Ma';
		WHEN 4 THEN RETURN 'Mi';
		WHEN 5 THEN RETURN 'Ju';
		WHEN 6 THEN RETURN 'Vi';
		WHEN 7 THEN RETURN 'Sa';
	END CASE; 	
 END; !
 
 DELIMITER ;


 
#---------------------------------------------------------------------------------------------------------------------------
# UBICACIONES
	
	INSERT INTO ubicaciones(pais,estado,ciudad,huso)
	VALUES ('Argentina', 'Buenos Aires', 'Buenos Aires', 0);
	INSERT INTO ubicaciones(pais,estado,ciudad,huso) 
	VALUES ('Argentina', 'Cordoba', 'Cordoba', 0);
	INSERT INTO ubicaciones(pais,estado,ciudad,huso)
	VALUES ('Argentina', 'Buenos Aires', 'Ezeiza', 0);
 	INSERT INTO ubicaciones(pais,estado,ciudad,huso) 
	VALUES ('Argentina', 'Buenos Aires', 'Mar del Plata', 0);
	INSERT INTO ubicaciones(pais,estado,ciudad,huso)
	VALUES ('Argentina', 'Mendoza', 'Mendoza', 0);
	
#---------------------------------------------------------------------------------------------------------------------------
# AEROPUERTOS


INSERT INTO aeropuertos(codigo,nombre,telefono,direccion,pais,estado,ciudad)
 VALUES ('AEP', 'Aeroparque Jorge Newbery', '(54)45765300', 'direaep', 'Argentina', 'Buenos Aires', 'Buenos Aires');
INSERT INTO aeropuertos(codigo,nombre,telefono,direccion,pais,estado,ciudad)
 VALUES ('COR', 'Ing.Aer.A.L.V. Taravella', '(54)03514750874', 'direcor', 'Argentina', 'Cordoba', 'Cordoba');
INSERT INTO aeropuertos(codigo,nombre,telefono,direccion,pais,estado,ciudad)
 VALUES ('EZE', 'Ministro Pistarini', '(54)44802514', 'direeze', 'Argentina', 'Buenos Aires', 'Ezeiza');
INSERT INTO aeropuertos(codigo,nombre,telefono,direccion,pais,estado,ciudad)
 VALUES ('MDQ', 'Brig. Gral. Bartolomé de Colina', '(54)02234785811', 'diremdq', 'Argentina', 'Buenos Aires', 'Mar del Plata');
INSERT INTO aeropuertos(codigo,nombre,telefono,direccion,pais,estado,ciudad) 
VALUES ('MDZ', 'Gdor. Francisco Gabrielli', '(54)02614480017', 'diremdz', 'Argentina', 'Mendoza', 'Mendoza');


#---------------------------------------------------------------------------------------------------------------------------
# VUELOS_PROGRAMADOS
    # dos vuelos de Buenos aires a Cordoba
	INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada)
	VALUES ('BC1', 'AEP', 'COR');
	INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada)
	VALUES ('BC2', 'AEP', 'COR');
	
	# dos vuelos de Cordoba a Buenos Aires
	INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada)
	VALUES ('CB1', 'COR', 'AEP');
	INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada)
	VALUES ('CB2', 'COR', 'AEP');
	    
	# un vuelo de Buenos aires a Mar del Plata
	INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada)
	VALUES ('BM', 'AEP', 'MDQ');

	# un vuelo de  Mar del Plata a Buenos aires
	INSERT INTO vuelos_programados(numero, aeropuerto_salida, aeropuerto_llegada)
	VALUES ('MB', 'MDQ', 'AEP');
	
		
#---------------------------------------------------------------------------------------------------------------------------
# MODELOS_AVION

	INSERT INTO modelos_avion(modelo, fabricante, cabinas, cant_asientos)
	VALUES ('B-737', 'Boeing', 2, 60);
	INSERT INTO modelos_avion(modelo, fabricante,cabinas, cant_asientos) 
	VALUES ('B-747', 'Boeing', 2, 90);
	INSERT INTO modelos_avion(modelo, fabricante,cabinas, cant_asientos) 
	VALUES ('B-757', 'Boeing', 2, 120);
	INSERT INTO modelos_avion(modelo, fabricante,cabinas, cant_asientos) 
	VALUES ('B-767', 'Boeing', 2, 150);

#---------------------------------------------------------------------------------------------------------------------------
# SALIDAS

	INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('BC1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), '08:00:00', '09:00:00', 'B-737');
	INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('BC1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), '18:00:00', '19:00:00', 'B-737');
	
    INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('BC2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), '09:00:00', '10:00:00', 'B-747');
	INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('BC2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05')), '17:00:00', '18:10:00', 'B-737');

    INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('CB1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), '08:00:00', '09:00:00', 'B-737');
	INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('CB1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), '18:00:00', '19:00:00', 'B-737');
	
    INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('CB2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), '09:00:00', '10:00:00', 'B-747');
	INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('CB2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05')), '17:00:00', '18:10:00', 'B-737');

    INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion)
	VALUES ('BM', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-03')), '09:00:00', '9:35:00', 'B-757');
	INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion)
	VALUES ('BM', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07')), '17:00:00', '17:40:00', 'B-757');

	INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('MB', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-03')), '09:00:00', '9:35:00', 'B-757');
	INSERT INTO salidas(vuelo, dia, hora_sale, hora_llega, modelo_avion) 
	VALUES ('MB', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07')), '19:00:00', '19:40:00', 'B-757');

#---------------------------------------------------------------------------------------------------------------------------
# INSTANCIAS_VUELO


	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('BC1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'a tiempo');
	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado)
	VALUES ('BC1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), 'demorado');

	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('BC2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'a tiempo');
	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('BC2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05')), 'demorado');

	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('CB1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'a tiempo');
	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('CB1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), 'demorado');

	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('CB2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'a tiempo');
	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('CB2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05')), 'demorado');

	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('BM', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-03'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-03')), 'a tiempo');
	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('BM', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07')), 'demorado');

	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('MB', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-03'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-03')), 'a tiempo');
	INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
	VALUES ('MB', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07'), dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07')), 'demorado');

#---------------------------------------------------------------------------------------------------------------------------
# CLASES

	INSERT INTO clases(nombre, porcentaje) VALUES ('Turista', 0.67);
	INSERT INTO clases(nombre, porcentaje) VALUES ('Ejecutiva', 0.5);
	INSERT INTO clases(nombre, porcentaje) VALUES ('Primera', 0.34);

#---------------------------------------------------------------------------------------------------------------------------
# COMODIDADES

	INSERT INTO comodidades(codigo,descripcion) VALUES (1, 'Asientos grandes');
	INSERT INTO comodidades(codigo,descripcion) VALUES (2, 'Comida rica');
	INSERT INTO comodidades(codigo,descripcion) VALUES (3, 'Azafata amable');
	INSERT INTO comodidades(codigo,descripcion) VALUES (4, 'Internet');

#---------------------------------------------------------------------------------------------------------------------------
# PASAJEROS

	INSERT INTO pasajeros(doc_tipo, doc_nro, nombre, apellido, direccion, telefono, nacionalidad) 
	VALUES ('DNI', 1, 'Julieta', 'Marcos', 'Roca 850', '02932 424565', 'Argentina');
	INSERT INTO pasajeros(doc_tipo, doc_nro, nombre, apellido, direccion, telefono, nacionalidad)
	VALUES ('DNI', 2, 'Luciano', 'Tamargo', 'Alem 222', '0291 45222', 'Argentino');
	INSERT INTO pasajeros(doc_tipo, doc_nro, nombre, apellido, direccion, telefono, nacionalidad)
	VALUES ('DNI', 3, 'Diego', 'Garcia', '12 de Octubre 333', '0291 45333', 'Argentino');
	INSERT INTO pasajeros(doc_tipo, doc_nro, nombre, apellido, direccion, telefono, nacionalidad)
	VALUES ('DNI', 4, 'Juan', 'Perez', 'Belgrano 14', '0291 4556733', 'Argentino');
	INSERT INTO pasajeros(doc_tipo, doc_nro, nombre, apellido, direccion, telefono, nacionalidad)
	VALUES ('DNI', 5, 'Julian', 'Gomez', 'Donado 533', '0291 4543563', 'Argentino');
	
#---------------------------------------------------------------------------------------------------------------------------
# EMPLEADOS
    
	INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, nombre, apellido, direccion, telefono) 
	VALUES (101,md5('emp101'),'DNI', 101, 'Nombre_Emp101', 'Apellido_Emp101', 'Dir_Emp101' , '0291-4540101');
	INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, nombre, apellido, direccion, telefono) 
	VALUES (102,md5('emp102'),'DNI', 102, 'Nombre_Emp102', 'Apellido_Emp102', 'Dir_Emp102' , '0291-4540102');
	INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, nombre, apellido, direccion, telefono) 
	VALUES (103,md5('emp103'),'DNI', 103, 'Nombre_Emp103', 'Apellido_Emp103', 'Dir_Emp103' , '0291-4540103');
	INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, nombre, apellido, direccion, telefono) 
	VALUES (104,md5('emp104'),'DNI', 104, 'Nombre_Emp104', 'Apellido_Emp104', 'Dir_Emp104' , '0291-4540104');
	INSERT INTO empleados(legajo, password, doc_tipo, doc_nro, nombre, apellido, direccion, telefono) 
	VALUES (105,md5('emp105'),'DNI', 105, 'Nombre_Emp105', 'Apellido_Emp105', 'Dir_Emp105' , '0291-4540105');
	

#---------------------------------------------------------------------------------------------------------------------------
# BRINDA

	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BC1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BC1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Primera',  2000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BC1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Ejecutiva', 3000.00, 2);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BC1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BC1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), 'Primera',  2000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('BC1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), 'Ejecutiva', 3000.00, 2);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BC2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BC2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Primera',  2000.00, 3);	
		
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BC2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('BC2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05')), 'Primera',  2000.00, 3);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Primera',  2000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Ejecutiva', 3000.00, 2);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), 'Primera',  2000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB1', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-04')), 'Ejecutiva', 3000.00, 2);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02')), 'Primera',  2000.00, 3);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('CB2', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05')), 'Primera',  2000.00, 3);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('BM', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-03')), 'Turista',  1000.00, 3);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('BM', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('BM', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07')), 'Primera',  2000.00, 3);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('MB', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-03')), 'Turista',  1000.00, 3);	
	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos)
	VALUES ('MB', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07')), 'Turista',  1000.00, 3);	
	INSERT INTO brinda(vuelo, dia, clase, precio, cant_asientos) 
	VALUES ('MB', dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-07')), 'Primera',  2000.00, 3);	
	
#---------------------------------------------------------------------------------------------------------------------------
# POSEE

	INSERT INTO posee(clase, comodidad) VALUES ('Turista', 3);
	
	INSERT INTO posee(clase, comodidad) VALUES ('Ejecutiva', 3);
	INSERT INTO posee(clase, comodidad) VALUES ('Ejecutiva', 4);	
	
	INSERT INTO posee(clase, comodidad) VALUES ('Primera', 1);
	INSERT INTO posee(clase, comodidad) VALUES ('Primera', 2);
	INSERT INTO posee(clase, comodidad) VALUES ('Primera', 3);
	INSERT INTO posee(clase, comodidad) VALUES ('Primera', 4);
	
#---------------------------------------------------------------------------------------------------------------------------
# RESERVAS 
# El empleado 10X realizo la reserva del pasajero X


        # reservas del vuelo BC1 del 201X-01-02
	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (1, 'DNI', 1, 101, CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'Confirmada');
	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (2, 'DNI', 2, 102,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'Confirmada');	
	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (3, 'DNI', 3, 103,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'Confirmada');	
	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (4, 'DNI', 4, 104,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'En Espera');	

	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (5, 'DNI', 5, 105,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'Confirmada');
        
        # reservas del vuelo CB2 del 201X-01-05
	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (6, 'DNI', 1, 101,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'Confirmada');
	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (7, 'DNI', 2, 102,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'Confirmada');	
	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (8, 'DNI', 3, 103,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'Confirmada');	
	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (9, 'DNI', 4, 104,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'En Espera');	

	INSERT INTO reservas(numero, doc_tipo, doc_nro, legajo, fecha, vencimiento, estado) 
        VALUES (10, 'DNI', 5, 105,CONCAT(YEAR(NOW()),'-10-01'), CONCAT(YEAR(NOW()),'-12-01'), 'Confirmada');


#---------------------------------------------------------------------------------------------------------------------------
# RESERVA_VUELO_CLASE

        # reservas del vuelo BC1 del 201X-01-02
	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (1, 'BC1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), 'Turista');
	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (2, 'BC1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), 'Turista');
	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (3, 'BC1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), 'Turista');
	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (4, 'BC1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), 'Turista');

	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (5, 'BC1', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-02'), 'Primera');

        # reservas del vuelo CB2 del 201X-01-05
        INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (6, 'CB2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05'), 'Turista');
	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (7, 'CB2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05'), 'Turista');
	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (8, 'CB2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05'), 'Turista');
	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (9, 'CB2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05'), 'Turista');

	INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
        VALUES (10, 'CB2', CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-05'), 'Primera');
	
#---------------------------------------------------------------------------------------------------------------------------

DROP FUNCTION dia;


