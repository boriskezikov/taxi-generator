package ru.taxi.generator.geoapi;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleBasicGeoConfigurator {

    @Value("${google-api.key}")
    protected String xGoogleApiKey;

    protected GeoApiContext geoApiContext;

    protected void initializeContext() {
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(xGoogleApiKey)
                .maxRetries(10)
                .build();
    }

}
