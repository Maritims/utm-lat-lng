package no.clueless.utm_lat_lng;

import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public class LatLngToUtmConverter {
    /**
     * Gets the UTM zone number for the given coordinates. A longitude of precisely 180 degrees belongs to both UTM zone 1 and 60.
     *
     * @param latLng The coordinates.
     * @return The UTM zone number.
     */
    static int getUtmZoneNumber(@NotNull LatLng latLng) {
        if (latLng.lat() > 84 || latLng.lat() < -84) {
            throw new IllegalArgumentException("Longitude must be between -84 and 84");
        }

        // THe four zones 31X, 33X, 35X and 37X are expanded to cover zones 31X to 37X north of the 72nd parallel. The zones 32X, 34X and 36X are not in use.
        if (latLng.lat() >= 72) {
            if (latLng.lng() >= 6 && latLng.lng() < 9) {
                return 31;
            }
            if (latLng.lng() >= 9 && latLng.lng() < 21) {
                return 33;
            }
            if (latLng.lng() >= 21 && latLng.lng() < 33) {
                return 35;
            }
            if (latLng.lng() >= 33 && latLng.lng() < 42) {
                return 37;
            }
        }

        return (int) (Math.floor((latLng.lng() + 180) / 6 % 60) + 1);
    }

    @NotNull
    public static UTM calculateUtm(@NotNull LatLng latLng) {
        var phi               = Math.toRadians(latLng.lat());
        var lambda            = Math.toRadians(latLng.lng());
        var utmZoneNumber     = getUtmZoneNumber(latLng);
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

        return new UTM(getUtmZoneNumber(latLng), hemisphere, adjustedEasting, adjustedNorthing);
    }

    // Why doesn't Java have this!?
    static double atanh(double x) {
        return 0.5 * Math.log((1 + x) / (1 - x));
    }
}
