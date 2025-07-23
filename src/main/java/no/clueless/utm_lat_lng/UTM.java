package no.clueless.utm_lat_lng;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class UTM {
    public static final char[] LATITUDE_BANDS = new char[]{'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X'};

    /**
     * Gets the UTM zone number for the given coordinates. A longitude of precisely 180 degrees belongs to both UTM zone 1 and 60.
     *
     * @param latLng The coordinates.
     * @return The UTM zone number.
     */
    public static int calculateZoneNumber(@NotNull LatLng latLng) {
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

    /**
     * Each UTM zone is divided into 19 latitude bands spanning from 80 degrees south to 72 degrees north, each spanning 8 degrees of latitude with their own individual letter code.
     * The most northern belt (X) spans 12 degrees from 72 degrees north to 84 degrees north.
     *
     * @param latitude A latitude in degrees.
     * @return The latitude band letter code.
     */
    public static char calculateLatitudeBand(double latitude) {
        if (latitude > 84 || latitude < -84) {
            throw new IllegalArgumentException("Latitude must be between -84 and 84");
        }

        // The most northern latitude band covers 12 degrees from the 72nd to the 84th parallel.
        if (latitude >= 72) {
            latitude = Math.min(latitude, 72);
        }

        return LATITUDE_BANDS[(int) ((latitude + 80) / 8)];
    }

    /**
     * Determine the hemisphere by the latitude band letter code.
     *
     * @param latitudeBand The latitude band letter code.
     * @return The {@link Hemisphere} containing the latitude band.
     */
    @NotNull
    public static Hemisphere getHemisphereByLatitudeBand(char latitudeBand) {
        if (latitudeBand < 'C' || latitudeBand > 'X') {
            throw new IllegalArgumentException("Latitude band must be between C and X");
        }

        return latitudeBand >= 'N' ? Hemisphere.Northern : Hemisphere.Southern;
    }

    private static final Pattern UTM_COORDINATE_WITH_LATITUDE_BAND_PATTERN = Pattern.compile("^(\\d{2})([CDEFGHJKLMNPQRSTUVWX])\\s(\\d*\\.?\\d*)E(\\s\\d*\\.?\\d*)N$");

    /**
     * The latitude band is sometimes included in UTM notation, which could potentially lead to ambiguity.
     * The letter "S" could refer to either the Southern Hemisphere itself or the latitude band in the Northern Hemisphere.
     * Use this method to parse a UTM coordinate string which you know is using the latitude band and not the hemisphere.
     *
     * @param utmString The UTM string with a latitude band, for instance, 32V 597292E 6643009N.
     * @return A {@link UTMCoordinate} representing the given string.
     */
    @NotNull
    public static UTMCoordinate parseWithLatitudeBand(@NotNull String utmString) {
        var matcher = UTM_COORDINATE_WITH_LATITUDE_BAND_PATTERN.matcher(utmString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid UTM coordinate with latitude band string");
        }

        var zoneNumber   = Integer.parseInt(matcher.group(1));
        var latitudeBand = matcher.group(2).charAt(0);
        var easting      = Double.parseDouble(matcher.group(3));
        var northing     = Double.parseDouble(matcher.group(4));

        return new UTMCoordinate(zoneNumber, getHemisphereByLatitudeBand(latitudeBand), easting, northing);
    }

    private static final Pattern UTM_COORDINATE_WITH_HEMISPHERE_PATTERN = Pattern.compile("^(\\d{2})([NS])\\s(\\d*\\.?\\d*)E(\\s\\d*\\.?\\d*)N$");

    /**
     * The latitude band is sometimes included in UTM notation, which could potentially lead to ambiguity.
     * The letter "S" could refer to either the Southern Hemisphere itself or the latitude band in the Northern Hemisphere.
     * Use this method to parse a UTM coordinate string which you know is using the hemisphere and not the latitude band.
     *
     * @param utmString The UTM string with hemisphere, for instance, 32N 597292E 6643009N.
     * @return A {@link UTMCoordinate} representing the given string.
     */
    @NotNull
    public static UTMCoordinate parseWithHemisphere(@NotNull String utmString) {
        var matcher = UTM_COORDINATE_WITH_HEMISPHERE_PATTERN.matcher(utmString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid UTM coordinate with hemisphere string");
        }

        var zoneNumber = Integer.parseInt(matcher.group(1));
        var hemisphere = matcher.group(2).charAt(0) == 'N' ? Hemisphere.Northern : Hemisphere.Southern;
        var easting    = Double.parseDouble(matcher.group(3));
        var northing   = Double.parseDouble(matcher.group(4));

        return new UTMCoordinate(zoneNumber, hemisphere, easting, northing);
    }
}