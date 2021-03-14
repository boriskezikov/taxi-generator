package ru.taxi.generator.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TripRecordRepository extends JpaRepository<TripRecordEntity, BigInteger> {

}
