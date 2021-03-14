package ru.taxi.generator.geoapi;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import ru.taxi.generator.address.AddressEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class GoogleApiRetriever {

    private static final List<AddressComponentType> addressComponentTypes = asList(
            AddressComponentType.STREET_NUMBER,
            AddressComponentType.ROUTE,
            AddressComponentType.LOCALITY,
            AddressComponentType.COUNTRY,
            AddressComponentType.POSTAL_CODE
    );

    public static AddressEntity retrieveAddressEntity(GeocodingResult geocodingResult) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setFormattedAddress(geocodingResult.formattedAddress);
        addressEntity.setLat(String.valueOf(geocodingResult.geometry.location.lat));
        addressEntity.setLng(String.valueOf(geocodingResult.geometry.location.lng));
        Map<AddressComponentType, String> addressMap = retrieveAddressComponents(geocodingResult.addressComponents);
        addressEntity.setCountry(addressMap.get(AddressComponentType.COUNTRY));
        addressEntity.setCity(addressMap.get(AddressComponentType.LOCALITY));
        addressEntity.setStreetNumber(addressMap.get(AddressComponentType.STREET_NUMBER));
        addressEntity.setStreet(addressMap.get(AddressComponentType.ROUTE));
        addressEntity.setZipCode(addressMap.get(AddressComponentType.POSTAL_CODE));
        return addressEntity;
    }

    public static LatLng retrieveCityGeometry(GeocodingResult geocodingResult) {
        return new LatLng(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng);
    }

    private static Map<AddressComponentType, String> retrieveAddressComponents(AddressComponent[] addressComponents) {
        return Arrays.stream(addressComponents)
                .filter(GoogleApiRetriever::listsIntersectionPredicate)
                .collect(Collectors.toMap(addressComponent -> addressComponent.types[0], addressComponent -> addressComponent.longName));
    }

    private static boolean listsIntersectionPredicate(AddressComponent addressComponent) {
        var isIntersected = Arrays.stream(addressComponent.types)
                .distinct()
                .filter(addressComponentTypes::contains)
                .count();
        return isIntersected != 0;
    }
}
