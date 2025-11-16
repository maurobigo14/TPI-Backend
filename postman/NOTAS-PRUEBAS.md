# Notas Importantes para Pruebas en Postman

## ⚠️ Errores Comunes y Soluciones

### 1. Error: "La solicitud ya tiene una ruta asignada"

**Causa:** Estás intentando asignar una ruta a una solicitud que ya tiene una ruta asignada.

**Solución:**
1. Crea una **nueva solicitud** usando el request "0. Crear Solicitud" en la colección
2. Copia el `numero` de la respuesta
3. Usa ese `numero` como `solicitudId` en el request "2. Asignar Ruta"

**Alternativa:** Usa una solicitud en estado "borrador" que no tenga ruta asignada.

---

### 2. Error: "Contenedor no encontrado" o "Cliente no encontrado"

**Causa:** Estás intentando usar IDs que no existen en la base de datos.

**Solución:**
1. Ejecuta el script `crear-datos-prueba.ps1` para crear todos los datos necesarios
2. O crea los datos manualmente usando los requests POST de cada servicio
3. Copia los IDs creados y úsalos en los demás requests

---

### 3. Error: "Tramo no encontrado"

**Causa:** Estás intentando asignar un camión a un `tramoId` que no existe.

**Solución:**
1. Primero asigna una ruta a la solicitud (request "2. Asignar Ruta")
2. De la respuesta, obtén los `tramoId` de los tramos creados
3. Usa esos `tramoId` reales en el request "3. Asignar Camión a Tramo"

**Nota:** Los `tramoId` NO son secuenciales (1, 2, 3...). Debes obtenerlos de la respuesta de asignar ruta.

---

### 4. Error: "Camión no encontrado" o "Dominio inválido"

**Causa:** Estás intentando usar un dominio de camión que no existe.

**Solución:**
1. Crea un camión usando `POST /api/camiones`
2. Copia el `dominio` de la respuesta
3. Usa ese `dominio` en el request de asignar camión

---

### 5. Error: "Cannot deserialize value of type `java.lang.Integer` from String"

**Causa:** Estás enviando `camionDominio` como String (ej: "ABC123") pero el servicio esperaba un Integer (esto ya está corregido).

**Solución:**
- **El `camionDominio` debe ser un String**, no un número
- Ejemplos válidos: `"ABC123"`, `"XYZ789"`, `"TEST-001"`
- Ejemplos inválidos: `123`, `456` (sin comillas)
- Si el error persiste, reinicia el servicio: `docker-compose restart solicitud-service`

---

### 6. Error: "value too long for type character varying(15)" en PUT /api/camiones

**Causa:** Estás intentando actualizar un camión con un `nombreTransportista` o `telefono` que excede el límite de 15 caracteres.

**Solución:**
- **`nombreTransportista`** tiene un límite de **15 caracteres** máximo
- **`telefono`** tiene un límite de **15 caracteres** máximo
- Usa nombres más cortos: `"J. Perez"` en lugar de `"Juan Perez Actualizado"`
- Ejemplos válidos: `"Juan Perez"` (10 chars), `"J. Perez"` (8 chars)
- Ejemplos inválidos: `"Juan Perez Actualizado"` (22 chars - demasiado largo)

---

## 📋 Orden Correcto de Pruebas

### Para Solicitud Service:

1. **Crear Solicitud** (`POST /api/solicitudes`)
   - Guarda el `numero` de la respuesta

2. **Obtener Rutas Tentativas** (`POST /api/solicitudes/rutas/tentativas`)
   - Usa el mismo body que para crear solicitud
   - Guarda una de las rutas sugeridas

3. **Asignar Ruta** (`POST /api/solicitudes/rutas/asignar`)
   - Usa el `numero` del paso 1 como `solicitudId`
   - Usa los tramos de la ruta del paso 2
   - **IMPORTANTE:** Guarda los `tramoId` de la respuesta

4. **Asignar Camión a Tramo** (`POST /api/solicitudes/tramos/asignar-camion`)
   - Usa los `tramoId` del paso 3
   - Crea un camión primero si no existe

5. **Iniciar Tramo** (`POST /api/solicitudes/tramos/{tramoId}/iniciar`)
   - Usa el `tramoId` del paso 3
   - Usa el `asignacionId` del paso 4

6. **Finalizar Tramo** (`POST /api/solicitudes/tramos/{tramoId}/finalizar`)
   - Usa el mismo `tramoId` y `asignacionId`

---

## 🔧 Scripts Útiles

### Crear todos los datos de prueba:
```powershell
.\postman\crear-datos-prueba.ps1
```

Este script crea:
- Cliente (DNI: TEST-12345)
- Contenedor (ID variable)
- Depósito (ID variable)
- Camión (Dominio variable)
- Tarifa (ID variable)

**Los IDs se muestran al final del script para copiarlos a Postman.**

---

## 💡 Tips

1. **Siempre crea datos nuevos** para cada prueba si no estás seguro de los IDs existentes
2. **Guarda los IDs** de las respuestas en variables de Postman o en un archivo de texto
3. **Usa el estado de las solicitudes** para saber si tienen ruta:
   - `borrador` = Sin ruta asignada
   - `programada` = Con ruta asignada
4. **Los tramoId NO son secuenciales** - siempre obténlos de la respuesta de asignar ruta
5. **Un contenedor puede estar en uso** - si falla DELETE, verifica que no esté asociado a una solicitud activa

---

## 🆘 Si algo no funciona

1. Verifica que todos los servicios estén corriendo:
   ```powershell
   docker-compose ps
   ```

2. Verifica los logs del servicio que falla:
   ```powershell
   docker-compose logs servicio-name --tail=50
   ```

3. Crea datos frescos:
   ```powershell
   .\postman\crear-datos-prueba.ps1
   ```

4. Usa una solicitud nueva para cada prueba de asignar ruta

