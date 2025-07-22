package no.clueless.utm_lat_lng;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.IntStream;

/**
 * This implementation is based on <a href="https://en.wikipedia.org/wiki/Universal_Transverse_Mercator_coordinate_system">https://en.wikipedia.org/wiki/Universal_Transverse_Mercator_coordinate_system</a>.
 */
public class UtmToLatLngConverter {
    /**
     * Convert a {@link UTM} reference to latitude and longitude using Krüger's method from 1912.
     *
     * @param utm The UTM reference to convert.
     * @return The latitude and longitude in degrees.
     */
    @NotNull
    public LatLng calculateLatLng(@NotNull UTM utm) {
        // Adjust N for hemisphere.
        var adjustedNorthing   = utm.northing() - utm.hemisphere().getFalseNorthing();
        var normalizedNorthing = adjustedNorthing / (WGS84.POINT_SCALE_FACTOR * WGS84.RECTIFYING_RADIUS);

        // Adjust E to avoid having to deal with negative numbers.
        var adjustedEasting   = utm.easting() - WGS84.FALSE_EASTING;
        var normalizedEasting = adjustedEasting / (WGS84.POINT_SCALE_FACTOR * WGS84.RECTIFYING_RADIUS);

        var xiPrime    = normalizedNorthing - IntStream.rangeClosed(1, 3).mapToDouble(j -> WGS84.BETA[j - 1] * Math.sin(2 * j * normalizedNorthing) * Math.cosh(2 * j * normalizedEasting)).sum();
        var etaPrime   = normalizedEasting - IntStream.rangeClosed(1, 3).mapToDouble(j -> WGS84.BETA[j - 1] * Math.cos(2 * j * normalizedNorthing) * Math.sinh(2 * j * normalizedEasting)).sum();
        var chi        = Math.asin(Math.sin(xiPrime) / Math.cosh(etaPrime));
        var phi        = chi + IntStream.rangeClosed(1, 3).mapToDouble(j -> WGS84.DELTA[j - 1] * Math.sin(2 * j * chi)).sum();
        var lambdaZero = utm.zoneNumber() * 6 - 183;
        var lambda     = lambdaZero + Math.toDegrees(Math.atan(Math.sinh(etaPrime) / Math.cos(xiPrime)));
        var lat        = BigDecimal.valueOf(Math.toDegrees(phi)).setScale(8, RoundingMode.CEILING).doubleValue();
        var lng        = BigDecimal.valueOf(lambda).setScale(8, RoundingMode.CEILING).doubleValue();

        return new LatLng(lat, lng);
    }
}