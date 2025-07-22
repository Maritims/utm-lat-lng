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
    public static LatLng calculateLatLng(@NotNull UTM utm) {
        // Adjust N for hemisphere.
        var adjustedNorthing   = utm.northing() - utm.hemisphere().getFalseNorthing();
        var normalizedNorthing = adjustedNorthing / WGS84.NORMALIZATION_FACTOR;

        // Adjust E to avoid having to deal with negative numbers.
        var adjustedEasting   = utm.easting() - WGS84.FALSE_EASTING;
        var normalizedEasting = adjustedEasting / WGS84.NORMALIZATION_FACTOR;

        var conformalLatitude            = normalizedNorthing - IntStream.rangeClosed(1, 3).mapToDouble(j -> WGS84.beta(j) * Math.sin(2 * j * normalizedNorthing) * Math.cosh(2 * j * normalizedEasting)).sum();
        var auxiliaryLongitudeDifference = normalizedEasting - IntStream.rangeClosed(1, 3).mapToDouble(j -> WGS84.beta(j) * Math.cos(2 * j * normalizedNorthing) * Math.sinh(2 * j * normalizedEasting)).sum();
        var rectifyingLatitude           = Math.asin(Math.sin(conformalLatitude) / Math.cosh(auxiliaryLongitudeDifference));
        var geodeticLatitude             = rectifyingLatitude + IntStream.rangeClosed(1, 3).mapToDouble(j -> WGS84.delta(j) * Math.sin(2 * j * rectifyingLatitude)).sum();
        var centralMeridianLongitude     = WGS84.calculateCentralMeridianLongitude(utm.zoneNumber());
        var geodeticLongitude            = centralMeridianLongitude + Math.toDegrees(Math.atan(Math.sinh(auxiliaryLongitudeDifference) / Math.cos(conformalLatitude)));

        var latitude                     = BigDecimal.valueOf(Math.toDegrees(geodeticLatitude)).setScale(8, RoundingMode.CEILING).doubleValue();
        var longitude                    = BigDecimal.valueOf(geodeticLongitude).setScale(8, RoundingMode.CEILING).doubleValue();

        return new LatLng(latitude, longitude);
    }
}