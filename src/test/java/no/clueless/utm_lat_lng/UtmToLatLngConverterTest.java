package no.clueless.utm_lat_lng;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtmToLatLngConverterTest {
    public static Stream<Arguments> calculateLatLng() {
        return Stream.of(
                Arguments.of(new UTM(32, Hemisphere.Northern, 553936.0, 6555976.0), new LatLng(59.13958562, 9.94263406)),
                Arguments.of(new UTM(17, Hemisphere.Northern, 630084.0, 4833438.0), new LatLng(43.64256178, -79.38714286))
        );
    }

    @ParameterizedTest
    @MethodSource
    void calculateLatLng(UTM utm, LatLng expectedResult) {
        var latLng = UtmToLatLngConverter.calculateLatLng(utm);
        assertEquals(expectedResult, latLng);
    }
}