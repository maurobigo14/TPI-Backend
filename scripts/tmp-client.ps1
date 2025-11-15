$json = '{"dni":"30000000","nombre":"Carla","apellido":"Suarez","email":"carla@example.com","telefono":"999888777"}';
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/clientes' -Method Post -ContentType 'application/json' -Body $json;
$response | ConvertTo-Json -Depth 5
