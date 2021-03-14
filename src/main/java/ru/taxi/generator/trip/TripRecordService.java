package ru.taxi.generator.trip;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripRecordService {

    private final TripRecordRepository tripRecordRepository;
    private final ModelMapper mapper;

    public void createRecord(TripRecordDTO tripRecordDTO) {
        TripRecordEntity entity = mapper.map(tripRecordDTO, TripRecordEntity.class);
        entity.setUuid(UUID.randomUUID());
        tripRecordRepository.save(entity);
        log.info("Added new record: {} ", entity.getId());
    }

    public void updateRecord(TripRecordEntity source) {
        tripRecordRepository.findById(source.getId())
                .map(record -> {
                    mapper.map(source, record);
                    return record;
                })
                .map(tripRecordRepository::save);
        log.info("Record updated: {} ", source.getId());
    }

    public void delete(BigInteger id) {
        tripRecordRepository.deleteById(id);
        log.info("Record deleted: {} ", id);
    }

    public List<TripRecordEntity> findAll() {
        return tripRecordRepository.findAll();
    }

}
