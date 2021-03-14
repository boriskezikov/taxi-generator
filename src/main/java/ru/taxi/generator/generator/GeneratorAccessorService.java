package ru.taxi.generator.generator;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.taxi.generator.geoapi.GoogleApiGeoEncoderGoogle;
import ru.taxi.generator.geoapi.GoogleApiRetriever;


import java.math.BigInteger;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GeneratorAccessorService {

    private final GeneratorParamsRepository generatorParamsRepository;
    private final GoogleApiGeoEncoderGoogle googleApiGeoEncoder;
    private final ArtificialDataGenerator artificialDataGenerator;
    private final ModelMapper mapper;

    public GeneratorParametersEntity updateGeneratorParams(GeneratorParams generatorParams) {
        GeocodingResult encodedResult = googleApiGeoEncoder.encode(generatorParams.getCity());
        LatLng cityGeometry = GoogleApiRetriever.retrieveCityGeometry(encodedResult);
        GeneratorParametersEntity parametersEntity = mapper.map(generatorParams, GeneratorParametersEntity.class);
        parametersEntity.setLat(cityGeometry.lat);
        parametersEntity.setLng(cityGeometry.lng);
        parametersEntity.setId(BigInteger.ONE);
        parametersEntity.setClean(generatorParams.isRemovePreviouslyGenerated());
        parametersEntity.setTripsDateLeftBorder(LocalDate.parse(generatorParams.getTripsDateLeftBorder()));
        parametersEntity.setTripsDateRightBorder(LocalDate.parse(generatorParams.getTripsDateRightBorder()));
        return generatorParamsRepository.save(parametersEntity);
    }

    public GeneratorParametersEntity loadParameters() {
        return generatorParamsRepository.findAll().stream().findFirst().orElse(null);
    }

    public void generate() {
        artificialDataGenerator.generate();
    }

}
