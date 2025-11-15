$json = '{"solicitudId":2,"descripcion":"Ruta directa","tramos":[{"numeroSecuencia":1,"origenDireccion":"Cordoba","origenLat":-31.42,"origenLng":-64.18,"destinoDireccion":"Mendoza","destinoLat":-32.89,"destinoLng":-68.83,"distanciaKm":467.21,"tiempoMin":467,"costo":571.0}] }';
try {
    $response = Invoke-RestMethod -Uri 'http://localhost:8085/api/solicitudes/rutas/asignar' -Method Post -ContentType 'application/json' -Body $json;
    $response | ConvertTo-Json -Depth 5
}
catch {
    Write-Host "Status: $($_.Exception.Response.StatusCode)";
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream());
    $reader.BaseStream.Position = 0;
    $reader.DiscardBufferedData();
    Write-Host ($reader.ReadToEnd());
}
