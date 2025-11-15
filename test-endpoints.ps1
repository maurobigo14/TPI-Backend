param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"
$gatewayUrl = $BaseUrl.TrimEnd("/")

function Show-ServerResponse {
    param($ErrorRecord)
    if ($null -eq $ErrorRecord.Exception) { return }
    $exception = $ErrorRecord.Exception
    if ($exception.Response -is [System.Net.WebResponse]) {
        try {
            $reader = New-Object System.IO.StreamReader($exception.Response.GetResponseStream())
            $body = $reader.ReadToEnd()
            if ($body) {
                Write-Host "Server response:" -ForegroundColor DarkYellow
                Write-Host $body
            }
        } catch {}
    }
}

function Run-Step {
    param(
        [string]$Message,
        [scriptblock]$Action
    )

    Write-Host "`n$Message" -ForegroundColor Yellow
    try {
        $result = & $Action
        Write-Host "[OK] $Message" -ForegroundColor Green
        if ($null -ne $result) {
            $json = $result | ConvertTo-Json -Depth 6
            Write-Host $json
        }
        return $result
    } catch {
        Write-Host "[FAIL] $Message" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        Show-ServerResponse $_
        throw
    }
}

function Invoke-Gateway {
    param(
        [string]$Method,
        [string]$Path,
        [object]$Body = $null
    )

    $uri = "$gatewayUrl$Path"
    $params = @{
        Uri    = $uri
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
            $params.Body = $Body | ConvertTo-Json -Depth 6
        }
    }

    return Invoke-RestMethod @params
}

Write-Host "=== DATA SEEDING ===" -ForegroundColor Cyan

$clientePayload = @{
    dni = "12345678"
    nombre = "Juan"
    apellido = "Perez"
    email = "juan.perez@example.com"
    telefono = "1234567890"
}
$cliente = Run-Step "Creando cliente" {
    Invoke-Gateway -Method Post -Path "/api/clientes" -Body $clientePayload
}

$contenedorPayload = @{
    numeroIdentificacion = "CONT-001"
    peso = 500
    volumen = 30
    estado = "NUEVO"
    clienteDni = $cliente.dni
}
$contenedor = Run-Step "Creando contenedor" {
    Invoke-Gateway -Method Post -Path "/api/contenedores" -Body $contenedorPayload
}

$depositoPayload = @{
    nombre = "Deposito Central"
    direccion = "Av. Corrientes 1234"
    ciudad = "Buenos Aires"
    costoEstadiaPorDia = 50.0
    capacidadMaxima = 1000.0
    activo = $true
}
$deposito = Run-Step "Creando deposito" {
    Invoke-Gateway -Method Post -Path "/api/depositos" -Body $depositoPayload
}

$solicitudPayload = @{
    cliente = @{
        dni = $cliente.dni
        nombre = $cliente.nombre
        apellido = $cliente.apellido
        email = $cliente.email
        telefono = $cliente.telefono
    }
    contenedor = @{
        identificacion = $contenedor.numeroIdentificacion
        pesoKg = 500.0
        volumenM3 = 30.0
        descripcion = "Contenedor estandar"
    }
    origenDireccion = "Buenos Aires, Argentina"
    origenLat = -34.6037
    origenLng = -58.3816
    destinoDireccion = "Rosario, Argentina"
    destinoLat = -32.9442
    destinoLng = -60.6505
}

Write-Host "`n=== PUNTO 1: CREAR SOLICITUD ===" -ForegroundColor Cyan
$solicitud = Run-Step "Creando solicitud" {
    Invoke-Gateway -Method Post -Path "/api/solicitudes" -Body $solicitudPayload
}
$solicitudNumero = $solicitud.numero

Write-Host "`n=== PUNTO 2: RUTAS TENTATIVAS ===" -ForegroundColor Cyan
$rutas = Run-Step "Generando rutas tentativas" {
    Invoke-Gateway -Method Post -Path "/api/solicitudes/rutas/tentativas" -Body $solicitudPayload
}

Write-Host "`n=== CONSULTAS ===" -ForegroundColor Cyan
$listadoSolicitudes = Run-Step "Listando solicitudes" {
    Invoke-Gateway -Method Get -Path "/api/solicitudes"
}

Run-Step "Obteniendo solicitud $solicitudNumero" {
    Invoke-Gateway -Method Get -Path "/api/solicitudes/$solicitudNumero"
}

Run-Step "Solicitudes del cliente $($cliente.dni)" {
    Invoke-Gateway -Method Get -Path "/api/solicitudes/cliente/$($cliente.dni)"
}

Run-Step "Solicitudes en estado borrador" {
    Invoke-Gateway -Method Get -Path "/api/solicitudes/estado/borrador"
}

Run-Step "Listando clientes" {
    Invoke-Gateway -Method Get -Path "/api/clientes"
}

Run-Step "Listando contenedores" {
    Invoke-Gateway -Method Get -Path "/api/contenedores"
}

Run-Step "Listando depositos" {
    Invoke-Gateway -Method Get -Path "/api/depositos"
}

Write-Host "`n=== FIN ===" -ForegroundColor Green

