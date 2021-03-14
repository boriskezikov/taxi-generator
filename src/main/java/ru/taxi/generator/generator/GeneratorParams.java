package ru.taxi.generator.generator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class GeneratorParams {

    @NotEmpty
    private String city;
    @NotNull
    private Integer rad;
    @NotNull
    private Integer ordersPerDayNumber;
    @NotEmpty
    private String language;
    @NotEmpty
    private String predictorUrl;
    @NotNull
    private String tripsDateLeftBorder;
    @NotNull
    private String tripsDateRightBorder;

    private boolean removePreviouslyGenerated;


}
