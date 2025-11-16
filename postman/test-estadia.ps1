# Script para probar el calculo de estadia en depositos
# Ejecutar despues de que todos los servicios esten corriendo

$baseUrl = "http://localhost:8080"

Write-Host "=== Prueba de Calculo de Estadia en Depositos ===" -ForegroundColor Cyan
Write-Host ""

# 1. Crear una solicitud nueva
Write-Host "1. Creando solicitud..." -ForegroundColor Yellow
$solicitudBody = @{
    cliente = @{
        dni = "99999999"
        nombre = "Test"
        apellido = "Estadia"
        email = "test@estadia.com"
        telefono = "1234567890"
    }
    contenedor = @{
        identificacion = "CONT-ESTADIA-001"
        pesoKg = 500
        volumenM3 = 30
        descripcion = "Contenedor para prueba de estadia"
    }
    origenDireccion = "Buenos Aires, Argentina"
    origenLat = -34.6037
    origenLng = -58.3816
    destinoDireccion = "Rosario, Argentina"
    destinoLat = -32.9442
    destinoLng = -60.6505
} | ConvertTo-Json -Depth 10

try {
    $solicitud = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes" -Method POST -Body $solicitudBody -ContentType "application/json"
    $solicitudId = $solicitud.numero
    Write-Host "   [OK] Solicitud creada: #$solicitudId" -ForegroundColor Green
} catch {
    Write-Host "   [ERROR] Error creando solicitud: $_" -ForegroundColor Red
    exit 1
}

# 2. Obtener rutas tentativas
Write-Host ""
Write-Host "2. Obteniendo rutas tentativas..." -ForegroundColor Yellow
try {
    $rutas = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes/rutas/tentativas" -Method POST -Body $solicitudBody -ContentType "application/json"
    Write-Host "   [OK] Rutas tentativas obtenidas: $($rutas.Count) opciones" -ForegroundColor Green
    
    # Buscar ruta con deposito intermedio (rutas con mas de 1 tramo o que mencionen deposito)
    $rutaConDeposito = $rutas | Where-Object { 
        ($_.descripcion -like "*deposito*" -or $_.descripcion -like "*deposito*") -or 
        ($_.tramos.Count -gt 1)
    } | Select-Object -First 1
    if (-not $rutaConDeposito) {
        Write-Host "   [WARN] No se encontro ruta con deposito, usando la primera" -ForegroundColor Yellow
        $rutaConDeposito = $rutas[0]
    } else {
        Write-Host "   [OK] Ruta con $($rutaConDeposito.tramos.Count) tramos encontrada" -ForegroundColor Green
    }
} catch {
    Write-Host "   [ERROR] Error obteniendo rutas: $_" -ForegroundColor Red
    exit 1
}

# 3. Asignar ruta (usando la ruta con deposito)
Write-Host ""
Write-Host "3. Asignando ruta con deposito..." -ForegroundColor Yellow
$asignarRutaBody = @{
    solicitudId = $solicitudId
    descripcion = $rutaConDeposito.descripcion
    tramos = $rutaConDeposito.tramos | ForEach-Object {
        @{
            numeroSecuencia = $_.numeroSecuencia
            origenDireccion = $_.origenDireccion
            origenLat = $_.origenLat
            origenLng = $_.origenLng
            destinoDireccion = $_.destinoDireccion
            destinoLat = $_.destinoLat
            destinoLng = $_.destinoLng
            distanciaKm = $_.distanciaKm
            tiempoMin = $_.tiempoMin
            costo = $_.costo
        }
    }
    distanciaTotalKm = $rutaConDeposito.distanciaTotalKm
    tiempoTotalMin = $rutaConDeposito.tiempoTotalMin
    costoTotal = $rutaConDeposito.costoTotal
} | ConvertTo-Json -Depth 10

try {
    $rutaAsignada = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes/rutas/asignar" -Method POST -Body $asignarRutaBody -ContentType "application/json"
    $rutaId = $rutaAsignada.rutaId
    Write-Host "   [OK] Ruta asignada: ID $rutaId" -ForegroundColor Green
    Write-Host "   Tramos: $($rutaAsignada.tramos.Count)" -ForegroundColor Gray
} catch {
    Write-Host "   [ERROR] Error asignando ruta: $_" -ForegroundColor Red
    exit 1
}

# 4. Crear camiones para los tramos
Write-Host ""
Write-Host "4. Creando camiones..." -ForegroundColor Yellow
$camiones = @()
$timestamp = Get-Date -Format "yyyyMMddHHmmss"
for ($i = 1; $i -le $rutaAsignada.tramos.Count; $i++) {
    $camionBody = @{
        dominio = "EST-$timestamp-$i"
        nombreTransportista = "Transportista $i"
        telefono = "123456789$i"
        capacidadPeso = 1000.0
        capacidadVolumen = 50.0
        disponibilidad = $true
        costos = 100.0
    } | ConvertTo-Json

    try {
        $camion = Invoke-RestMethod -Uri "$baseUrl/api/camiones" -Method POST -Body $camionBody -ContentType "application/json"
        $camiones += $camion
        Write-Host "   [OK] Camion creado: $($camion.dominio)" -ForegroundColor Green
    } catch {
        Write-Host "   [ERROR] Error creando camion: $_" -ForegroundColor Red
    }
}

# 5. Obtener los tramos de la ruta (necesitamos los tramoId reales)
Write-Host ""
Write-Host "5. Obteniendo tramos de la ruta..." -ForegroundColor Yellow
Write-Host "   (Necesitamos consultar la base de datos o usar el endpoint de transportista)" -ForegroundColor Gray
Write-Host "   Por ahora, asumiremos que los tramoId son secuenciales empezando desde 1" -ForegroundColor Gray
Write-Host "   En produccion, deberias obtenerlos de la respuesta de asignarRuta o de la BD" -ForegroundColor Gray

# 6. Asignar camiones a los tramos
Write-Host ""
Write-Host "6. Asignando camiones a tramos..." -ForegroundColor Yellow
$asignaciones = @()
$tramoIds = @()  # Guardaremos los tramoId que funcionen

# Intentar asignar camiones a tramos secuenciales
for ($i = 0; $i -lt $rutaAsignada.tramos.Count; $i++) {
    $tramoId = $i + 1  # Asumimos que los tramoId son secuenciales
    $transportistaDni = "DNI-TEST-$i"
    
    $asignarCamionBody = @{
        tramoId = $tramoId
        camionDominio = $camiones[$i].dominio
        transportistaDni = $transportistaDni
    } | ConvertTo-Json

    try {
        $asignacion = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes/tramos/asignar-camion" -Method POST -Body $asignarCamionBody -ContentType "application/json"
        $asignaciones += $asignacion
        $tramoIds += $asignacion.tramoId
        Write-Host "   [OK] Camion $($camiones[$i].dominio) asignado al tramo $($asignacion.tramoId) (asignacionId: $($asignacion.asignacionId))" -ForegroundColor Green
    } catch {
        $errorMsg = $_.Exception.Message
        Write-Host "   [ERROR] Error asignando camion al tramo ${tramoId}: ${errorMsg}" -ForegroundColor Red
        Write-Host "   Intentando obtener tramos desde endpoint de transportista..." -ForegroundColor Yellow
        
        # Intentar obtener tramos del transportista para ver que tramoId existen
        try {
            $tramosTransportista = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes/transportistas/$transportistaDni/tramos" -Method GET
            if ($tramosTransportista.Count -gt 0) {
                Write-Host "   [OK] Encontrados $($tramosTransportista.Count) tramos para transportista $transportistaDni" -ForegroundColor Green
                foreach ($t in $tramosTransportista) {
                    Write-Host "     - TramoId: $($t.tramoId), AsignacionId: $($t.asignacionId)" -ForegroundColor Gray
                }
            }
        } catch {
            Write-Host "   [WARN] No se pudieron obtener tramos del transportista" -ForegroundColor Yellow
        }
    }
}

# 7. Obtener tramos asignados usando el endpoint de transportista
Write-Host ""
Write-Host "7. Obteniendo tramos asignados..." -ForegroundColor Yellow
$tramosAsignados = @()
$transportistasUnicos = $asignaciones | Select-Object -ExpandProperty transportistaDni -Unique
foreach ($transportistaDni in $transportistasUnicos) {
    try {
        $tramos = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes/transportistas/$transportistaDni/tramos" -Method GET
        $tramosAsignados += $tramos
        Write-Host "   [OK] Obtenidos $($tramos.Count) tramos para transportista $transportistaDni" -ForegroundColor Green
    } catch {
        Write-Host "   [WARN] No se pudieron obtener tramos para transportista $transportistaDni" -ForegroundColor Yellow
    }
}

# 8. Mostrar resumen y proximos pasos
Write-Host ""
Write-Host "=== RESUMEN ===" -ForegroundColor Cyan
Write-Host "Solicitud ID: $solicitudId" -ForegroundColor Green
Write-Host "Ruta ID: $rutaId" -ForegroundColor Green
Write-Host "Tramos asignados: $($asignaciones.Count)" -ForegroundColor Green
Write-Host ""

if ($asignaciones.Count -gt 0) {
    Write-Host "=== PROXIMOS PASOS PARA PROBAR ESTADIA ===" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Para probar el calculo de estadia, ejecuta estos comandos en orden:" -ForegroundColor Yellow
    Write-Host ""
    
    # Mostrar comandos para cada tramo
    for ($i = 0; $i -lt $asignaciones.Count; $i++) {
        $asignacion = $asignaciones[$i]
        $asignacionId = $asignacion.asignacionId
        $tramoId = $asignacion.tramoId
        
        Write-Host "TRAMO $($i + 1):" -ForegroundColor Cyan
        Write-Host "  AsignacionId: $asignacionId" -ForegroundColor Gray
        Write-Host "  TramoId: $tramoId" -ForegroundColor Gray
        Write-Host ""
        Write-Host "  Iniciar tramo:" -ForegroundColor White
        Write-Host "  POST $baseUrl/api/solicitudes/tramos/$tramoId/iniciar" -ForegroundColor Gray
        Write-Host "  Body: { `"asignacionCamionId`": $asignacionId }" -ForegroundColor Gray
        Write-Host ""
        Write-Host "  (Esperar unos minutos...)" -ForegroundColor DarkGray
        Write-Host ""
        Write-Host "  Finalizar tramo:" -ForegroundColor White
        Write-Host "  POST $baseUrl/api/solicitudes/tramos/$tramoId/finalizar" -ForegroundColor Gray
        Write-Host "  Body: { `"asignacionCamionId`": $asignacionId }" -ForegroundColor Gray
        Write-Host ""
        
        if ($i -lt $asignaciones.Count - 1) {
            Write-Host "  [IMPORTANTE] Para probar estadia, espera varios dias entre finalizar este tramo" -ForegroundColor Yellow
            Write-Host "    y iniciar el siguiente. El sistema calculara automaticamente los dias de estadia." -ForegroundColor Yellow
            Write-Host ""
        }
    }
    
    Write-Host "Verificar resultado final:" -ForegroundColor White
    Write-Host "  GET $baseUrl/api/solicitudes/$solicitudId" -ForegroundColor Gray
    Write-Host "  El campo 'costoFinal' deberia incluir el costo de estadia en depositos" -ForegroundColor Gray
    Write-Host ""
} else {
    Write-Host "[WARN] No se pudieron asignar camiones a los tramos." -ForegroundColor Yellow
    Write-Host "  Verifica que los tramoId sean correctos consultando la base de datos." -ForegroundColor Yellow
    Write-Host ""
}
