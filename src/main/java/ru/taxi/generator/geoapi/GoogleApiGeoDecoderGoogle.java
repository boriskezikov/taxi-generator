package ru.taxi.generator.geoapi;

import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.taxi.generator.address.AddressEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.maps.model.AddressType.STREET_ADDRESS;

@Service
@RequiredArgsConstructor
public class GoogleApiGeoDecoderGoogle extends GoogleBasicGeoConfigurator {

    public List<AddressEntity> decode(LatLng latLng, String lang) {
        try {
            initializeContext();
            List<GeocodingResult> geocodingResults = Arrays.asList(GeocodingApi.newRequest(geoApiContext)
                    .latlng(latLng)
                    .language(lang)
                    .resultType(STREET_ADDRESS)
                    .await());
            return geocodingResults.stream().map(GoogleApiRetriever::retrieveAddressEntity).collect(Collectors.toList());
        } catch (ApiException | InterruptedException | IOException e) {
            throw new GoogleApiException("Error:", e);
        } finally {
            geoApiContext.shutdown();
        }
    }
}
