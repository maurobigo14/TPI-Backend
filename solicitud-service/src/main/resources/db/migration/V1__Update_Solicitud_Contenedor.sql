-- Migration: Actualizar tabla solicitudes
-- Descripción: Cambiar estructura de Solicitud y agregar enum para estados

-- Cambiar nombre de id a numero y asegurarse de que sea PK
ALTER TABLE solicitudes RENAME COLUMN id TO numero;

-- Actualizar columna estado para usar VARCHAR (enum en string)
ALTER TABLE solicitudes MODIFY estado VARCHAR(50) DEFAULT 'BORRADOR';

-- Agregar columna para ubicación origen y destino si no existen
ALTER TABLE solicitudes ADD COLUMN IF NOT EXISTS origen_direccion VARCHAR(255);
ALTER TABLE solicitudes ADD COLUMN IF NOT EXISTS origen_lat DOUBLE;
ALTER TABLE solicitudes ADD COLUMN IF NOT EXISTS origen_lng DOUBLE;
ALTER TABLE solicitudes ADD COLUMN IF NOT EXISTS destino_direccion VARCHAR(255);
ALTER TABLE solicitudes ADD COLUMN IF NOT EXISTS destino_lat DOUBLE;
ALTER TABLE solicitudes ADD COLUMN IF NOT EXISTS destino_lng DOUBLE;

-- Actualizar tabla contenedores
ALTER TABLE contenedores ADD COLUMN IF NOT EXISTS numero_identificacion VARCHAR(255) UNIQUE NOT NULL;
ALTER TABLE contenedores MODIFY estado VARCHAR(50) DEFAULT 'NUEVO';
ALTER TABLE contenedores RENAME COLUMN cliente TO cliente_dni;
