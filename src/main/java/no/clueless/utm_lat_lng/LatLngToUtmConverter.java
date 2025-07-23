package no.clueless.utm_lat_lng;

import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

import static no.clueless.utm_lat_lng.UTM.calculateZoneNumber;

public class LatLngToUtmConverter {
    @NotNull
    public static UTMCoordinate calculateUtm(@NotNull LatLng latLng) {
        var phi               = Math.toRadians(latLng.lat());
        var lambda            = Math.toRadians(latLng.lng());
        var utmZoneNumber     = calculateZoneNumber(latLng);
        var lambdaZero        = Math.toRadians(WGS84.calculateCentralMeridianLongitude(utmZoneNumber));
        var sinPhi            = Math.sin(phi);
        var coefficient       = (2.0 * Math.sqrt(WGS84.THIRD_FLATTENING)) / (1 + WGS84.THIRD_FLATTENING);
        var term1             = atanh(sinPhi);
        var term2             = atanh(coefficient * sinPhi);
        var conformalLatitude = Math.sinh(term1 - (coefficient * term2));

        var hemisphere                   = phi < 0 ? Hemisphere.Southern : Hemisphere.Northern;
        var transverseConformalLongitude = Math.atan(conformalLatitude / Math.cos(lambda - lambdaZero));
        var transverseConformalLatitude  = atanh(Math.sin(lambda - lambdaZero) / Math.sqrt(1 + Math.pow(conformalLatitude, 2)));
        var normalizedEasting            = WGS84.NORMALIZATION_FACTOR * (transverseConformalLatitude + IntStream.rangeClosed(1, 3).mapToDouble(j -> WGS84.alpha(j) * Math.cos(2 * j * transverseConformalLongitude) * Math.sinh(2 * j * transverseConformalLatitude)).sum());
        var adjustedEasting              = WGS84.FALSE_EASTING + normalizedEasting;
        var normalizedNorthing           = WGS84.NORMALIZATION_FACTOR * (transverseConformalLongitude + IntStream.rangeClosed(1, 3).mapToDouble(j -> WGS84.alpha(j) * Math.sin(2 * j * transverseConformalLongitude) * Math.cosh(2 * j * transverseConformalLatitude)).sum());
        var adjustedNorthing             = hemisphere.getFalseNorthing() + normalizedNorthing;

        return new UTMCoordinate(calculateZoneNumber(latLng), hemisphere, adjustedEasting, adjustedNorthing);
    }

    // Why doesn't Java have this!?
    static double atanh(double x) {
        return 0.5 * Math.log((1 + x) / (1 - x));
    }
}
