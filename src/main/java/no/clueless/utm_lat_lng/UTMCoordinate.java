package no.clueless.utm_lat_lng;

public record UTMCoordinate(int zoneNumber, Hemisphere hemisphere, double easting, double northing) {
    public UTMCoordinate {
        if (zoneNumber < 1 || zoneNumber > 60) {
            throw new IllegalArgumentException("Zone number must be between 1 and 60");
        }
    }
}
