package Dino.Duett.domain.profile.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Region {
    private String address;
    private double latitude;
    private double longitude;

    public static Region of(String address, double latitude, double longitude) {
        return new Region(address, latitude, longitude);
    }
}
