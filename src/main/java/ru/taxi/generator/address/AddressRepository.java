package ru.taxi.generator.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, BigInteger> {


    Optional<AddressEntity> findByLatAndLng(String lat, String lng);

    @Query(nativeQuery=true, value="SELECT *  FROM addresses ORDER BY random() LIMIT 2")
    List<AddressEntity> findRandom();
}
