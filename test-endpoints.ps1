# Script para probar los endpoints del TPI
# Ejecutar: .\test-endpoints.ps1

$baseUrl = "http://localhost:8080"  # API Gateway
# Si pruebas directo: $baseUrl = "http://localhost:8081" para clientes, etc.

Write-Host "=== POBLANDO DATOS INICIALES ===" -ForegroundColor Green

# 1. CREAR CLIENTE
Write-Host "`n1. Creando cliente..." -ForegroundColor Yellow
$cliente = @{
    dni = "12345678"
    nombre = "Juan"
    apellido = "Pérez"
    email = "juan.perez@example.com"
    telefono = "1234567890"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/clientes" -Method Post -ContentType "application/json" -Body $cliente
    Write-Host "✓ Cliente creado: $($response.dni)" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "✗ Error creando cliente: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host $_.Exception.Response
}

# 2. CREAR CONTENEDOR
Write-Host "`n2. Creando contenedor..." -ForegroundColor Yellow
$contenedor = @{
    numeroIdentificacion = "CONT-001"
    peso = 500
    volumen = 30
    estado = "NUEVO"
    clienteDni = "12345678"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/contenedores" -Method Post -ContentType "application/json" -Body $contenedor
    Write-Host "✓ Contenedor creado: $($response.id)" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "✗ Error creando contenedor: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. CREAR DEPÓSITO
Write-Host "`n3. Creando depósito..." -ForegroundColor Yellow
$deposito = @{
    nombre = "Depósito Central"
    direccion = "Av. Corrientes 1234"
    ciudad = "Buenos Aires"
    costoEstadiaPorDia = 50.0
    capacidadMaxima = 1000.0
    activo = $true
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/depositos" -Method Post -ContentType "application/json" -Body $deposito
    Write-Host "✓ Depósito creado: $($response.id)" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "✗ Error creando depósito: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. CREAR SOLICITUD (Punto 1)
Write-Host "`n=== PUNTO 1: CREAR SOLICITUD ===" -ForegroundColor Cyan
$solicitudRequest = @{
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

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes" -Method Post -ContentType "application/json" -Body $solicitudRequest
    Write-Host "✓ Solicitud creada: Número $($response.numero)" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json -Depth 3)
    $solicitudNumero = $response.numero
} catch {
    Write-Host "✗ Error creando solicitud: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Respuesta del servidor: $responseBody" -ForegroundColor Red
    }
}

# 5. GENERAR RUTAS TENTATIVAS (Punto 2)
Write-Host "`n=== PUNTO 2: GENERAR RUTAS TENTATIVAS ===" -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes/rutas/tentativas" -Method Post -ContentType "application/json" -Body $solicitudRequest
    Write-Host "✓ Rutas tentativas generadas: $($response.Count) rutas" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json -Depth 5)
} catch {
    Write-Host "✗ Error generando rutas: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Respuesta del servidor: $responseBody" -ForegroundColor Red
    }
}

# 6. LISTAR DATOS
Write-Host "`n=== VERIFICANDO DATOS ===" -ForegroundColor Green

Write-Host "`nClientes:" -ForegroundColor Yellow
try {
    $clientes = Invoke-RestMethod -Uri "$baseUrl/api/clientes" -Method Get
    Write-Host ($clientes | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "✗ Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nContenedores:" -ForegroundColor Yellow
try {
    $contenedores = Invoke-RestMethod -Uri "$baseUrl/api/contenedores" -Method Get
    Write-Host ($contenedores | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "✗ Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nSolicitudes:" -ForegroundColor Yellow
try {
    $solicitudes = Invoke-RestMethod -Uri "$baseUrl/api/solicitudes" -Method Get
    Write-Host ($solicitudes | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "✗ Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== FIN ===" -ForegroundColor Green

