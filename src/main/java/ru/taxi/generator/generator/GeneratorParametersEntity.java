package ru.taxi.generator.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "generatorParams")
public class GeneratorParametersEntity {

    @Id
    private BigInteger id;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = false)
    private int rad;

    @Column(nullable = false)
    private int ordersPerDayNumber;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalDate tripsDateLeftBorder;

    @Column(nullable = false)
    private LocalDate tripsDateRightBorder;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String predictorUrl;

    @Column
    private boolean clean;

}
