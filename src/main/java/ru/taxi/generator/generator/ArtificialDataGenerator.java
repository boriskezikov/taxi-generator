package ru.taxi.generator.generator;

import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.taxi.generator.address.AddressEntity;
import ru.taxi.generator.address.AddressRepository;
import ru.taxi.generator.geoapi.GoogleApiDistanceMatrix;
import ru.taxi.generator.geoapi.GoogleApiGeoDecoderGoogle;
import ru.taxi.generator.trip.TripRecordEntity;
import ru.taxi.generator.trip.TripRecordRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.taxi.generator.Constants.DEFAULT_PRICE_PER_MIN;


@Slf4j
@Component
@RequiredArgsConstructor
public class ArtificialDataGenerator {

    private final GoogleApiGeoDecoderGoogle googleApiGeoDecoder;
    private final GoogleApiDistanceMatrix roadRetriever;
    private final TripRecordRepository tripRecordRepository;
    private final AddressRepository addressRepository;
    private final GeneratorParamsRepository generatorParamsRepository;
    private static final int MULTIPLIER = 2;

    public void generateAddresses() {
        generatorParamsRepository.findById(BigInteger.ONE).ifPresent(gParams -> {
            if (gParams.isClean()) {
                tripRecordRepository.deleteAll();
            }
            final int days = (int) gParams.getTripsDateLeftBorder().until(gParams.getTripsDateRightBorder(), ChronoUnit.DAYS);
            List<LocalDate> dateRange = Stream.iterate(gParams.getTripsDateLeftBorder(), d -> d.plusDays(1))
                    .limit(days).collect(Collectors.toList());
            if (addressRepository.count() < 5000) {
                CompletableFuture.runAsync(() -> generateTotal(gParams, dateRange.size() * gParams.getOrdersPerDayNumber() * MULTIPLIER))
                        .join();
            }

        });
    }

    public void generateTrips() {
        generatorParamsRepository.findById(BigInteger.ONE).ifPresent(gParams -> {
            if (gParams.isClean()) {
                tripRecordRepository.deleteAll();
            }
            final int days = (int) gParams.getTripsDateLeftBorder().until(gParams.getTripsDateRightBorder(), ChronoUnit.DAYS);
            List<LocalDate> dateRange = Stream.iterate(gParams.getTripsDateLeftBorder(), d -> d.plusDays(1))
                    .limit(days).collect(Collectors.toList());
            dateRange.parallelStream().forEach(date -> {
                generateTripForDate(gParams, date);
                log.info("Date {} finished", date);
            });
        });
    }

    private void generateTotal(GeneratorParametersEntity gParams, int totalAddresses) {
        IntStream.range(0, totalAddresses).forEach(i -> {
            try {
                createAndSaveNewAddress(new AtomicReference<>(gParams));
                log.info("Address job {} finished. To do: {}", i, totalAddresses - i);
            } catch (Exception e) {
                log.error("Address generation failed with error {}", e.toString());
            }
        });
        log.info("Addresses job finished");
    }

    private boolean createAndSaveNewAddress(AtomicReference<GeneratorParametersEntity> gParams) {
        LatLng randomPoint = ArtificialDataHelper.getRandomPoint(gParams.get());
        Optional<AddressEntity> byLatAndLng = addressRepository.findByLatAndLng(String.valueOf(randomPoint.lat), String.valueOf(randomPoint.lng));
        if (byLatAndLng.isPresent()) {
            return true;
        }
        AddressEntity addressEntity = supplyAddress(randomPoint, gParams.get().getLanguage());
        if (addressEntity != null) {
            addressRepository.save(addressEntity);
            log.info("New address found. {}", addressEntity.getFormattedAddress());
            return true;
        }
        return false;
    }

    private boolean generateTripForDate(GeneratorParametersEntity gParams, LocalDate date) {
        IntStream.range(0, gParams.getOrdersPerDayNumber()).forEach(i -> generateTrip(date));
        return true;
    }

    private void generateTrip(LocalDate date) {
        try {
            Pair<AddressEntity, AddressEntity> fromTo = supplyAddresses();
            TripRecordEntity trip = supplySingleTrip(fromTo, date);
            tripRecordRepository.save(trip);
            log.info("Успешная генерация для даты {}", date);
        } catch (Exception e) {
            log.info("Не удалось сгенерировать для даты {} >>{} ", date, e.toString());
        }
    }

    private TripRecordEntity supplySingleTrip(Pair<AddressEntity, AddressEntity> fromTo, LocalDate date) {
        LocalDateTime tripBegin = LocalDateTime.of(date, ArtificialDataHelper.getTime());
//        long roadDuration = roadRetriever.findRoadDuration(fromTo.getFirst().getGeometry(), fromTo.getSecond().getGeometry());
        long roadDuration = ThreadLocalRandom.current().nextLong(1000, 5000);
        LocalDateTime tripEnd = tripBegin.plusSeconds(roadDuration);
        return TripRecordEntity.builder()
                .fromAddressEntity(fromTo.getFirst())
                .toAddressEntity(fromTo.getSecond())
                .tripBeginTime(tripBegin)
                .tripEndTime(tripEnd)
                .price(DEFAULT_PRICE_PER_MIN * roadDuration / 60)
                .build();
    }


//    private Pair<AddressEntity, AddressEntity> supplyAddresses(Pair<LatLng, LatLng> fromToPair, String lang) {
//        CompletableFuture<AddressEntity> from = CompletableFuture.supplyAsync(() -> supplyAddress(fromToPair.getFirst(), lang));
//        CompletableFuture<AddressEntity> to = CompletableFuture.supplyAsync(() -> supplyAddress(fromToPair.getSecond(), lang));
//        return Pair.of(from.join(), to.join());
//    }

    private Pair<AddressEntity, AddressEntity> supplyAddresses() {
        List<AddressEntity> random = addressRepository.findRandom();
        return Pair.of(random.get(0), random.get(1));
    }

    private AddressEntity supplyAddress(LatLng latLng, String lang) {
        List<AddressEntity> decoded = googleApiGeoDecoder.decode(latLng, lang);
        return decoded.isEmpty() ? null : decoded.stream().findFirst().get();
    }
}
