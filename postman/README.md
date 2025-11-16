# Carpeta de Pruebas - Postman y Scripts

Esta carpeta contiene todos los archivos relacionados con pruebas de los servicios usando Postman y scripts de PowerShell.

## 📁 Estructura de Archivos

### Colecciones de Postman

- **`tarifa-service-postman-collection.json`**
  - Colección completa de Postman para probar el servicio de tarifas
  - Incluye todos los endpoints: GET, POST, PUT, DELETE y cálculo de costos
  - **Cómo usar:** Importar en Postman desde File > Import

- **`tarifa-service-postman-guide.md`**
  - Guía detallada con ejemplos de todos los endpoints
  - Incluye formatos de request/response
  - Instrucciones paso a paso

### Scripts de Prueba (PowerShell)

- **`test-endpoints.ps1`**
  - Script para probar endpoints básicos del sistema
  - Verifica conectividad y funcionalidad de servicios

- **`test-estadia.ps1`**
  - Script específico para probar el cálculo de estadía en depósitos
  - Crea solicitudes, asigna rutas, camiones y tramos
  - Incluye instrucciones para probar manualmente el cálculo de días de estadía

## 🚀 Cómo Usar

### Importar Colección en Postman

1. Abre Postman
2. Click en **Import** (botón superior izquierdo)
3. Selecciona `tarifa-service-postman-collection.json`
4. La colección aparecerá en tu workspace

### Ejecutar Scripts de Prueba

Desde la raíz del proyecto (`TPI-Backend`):

```powershell
# Probar endpoints básicos
.\postman\test-endpoints.ps1

# Probar cálculo de estadía
.\postman\test-estadia.ps1
```

**Nota:** Asegúrate de que todos los servicios estén corriendo antes de ejecutar los scripts:
```powershell
docker-compose up -d
```

## 📋 Endpoints Disponibles

### Tarifa Service (Puerto 8087)

- `GET /api/tarifas` - Listar todas las tarifas
- `GET /api/tarifas/{id}` - Obtener tarifa por ID
- `POST /api/tarifas` - Crear nueva tarifa
- `PUT /api/tarifas/{id}` - Actualizar tarifa
- `POST /api/tarifas/calc` - Calcular costo de transporte
- `DELETE /api/tarifas/{id}` - Eliminar tarifa

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

