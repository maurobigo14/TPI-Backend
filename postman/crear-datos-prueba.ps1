# Script para crear datos de prueba necesarios para probar los endpoints
# Ejecutar antes de probar las colecciones de Postman

param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Continue"
$gatewayUrl = $BaseUrl.TrimEnd("/")

Write-Host "=== Creando Datos de Prueba ===" -ForegroundColor Cyan
Write-Host ""

# 1. Crear Cliente
Write-Host "1. Creando cliente de prueba..." -ForegroundColor Yellow
$clienteBody = @{
    dni = "TEST-12345"
    nombre = "Test"
    apellido = "Usuario"
    email = "test@test.com"
    telefono = "1234567890"
} | ConvertTo-Json

try {
    $cliente = Invoke-RestMethod -Uri "$gatewayUrl/api/clientes" -Method POST -Body $clienteBody -ContentType "application/json"
    Write-Host "   [OK] Cliente creado: DNI $($cliente.dni)" -ForegroundColor Green
    $clienteDni = $cliente.dni
} catch {
    # Si ya existe, intentar obtenerlo
    try {
        $cliente = Invoke-RestMethod -Uri "$gatewayUrl/api/clientes/$clienteBody.dni" -Method GET
        Write-Host "   [OK] Cliente ya existe: DNI $($cliente.dni)" -ForegroundColor Yellow
        $clienteDni = $cliente.dni
    } catch {
        Write-Host "   [ERROR] No se pudo crear/obtener cliente: $_" -ForegroundColor Red
        $clienteDni = "TEST-12345"
    }
}

# 2. Crear Contenedor
Write-Host ""
Write-Host "2. Creando contenedor de prueba..." -ForegroundColor Yellow
$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$contenedorBody = @{
    numeroIdentificacion = "TEST-CONT-$timestamp"
    peso = 500
    volumen = 30
    estado = "NUEVO"
    clienteDni = $clienteDni
} | ConvertTo-Json

try {
    $contenedor = Invoke-RestMethod -Uri "$gatewayUrl/api/contenedores" -Method POST -Body $contenedorBody -ContentType "application/json"
    Write-Host "   [OK] Contenedor creado: ID $($contenedor.id), Identificación: $($contenedor.numeroIdentificacion)" -ForegroundColor Green
    $contenedorId = $contenedor.id
} catch {
    Write-Host "   [ERROR] No se pudo crear contenedor: $_" -ForegroundColor Red
    $contenedorId = 1
}

# 3. Crear Depósito
Write-Host ""
Write-Host "3. Creando depósito de prueba..." -ForegroundColor Yellow
$depositoBody = @{
    nombre = "Depósito Test"
    direccion = "Av. Test 123"
    ciudad = "Buenos Aires"
    costoEstadiaPorDia = 50.0
    capacidadMaxima = 1000.0
    activo = $true
} | ConvertTo-Json

try {
    $deposito = Invoke-RestMethod -Uri "$gatewayUrl/api/depositos" -Method POST -Body $depositoBody -ContentType "application/json"
    Write-Host "   [OK] Depósito creado: ID $($deposito.id), Nombre: $($deposito.nombre)" -ForegroundColor Green
    $depositoId = $deposito.id
} catch {
    Write-Host "   [ERROR] No se pudo crear depósito: $_" -ForegroundColor Red
    $depositoId = 1
}

# 4. Crear Camión
Write-Host ""
Write-Host "4. Creando camión de prueba..." -ForegroundColor Yellow
$camionBody = @{
    dominio = "TEST-CAM-$timestamp"
    nombreTransportista = "Transportista Test"
    telefono = "1234567890"
    capacidadPeso = 1000.0
    capacidadVolumen = 50.0
    disponibilidad = $true
    costos = 100.0
} | ConvertTo-Json

try {
    $camion = Invoke-RestMethod -Uri "$gatewayUrl/api/camiones" -Method POST -Body $camionBody -ContentType "application/json"
    Write-Host "   [OK] Camión creado: Dominio $($camion.dominio)" -ForegroundColor Green
    $camionDominio = $camion.dominio
} catch {
    Write-Host "   [ERROR] No se pudo crear camión: $_" -ForegroundColor Red
    $camionDominio = "TEST-CAM-001"
}

# 5. Crear Tarifa
Write-Host ""
Write-Host "5. Creando tarifa de prueba..." -ForegroundColor Yellow
$tarifaBody = @{
    descripcion = "Tarifa de Prueba"
    costoBaseKm = 10.5
    valorLitroCombustible = 850.0
    consumoPromedioKm = 8.5
    costoEstadiaDiaria = 5000.0
    cargoGestion = 15.0
} | ConvertTo-Json

try {
    $tarifa = Invoke-RestMethod -Uri "$gatewayUrl/api/tarifas" -Method POST -Body $tarifaBody -ContentType "application/json"
    Write-Host "   [OK] Tarifa creada: ID $($tarifa.id)" -ForegroundColor Green
    $tarifaId = $tarifa.id
} catch {
    Write-Host "   [ERROR] No se pudo crear tarifa: $_" -ForegroundColor Red
    $tarifaId = 1
}

# Resumen
Write-Host ""
Write-Host "=== RESUMEN DE DATOS CREADOS ===" -ForegroundColor Cyan
Write-Host "Cliente DNI: $clienteDni" -ForegroundColor Green
Write-Host "Contenedor ID: $contenedorId" -ForegroundColor Green
Write-Host "Depósito ID: $depositoId" -ForegroundColor Green
Write-Host "Camión Dominio: $camionDominio" -ForegroundColor Green
Write-Host "Tarifa ID: $tarifaId" -ForegroundColor Green
Write-Host ""
Write-Host "Ahora puedes probar los endpoints en Postman usando estos IDs." -ForegroundColor Yellow
Write-Host ""
Write-Host "Para actualizar las colecciones de Postman, usa estos valores:" -ForegroundColor Yellow
Write-Host "  - Contenedor ID: $contenedorId" -ForegroundColor Gray
Write-Host "  - Cliente DNI: $clienteDni" -ForegroundColor Gray
Write-Host "  - Depósito ID: $depositoId" -ForegroundColor Gray
Write-Host "  - Camión Dominio: $camionDominio" -ForegroundColor Gray
Write-Host "  - Tarifa ID: $tarifaId" -ForegroundColor Gray

