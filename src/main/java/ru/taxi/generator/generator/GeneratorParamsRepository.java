package ru.taxi.generator.generator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface GeneratorParamsRepository extends JpaRepository<GeneratorParametersEntity, BigInteger> {
}
