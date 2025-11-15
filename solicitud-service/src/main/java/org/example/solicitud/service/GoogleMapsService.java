package org.example.solicitud.service;

import com.google.maps.GeoApiContext;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleMapsService {

    @Value("${google.maps.api-key:}")
    private String googleMapsApiKey;

    private GeoApiContext geoApiContext;

    private GeoApiContext getGeoApiContext() {
        if (geoApiContext == null && googleMapsApiKey != null && !googleMapsApiKey.isBlank()) {
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(googleMapsApiKey)
                    .build();
        }
        return geoApiContext;
    }

    /**
     * Obtiene distancia y duración entre dos puntos usando Google Maps Distance Matrix API.
     * Si la API key no está configurada o la llamada falla, se usa un fallback (Haversine).
     */
    public DistanceInfo getDistance(double originLat, double originLng,
                                    double destLat, double destLng) {
        if (googleMapsApiKey == null || googleMapsApiKey.isBlank()) {
            log.warn("Google Maps API key no configurada. Usando fallback Haversine.");
            return fallbackDistance(originLat, originLng, destLat, destLng, "api-key-missing");
        }

        try {
            LatLng origin = new LatLng(originLat, originLng);
            LatLng destination = new LatLng(destLat, destLng);

            DistanceMatrix result = DistanceMatrixApi.newRequest(getGeoApiContext())
                    .origins(origin)
                    .destinations(destination)
                    .await();

            if (result.rows.length > 0 && result.rows[0].elements.length > 0) {
                com.google.maps.model.DistanceMatrixElement element = result.rows[0].elements[0];

                if (element.distance != null && element.duration != null) {
                    double distanceKm = element.distance.inMeters / 1000.0;
                    int durationMin = (int) (element.duration.inSeconds / 60);

                    return DistanceInfo.builder()
                            .distanceKm(distanceKm)
                            .durationMinutes(durationMin)
                            .success(true)
                            .build();
                }
            }

            log.warn("No se encontró ruta válida entre ({}, {}) y ({}, {})",
                    originLat, originLng, destLat, destLng);
            return fallbackDistance(originLat, originLng, destLat, destLng, "no-route-found");

        } catch (Exception e) {
            log.error("Error llamando a Google Maps API: {}", e.getMessage());
            return fallbackDistance(originLat, originLng, destLat, destLng, e.getMessage());
        }
    }

    private DistanceInfo fallbackDistance(double originLat, double originLng,
                                          double destLat, double destLng,
                                          String reason) {
        double distanceKm = haversine(originLat, originLng, destLat, destLng);
        int durationMin = Math.max(1, (int) Math.round(distanceKm));

        return DistanceInfo.builder()
                .distanceKm(distanceKm)
                .durationMinutes(durationMin)
                .success(true)
                .errorMessage(reason)
                .build();
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(R * c * 100.0) / 100.0;
    }

    public static class DistanceInfo {
        private Double distanceKm;
        private Integer durationMinutes;
        private boolean success;
        private String errorMessage;

        public DistanceInfo(Double distanceKm, Integer durationMinutes, boolean success, String errorMessage) {
            this.distanceKm = distanceKm;
            this.durationMinutes = durationMinutes;
            this.success = success;
            this.errorMessage = errorMessage;
        }

        public Double getDistanceKm() {
            return distanceKm;
        }

        public Integer getDurationMinutes() {
            return durationMinutes;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Double distanceKm;
            private Integer durationMinutes;
            private boolean success;
            private String errorMessage;

            public Builder distanceKm(Double distanceKm) {
                this.distanceKm = distanceKm;
                return this;
            }

            public Builder durationMinutes(Integer durationMinutes) {
                this.durationMinutes = durationMinutes;
                return this;
            }

            public Builder success(boolean success) {
                this.success = success;
                return this;
            }

            public Builder errorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
                return this;
            }

            public DistanceInfo build() {
                return new DistanceInfo(distanceKm, durationMinutes, success, errorMessage);
            }
        }
    }
}
