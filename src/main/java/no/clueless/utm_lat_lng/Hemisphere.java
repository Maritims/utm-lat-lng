package no.clueless.utm_lat_lng;

public enum Hemisphere {
    Northern(0),
    Southern(10000000);

    private final int falseNorthing;

    Hemisphere(int falseNorthing) {
        this.falseNorthing = falseNorthing;
    }

    public int getFalseNorthing() {
        return falseNorthing;
    }
}
