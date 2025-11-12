# Stop and remove containers created by docker compose
Set-Location -Path $PSScriptRoot
Write-Host "Stopping and removing containers..."
docker compose down
Write-Host "Stopped."