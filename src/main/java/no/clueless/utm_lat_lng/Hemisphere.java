package no.clueless.utm_lat_lng;

public enum Hemisphere {
    Northern(0, 0, 9334080),
    Southern(10000000, 1100000, 10000000);

    private final int falseNorthing;
    private final int minimumNorthing;
    private final int maximumNorthing;

    Hemisphere(int falseNorthing, int minimumNorthing, int maximumNorthing) {
        this.falseNorthing   = falseNorthing;
        this.minimumNorthing = minimumNorthing;
        this.maximumNorthing = maximumNorthing;
    }

    public int getFalseNorthing() {
        return falseNorthing;
    }

    public void throwIfInvalid(double northing) {
        if(northing < minimumNorthing || northing > maximumNorthing) {
            throw new IllegalArgumentException("Northing must be between " + minimumNorthing + " and " + maximumNorthing + " in the " + name() + " hemisphere");
        }
    }
}
