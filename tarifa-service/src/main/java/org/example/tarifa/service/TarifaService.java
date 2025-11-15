package org.example.tarifa.service;

import org.example.tarifa.model.Tarifa;
import org.example.tarifa.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {
    
    @Autowired
    private TarifaRepository tarifaRepository;

    public List<Tarifa> getAllTarifas() {
        return tarifaRepository.findAll();
    }

    public Optional<Tarifa> getTarifaById(Long id) {
        return tarifaRepository.findById(id);
    }

    public Tarifa saveTarifa(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public void deleteTarifa(Long id) {
        tarifaRepository.deleteById(id);
    }

    public org.example.tarifa.dto.TarifaCalcResponse calcularCosto(org.example.tarifa.dto.TarifaCalcRequest req) {
        // Use the first tarifa available as base; otherwise fallback to defaults
        Tarifa t = tarifaRepository.findAll().stream().findFirst().orElse(null);

        double costoBaseKm = (t != null && t.getCostoBaseKm() != null) ? t.getCostoBaseKm() : 10.0; // ARS per km default
        double valorLitro = (t != null && t.getValorLitroCombustible() != null) ? t.getValorLitroCombustible() : 200.0;
        double consumoKm = (t != null && t.getConsumoPromedioKm() != null) ? t.getConsumoPromedioKm() : 0.3; // litros/km
        double costoEstadiaDiaria = (t != null && t.getCostoEstadiaDiaria() != null) ? t.getCostoEstadiaDiaria() : 1000.0;
        double cargoGestion = (t != null && t.getCargoGestion() != null) ? t.getCargoGestion() : 500.0;

        double distancia = req.getDistanciaKm();
        int dias = Math.max(0, req.getDiasEstadia());

        double costoKm = distancia * costoBaseKm;
        double costoCombustible = distancia * consumoKm * valorLitro;
        double costoEstadia = dias * costoEstadiaDiaria;

        double total = costoKm + costoCombustible + costoEstadia + cargoGestion;

        return org.example.tarifa.dto.TarifaCalcResponse.builder()
                .distanciaKm(distancia)
                .costoBaseKm(costoKm)
                .costoCombustible(costoCombustible)
                .cargoGestion(cargoGestion)
                .costoEstadia(costoEstadia)
                .costoTotal(total)
                .moneda("ARS")
                .build();
    }
}