$json = '{"dominio":1001,"nombreTransportista":"Mario Gomez","telefono":"3511111111","capacidadPeso":1000,"capacidadVolumen":60,"disponibilidad":true,"costos":200}';
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/camiones' -Method Post -ContentType 'application/json' -Body $json;
$response | ConvertTo-Json -Depth 5
