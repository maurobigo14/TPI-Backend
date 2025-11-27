# URLs de Swagger/OpenAPI - TPI Backend

## 📋 Servicios y URLs de Swagger

### 🔵 Solicitud Service (Puerto 8085)
**Swagger UI:**
- http://localhost:8085/swagger-ui.html
- http://localhost:8085/swagger-ui/index.html

**OpenAPI JSON:**
- http://localhost:8085/v3/api-docs

**Endpoints principales:**
- `POST /api/solicitudes` - Registrar solicitud
- `GET /api/solicitudes/{numero}` - Consultar estado
- `POST /api/solicitudes/rutas/tentativas` - Rutas tentativas
- `POST /api/solicitudes/rutas/asignar` - Asignar ruta
- `POST /api/solicitudes/tramos/asignar-camion` - Asignar camión
- `GET /api/solicitudes/transportistas/{dni}/tramos` - Tramos asignados
- `POST /api/solicitudes/tramos/{id}/iniciar` - Iniciar tramo
- `POST /api/solicitudes/tramos/{id}/finalizar` - Finalizar tramo

---

### 📦 Contenedor Service (Puerto 8082)
**Swagger UI:**
- http://localhost:8082/swagger-ui.html
- http://localhost:8082/swagger-ui/index.html

**OpenAPI JSON:**
- http://localhost:8082/v3/api-docs

**Endpoints principales:**
- `GET /api/contenedores` - Listar contenedores
- `POST /api/contenedores` - Crear contenedor
- `GET /api/contenedores/{id}` - Obtener contenedor
- `GET /api/contenedores/pendientes/entrega` - Pendientes de entrega
- `GET /api/contenedores/estado/{estado}` - Por estado

---

### 👤 Cliente Service (Puerto 8081)
**Swagger UI:**
- http://localhost:8081/swagger-ui.html
- http://localhost:8081/swagger-ui/index.html

**OpenAPI JSON:**
- http://localhost:8081/v3/api-docs

**Endpoints principales:**
- `GET /api/clientes` - Listar clientes
- `POST /api/clientes` - Crear cliente
- `GET /api/clientes/{dni}` - Obtener cliente
- `PUT /api/clientes/{dni}` - Actualizar cliente
- `DELETE /api/clientes/{dni}` - Eliminar cliente

---

### 🚛 Camion Service (Puerto 8083)
**Swagger UI:**
- http://localhost:8083/swagger-ui.html
- http://localhost:8083/swagger-ui/index.html

**OpenAPI JSON:**
- http://localhost:8083/v3/api-docs

**Endpoints principales:**
- `GET /api/camiones` - Listar camiones
- `POST /api/camiones` - Crear camión
- `GET /api/camiones/{dominio}` - Obtener camión
- `PUT /api/camiones/{dominio}` - Actualizar camión
- `DELETE /api/camiones/{dominio}` - Eliminar camión

---

### 🏭 Deposito Service (Puerto 8084)
**Swagger UI:**
- http://localhost:8084/swagger-ui.html
- http://localhost:8084/swagger-ui/index.html

**OpenAPI JSON:**
- http://localhost:8084/v3/api-docs

**Endpoints principales:**
- `GET /api/depositos` - Listar depósitos
- `POST /api/depositos` - Crear depósito
- `GET /api/depositos/{id}` - Obtener depósito
- `PUT /api/depositos/{id}` - Actualizar depósito
- `DELETE /api/depositos/{id}` - Eliminar depósito
- `GET /api/depositos/activos` - Depósitos activos
- `GET /api/depositos/ciudad/{nombre}` - Por ciudad

---

### 💰 Tarifa Service (Puerto 8087)
**Swagger UI:**
- http://localhost:8087/swagger-ui.html
- http://localhost:8087/swagger-ui/index.html

**OpenAPI JSON:**
- http://localhost:8087/v3/api-docs

**Endpoints principales:**
- `GET /api/tarifas` - Listar tarifas
- `POST /api/tarifas` - Crear tarifa
- `GET /api/tarifas/{id}` - Obtener tarifa
- `PUT /api/tarifas/{id}` - Actualizar tarifa
- `POST /api/tarifas/calc` - Calcular costo

---

### 🗺️ Ruta Service (Puerto 8086)
**Swagger UI:**
- http://localhost:8086/swagger-ui.html
- http://localhost:8086/swagger-ui/index.html

**OpenAPI JSON:**
- http://localhost:8086/v3/api-docs

**Endpoints principales:**
- `GET /api/rutas` - Listar rutas
- `GET /api/rutas/solicitud/{solicitudId}` - Rutas por solicitud
- `POST /api/rutas` - Crear ruta

---

## 🌐 API Gateway (Puerto 8080)

**Nota:** El API Gateway redirige las peticiones a los servicios correspondientes. Los endpoints están disponibles a través del gateway:

- http://localhost:8080/api/solicitudes/**
- http://localhost:8080/api/contenedores/**
- http://localhost:8080/api/clientes/**
- http://localhost:8080/api/camiones/**
- http://localhost:8080/api/depositos/**
- http://localhost:8080/api/tarifas/**
- http://localhost:8080/api/rutas/**

---

## ⚠️ Nota sobre Autenticación

**Importante:** Todos los endpoints funcionan sin autenticación. No se requiere configuración de seguridad ni tokens.

---

## 🔍 Verificación Rápida

Si Swagger no está disponible en un servicio, verifica:

1. Que el servicio esté corriendo: `docker-compose ps`
2. Que la dependencia `springdoc-openapi-starter-webmvc-ui` esté en el `pom.xml`

---

## 📝 Requerimientos del TPI - Endpoints Clave

### Requerimiento 1: Registrar solicitud
- **Swagger:** http://localhost:8085/swagger-ui.html
- **Endpoint:** `POST /api/solicitudes`

### Requerimiento 2: Consultar estado
- **Swagger:** http://localhost:8085/swagger-ui.html
- **Endpoint:** `GET /api/solicitudes/{numero}`

### Requerimiento 3: Rutas tentativas
- **Swagger:** http://localhost:8085/swagger-ui.html
- **Endpoint:** `POST /api/solicitudes/rutas/tentativas`

### Requerimiento 4: Asignar ruta
- **Swagger:** http://localhost:8085/swagger-ui.html
- **Endpoint:** `POST /api/solicitudes/rutas/asignar`

### Requerimiento 5: Contenedores pendientes
- **Swagger:** http://localhost:8082/swagger-ui.html
- **Endpoint:** `GET /api/contenedores/pendientes/entrega`

### Requerimiento 6: Asignar camión
- **Swagger:** http://localhost:8085/swagger-ui.html
- **Endpoint:** `POST /api/solicitudes/tramos/asignar-camion`

### Requerimiento 7: Iniciar/Finalizar tramo
- **Swagger:** http://localhost:8085/swagger-ui.html
- **Endpoints:** 
  - `POST /api/solicitudes/tramos/{id}/iniciar`
  - `POST /api/solicitudes/tramos/{id}/finalizar`

### Requerimiento 8: Calcular costo
- **Swagger:** http://localhost:8087/swagger-ui.html
- **Endpoint:** `POST /api/tarifas/calc`

### Requerimiento 9: Tiempo/Costo real
- **Swagger:** http://localhost:8085/swagger-ui.html
- **Endpoint:** `GET /api/solicitudes/{numero}` (ver campos `tiempoReal` y `costoFinal`)

### Requerimiento 10: CRUD Depósitos/Camiones/Tarifas
- **Depósitos:** http://localhost:8084/swagger-ui.html
- **Camiones:** http://localhost:8083/swagger-ui.html
- **Tarifas:** http://localhost:8087/swagger-ui.html

### Requerimiento 11: Validar capacidad
- **Swagger:** http://localhost:8085/swagger-ui.html
- **Endpoint:** `POST /api/solicitudes/tramos/asignar-camion` (validación automática)


