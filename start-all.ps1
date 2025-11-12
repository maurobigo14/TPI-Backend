# Start all services using docker compose (Docker Desktop required)
Set-Location -Path $PSScriptRoot
Write-Host "Building and starting containers..."
docker compose up -d --build
Write-Host "Containers started. Use 'docker compose logs -f' to view logs."