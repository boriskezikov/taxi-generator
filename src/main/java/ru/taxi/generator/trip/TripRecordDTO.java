package ru.taxi.generator.trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripRecordDTO {

    private UUID fromAddress;

    private UUID toAddress;

    private LocalDateTime tripBeginTime;

    private LocalDateTime tripEndTime;

    private Double price;

    private BigInteger id;
}
