# Estado de Pruebas de Endpoints - TPI Backend

Este documento lista todos los endpoints disponibles y su estado de prueba con Postman.

## 📊 Resumen General

| Servicio | Total Endpoints | Probados | No Probados | % Completado |
|----------|----------------|----------|-------------|--------------|
| **Tarifa Service** | 6 | 6 | 0 | ✅ 100% |
| **Solicitud Service** | 11 | 5 | 6 | ⚠️ 45% |
| **Cliente Service** | 5 | 2 | 3 | ⚠️ 40% |
| **Contenedor Service** | 7 | 3 | 4 | ⚠️ 43% |
| **Camion Service** | 6 | 0 | 6 | ❌ 0% |
| **Deposito Service** | 6 | 1 | 5 | ⚠️ 17% |
| **Ruta Service** | 3 | 0 | 3 | ❌ 0% |
| **TOTAL** | **44** | **17** | **27** | **39%** |

---

## ✅ TARIFA SERVICE - 100% Probado

**Base URL:** `http://localhost:8087/api/tarifas` o `http://localhost:8080/api/tarifas` (Gateway)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ✅ GET | `/api/tarifas` | **Probado** | Lista todas las tarifas |
| ✅ GET | `/api/tarifas/{id}` | **Probado** | Obtiene tarifa por ID |
| ✅ POST | `/api/tarifas` | **Probado** | Crea nueva tarifa |
| ✅ PUT | `/api/tarifas/{id}` | **Probado** | Actualiza tarifa |
| ✅ POST | `/api/tarifas/calc` | **Probado** | Calcula costo de transporte |
| ✅ DELETE | `/api/tarifas/{id}` | **Probado** | Elimina tarifa |

**Colección Postman:** `tarifa-service-postman-collection.json`

---

## ⚠️ SOLICITUD SERVICE - 45% Probado

**Base URL:** `http://localhost:8080/api/solicitudes` (Gateway)

### ✅ Probados (5/11)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ✅ GET | `/api/solicitudes` | **Probado** | Lista todas las solicitudes (script) |
| ✅ GET | `/api/solicitudes/{numero}` | **Probado** | Obtiene solicitud por número (script) |
| ✅ GET | `/api/solicitudes/cliente/{dni}` | **Probado** | Solicitudes por cliente (script) |
| ✅ GET | `/api/solicitudes/estado/{estado}` | **Probado** | Solicitudes por estado (script) |
| ✅ POST | `/api/solicitudes` | **Probado** | Crea nueva solicitud (script) |

### ❌ No Probados (6/11)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ❌ POST | `/api/solicitudes/rutas/tentativas` | **No probado** | Genera rutas tentativas (usado en script pero no probado directamente) |
| ❌ POST | `/api/solicitudes/rutas/asignar` | **No probado** | Asigna ruta a solicitud (usado en script pero no probado directamente) |
| ❌ POST | `/api/solicitudes/tramos/asignar-camion` | **No probado** | Asigna camión a tramo (usado en script pero no probado directamente) |
| ❌ POST | `/api/solicitudes/tramos/{id}/iniciar` | **No probado** | Inicia un tramo |
| ❌ POST | `/api/solicitudes/tramos/{id}/finalizar` | **No probado** | Finaliza un tramo |
| ❌ GET | `/api/solicitudes/transportistas/{dni}/tramos` | **No probado** | Obtiene tramos asignados a transportista |

**Scripts de prueba:** `test-endpoints.ps1`, `test-estadia.ps1`

---

## ⚠️ CLIENTE SERVICE - 40% Probado

**Base URL:** `http://localhost:8080/api/clientes` (Gateway)

### ✅ Probados (2/5)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ✅ GET | `/api/clientes` | **Probado** | Lista todos los clientes (script) |
| ✅ POST | `/api/clientes` | **Probado** | Crea nuevo cliente (script) |

### ❌ No Probados (3/5)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ❌ GET | `/api/clientes/{dni}` | **No probado** | Obtiene cliente por DNI |
| ❌ PUT | `/api/clientes/{dni}` | **No probado** | Actualiza cliente |
| ❌ DELETE | `/api/clientes/{dni}` | **No probado** | Elimina cliente |

---

## ⚠️ CONTENEDOR SERVICE - 43% Probado

**Base URL:** `http://localhost:8080/api/contenedores` (Gateway)

### ✅ Probados (3/7)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ✅ GET | `/api/contenedores` | **Probado** | Lista todos los contenedores (script) |
| ✅ POST | `/api/contenedores` | **Probado** | Crea nuevo contenedor (script) |
| ✅ GET | `/api/contenedores/pendientes/entrega` | **Parcial** | Pendientes de entrega (sin filtros) |

### ❌ No Probados (4/7)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ❌ GET | `/api/contenedores/{id}` | **No probado** | Obtiene contenedor por ID |
| ❌ GET | `/api/contenedores/cliente/{dni}` | **No probado** | Contenedores por cliente |
| ❌ GET | `/api/contenedores/estado/{estado}` | **No probado** | Contenedores por estado |
| ❌ PUT | `/api/contenedores/{id}` | **No probado** | Actualiza contenedor |
| ❌ DELETE | `/api/contenedores/{id}` | **No probado** | Elimina contenedor |

**Nota:** El endpoint `/pendientes/entrega` existe pero falta implementar filtros por estado/ubicación según el TODO.

---

## ❌ CAMION SERVICE - 0% Probado

**Base URL:** `http://localhost:8080/api/camiones` (Gateway)

### ❌ No Probados (6/6)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ❌ GET | `/api/camiones` | **No probado** | Lista todos los camiones |
| ❌ GET | `/api/camiones/{dominio}` | **No probado** | Obtiene camión por dominio |
| ❌ GET | `/api/camiones/disponibles` | **No probado** | Lista camiones disponibles |
| ❌ POST | `/api/camiones` | **No probado** | Crea nuevo camión |
| ❌ PUT | `/api/camiones/{dominio}` | **No probado** | Actualiza camión |
| ❌ DELETE | `/api/camiones/{dominio}` | **No probado** | Elimina camión |

**Nota:** Los camiones se crean en scripts pero no se prueban los endpoints directamente.

---

## ⚠️ DEPOSITO SERVICE - 17% Probado

**Base URL:** `http://localhost:8080/api/depositos` (Gateway)

### ✅ Probados (1/6)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ✅ GET | `/api/depositos` | **Probado** | Lista todos los depósitos (script) |

### ❌ No Probados (5/6)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ❌ GET | `/api/depositos/activos` | **No probado** | Lista depósitos activos |
| ❌ GET | `/api/depositos/ciudad/{nombre}` | **No probado** | Depósitos por ciudad |
| ❌ POST | `/api/depositos` | **No probado** | Crea nuevo depósito (usado en script pero no probado directamente) |
| ❌ PUT | `/api/depositos/{id}` | **No probado** | Actualiza depósito |
| ❌ DELETE | `/api/depositos/{id}` | **No probado** | Elimina depósito |

---

## ❌ RUTA SERVICE - 0% Probado

**Base URL:** `http://localhost:8080/api/rutas` (Gateway)

### ❌ No Probados (3/3)

| Método | Endpoint | Estado | Notas |
|--------|----------|--------|-------|
| ❌ GET | `/api/rutas` | **No probado** | Lista todas las rutas |
| ❌ GET | `/api/rutas/solicitud/{id}` | **No probado** | Rutas por solicitud |
| ❌ POST | `/api/rutas` | **No probado** | Crea nueva ruta |

---

## 📝 Notas Importantes

### Endpoints Usados en Scripts pero No Probados Directamente

Algunos endpoints se usan en los scripts de PowerShell pero no se han probado directamente con Postman:

- `POST /api/solicitudes/rutas/tentativas`
- `POST /api/solicitudes/rutas/asignar`
- `POST /api/solicitudes/tramos/asignar-camion`
- `POST /api/solicitudes/tramos/{id}/iniciar`
- `POST /api/solicitudes/tramos/{id}/finalizar`
- `GET /api/solicitudes/transportistas/{dni}/tramos`

### Endpoints con Funcionalidad Parcial

- `GET /api/contenedores/pendientes/entrega` - Existe pero falta implementar filtros por estado/ubicación

### Prioridades para Pruebas

1. **Alta Prioridad:**
   - Endpoints de Solicitud Service (flujo principal)
   - Endpoints de Camion Service (CRUD completo)
   - Endpoints de Ruta Service

2. **Media Prioridad:**
   - CRUD completo de Cliente Service
   - CRUD completo de Contenedor Service
   - CRUD completo de Deposito Service

3. **Baja Prioridad:**
   - Endpoints de consulta específicos (filtros, búsquedas)

---

## 🎯 Próximos Pasos

1. Crear colecciones de Postman para los servicios faltantes
2. Probar endpoints críticos del flujo de solicitudes
3. Verificar funcionalidad completa de CRUD en todos los servicios
4. Documentar ejemplos de request/response para cada endpoint

---

**Última actualización:** 2025-11-16  
**Total de endpoints:** 44  
**Endpoints probados:** 17 (39%)  
**Endpoints pendientes:** 27 (61%)

