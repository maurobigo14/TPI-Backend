package org.example.solicitud.client;

import org.example.solicitud.dto.tarifa.TarifaCalcRequest;
import org.example.solicitud.dto.tarifa.TarifaCalcResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tarifa-service", url = "${tarifa-service.url:http://localhost:8083}")
public interface TarifaClient {

    @PostMapping("/api/tarifas/calc")
    TarifaCalcResponse calcular(@RequestBody TarifaCalcRequest req);
}
