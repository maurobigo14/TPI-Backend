# 🗺️ Configuración de Google Maps API

## Resumen

El sistema utiliza Google Maps Distance Matrix API para calcular distancias y tiempos reales entre puntos. Si la API key no está configurada, el sistema usa un fallback (fórmula Haversine) que funciona pero con menor precisión.

---

## ✅ Estado Actual

- **Código:** ✅ Implementado y funcionando con fallback
- **Configuración:** ⚠️ Requiere API key para usar Google Maps real
- **Fallback:** ✅ Disponible (fórmula Haversine)

---

## 📋 Pasos para Habilitar Google Maps API

### 1. Obtener API Key de Google Cloud

1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Crea un nuevo proyecto o selecciona uno existente
3. Habilita la **Distance Matrix API**:
   - Menú lateral > APIs & Services > Library
   - Busca "Distance Matrix API"
   - Haz clic en "Enable"
4. Crea credenciales:
   - Menú lateral > APIs & Services > Credentials
   - Click en "Create Credentials" > "API Key"
   - **Importante:** Restringe la API key:
     - Application restrictions: None (para desarrollo) o IP addresses (para producción)
     - API restrictions: Selecciona solo "Distance Matrix API"
5. Copia la API key generada

### 2. Configurar en el Proyecto

#### Opción A: Archivo .env (Recomendado para desarrollo)

Crea un archivo `.env` en la raíz del proyecto:

```env
GOOGLE_MAPS_API_KEY=tu-api-key-aqui
```

#### Opción B: Variable de entorno del sistema

**Windows (PowerShell):**
```powershell
$env:GOOGLE_MAPS_API_KEY="tu-api-key-aqui"
```

**Linux/Mac:**
```bash
export GOOGLE_MAPS_API_KEY="tu-api-key-aqui"
```

#### Opción C: Directamente en docker-compose.yml (No recomendado)

Edita `docker-compose.yml` en la sección `solicitud-service`:
```yaml
solicitud-service:
  environment:
    - GOOGLE_MAPS_API_KEY=tu-api-key-aqui
```

### 3. Reiniciar el Servicio

```powershell
# Detener servicios
docker compose down

# Levantar con la nueva configuración
docker compose up -d

# Verificar logs
docker compose logs solicitud-service --tail=50
```

---

## 🧪 Verificar que Funciona

### Logs sin API key (Fallback):
```
WARN  o.e.solicitud.service.GoogleMapsService : Google Maps API key no configurada. Usando fallback Haversine.
```

### Logs con API key configurada:
```
INFO  o.e.solicitud.service.GoogleMapsService : Usando Google Maps Distance Matrix API
```

### Test Manual

```powershell
# Crear una solicitud y verificar la distancia calculada
curl -X POST http://localhost:8080/api/solicitudes/rutas/tentativas `
  -H "Content-Type: application/json" `
  -d '{
    "cliente": {"dni": "12345678", "nombre": "Test", "apellido": "User", "email": "test@test.com", "telefono": "123456"},
    "contenedor": {"identificacion": "CONT-TEST", "pesoKg": 500, "volumenM3": 30, "descripcion": "Test"},
    "origenDireccion": "Buenos Aires, Argentina",
    "origenLat": -34.6037,
    "origenLng": -58.3816,
    "destinoDireccion": "Rosario, Argentina",
    "destinoLat": -32.9442,
    "destinoLng": -60.6505
  }'
```

**Con API key:** Verás distancias reales (ej: 305.2 km)  
**Sin API key:** Verás distancias aproximadas con Haversine (ej: 300 km)

---

## 💰 Costos de Google Maps API

### Pricing (2025)
- **Distance Matrix API:** $5 USD por 1,000 requests
- **Free tier:** $200 USD de crédito mensual = ~40,000 requests gratis/mes

### Estimación para el TPI
- Para desarrollo y demos: **100% gratis** (dentro del free tier)
- Para producción pequeña: **< $10 USD/mes**

---

## 🔒 Seguridad

### ⚠️ NO hacer:
- ❌ Subir la API key al repositorio público
- ❌ Compartir la API key en capturas de pantalla
- ❌ Dejar la API key sin restricciones

### ✅ SÍ hacer:
- ✅ Usar archivo `.env` (ya está en `.gitignore`)
- ✅ Restringir API key solo a Distance Matrix API
- ✅ Configurar restricciones de IP en producción
- ✅ Rotar la API key periódicamente

---

## 📊 Diferencias: Google Maps vs Fallback

| Aspecto | Google Maps API | Fallback (Haversine) |
|---------|----------------|----------------------|
| **Precisión distancia** | ✅ Real (rutas de carreteras) | ⚠️ Aproximada (línea recta) |
| **Tiempo estimado** | ✅ Real (tráfico, velocidades) | ⚠️ Aproximado (distancia/50) |
| **Costo** | 💰 $5/1000 requests | 🆓 Gratis |
| **Requiere internet** | ✅ Sí | ❌ No |
| **Ideal para** | Producción, demos | Desarrollo, tests |

---

## 🐛 Troubleshooting

### Problema: "API key not found"
**Solución:** Verifica que el archivo `.env` esté en la raíz y reinicia Docker Compose.

### Problema: "API key invalid"
**Solución:** 
1. Verifica que hayas habilitado Distance Matrix API
2. Revisa que la API key no tenga espacios extra
3. Espera 5 minutos si acabas de crear la key (propagación)

### Problema: "Over quota"
**Solución:** Has excedido el free tier. Verifica tu consumo en Google Cloud Console.

### Problema: Sigue usando fallback
**Solución:**
```powershell
# Verifica la variable de entorno
docker compose exec solicitud-service printenv GOOGLE_MAPS_API_KEY

# Si no aparece, reconstruye la imagen
docker compose up -d --build solicitud-service
```

---

## ✅ Checklist de Entrega del TPI

Para la entrega del TPI, **NO es obligatorio** tener Google Maps configurado:

- ✅ **Opción 1 (Recomendada):** Configura Google Maps API para tener distancias reales
- ✅ **Opción 2 (Válida):** Usa el fallback Haversine (ya funciona al 100%)

El sistema funciona perfectamente con ambas opciones. La API de Google solo mejora la precisión.

---

## 📝 Documentación Adicional

- [Google Maps Distance Matrix API](https://developers.google.com/maps/documentation/distance-matrix)
- [Google Cloud Pricing Calculator](https://cloud.google.com/products/calculator)
- [Restricting API Keys](https://cloud.google.com/docs/authentication/api-keys#securing_an_api_key)

---

**Última actualización:** 27 de Noviembre de 2025
