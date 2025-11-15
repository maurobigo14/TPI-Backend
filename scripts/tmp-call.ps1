 = '{"cliente":{"dni":"20000001","nombre":"Luis","apellido":"Perez","email":"luis@example.com","telefono":"111111"},"contenedor":{"identificacion":"CONT-200","pesoKg":400,"volumenM3":25,"descripcion":"Contenedor 25m3"},"origenDireccion":"Buenos Aires","origenLat":-34.60,"origenLng":-58.38,"destinoDireccion":"Rosario","destinoLat":-32.95,"destinoLng":-60.65}';
 = Invoke-RestMethod -Uri 'http://localhost:8080/api/solicitudes' -Method Post -ContentType 'application/json' -Body ;
 | ConvertTo-Json -Depth 5
