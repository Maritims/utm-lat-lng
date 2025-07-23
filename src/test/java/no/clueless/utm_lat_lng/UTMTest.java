package no.clueless.utm_lat_lng;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UTMTest {
    public static Stream<Arguments> getUtmZoneNumber() {
        return IntStream.range(0, 60)
                .mapToObj(i -> {
                    var startLongitude = -180 + (i * 6);
                    var endLongitude   = -180 + ((i + 1) * 6);
                    return IntStream.range(startLongitude, endLongitude).mapToObj(lng -> Arguments.of(new LatLng(0, lng), i + 1));
                })
                .flatMap(s -> s);
    }

    public static Stream<Arguments> calculateLatitudeBand() {
        return Stream.of(
                Arguments.of(-80.0, 'C'),
                Arguments.of(-72.0, 'D'),
                Arguments.of(-64.0, 'E'),
                Arguments.of(-56.0, 'F'),
                Arguments.of(-48.0, 'G'),
                Arguments.of(-40.0, 'H'),
                Arguments.of(-32.0, 'J'),
                Arguments.of(-24.0, 'K'),
                Arguments.of(-16.0, 'L'),
                Arguments.of(-8.0, 'M'),
                Arguments.of(0.0, 'N'),
                Arguments.of(8.0, 'P'),
                Arguments.of(16.0, 'Q'),
                Arguments.of(24.0, 'R'),
                Arguments.of(32.0, 'S'),
                Arguments.of(40.0, 'T'),
                Arguments.of(48.0, 'U'),
                Arguments.of(56.0, 'V'),
                Arguments.of(64.0, 'W'),
                Arguments.of(72.0, 'X'),
                Arguments.of(73.0, 'X'),
                Arguments.of(74.0, 'X'),
                Arguments.of(75.0, 'X'),
                Arguments.of(76.0, 'X'),
                Arguments.of(77.0, 'X'),
                Arguments.of(78.0, 'X'),
                Arguments.of(79.0, 'X'),
                Arguments.of(80.0, 'X'),
                Arguments.of(81.0, 'X'),
                Arguments.of(82.0, 'X'),
                Arguments.of(83.0, 'X'),
                Arguments.of(84.0, 'X')
        );
    }

    @ParameterizedTest
    @MethodSource
    public void getUtmZoneNumber(LatLng latLng, int expectedResult) {
        var result = UTM.calculateZoneNumber(latLng);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {6, 7, 8})
    public void getUtmZoneNumber_shouldReturn31_whenLngIsBetween6And9(double lng) {
        var latLng = new LatLng(72, lng);
        assertEquals(31, UTM.calculateZoneNumber(latLng));
    }

    @ParameterizedTest
    @ValueSource(doubles = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    public void getUtmZoneNumber_shouldReturn33_whenLngIsBetween9And21(double lng) {
        var latLng = new LatLng(72, lng);
        assertEquals(33, UTM.calculateZoneNumber(latLng));
    }

    @ParameterizedTest
    @ValueSource(doubles = {21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32})
    public void getUtmZoneNumber_shouldReturn35_whenLngIsBetween21And33(double lng) {
        var latLng = new LatLng(72, lng);
        assertEquals(35, UTM.calculateZoneNumber(latLng));
    }

    @ParameterizedTest
    @ValueSource(doubles = {33, 34, 35, 36, 37, 38, 39, 40, 41})
    public void getUtmZoneNumber_shouldReturn37_whenLngIsBetween33And42(double lng) {
        var latLng = new LatLng(72, lng);
        assertEquals(37, UTM.calculateZoneNumber(latLng));
    }

    @ParameterizedTest
    @MethodSource
    public void calculateLatitudeBand(double latitude, char expectedResult) {
        var result = UTM.calculateLatitudeBand(latitude);
        assertEquals(expectedResult, result);
    }

    @Test
    public void parseWithLatitudeBand() {
        var utmCoordinate = UTM.parseWithLatitudeBand("32V 0553936E 655576N");
        assertEquals(new UTMCoordinate(32, Hemisphere.Northern, 553936.0, 655576.0), utmCoordinate);
    }

    @Test
    public void parseWithHemisphere() {
        var utmCoordinate = UTM.parseWithHemisphere("32N 0553936E 655576N");
        assertEquals(new UTMCoordinate(32, Hemisphere.Northern, 553936.0, 655576.0), utmCoordinate);
    }
}
