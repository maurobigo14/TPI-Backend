# Guía Rápida - Probar Requerimientos del TPI en Postman

## 🔧 Configuración Inicial

**Base URL:** `http://localhost:8080` (API Gateway)

**✅ IMPORTANTE:** La autenticación está **DESHABILITADA** para pruebas. No necesitas tokens ni Keycloak.

---

## ✅ REQUERIMIENTO 1: Registrar nueva solicitud de transporte (Cliente)

**Método:** `POST`  
**URL:** `http://localhost:8080/api/solicitudes`  
**Headers:**
```
Content-Type: application/json
```
**Nota:** No requiere autenticación

**Body (JSON):**
```json
{
  "cliente": {
    "dni": "12345678",
    "nombre": "Juan",
    "apellido": "Perez",
    "email": "juan.perez@example.com",
    "telefono": "1234567890"
  },
  "contenedor": {
    "identificacion": "CONT-001",
    "pesoKg": 500.0,
    "volumenM3": 30.0,
    "descripcion": "Contenedor estandar"
  },
  "origenDireccion": "Buenos Aires, Argentina",
  "origenLat": -34.6037,
  "origenLng": -58.3816,
  "destinoDireccion": "Rosario, Argentina",
  "destinoLat": -32.9442,
  "destinoLng": -60.6505
}
```

**Respuesta esperada:** `numero` de solicitud (guárdalo para los siguientes pasos)

---

## ✅ REQUERIMIENTO 2: Consultar estado del transporte (Cliente)

**Método:** `GET`  
**URL:** `http://localhost:8080/api/solicitudes/{numero}`  
**Ejemplo:** `http://localhost:8080/api/solicitudes/1`

**Respuesta esperada:** Estado de la solicitud, contenedorId, costoEstimado, etc.

---

## ✅ REQUERIMIENTO 3: Consultar rutas tentativas (Operador)

**Método:** `POST`  
**URL:** `http://localhost:8080/api/solicitudes/rutas/tentativas`  
**Headers:**
```
Content-Type: application/json
```

**Body (JSON):** (mismo que Requerimiento 1)

**Respuesta esperada:** Lista de rutas tentativas con tramos, costos y tiempos estimados

---

## ✅ REQUERIMIENTO 4: Asignar ruta a la solicitud (Operador)

**Método:** `POST`  
**URL:** `http://localhost:8080/api/solicitudes/rutas/asignar`  
**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "solicitudId": 1,
  "descripcion": "Ruta confirmada",
  "tramos": [
    {
      "numeroSecuencia": 1,
      "origenDireccion": "Buenos Aires, Argentina",
      "origenLat": -34.6037,
      "origenLng": -58.3816,
      "destinoDireccion": "Rosario, Argentina",
      "destinoLat": -32.9442,
      "destinoLng": -60.6505,
      "distanciaKm": 300.0,
      "tiempoMin": 180,
      "costo": 5000.0
    }
  ],
  "distanciaTotalKm": 300.0,
  "tiempoTotalMin": 180,
  "costoTotal": 5000.0
}
```

**Respuesta esperada:** `rutaId` y `tramos` con sus IDs (guarda el `tramoId` del primer tramo)

---

## ✅ REQUERIMIENTO 5: Consultar contenedores pendientes (Operador)

**Método:** `GET`  
**URL:** `http://localhost:8080/api/contenedores/pendientes/entrega`

**Filtro por estado:**
**Método:** `GET`  
**URL:** `http://localhost:8080/api/contenedores/estado/EN_TRANSITO`

**Respuesta esperada:** Lista de contenedores pendientes

---

## ✅ REQUERIMIENTO 6: Asignar camión a tramo (Operador)

**Primero, crear un camión:**
**Método:** `POST`  
**URL:** `http://localhost:8080/api/camiones`  
**Body:**
```json
{
  "dominio": "ABC123",
  "nombreTransportista": "J. Perez",
  "telefono": "1234567890",
  "capacidadPeso": 10000.0,
  "capacidadVolumen": 50.0,
  "disponibilidad": true,
  "costos": 100.0
}
```

**Luego, asignar el camión al tramo:**
**Método:** `POST`  
**URL:** `http://localhost:8080/api/solicitudes/tramos/asignar-camion`  
**Body:**
```json
{
  "tramoId": 1,
  "camionDominio": "ABC123",
  "transportistaDni": "12345678"
}
```

**Respuesta esperada:** `asignacionId` (guárdalo para el siguiente paso)

---

## ✅ REQUERIMIENTO 7: Iniciar/Finalizar tramo (Transportista)

**Iniciar tramo:**
**Método:** `POST`  
**URL:** `http://localhost:8080/api/solicitudes/tramos/{tramoId}/iniciar`  
**Ejemplo:** `http://localhost:8080/api/solicitudes/tramos/1/iniciar`  
**Body:**
```json
{
  "asignacionCamionId": 1,
  "observaciones": "Inicio de tramo"
}
```

**Finalizar tramo:**
**Método:** `POST`  
**URL:** `http://localhost:8080/api/solicitudes/tramos/{tramoId}/finalizar`  
**Ejemplo:** `http://localhost:8080/api/solicitudes/tramos/1/finalizar`  
**Body:**
```json
{
  "asignacionCamionId": 1,
  "observaciones": "Fin de tramo"
}
```

**Respuesta esperada:** `costoFinal` calculado

---

## ✅ REQUERIMIENTO 8: Calcular costo total

**Método:** `POST`  
**URL:** `http://localhost:8080/api/tarifas/calc`  
**Body:**
```json
{
  "distanciaKm": 300.0,
  "pesoKg": 500.0,
  "volumenM3": 30.0,
  "tipo": "ESTANDAR"
}
```

**Respuesta esperada:** `costoTotal` calculado

---

## ✅ REQUERIMIENTO 9: Ver tiempo/costo real

**Método:** `GET`  
**URL:** `http://localhost:8080/api/solicitudes/{numero}`  
**Ejemplo:** `http://localhost:8080/api/solicitudes/1`

**Respuesta esperada:** Campos `tiempoReal` y `costoFinal` (se llenan al finalizar todos los tramos)

---

## ✅ REQUERIMIENTO 10: CRUD Depósitos, Camiones y Tarifas

### Depósitos

**Crear:**
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/depositos`
- **Body:**
```json
{
  "nombre": "Deposito Central",
  "direccion": "Calle Falsa 123",
  "ciudad": "Buenos Aires",
  "costoEstadiaPorDia": 50.0,
  "capacidadMaxima": 100.0,
  "activo": true
}
```

**Actualizar:**
- **Método:** `PUT`
- **URL:** `http://localhost:8080/api/depositos/{id}`
- **Body:** (mismo formato que crear)

### Camiones

**Crear:** (ya lo hiciste en Requerimiento 6)

**Actualizar:**
- **Método:** `PUT`
- **URL:** `http://localhost:8080/api/camiones/{dominio}`
- **Ejemplo:** `http://localhost:8080/api/camiones/ABC123`
- **Body:**
```json
{
  "dominio": "ABC123",
  "nombreTransportista": "J. Perez",
  "telefono": "9876543210",
  "capacidadPeso": 12000.0,
  "capacidadVolumen": 60.0,
  "disponibilidad": false,
  "costos": 150.0
}
```

### Tarifas

**Crear:**
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/tarifas`
- **Body:**
```json
{
  "tipo": "ESTANDAR",
  "precioBase": 1000.0,
  "precioPorKm": 10.0,
  "precioPorKg": 5.0,
  "precioPorM3": 3.0,
  "activa": true
}
```

**Actualizar:**
- **Método:** `PUT`
- **URL:** `http://localhost:8080/api/tarifas/{id}`
- **Body:** (mismo formato que crear)

---

## ✅ REQUERIMIENTO 11: Validar capacidad máxima

**Crear camión con capacidad limitada:**
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/camiones`
- **Body:**
```json
{
  "dominio": "LIM-001",
  "nombreTransportista": "Test",
  "telefono": "1234567890",
  "capacidadPeso": 100.0,
  "capacidadVolumen": 10.0,
  "disponibilidad": true,
  "costos": 100.0
}
```

**Intentar asignar contenedor que excede capacidad:**
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/solicitudes/tramos/asignar-camion`
- **Body:**
```json
{
  "tramoId": 1,
  "camionDominio": "LIM-001",
  "transportistaDni": "12345678"
}
```

**Respuesta esperada:** Error 400 si el contenedor excede la capacidad

---

## 📝 Orden Recomendado para Probar

1. **Requerimiento 1** - Crear solicitud (guarda el `numero`)
2. **Requerimiento 2** - Consultar estado
3. **Requerimiento 3** - Rutas tentativas
4. **Requerimiento 4** - Asignar ruta (guarda el `tramoId`)
5. **Requerimiento 5** - Contenedores pendientes
6. **Requerimiento 6** - Crear camión y asignarlo (guarda el `asignacionId`)
7. **Requerimiento 7** - Iniciar y finalizar tramo
8. **Requerimiento 8** - Calcular costo
9. **Requerimiento 9** - Ver tiempo/costo real
10. **Requerimiento 10** - CRUD depósitos, camiones, tarifas
11. **Requerimiento 11** - Validar capacidad

---

## ⚠️ Notas Importantes

- **Autenticación:** ✅ DESHABILITADA - Puedes probar todos los endpoints sin tokens
- **IDs:** Guarda los IDs que devuelven las respuestas (`numero`, `tramoId`, `asignacionId`, etc.) para usarlos en los siguientes pasos
- **Errores comunes:**
   - 404: El recurso no existe (verifica que hayas creado los datos previos)
   - 400: Datos inválidos (revisa el formato del JSON)
   - 500: Error del servidor (revisa los logs de Docker)

---

## 🚀 Colecciones de Postman

Si prefieres importar colecciones completas, están en la carpeta `postman/`:
- `solicitud-service-postman-collection.json`
- `contenedor-service-postman-collection.json`
- `cliente-service-postman-collection.json`
- `deposito-service-postman-collection.json`
- `camion-service-postman-collection.json`
- `tarifa-service-postman-collection.json`
- `ruta-service-postman-collection.json`

