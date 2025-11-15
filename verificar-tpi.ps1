# Script completo para verificar el TPI - Backend de Aplicaciones 2025
# Ejecutar: .\verificar-tpi.ps1

param(
    [switch]$SoloPruebas,
    [string]$BaseUrl = "http://localhost:8080"
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  VERIFICACION TPI - BACKEND 2025" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

function Wait-ForService {
    param(
        [string]$Url,
        [string]$ServiceName,
        [int]$MaxAttempts = 30,
        [int]$DelaySeconds = 2
    )
    
    Write-Host "`nEsperando que $ServiceName este listo..." -ForegroundColor Yellow
    for ($i = 1; $i -le $MaxAttempts; $i++) {
        try {
            $response = Invoke-WebRequest -Uri $Url -Method Get -TimeoutSec 2 -ErrorAction SilentlyContinue
            if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 404) {
                Write-Host "[OK] $ServiceName esta listo" -ForegroundColor Green
                return $true
            }
        } catch {
            Write-Host "." -NoNewline -ForegroundColor Gray
            Start-Sleep -Seconds $DelaySeconds
        }
    }
    Write-Host "`n[ERROR] $ServiceName no respondio" -ForegroundColor Red
    return $false
}

if (-not $SoloPruebas) {
    Write-Host "`n=== PASO 1: LEVANTANDO SERVICIOS ===" -ForegroundColor Green
    
    $dockerComposePath = Join-Path $PSScriptRoot "docker-compose.yml"
    
    if (-not (Test-Path $dockerComposePath)) {
        Write-Host "[ERROR] No se encontro docker-compose.yml" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "Deteniendo servicios anteriores..." -ForegroundColor Yellow
    docker-compose -f $dockerComposePath down 2>&1 | Out-Null
    
    Write-Host "Levantando servicios..." -ForegroundColor Yellow
    docker-compose -f $dockerComposePath up -d
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Error al levantar servicios" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "Esperando 30 segundos para que los servicios inicien completamente..." -ForegroundColor Yellow
    Start-Sleep -Seconds 30
    
    Write-Host "`nVerificando servicios..." -ForegroundColor Yellow
    Wait-ForService -Url "http://localhost:8761" -ServiceName "Eureka Server"
    Wait-ForService -Url "$BaseUrl/api/clientes" -ServiceName "API Gateway"
    Wait-ForService -Url "$BaseUrl/api/solicitudes" -ServiceName "Solicitud Service"
}

Write-Host "`n=== PASO 2: POBLANDO DATOS INICIALES ===" -ForegroundColor Green

Write-Host "`n2.1. Creando cliente..." -ForegroundColor Yellow
$cliente = @{
    dni = "12345678"
    nombre = "Juan"
    apellido = "Perez"
    email = "juan.perez@example.com"
    telefono = "1234567890"
} | ConvertTo-Json

try {
    $clienteResponse = Invoke-RestMethod -Uri "$BaseUrl/api/clientes" -Method Post -ContentType "application/json" -Body $cliente -ErrorAction Stop
    Write-Host "[OK] Cliente creado: DNI $($clienteResponse.dni)" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Error creando cliente: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "  Detalle: $responseBody" -ForegroundColor Red
    }
    exit 1
}

Write-Host "`n2.2. Creando deposito..." -ForegroundColor Yellow
$deposito = @{
    nombre = "Deposito Central Buenos Aires"
    direccion = "Av. Corrientes 1234"
    ciudad = "Buenos Aires"
    costoEstadiaPorDia = 50.0
    capacidadMaxima = 1000.0
    activo = $true
} | ConvertTo-Json

try {
    $depositoResponse = Invoke-RestMethod -Uri "$BaseUrl/api/depositos" -Method Post -ContentType "application/json" -Body $deposito -ErrorAction Stop
    Write-Host "[OK] Deposito creado: $($depositoResponse.nombre)" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Error creando deposito: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n2.3. Creando contenedor..." -ForegroundColor Yellow
$contenedor = @{
    numeroIdentificacion = "CONT-001"
    peso = 500
    volumen = 30
    estado = "NUEVO"
    clienteDni = "12345678"
} | ConvertTo-Json

try {
    $contenedorResponse = Invoke-RestMethod -Uri "$BaseUrl/api/contenedores" -Method Post -ContentType "application/json" -Body $contenedor -ErrorAction Stop
    Write-Host "[OK] Contenedor creado: $($contenedorResponse.numeroIdentificacion)" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Error creando contenedor: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== PUNTO 1: REGISTRAR SOLICITUD DE TRANSPORTE ===" -ForegroundColor Cyan
Write-Host "Requerimiento: Registrar una nueva solicitud de transporte de contenedor (Cliente)" -ForegroundColor Gray

$solicitudRequest = @{
    cliente = @{
        dni = "12345678"
        nombre = "Juan"
        apellido = "Perez"
        email = "juan.perez@example.com"
        telefono = "1234567890"
    }
    contenedor = @{
        identificacion = "CONT-001"
        pesoKg = 500.0
        volumenM3 = 30.0
        descripcion = "Contenedor estandar para construccion"
    }
    origenDireccion = "Buenos Aires, Argentina"
    origenLat = -34.6037
    origenLng = -58.3816
    destinoDireccion = "Rosario, Argentina"
    destinoLat = -32.9442
    destinoLng = -60.6505
} | ConvertTo-Json -Depth 3

Write-Host "`nEnviando solicitud..." -ForegroundColor Yellow
Write-Host "POST $BaseUrl/api/solicitudes" -ForegroundColor Gray

try {
    $solicitudResponse = Invoke-RestMethod -Uri "$BaseUrl/api/solicitudes" -Method Post -ContentType "application/json" -Body $solicitudRequest -ErrorAction Stop
    Write-Host "[OK] Solicitud creada exitosamente!" -ForegroundColor Green
    Write-Host "  Numero de solicitud: $($solicitudResponse.numero)" -ForegroundColor White
    Write-Host "  Estado: $($solicitudResponse.estado)" -ForegroundColor White
    if ($solicitudResponse.costoEstimado) {
        Write-Host "  Costo estimado: `$$($solicitudResponse.costoEstimado)" -ForegroundColor White
    }
    if ($solicitudResponse.tiempoEstimado) {
        Write-Host "  Tiempo estimado: $($solicitudResponse.tiempoEstimado) horas" -ForegroundColor White
    }
    $solicitudNumero = $solicitudResponse.numero
} catch {
    Write-Host "[ERROR] ERROR al crear solicitud: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "  Respuesta del servidor:" -ForegroundColor Red
        Write-Host "  $responseBody" -ForegroundColor Red
    }
    $solicitudNumero = $null
}

Write-Host "`n=== PUNTO 2: CONSULTAR RUTAS TENTATIVAS ===" -ForegroundColor Cyan
Write-Host "Requerimiento: Consultar rutas tentativas con todos los tramos sugeridos" -ForegroundColor Gray

Write-Host "`nGenerando rutas tentativas..." -ForegroundColor Yellow
Write-Host "POST $BaseUrl/api/solicitudes/rutas/tentativas" -ForegroundColor Gray

try {
    $rutasResponse = Invoke-RestMethod -Uri "$BaseUrl/api/solicitudes/rutas/tentativas" -Method Post -ContentType "application/json" -Body $solicitudRequest -ErrorAction Stop
    Write-Host "[OK] Rutas tentativas generadas exitosamente!" -ForegroundColor Green
    Write-Host "  Cantidad de rutas: $($rutasResponse.Count)" -ForegroundColor White
    
    if ($rutasResponse.Count -gt 0) {
        Write-Host "`nDetalle de rutas:" -ForegroundColor Yellow
        for ($i = 0; $i -lt $rutasResponse.Count; $i++) {
            $ruta = $rutasResponse[$i]
            Write-Host "`n  Ruta $($i + 1):" -ForegroundColor Cyan
            Write-Host "    Cantidad de tramos: $($ruta.cantidadTramos)" -ForegroundColor White
            if ($ruta.costoEstimado) {
                Write-Host "    Costo estimado: `$$($ruta.costoEstimado)" -ForegroundColor White
            }
            if ($ruta.tiempoEstimado) {
                Write-Host "    Tiempo estimado: $($ruta.tiempoEstimado) horas" -ForegroundColor White
            }
            if ($ruta.tramos) {
                Write-Host "    Tramos:" -ForegroundColor White
                foreach ($tramo in $ruta.tramos) {
                    Write-Host "      - $($tramo.origen) -> $($tramo.destino)" -ForegroundColor Gray
                }
            }
        }
    } else {
        Write-Host "  [ADVERTENCIA] No se generaron rutas tentativas" -ForegroundColor Yellow
    }
} catch {
    Write-Host "[ERROR] ERROR al generar rutas tentativas: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "  Respuesta del servidor:" -ForegroundColor Red
        Write-Host "  $responseBody" -ForegroundColor Red
    }
}

Write-Host "`n=== VERIFICANDO CONSULTAS ===" -ForegroundColor Green

Write-Host "`n5.1. Listando todas las solicitudes..." -ForegroundColor Yellow
try {
    $solicitudes = Invoke-RestMethod -Uri "$BaseUrl/api/solicitudes" -Method Get -ErrorAction Stop
    Write-Host "[OK] Total de solicitudes: $($solicitudes.Count)" -ForegroundColor Green
    foreach ($sol in $solicitudes) {
        Write-Host "  - Solicitud #$($sol.numero): Estado $($sol.estado)" -ForegroundColor White
    }
} catch {
    Write-Host "[ERROR] Error: $($_.Exception.Message)" -ForegroundColor Red
}

if ($solicitudNumero) {
    Write-Host "`n5.2. Consultando solicitud #$solicitudNumero..." -ForegroundColor Yellow
    try {
        $solicitudDetalle = Invoke-RestMethod -Uri "$BaseUrl/api/solicitudes/$solicitudNumero" -Method Get -ErrorAction Stop
        Write-Host "[OK] Solicitud encontrada" -ForegroundColor Green
        Write-Host "  Cliente: $($solicitudDetalle.cliente.nombre) $($solicitudDetalle.cliente.apellido)" -ForegroundColor White
        Write-Host "  Origen: $($solicitudDetalle.origenDireccion)" -ForegroundColor White
        Write-Host "  Destino: $($solicitudDetalle.destinoDireccion)" -ForegroundColor White
    } catch {
        Write-Host "[ERROR] Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n5.3. Consultando solicitudes del cliente 12345678..." -ForegroundColor Yellow
try {
    $solicitudesCliente = Invoke-RestMethod -Uri "$BaseUrl/api/solicitudes/cliente/12345678" -Method Get -ErrorAction Stop
    Write-Host "[OK] Solicitudes del cliente: $($solicitudesCliente.Count)" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  RESUMEN DE VERIFICACION" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "`nPunto 1 (Registrar solicitud): " -NoNewline
if ($solicitudNumero) {
    Write-Host "COMPLETADO" -ForegroundColor Green
} else {
    Write-Host "FALLO" -ForegroundColor Red
}

Write-Host "Punto 2 (Rutas tentativas): " -NoNewline
if ($rutasResponse -and $rutasResponse.Count -gt 0) {
    Write-Host "COMPLETADO" -ForegroundColor Green
} else {
    Write-Host "FALLO" -ForegroundColor Red
}

Write-Host "`nPara mas pruebas, ejecuta: .\test-endpoints.ps1" -ForegroundColor Yellow
Write-Host "`n========================================" -ForegroundColor Cyan
