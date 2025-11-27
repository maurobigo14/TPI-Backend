# Script de Verificación Rápida - TPI Backend
# Prueba los requerimientos principales del sistema de forma rápida
# Ejecutar: .\verificar-rapido.ps1

param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Continue"
$testsPassed = 0
$testsFailed = 0
$testsTotal = 0

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  VERIFICACION RAPIDA - TPI BACKEND" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Base URL: $BaseUrl`n" -ForegroundColor Gray

function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Path,
        [object]$Body = $null,
        [string]$Description,
        [int]$ExpectedStatus = 200
    )
    
    $script:testsTotal++
    $uri = "$BaseUrl$Path"
    
    Write-Host "[TEST $script:testsTotal] $Description" -ForegroundColor Yellow
    Write-Host "  $Method $Path" -ForegroundColor Gray
    
    try {
        $params = @{
            Uri = $uri
            Method = $Method
            Headers = @{
                Accept = "application/json"
            }
            ErrorAction = "Stop"
        }
        
        if ($Body -ne $null) {
            $params.ContentType = "application/json"
            if ($Body -is [string]) {
                $params.Body = $Body
            } else {
                $params.Body = $Body | ConvertTo-Json -Depth 10
            }
        }
        
        $response = Invoke-RestMethod @params
        $statusCode = 200
        
        Write-Host "  [OK] Status: $statusCode" -ForegroundColor Green
        $script:testsPassed++
        return $true
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        if ($statusCode -eq $ExpectedStatus) {
            Write-Host "  [OK] Status esperado: $statusCode" -ForegroundColor Green
            $script:testsPassed++
            return $true
        }
        else {
            Write-Host "  [FAIL] Status: $statusCode (esperado: $ExpectedStatus)" -ForegroundColor Red
            if ($_.Exception.Response) {
                $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
                $responseBody = $reader.ReadToEnd()
                Write-Host "  Response: $responseBody" -ForegroundColor Red
            }
            $script:testsFailed++
            return $false
        }
    }
}

Write-Host "`n=== VERIFICACION DE SERVICIOS ===" -ForegroundColor Green

# 1. Verificar que los servicios estén corriendo
Write-Host "`n[1] Verificando servicios..." -ForegroundColor Cyan
Test-Endpoint -Method "GET" -Path "/api/clientes" -Description "Cliente Service - Listar clientes" -ExpectedStatus 200
Test-Endpoint -Method "GET" -Path "/api/contenedores" -Description "Contenedor Service - Listar contenedores" -ExpectedStatus 200
Test-Endpoint -Method "GET" -Path "/api/camiones" -Description "Camion Service - Listar camiones" -ExpectedStatus 200
Test-Endpoint -Method "GET" -Path "/api/depositos" -Description "Deposito Service - Listar depósitos" -ExpectedStatus 200
Test-Endpoint -Method "GET" -Path "/api/tarifas" -Description "Tarifa Service - Listar tarifas" -ExpectedStatus 200
Test-Endpoint -Method "GET" -Path "/api/rutas" -Description "Ruta Service - Listar rutas" -ExpectedStatus 200
Test-Endpoint -Method "GET" -Path "/api/solicitudes" -Description "Solicitud Service - Listar solicitudes" -ExpectedStatus 200

Write-Host "`n=== VERIFICACION DE CRUD BASICO ===" -ForegroundColor Green

# 2. CRUD Cliente
Write-Host "`n[2] CRUD Cliente..." -ForegroundColor Cyan
$clienteBody = @{
    dni = "TEST-DNI-001"
    nombre = "Test"
    apellido = "Usuario"
    email = "test@test.com"
    telefono = "1234567890"
}
$clienteCreado = Test-Endpoint -Method "POST" -Path "/api/clientes" -Body $clienteBody -Description "Crear cliente"
if ($clienteCreado) {
    Test-Endpoint -Method "GET" -Path "/api/clientes/TEST-DNI-001" -Description "Obtener cliente por DNI"
}

# 3. CRUD Contenedor
Write-Host "`n[3] CRUD Contenedor..." -ForegroundColor Cyan
$contenedorBody = @{
    numeroIdentificacion = ("TEST-CONT-" + (Get-Date -Format 'yyMMddHHmmss'))
    peso = 500
    volumen = 30
    estado = "NUEVO"
    clienteDni = "TEST-DNI-001"
}
$contenedorCreado = Test-Endpoint -Method "POST" -Path "/api/contenedores" -Body $contenedorBody -Description "Crear contenedor"
if ($contenedorCreado) {
    Test-Endpoint -Method "GET" -Path "/api/contenedores/estado/NUEVO" -Description "Obtener contenedores por estado"
}

# 4. CRUD Camion
Write-Host "`n[4] CRUD Camion..." -ForegroundColor Cyan
$camionDominio = ("TC" + (Get-Date -Format 'yyMMddHHmm'))
$camionBody = @{
    dominio = $camionDominio
    nombreTransportista = "J. Perez"
    telefono = "1234567890"
    capacidadPeso = 10000.0
    capacidadVolumen = 50.0
    disponibilidad = $true
    costos = 100.0
}
$camionCreado = Test-Endpoint -Method "POST" -Path "/api/camiones" -Body $camionBody -Description "Crear camión"
if ($camionCreado) {
    Test-Endpoint -Method "GET" -Path ("/api/camiones/" + $camionDominio) -Description "Obtener camión por dominio"
    Test-Endpoint -Method "GET" -Path "/api/camiones/disponibles" -Description "Obtener camiones disponibles"
}

# 5. CRUD Deposito
Write-Host "`n[5] CRUD Deposito..." -ForegroundColor Cyan
$depositoBody = @{
    nombre = ("Deposito Test " + (Get-Date -Format 'yyyyMMddHHmmss'))
    direccion = "Calle Test 123"
    ciudad = "Buenos Aires"
    costoEstadiaPorDia = 50.0
    capacidadMaxima = 100.0
    activo = $true
}
$depositoCreado = Test-Endpoint -Method "POST" -Path "/api/depositos" -Body $depositoBody -Description "Crear depósito"

# 6. CRUD Tarifa
Write-Host "`n[6] CRUD Tarifa..." -ForegroundColor Cyan
$tarifaBody = @{
    tipo = "ESTANDAR"
    precioBase = 1000.0
    precioPorKm = 10.0
    precioPorKg = 5.0
    precioPorM3 = 3.0
    activa = $true
}
$tarifaCreada = Test-Endpoint -Method "POST" -Path "/api/tarifas" -Body $tarifaBody -Description "Crear tarifa"
if ($tarifaCreada) {
    Test-Endpoint -Method "POST" -Path "/api/tarifas/calc" -Body @{
        distanciaKm = 100.0
        pesoKg = 500.0
        volumenM3 = 30.0
        tipo = "ESTANDAR"
    } -Description "Calcular costo de transporte"
}

Write-Host "`n=== VERIFICACION DE FLUJO DE SOLICITUDES ===" -ForegroundColor Green

# 7. Flujo completo de solicitud
Write-Host "`n[7] Flujo de Solicitud..." -ForegroundColor Cyan

# 7.1 Crear solicitud
$solicitudBody = @{
    cliente = @{
        dni = "TEST-DNI-001"
        nombre = "Test"
        apellido = "Usuario"
        email = "test@test.com"
        telefono = "1234567890"
    }
    contenedor = @{
        identificacion = "TEST-CONT-002"
        pesoKg = 500.0
        volumenM3 = 30.0
        descripcion = "Contenedor de prueba"
    }
    origenDireccion = "Buenos Aires, Argentina"
    origenLat = -34.6037
    origenLng = -58.3816
    destinoDireccion = "Rosario, Argentina"
    destinoLat = -32.9442
    destinoLng = -60.6505
}
$solicitudCreada = Test-Endpoint -Method "POST" -Path "/api/solicitudes" -Body $solicitudBody -Description "Crear solicitud de transporte"

if ($solicitudCreada) {
    # 7.2 Obtener rutas tentativas
    Test-Endpoint -Method "POST" -Path "/api/solicitudes/rutas/tentativas" -Body $solicitudBody -Description "Obtener rutas tentativas"
    
    # 7.3 Obtener solicitud por número (asumiendo que se creó con número 1 o el siguiente disponible)
    Test-Endpoint -Method "GET" -Path "/api/solicitudes" -Description "Listar solicitudes"
}

Write-Host "`n=== VERIFICACION DE ENDPOINTS ESPECIALES ===" -ForegroundColor Green

# 8. Endpoints especiales
Write-Host "`n[8] Endpoints Especiales..." -ForegroundColor Cyan
Test-Endpoint -Method "GET" -Path "/api/contenedores/pendientes/entrega" -Description "Contenedores pendientes de entrega"
Test-Endpoint -Method "GET" -Path "/api/depositos/activos" -Description "Depósitos activos"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  RESUMEN DE VERIFICACION" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Total de pruebas: $testsTotal" -ForegroundColor White
Write-Host "Exitosas: $testsPassed" -ForegroundColor Green
Write-Host "Fallidas: $testsFailed" -ForegroundColor Red
if ($testsTotal -gt 0) {
    Write-Host "Porcentaje de éxito: $([math]::Round(($testsPassed / $testsTotal) * 100, 2))%" -ForegroundColor $(if ($testsFailed -eq 0) { "Green" } else { "Yellow" })
} else {
    Write-Host "Porcentaje de éxito: N/A (sin pruebas contabilizadas)" -ForegroundColor Yellow
}

if ($testsFailed -eq 0) {
    Write-Host "`n✅ TODAS LAS PRUEBAS PASARON" -ForegroundColor Green
    exit 0
} else {
    Write-Host "`n⚠️  ALGUNAS PRUEBAS FALLARON" -ForegroundColor Yellow
    exit 1
}

