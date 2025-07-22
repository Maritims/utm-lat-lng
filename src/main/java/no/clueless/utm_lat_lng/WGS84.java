package no.clueless.utm_lat_lng;

/**
 * Represents constants for the WGS 84 spatial reference system.
 */
public class WGS84 {
    public static final  double   EQUATORIAL_RADIUS  = 6378137.0;
    public static final  double   INVERSE_FLATTENING = 1.0 / 298.257223563;
    public static final  double   THIRD_FLATTENING   = INVERSE_FLATTENING / (2 - INVERSE_FLATTENING);
    public static final  double   POINT_SCALE_FACTOR = 0.9996;
    public static final  double   FALSE_EASTING      = 500000.0;
    private static final double[] ALPHA;
    private static final double[] BETA;
    private static final double[] DELTA;
    public static final  double   RECTIFYING_RADIUS;
    public static final  double   NORMALIZATION_FACTOR;

    static {
        final var n2 = Math.pow(THIRD_FLATTENING, 2);
        final var n3 = Math.pow(THIRD_FLATTENING, 3);
        final var n4 = Math.pow(THIRD_FLATTENING, 4);
        final var n6 = Math.pow(THIRD_FLATTENING, 6);
        final var n8 = Math.pow(THIRD_FLATTENING, 8);

        RECTIFYING_RADIUS    = (EQUATORIAL_RADIUS / (1 + THIRD_FLATTENING)) * (1 + (n2 / 4) + (n4 / 64) + (n6 / 256) + ((25.0 / 16384.0) * n8));
        NORMALIZATION_FACTOR = POINT_SCALE_FACTOR * RECTIFYING_RADIUS;
        ALPHA                = new double[]{
                ((1.0 / 2.0) * THIRD_FLATTENING) - ((2.0 / 3.0) * n2) + ((5.0 / 16.0) * n3),
                ((13.0 / 48.0) * n2) - ((3.0 / 5.0) * n3),
                (61.0 / 240.0) * n3
        };
        BETA                 = new double[]{
                ((1.0 / 2.0) * THIRD_FLATTENING) - ((2.0 / 3.0) * n2) + ((37.0 / 96.0) * n3),
                ((1.0 / 48.0) * n2) + ((1.0 / 15.0) * n3),
                ((17.0 / 480.0) * n3)
        };
        DELTA                = new double[]{
                (2.0 * THIRD_FLATTENING) - ((2.0 / 3.0) * n2) - (2.0 * n3),
                ((7.0 / 3.0) * n2) - ((8.0 / 5.0) * n3),
                ((56.0 / 15.0) * n3)
        };
    }

    private WGS84() {
    }

    public static double calculateCentralMeridianLongitude(int utmZoneNumber) {
        return utmZoneNumber * 6 - 183;
    }

    public static double alpha(int j) {
        if (j < 1 || j > 3) {
            throw new IllegalArgumentException("j must be between 1 and 3");
        }

        return ALPHA[j - 1];
    }

    public static double beta(int j) {
        if (j < 1 || j > 3) {
            throw new IllegalArgumentException("j must be between 1 and 3");
        }

        return BETA[j - 1];
    }

    public static double delta(int j) {
        if (j < 1 || j > 3) {
            throw new IllegalArgumentException("j must be between 1 and 3");
        }

        return DELTA[j - 1];
    }
}
