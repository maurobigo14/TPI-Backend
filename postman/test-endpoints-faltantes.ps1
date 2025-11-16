# Script para probar los endpoints faltantes de los servicios
# Ejecutar después de que todos los servicios estén corriendo

param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Continue"
$gatewayUrl = $BaseUrl.TrimEnd("/")

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Path,
        [object]$Body = $null,
        [bool]$ExpectedSuccess = $true
    )

    Write-Host "`n[$Method] $Name" -ForegroundColor Yellow
    Write-Host "  Path: $Path" -ForegroundColor Gray
    
    try {
        $uri = "$gatewayUrl$Path"
        $params = @{
            Uri = $uri
            Method = $Method
            Headers = @{
                Accept = "application/json"
            }
        }

        if ($Body -ne $null) {
            $params.ContentType = "application/json"
            if ($Body -is [string]) {
                $params.Body = $Body
            } else {
                $params.Body = $Body | ConvertTo-Json -Depth 10
            }
        }

        $response = Invoke-RestMethod @params -ErrorAction Stop
        Write-Host "  [OK] Éxito" -ForegroundColor Green
        if ($response) {
            $json = $response | ConvertTo-Json -Depth 3
            Write-Host "  Response: $json" -ForegroundColor Gray
        }
        return $true
    } catch {
        if ($ExpectedSuccess) {
            Write-Host "  [ERROR] Falló: $($_.Exception.Message)" -ForegroundColor Red
            if ($_.Exception.Response) {
                $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
                $body = $reader.ReadToEnd()
                Write-Host "  Server response: $body" -ForegroundColor DarkYellow
            }
            return $false
        } else {
            Write-Host "  [OK] Error esperado (404, etc.)" -ForegroundColor Green
            return $true
        }
    }
}

Write-Host "=== PRUEBA DE ENDPOINTS FALTANTES ===" -ForegroundColor Cyan
Write-Host "Base URL: $gatewayUrl" -ForegroundColor Gray
Write-Host ""

# ============================================
# SOLICITUD SERVICE
# ============================================
Write-Host "=== SOLICITUD SERVICE ===" -ForegroundColor Cyan

# Primero necesitamos crear datos de prueba
Write-Host "`nPreparando datos de prueba..." -ForegroundColor Yellow

# Crear cliente
$clienteBody = @{
    dni = "TEST-12345"
    nombre = "Test"
    apellido = "Usuario"
    email = "test@test.com"
    telefono = "1234567890"
}
$cliente = Test-Endpoint "Crear Cliente (preparación)" "POST" "/api/clientes" $clienteBody

# Crear contenedor
$contenedorBody = @{
    numeroIdentificacion = "TEST-CONT-001"
    peso = 500
    volumen = 30
    estado = "NUEVO"
    clienteDni = "TEST-12345"
}
$contenedor = Test-Endpoint "Crear Contenedor (preparación)" "POST" "/api/contenedores" $contenedorBody

# Crear solicitud
$solicitudBody = @{
    cliente = @{
        dni = "TEST-12345"
        nombre = "Test"
        apellido = "Usuario"
        email = "test@test.com"
        telefono = "1234567890"
    }
    contenedor = @{
        identificacion = "TEST-CONT-001"
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
$solicitud = Test-Endpoint "Crear Solicitud (preparación)" "POST" "/api/solicitudes" $solicitudBody

# Ahora probamos los endpoints faltantes
Test-Endpoint "Rutas Tentativas" "POST" "/api/solicitudes/rutas/tentativas" $solicitudBody

# Para asignar ruta necesitamos obtener las rutas tentativas primero
$rutasTentativas = Invoke-RestMethod -Uri "$gatewayUrl/api/solicitudes/rutas/tentativas" -Method POST -Body ($solicitudBody | ConvertTo-Json -Depth 10) -ContentType "application/json" -ErrorAction SilentlyContinue
if ($rutasTentativas -and $rutasTentativas.Count -gt 0) {
    $ruta = $rutasTentativas[0]
    $asignarRutaBody = @{
        solicitudId = 1
        descripcion = $ruta.descripcion
        tramos = $ruta.tramos | ForEach-Object {
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
        distanciaTotalKm = $ruta.distanciaTotalKm
        tiempoTotalMin = $ruta.tiempoTotalMin
        costoTotal = $ruta.costoTotal
    }
    Test-Endpoint "Asignar Ruta" "POST" "/api/solicitudes/rutas/asignar" $asignarRutaBody
}

# Crear camión para asignar
$camionBody = @{
    dominio = "TEST-CAM-001"
    nombreTransportista = "Transportista Test"
    telefono = "1234567890"
    capacidadPeso = 1000.0
    capacidadVolumen = 50.0
    disponibilidad = $true
    costos = 100.0
}
$camion = Test-Endpoint "Crear Camión (preparación)" "POST" "/api/camiones" $camionBody

Test-Endpoint "Asignar Camión a Tramo" "POST" "/api/solicitudes/tramos/asignar-camion" @{
    tramoId = 1
    camionDominio = "TEST-CAM-001"
    transportistaDni = "DNI-TEST-001"
}

Test-Endpoint "Iniciar Tramo" "POST" "/api/solicitudes/tramos/1/iniciar" @{
    asignacionCamionId = 1
    observaciones = "Iniciando tramo de prueba"
}

Test-Endpoint "Finalizar Tramo" "POST" "/api/solicitudes/tramos/1/finalizar" @{
    asignacionCamionId = 1
    observaciones = "Tramo finalizado"
}

Test-Endpoint "Obtener Tramos de Transportista" "GET" "/api/solicitudes/transportistas/DNI-TEST-001/tramos"

# ============================================
# CLIENTE SERVICE
# ============================================
Write-Host "`n=== CLIENTE SERVICE ===" -ForegroundColor Cyan

Test-Endpoint "Obtener Cliente por DNI" "GET" "/api/clientes/TEST-12345"

Test-Endpoint "Actualizar Cliente" "PUT" "/api/clientes/TEST-12345" @{
    dni = "TEST-12345"
    nombre = "Test Actualizado"
    apellido = "Usuario"
    email = "test.updated@test.com"
    telefono = "1234567890"
}

# No eliminamos el cliente porque lo necesitamos para otras pruebas
# Test-Endpoint "Eliminar Cliente" "DELETE" "/api/clientes/TEST-12345" $null $false

# ============================================
# CONTENEDOR SERVICE
# ============================================
Write-Host "`n=== CONTENEDOR SERVICE ===" -ForegroundColor Cyan

Test-Endpoint "Obtener Contenedor por ID" "GET" "/api/contenedores/1"

Test-Endpoint "Obtener Contenedores por Cliente" "GET" "/api/contenedores/cliente/TEST-12345"

Test-Endpoint "Obtener Contenedores por Estado" "GET" "/api/contenedores/estado/NUEVO"

Test-Endpoint "Actualizar Contenedor" "PUT" "/api/contenedores/1" @{
    numeroIdentificacion = "TEST-CONT-001"
    peso = 600
    volumen = 35
    estado = "EN_TRANSITO"
    clienteDni = "TEST-12345"
}

# No eliminamos el contenedor porque puede estar en uso
# Test-Endpoint "Eliminar Contenedor" "DELETE" "/api/contenedores/1" $null $false

# ============================================
# DEPOSITO SERVICE
# ============================================
Write-Host "`n=== DEPOSITO SERVICE ===" -ForegroundColor Cyan

Test-Endpoint "Listar Depósitos Activos" "GET" "/api/depositos/activos"

Test-Endpoint "Obtener Depósitos por Ciudad" "GET" "/api/depositos/ciudad/Buenos Aires"

Test-Endpoint "Crear Depósito" "POST" "/api/depositos" @{
    nombre = "Depósito Test"
    direccion = "Av. Test 123"
    ciudad = "Buenos Aires"
    costoEstadiaPorDia = 50.0
    capacidadMaxima = 1000.0
    activo = $true
}

Test-Endpoint "Actualizar Depósito" "PUT" "/api/depositos/1" @{
    nombre = "Depósito Test Actualizado"
    direccion = "Av. Test 123"
    ciudad = "Buenos Aires"
    costoEstadiaPorDia = 55.0
    capacidadMaxima = 1200.0
    activo = $true
}

# No eliminamos el depósito porque puede estar en uso
# Test-Endpoint "Eliminar Depósito" "DELETE" "/api/depositos/1" $null $false

Write-Host "`n=== FIN DE PRUEBAS ===" -ForegroundColor Green
Write-Host "Revisa los resultados arriba para ver qué endpoints funcionan correctamente." -ForegroundColor Yellow

