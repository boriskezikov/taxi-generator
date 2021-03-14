package ru.taxi.generator.geoapi;

import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleApiGeoEncoderGoogle extends GoogleBasicGeoConfigurator {

    public GeocodingResult encode(String city) {
        try {
            initializeContext();
            List<GeocodingResult> geocodingResults = Arrays.asList(GeocodingApi.newRequest(geoApiContext)
                    .address(city)
                    .await());
            if (geocodingResults.size() != 1) {
                throw new GoogleApiException("No bounds found for provided city_name: " + city + " or result is unclear." +
                        " Try to add more detailed information!");
            }
            return geocodingResults.iterator().next();
        } catch (InterruptedException | IOException | ApiException e) {
            throw new GoogleApiException("Error:", e);
        } finally {
            geoApiContext.shutdown();
        }
    }
}
