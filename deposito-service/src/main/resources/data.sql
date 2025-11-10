-- Opcional: limpiar para tener un arranque determinista en dev
TRUNCATE TABLE depositos RESTART IDENTITY CASCADE;

INSERT INTO depositos (nombre, direccion, ciudad, costo_estadia_por_dia, capacidad_maxima, activo) VALUES
('Depósito Córdoba', 'Av. Colón 123', 'Córdoba', 2500, 50000, true),
('Depósito Rosario', 'Bv. Oroño 456', 'Rosario', 3000, 60000, true),
('Depósito Buenos Aires', 'Av. Rivadavia 890', 'Buenos Aires', 4000, 80000, false);
