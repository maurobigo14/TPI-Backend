# Guía de Pruebas - Tarifa Service con Postman

## URL Base
```
http://localhost:8087
```

## Endpoints Disponibles

### 1. Listar todas las tarifas
**GET** `/api/tarifas`

**Ejemplo de respuesta:**
```json
[
  {
    "id": 1,
    "descripcion": "Tarifa Estándar",
    "costoBaseKm": 10.5,
    "valorLitroCombustible": 850.0,
    "consumoPromedioKm": 8.5,
    "costoEstadiaDiaria": 5000.0,
    "cargoGestion": 15.0
  }
]
```

---

### 2. Obtener tarifa por ID
**GET** `/api/tarifas/{id}`

**Parámetros:**
- `id` (path): ID de la tarifa

**Ejemplo:** `GET /api/tarifas/1`

---

### 3. Crear nueva tarifa
**POST** `/api/tarifas`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "descripcion": "Tarifa Estándar",
    "costoBaseKm": 10.5,
    "valorLitroCombustible": 850.0,
    "consumoPromedioKm": 8.5,
    "costoEstadiaDiaria": 5000.0,
    "cargoGestion": 15.0
}
```

**Campos:**
- `descripcion` (String): Descripción de la tarifa
- `costoBaseKm` (Double): Costo base por kilómetro
- `valorLitroCombustible` (Double): Valor del litro de combustible
- `consumoPromedioKm` (Double): Consumo promedio por kilómetro
- `costoEstadiaDiaria` (Double): Costo de estadía diaria
- `cargoGestion` (Double): Cargo de gestión

---

### 4. Actualizar tarifa
**PUT** `/api/tarifas/{id}`

**Parámetros:**
- `id` (path): ID de la tarifa a actualizar

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "descripcion": "Tarifa Actualizada",
    "costoBaseKm": 12.0,
    "valorLitroCombustible": 900.0,
    "consumoPromedioKm": 8.0,
    "costoEstadiaDiaria": 5500.0,
    "cargoGestion": 18.0
}
```

---

### 5. Calcular costo de tarifa
**POST** `/api/tarifas/calc`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "distanciaKm": 150.5,
    "pesoKg": 500.0,
    "volumenM3": 30.0,
    "diasEstadia": 2
}
```

**Campos:**
- `distanciaKm` (Double): Distancia en kilómetros
- `pesoKg` (Double): Peso en kilogramos
- `volumenM3` (Double): Volumen en metros cúbicos
- `diasEstadia` (Integer): Días de estadía (opcional, default: 0)

**Ejemplo de respuesta:**
```json
{
    "distanciaKm": 150.5,
    "costoBaseKm": 1806.0,
    "costoCombustible": 1083600.0,
    "cargoGestion": 18.0,
    "costoEstadia": 11000.0,
    "costoTotal": 1096424.0,
    "moneda": "ARS"
}
```

---

### 6. Eliminar tarifa
**DELETE** `/api/tarifas/{id}`

**Parámetros:**
- `id` (path): ID de la tarifa a eliminar

**Ejemplo:** `DELETE /api/tarifas/1`

---

## Cómo importar la colección en Postman

1. Abre Postman
2. Click en **Import** (botón superior izquierdo)
3. Selecciona el archivo `tarifa-service-postman-collection.json`
4. La colección aparecerá en tu workspace

## Orden recomendado de pruebas

1. **Crear tarifa** (POST) - Para tener datos de prueba
2. **Listar tarifas** (GET) - Verificar que se creó
3. **Obtener tarifa por ID** (GET) - Verificar detalles
4. **Calcular costo** (POST /calc) - Probar el cálculo
5. **Actualizar tarifa** (PUT) - Modificar datos
6. **Eliminar tarifa** (DELETE) - Limpiar datos de prueba

## Notas

- Asegúrate de que el servicio esté corriendo en `http://localhost:8087`
- Si el servicio está en Docker, verifica que el puerto esté mapeado correctamente
- Los IDs de tarifas son auto-generados, comienza desde 1

