# Running the TPI Backend with Docker Compose

This document explains how to run the project using Docker (Docker Desktop recommended).

Prerequisites
- Docker Desktop installed and running on Windows.
- PowerShell or terminal with `docker` available.

Start the full system
```powershell
# from project root
docker compose build --no-cache
docker compose up
```

Start in background
```powershell
docker compose up -d --build
docker compose logs -f
```

Stop
```powershell
docker compose down
```

Quick checks
- Eureka UI: http://localhost:8761
- API Gateway: http://localhost:8080
- Try example: http://localhost:8080/api/clientes

Notes
- Modern Docker uses `docker compose` (no hyphen). If `docker compose` is not available, install Docker Desktop.
- If you prefer to run services individually with Maven, follow the per-service readme (not included).