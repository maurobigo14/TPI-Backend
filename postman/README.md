# Carpeta de Pruebas - Postman y Scripts

Esta carpeta contiene todos los archivos relacionados con pruebas de los servicios usando Postman y scripts de PowerShell.

## 📁 Estructura de Archivos

### Colecciones de Postman

- **`tarifa-service-postman-collection.json`**
  - Colección completa de Postman para probar el servicio de tarifas
  - Incluye todos los endpoints: GET, POST, PUT, DELETE y cálculo de costos
  - **Cómo usar:** Importar en Postman desde File > Import

- **`solicitud-service-postman-collection.json`**
  - Colección para probar el servicio de solicitudes
  - Incluye: crear solicitud, rutas tentativas, asignar ruta, asignar camión, iniciar/finalizar tramo, etc.

- **`cliente-service-postman-collection.json`**
  - Colección para probar el servicio de clientes
  - CRUD completo de clientes

- **`contenedor-service-postman-collection.json`**
  - Colección para probar el servicio de contenedores
  - Incluye creación de contenedor y endpoints de consulta

- **`deposito-service-postman-collection.json`**
  - Colección para probar el servicio de depósitos
  - CRUD completo de depósitos

- **`camion-service-postman-collection.json`** ⭐ NUEVO
  - Colección completa para probar el servicio de camiones
  - Incluye todos los endpoints: GET, POST, PUT, DELETE
  - **IMPORTANTE:** El dominio es un String (ej: "ABC123"), no un número

- **`ruta-service-postman-collection.json`** ⭐ NUEVO
  - Colección para probar el servicio de rutas
  - Endpoints básicos de consulta y creación

- **`tarifa-service-postman-guide.md`**
  - Guía detallada con ejemplos de todos los endpoints
  - Incluye formatos de request/response
  - Instrucciones paso a paso

### Scripts de Prueba (PowerShell)

- **`crear-datos-prueba.ps1`**
  - **IMPORTANTE:** Ejecuta este script primero para crear los datos necesarios
  - Crea cliente, contenedor, depósito, camión y tarifa de prueba
  - Muestra los IDs creados para usar en las colecciones de Postman
  - Ejecutar antes de probar las colecciones

- **`test-endpoints.ps1`**
  - Script para probar endpoints básicos del sistema
  - Verifica conectividad y funcionalidad de servicios

- **`test-estadia.ps1`**
  - Script específico para probar el cálculo de estadía en depósitos
  - Crea solicitudes, asigna rutas, camiones y tramos
  - Incluye instrucciones para probar manualmente el cálculo de días de estadía

- **`test-endpoints-faltantes.ps1`**
  - Script para probar automáticamente todos los endpoints faltantes
  - Prueba endpoints de Solicitud, Cliente, Contenedor y Depósito Service

## 🚀 Cómo Usar

### Importar Colección en Postman

1. Abre Postman
2. Click en **Import** (botón superior izquierdo)
3. Selecciona `tarifa-service-postman-collection.json`
4. La colección aparecerá en tu workspace

### Ejecutar Scripts de Prueba

Desde la raíz del proyecto (`TPI-Backend`):

```powershell
# 1. PRIMERO: Crear datos de prueba (cliente, contenedor, depósito, etc.)
.\postman\crear-datos-prueba.ps1

# 2. Luego puedes probar los endpoints:
.\postman\test-endpoints.ps1          # Endpoints básicos
.\postman\test-estadia.ps1           # Cálculo de estadía
.\postman\test-endpoints-faltantes.ps1  # Endpoints faltantes
```

**Nota:** Asegúrate de que todos los servicios estén corriendo antes de ejecutar los scripts:
```powershell
docker-compose up -d
```

**Importante:** Si eliminas datos de prueba, ejecuta `crear-datos-prueba.ps1` nuevamente para recrearlos.

## 📋 Endpoints Disponibles

### Tarifa Service (Puerto 8087)
- `GET /api/tarifas` - Listar todas las tarifas
- `GET /api/tarifas/{id}` - Obtener tarifa por ID
- `POST /api/tarifas` - Crear nueva tarifa
- `PUT /api/tarifas/{id}` - Actualizar tarifa
- `POST /api/tarifas/calc` - Calcular costo de transporte
- `DELETE /api/tarifas/{id}` - Eliminar tarifa

### Camion Service (Puerto 8083) ⭐ NUEVO
- `GET /api/camiones` - Listar todos los camiones
- `GET /api/camiones/{dominio}` - Obtener camión por dominio (String)
- `GET /api/camiones/disponibles` - Listar camiones disponibles
- `POST /api/camiones` - Crear nuevo camión
- `PUT /api/camiones/{dominio}` - Actualizar camión
- `DELETE /api/camiones/{dominio}` - Eliminar camión

### Ruta Service (Puerto 8086) ⭐ NUEVO
- `GET /api/rutas` - Listar todas las rutas
- `GET /api/rutas/solicitud/{id}` - Obtener rutas por solicitud
- `POST /api/rutas` - Crear nueva ruta

## 🔧 Requisitos

- Postman instalado (para las colecciones)
- PowerShell (para los scripts)
- Docker y Docker Compose (para levantar los servicios)
- Servicios corriendo en los puertos configurados

## 📝 Notas

- Los scripts asumen que los servicios están corriendo en `http://localhost:8080` (API Gateway)
- Los servicios individuales también pueden probarse directamente en sus puertos:
  - Tarifa Service: `http://localhost:8087`
  - Solicitud Service: `http://localhost:8085`
  - Camion Service: `http://localhost:8083`
  - etc.

## 🆕 Agregar Nuevas Colecciones

Para agregar nuevas colecciones de Postman:

1. Exporta la colección desde Postman (Collection > Export)
2. Guarda el archivo JSON en esta carpeta
3. Actualiza este README con la información del nuevo servicio

