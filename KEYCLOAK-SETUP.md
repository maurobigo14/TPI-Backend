# Keycloak - Configuración de Seguridad y Autenticación

## 📋 Resumen

El proyecto integra **Keycloak 25.0.2** como proveedor de identidad (IdP) con autenticación OAuth2/OIDC, protegiendo el API Gateway y definiendo roles para controlar el acceso a endpoints según responsabilidades del negocio.

---

## 🔐 Arquitectura de Seguridad

### Componentes
- **Keycloak**: servidor de autenticación OAuth2/OIDC en `http://localhost:8088`
- **API Gateway**: valida JWT con Spring Security Resource Server
- **Realm**: `tpi-realm` (importado automáticamente desde `keycloak/realms/tpi-realm.json`)
- **Client OIDC**: `api-gateway` (público, password grant habilitado)

### Flujo de Autenticación
1. Cliente solicita token a Keycloak con credenciales (username/password).
2. Keycloak valida y devuelve JWT con `access_token` y roles en `realm_access.roles`.
3. Cliente incluye `Authorization: Bearer <access_token>` en requests al Gateway.
4. Gateway valida JWT con JWKS de Keycloak y mapea roles a `ROLE_*`.
5. Spring Security autoriza acceso según reglas por path/método.

---

## 👥 Usuarios y Roles

| Usuario       | Password           | Rol(es)       | Permisos                                       |
|---------------|-------------------|---------------|------------------------------------------------|
| `admin`       | `Admin123!`       | ADMIN         | Acceso total a todos los endpoints             |
| `operador`    | `Operador123!`    | OPERADOR      | CRUD tarifas/depósitos/camiones, asignar ruta/camión, consultas |
| `transportista` | `Transportista123!` | TRANSPORTISTA | Iniciar/finalizar tramos                       |
| `cliente`     | `Cliente123!`     | CLIENTE       | Crear/consultar solicitudes propias            |

---

## 🔑 Obtener Access Token

### cURL
```bash
curl -X POST "http://localhost:8088/realms/tpi-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=api-gateway" \
  -d "username=operador" \
  -d "password=Operador123!"
```

### PowerShell
```powershell
$body = 'grant_type=password&client_id=api-gateway&username=operador&password=Operador123!'
$resp = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8088/realms/tpi-realm/protocol/openid-connect/token" `
  -ContentType 'application/x-www-form-urlencoded' `
  -Body $body
$token = $resp.access_token
Write-Host "Token: $token"
```

### Postman
1. **Request**: `POST http://localhost:8088/realms/tpi-realm/protocol/openid-connect/token`
2. **Body (x-www-form-urlencoded)**:
   - `grant_type` = `password`
   - `client_id` = `api-gateway`
   - `username` = `operador`
   - `password` = `Operador123!`
3. **Guardar token**: en variable `access_token` desde la respuesta JSON.
4. **Usar en requests**: Header `Authorization: Bearer {{access_token}}`

---

## 🛡️ Control de Acceso por Rol

### Solicitudes
- **POST `/api/solicitudes`**: CLIENTE, OPERADOR, ADMIN
- **GET `/api/solicitudes/**`**: CLIENTE (propia), OPERADOR, ADMIN

### Rutas
- **POST `/api/solicitudes/rutas/**`**: OPERADOR, ADMIN (tentativas, asignar)

### Tramos
- **POST `/api/solicitudes/tramos/asignar-camion`**: OPERADOR, ADMIN
- **POST `/api/solicitudes/tramos/{id}/iniciar`**: TRANSPORTISTA, ADMIN
- **POST `/api/solicitudes/tramos/{id}/finalizar`**: TRANSPORTISTA, ADMIN

### Camiones
- **POST/PUT/DELETE `/api/camiones/**`**: OPERADOR, ADMIN

### Depósitos
- **POST/PUT/DELETE `/api/depositos/**`**: OPERADOR, ADMIN

### Tarifas
- **POST/PUT/DELETE `/api/tarifas/**`**: OPERADOR, ADMIN
- **GET `/api/tarifas/**`**: Autenticado

### Contenedores
- **GET `/api/contenedores/**`**: OPERADOR, ADMIN

---

## 🚀 Arranque y Verificación

### 1. Levantar Keycloak
```powershell
docker compose up -d keycloak-db keycloak
```
Espera ~10 segundos para que Keycloak importe el realm.

### 2. Verificar Keycloak
```powershell
# Comprobar que responde
Invoke-RestMethod -Uri "http://localhost:8088/realms/tpi-realm/.well-known/openid-configuration" | Select-Object issuer, token_endpoint
```

### 3. Levantar API Gateway
```powershell
docker compose up -d api-gateway
```

### 4. Obtener Token y Probar
```powershell
$body = 'grant_type=password&client_id=api-gateway&username=operador&password=Operador123!'
$resp = Invoke-RestMethod -Method Post -Uri "http://localhost:8088/realms/tpi-realm/protocol/openid-connect/token" -ContentType 'application/x-www-form-urlencoded' -Body $body
$token = $resp.access_token

# Llamar endpoint protegido
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes" -Headers @{ Authorization = "Bearer $token" }
```

### 5. Ejecutar Verificación Completa
```powershell
.\verificar-requerimientos.ps1
```
Debería mostrar **11/11 OK** con autenticación por roles.

---

## 📝 Notas Técnicas

### Configuración del Gateway
- **Dependencias**: `spring-boot-starter-oauth2-resource-server`, `spring-boot-starter-security`
- **JWT Validation**: solo por JWKS (sin validación estricta de issuer para compatibilidad localhost/contenedor)
- **Role Mapping**: `KeycloakRealmRoleConverter` mapea `realm_access.roles` → `ROLE_*`

### Realm Import
- Archivo: `keycloak/realms/tpi-realm.json`
- Montado en: `/opt/keycloak/data/import`
- Flag: `--import-realm` en comando de Keycloak

### Limitaciones de Memoria
- Keycloak configurado con `JAVA_OPTS=-Xms256m -Xmx512m` para evitar OOM en Docker Desktop.

### Endpoints Públicos (sin auth)
- `/actuator/**`
- `/swagger-ui/**`
- `/v3/api-docs/**`

---

## 🔧 Troubleshooting

### Error 401 con token válido
- **Causa**: Issuer mismatch entre Keycloak y Gateway.
- **Solución**: Gateway usa solo `jwk-set-uri` (no `issuer-uri`) para validación flexible.

### Error "Account is not fully set up"
- **Causa**: Usuarios sin email o con `requiredActions`.
- **Solución**: Realm import incluye `email`, `emailVerified: true`, `requiredActions: []`.

### Keycloak exit 137 (OOM killed)
- **Causa**: JVM excede memoria de Docker.
- **Solución**: Ajustado `JAVA_OPTS` con límites explícitos (-Xmx512m).

### Token expira
- **Duración**: 3600s (1 hora) definido en realm `accessTokenLifespan`.
- **Renovación**: solicitar nuevo token con `grant_type=password` o implementar `refresh_token`.

---

## 📚 Referencias

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
- [OpenID Connect Discovery](http://localhost:8088/realms/tpi-realm/.well-known/openid-configuration)

---

## ✅ Estado de Implementación

- [x] Keycloak en Docker con Postgres
- [x] Realm `tpi-realm` con 4 roles y 4 usuarios
- [x] Client `api-gateway` (public, password grant)
- [x] API Gateway con JWT Resource Server
- [x] Control de acceso por rol en Gateway (path/method)
- [x] Scripts actualizados con obtención automática de token
- [x] Documentación Postman con flujo de autenticación
- [x] Verificación end-to-end 11/11 OK con autenticación

**Sistema 100% funcional con seguridad por roles.**
