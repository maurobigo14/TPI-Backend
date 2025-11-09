-- init.sql generado a partir de los modelos Java recibidos
-- Crea tablas y relaciones para: Camion, Contenedor, Cliente, Deposito, Ruta, Solicitud, Tarifa, Tramo
-- Nota: revisa los tipos y constraints antes de ejecutar en producción. Ejecutar con un superusuario o ajustar CREATE DATABASE/USER según tu entorno.

-- DROP en orden inverso para evitar errores por claves foráneas
DROP TABLE IF EXISTS tramos CASCADE;
DROP TABLE IF EXISTS rutas CASCADE;
DROP TABLE IF EXISTS solicitudes CASCADE;
DROP TABLE IF EXISTS contenedores CASCADE;
DROP TABLE IF EXISTS tarifas CASCADE;
DROP TABLE IF EXISTS camiones CASCADE;
DROP TABLE IF EXISTS depositos CASCADE;
DROP TABLE IF EXISTS clientes CASCADE;

-- Tabla: clientes
CREATE TABLE IF NOT EXISTS clientes (
    dni VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(15),
    email VARCHAR(50),
    telefono VARCHAR(15)
);

-- Tabla: camiones
CREATE TABLE IF NOT EXISTS camiones (
    dominio SERIAL PRIMARY KEY,
    nombre_transportista VARCHAR(15) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    capacidad_peso DOUBLE PRECISION NOT NULL CHECK (capacidad_peso >= 0),
    capacidad_volumen DOUBLE PRECISION NOT NULL CHECK (capacidad_volumen >= 0),
    disponibilidad BOOLEAN NOT NULL,
    costos DOUBLE PRECISION NOT NULL CHECK (costos >= 0)
);

-- Tabla: depositos
CREATE TABLE IF NOT EXISTS depositos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(15),
    direccion VARCHAR(20),
    coordenadas INTEGER
);

-- Tabla: tarifas
CREATE TABLE IF NOT EXISTS tarifas (
    id BIGSERIAL PRIMARY KEY,
    descripcion TEXT,
    costo_base_km DOUBLE PRECISION CHECK (costo_base_km >= 0),
    valor_litro_combustible DOUBLE PRECISION CHECK (valor_litro_combustible >= 0),
    consumo_promedio_km DOUBLE PRECISION CHECK (consumo_promedio_km >= 0),
    costo_estadia_diaria DOUBLE PRECISION CHECK (costo_estadia_diaria >= 0),
    cargo_gestion DOUBLE PRECISION CHECK (cargo_gestion >= 0)
);

-- Tabla: contenedores
CREATE TABLE IF NOT EXISTS contenedores (
    id SERIAL PRIMARY KEY,
    peso INTEGER CHECK (peso >= 0),
    volumen INTEGER CHECK (volumen >= 0),
    estado VARCHAR(50),
    cliente VARCHAR(50),
    CONSTRAINT fk_contenedor_cliente FOREIGN KEY (cliente) REFERENCES clientes(dni)
);

-- Tabla: solicitudes
CREATE TABLE IF NOT EXISTS solicitudes (
    numero SERIAL PRIMARY KEY,
    contenedor INTEGER,
    cliente VARCHAR(50),
    costo_estimado DOUBLE PRECISION NOT NULL CHECK (costo_estimado >= 0),
    tiempo_estimado INTEGER NOT NULL CHECK (tiempo_estimado >= 0),
    costo_final DOUBLE PRECISION CHECK (costo_final >= 0),
    tiempoReal INTEGER CHECK (tiempoReal >= 0),
    CONSTRAINT fk_solicitud_contenedor FOREIGN KEY (contenedor) REFERENCES contenedores(id),
    CONSTRAINT fk_solicitud_cliente FOREIGN KEY (cliente) REFERENCES clientes(dni)
);

-- Tabla: rutas
CREATE TABLE IF NOT EXISTS rutas (
    id BIGSERIAL PRIMARY KEY,
    solicitud INTEGER,
    cantidad_tramos INTEGER CHECK (cantidad_tramos >= 0),
    cantidad_depositos INTEGER CHECK (cantidad_depositos >= 0),
    CONSTRAINT fk_ruta_solicitud FOREIGN KEY (solicitud) REFERENCES solicitudes(numero)
);

-- Tabla: tramos
CREATE TABLE IF NOT EXISTS tramos (
    id_tramo SERIAL PRIMARY KEY,
    origen VARCHAR(255),
    destino VARCHAR(255),
    tipo VARCHAR(50),
    estado VARCHAR(50),
    costo_aproximado DOUBLE PRECISION CHECK (costo_aproximado >= 0),
    costo_real DOUBLE PRECISION CHECK (costo_real >= 0),
    fecha_hora_inicio INTEGER,
    fecha_hora_fin INTEGER,
    camion INTEGER,
    CONSTRAINT fk_tramo_camion FOREIGN KEY (camion) REFERENCES camiones(dominio)
);

-- Índices sugeridos
CREATE INDEX IF NOT EXISTS idx_solicitud_cliente ON solicitudes(cliente);
CREATE INDEX IF NOT EXISTS idx_contenedor_cliente ON contenedores(cliente);
CREATE INDEX IF NOT EXISTS idx_ruta_solicitud ON rutas(solicitud);

-- Mensaje final
-- Revisar que los nombres de columnas coincidan con los mapeos JPA si se cambió alguno en los modelos.
