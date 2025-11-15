$json = '{"nombre":"Deposito Norte","direccion":"Av. Colon 123","ciudad":"Cordoba","costoEstadiaPorDia":60,"capacidadMaxima":800,"activo":true}';
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/depositos' -Method Post -ContentType 'application/json' -Body $json;
$response | ConvertTo-Json -Depth 5
