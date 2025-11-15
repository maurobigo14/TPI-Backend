# 📋 Comandos para Verificar el TPI - Backend 2025

## 🚀 Inicio Rápido

### Opción 1: Verificación Completa (Recomendado)
Este comando levanta todos los servicios y verifica los puntos 1 y 2:

```powershell
cd C:\Users\valem\OneDrive\Escritorio\BDA\TPI\TPI-Backend
.\verificar-tpi.ps1
```

### Opción 2: Solo Pruebas (si los servicios ya están corriendo)
```powershell
.\verificar-tpi.ps1 -SoloPruebas
```

### Opción 3: Con URL personalizada
```powershell
.\verificar-tpi.ps1 -BaseUrl "http://localhost:8081"
```

---

## 📝 Comandos Manuales

### 1. Levantar todos los servicios con Docker Compose

```powershell
cd C:\Users\valem\OneDrive\Escritorio\BDA\TPI\TPI-Backend
docker-compose up -d
```

**Verificar que están corriendo:**
```powershell
docker-compose ps
```

**Ver logs de un servicio específico:**
```powershell
docker-compose logs -f deposito-service
docker-compose logs -f solicitud-service
docker-compose logs -f eureka-server
```

**Detener todos los servicios:**
```powershell
docker-compose down
```

---

### 2. Verificar que los servicios están activos

**Eureka Server (Registro de servicios):**
```powershell
# Abrir en navegador
start http://localhost:8761
```

**API Gateway:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes" -Method Get
```

---

### 3. Punto 1: Registrar Nueva Solicitud de Transporte

**Comando PowerShell:**
```powershell
$body = @{
    cliente = @{
        dni = "12345678"
        nombre = "Juan"
        apellido = "Pérez"
        email = "juan.perez@example.com"
        telefono = "1234567890"
    }
    contenedor = @{
        identificacion = "CONT-001"
        pesoKg = 500.0
        volumenM3 = 30.0
        descripcion = "Contenedor estándar"
    }
    origenDireccion = "Buenos Aires, Argentina"
    origenLat = -34.6037
    origenLng = -58.3816
    destinoDireccion = "Rosario, Argentina"
    destinoLat = -32.9442
    destinoLng = -60.6505
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Uri "http://localhost:8080/api/solicitudes" -Method Post -ContentType "application/json" -Body $body
```

**Con cURL (si tienes Git Bash o WSL):**
```bash
curl -X POST http://localhost:8080/api/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": {
      "dni": "12345678",
      "nombre": "Juan",
      "apellido": "Pérez",
      "email": "juan.perez@example.com",
      "telefono": "1234567890"
    },
    "contenedor": {
      "identificacion": "CONT-001",
      "pesoKg": 500.0,
      "volumenM3": 30.0,
      "descripcion": "Contenedor estándar"
    },
    "origenDireccion": "Buenos Aires, Argentina",
    "origenLat": -34.6037,
    "origenLng": -58.3816,
    "destinoDireccion": "Rosario, Argentina",
    "destinoLat": -32.9442,
    "destinoLng": -60.6505
  }'
```

---

### 4. Punto 2: Consultar Rutas Tentativas

**Comando PowerShell:**
```powershell
$body = @{
    cliente = @{
        dni = "12345678"
        nombre = "Juan"
        apellido = "Pérez"
        email = "juan.perez@example.com"
        telefono = "1234567890"
    }
    contenedor = @{
        identificacion = "CONT-001"
        pesoKg = 500.0
        volumenM3 = 30.0
        descripcion = "Contenedor estándar"
    }
    origenDireccion = "Buenos Aires, Argentina"
    origenLat = -34.6037
    origenLng = -58.3816
    destinoDireccion = "Rosario, Argentina"
    destinoLat = -32.9442
    destinoLng = -60.6505
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Uri "http://localhost:8080/api/solicitudes/rutas/tentativas" -Method Post -ContentType "application/json" -Body $body
```

---

### 5. Consultas Adicionales

**Listar todas las solicitudes:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/solicitudes" -Method Get
```

**Consultar solicitud por número:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/solicitudes/1" -Method Get
```

**Consultar solicitudes por cliente:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/solicitudes/cliente/12345678" -Method Get
```

**Listar clientes:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes" -Method Get
```

**Listar contenedores:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/contenedores" -Method Get
```

**Listar depósitos:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/depositos" -Method Get
```

---

## 🔧 Solución de Problemas

### Error: "docker-compose no se reconoce"
**Solución:** Instala Docker Desktop o usa `docker compose` (sin guión) si tienes Docker v2.

### Error: "Puerto ya en uso"
**Solución:** 
```powershell
# Ver qué está usando el puerto
netstat -ano | findstr :8080

# Detener servicios anteriores
docker-compose down
```

### Error: "Connection refused" o "No se puede conectar"
**Solución:**
1. Verifica que los servicios estén corriendo: `docker-compose ps`
2. Espera unos segundos más (los servicios pueden tardar en iniciar)
3. Revisa los logs: `docker-compose logs -f`

### Error: "404 Not Found" en API Gateway
**Solución:**
1. Verifica que Eureka esté corriendo: `http://localhost:8761`
2. Verifica que los servicios estén registrados en Eureka
3. Prueba acceder directamente al servicio (sin gateway):
   - Cliente: `http://localhost:8081/api/clientes`
   - Solicitud: `http://localhost:8085/api/solicitudes`

### Los GET devuelven listas vacías
**Solución:** Es normal si no has creado datos. Usa el script `verificar-tpi.ps1` para poblar datos iniciales.

---

## 📊 Endpoints por Microservicio

### Cliente Service (Puerto 8081)
- `GET /api/clientes` - Listar todos
- `GET /api/clientes/{dni}` - Obtener por DNI
- `POST /api/clientes` - Crear
- `PUT /api/clientes/{dni}` - Actualizar
- `DELETE /api/clientes/{dni}` - Eliminar

### Contenedor Service (Puerto 8082)
- `GET /api/contenedores` - Listar todos
- `GET /api/contenedores/{id}` - Obtener por ID
- `GET /api/contenedores/cliente/{dni}` - Por cliente
- `GET /api/contenedores/estado/{estado}` - Por estado
- `POST /api/contenedores` - Crear
- `PUT /api/contenedores/{id}` - Actualizar

### Depósito Service (Puerto 8084/8085)
- `GET /api/depositos` - Listar todos
- `GET /api/depositos/activos` - Solo activos
- `GET /api/depositos/ciudad/{nombre}` - Por ciudad
- `POST /api/depositos` - Crear
- `PUT /api/depositos/{id}` - Actualizar
- `DELETE /api/depositos/{id}` - Eliminar

### Solicitud Service (Puerto 8085)
- `GET /api/solicitudes` - Listar todas
- `GET /api/solicitudes/{numero}` - Obtener por número
- `GET /api/solicitudes/cliente/{dni}` - Por cliente
- `GET /api/solicitudes/estado/{estado}` - Por estado
- `POST /api/solicitudes` - **PUNTO 1: Crear solicitud**
- `POST /api/solicitudes/rutas/tentativas` - **PUNTO 2: Rutas tentativas**

---

## ✅ Checklist de Verificación

- [ ] Docker Desktop está corriendo
- [ ] `docker-compose up -d` ejecutado sin errores
- [ ] Eureka Server accesible en `http://localhost:8761`
- [ ] API Gateway responde en `http://localhost:8080`
- [ ] Punto 1: Crear solicitud funciona
- [ ] Punto 2: Generar rutas tentativas funciona
- [ ] Los GET devuelven datos (no listas vacías)
- [ ] Los logs no muestran errores críticos

---

## 📚 Recursos Adicionales

- **Swagger/OpenAPI:** Una vez implementado, debería estar en cada servicio
- **Eureka Dashboard:** `http://localhost:8761` - Ver servicios registrados
- **Logs consolidados:** `docker-compose logs -f` - Ver todos los logs

---

## 🆘 ¿Necesitas Ayuda?

Si algo no funciona:
1. Revisa los logs: `docker-compose logs -f [nombre-servicio]`
2. Verifica que los servicios estén en Eureka: `http://localhost:8761`
3. Prueba acceder directamente al servicio (sin gateway)
4. Verifica que las bases de datos estén corriendo: `docker ps`

