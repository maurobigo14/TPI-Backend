$json = '{"numeroIdentificacion":"CONT-900","peso":450,"volumen":28,"estado":"NUEVO","clienteDni":"30000000"}';
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/contenedores' -Method Post -ContentType 'application/json' -Body $json;
$response | ConvertTo-Json -Depth 5
