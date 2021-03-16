package ru.taxi.generator.address;

import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.taxi.generator.trip.TripRecordEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity(name = "addresses")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addresses_ids_gen")
    @SequenceGenerator(name = "addresses_ids_gen", sequenceName = "addresses_id_seq", allocationSize = 1)
    private BigInteger id;

    @Builder.Default
    @Column
    private UUID uuid = UUID.randomUUID();

    @Column
    private SimpleTimeZone timeZone;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String streetNumber;

    @Column
    private String zipCode;

    private String formattedAddress;

    @Column(nullable = false)
    private String lng;

    @Column(nullable = false)
    private String lat;

    @ManyToMany
    public List<TripRecordEntity> tripRecordEntities = new ArrayList<>();

    @Override
    public String toString() {
        return country + "," + city + "," + street + "," + streetNumber + "," + zipCode;
    }


    public LatLng getGeometry() {
        return new LatLng(Double.parseDouble(this.lat), Double.parseDouble(this.lng));
    }

}
