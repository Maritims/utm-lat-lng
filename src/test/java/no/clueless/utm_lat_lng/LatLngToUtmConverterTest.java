package no.clueless.utm_lat_lng;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LatLngToUtmConverterTest {
    public static Stream<Arguments> getUtmZoneNumber() {
        return IntStream.range(0, 60)
                .mapToObj(i -> {
                    var startLongitude = -180 + (i * 6);
                    var endLongitude   = -180 + ((i + 1) * 6);
                    return IntStream.range(startLongitude, endLongitude).mapToObj(lng -> Arguments.of(new LatLng(0, lng), i + 1));
                })
                .flatMap(s -> s);
    }

    public static Stream<Arguments> calculateUtm() {
        return Stream.of(
                Arguments.of(new LatLng(59.139586, 9.942634), new UTM(32, Hemisphere.Northern, 553935.9960721927, 6555976.042645933)),
                Arguments.of(new LatLng(43.642567, -79.387139), new UTM(17, Hemisphere.Northern, 630084.3008325039, 4833438.585627057))
        );
    }

    @ParameterizedTest
    @MethodSource
    public void getUtmZoneNumber(LatLng latLng, int expectedResult) {
        var result = LatLngToUtmConverter.getUtmZoneNumber(latLng);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {6, 7, 8})
    public void getUtmZoneNumber_shouldReturn31_whenLngIsBetween6And9(double lng) {
        var latLng = new LatLng(72, lng);
        assertEquals(31, LatLngToUtmConverter.getUtmZoneNumber(latLng));
    }

    @ParameterizedTest
    @ValueSource(doubles = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    public void getUtmZoneNumber_shouldReturn33_whenLngIsBetween9And21(double lng) {
        var latLng = new LatLng(72, lng);
        assertEquals(33, LatLngToUtmConverter.getUtmZoneNumber(latLng));
    }

    @ParameterizedTest
    @ValueSource(doubles = {21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32})
    public void getUtmZoneNumber_shouldReturn35_whenLngIsBetween21And33(double lng) {
        var latLng = new LatLng(72, lng);
        assertEquals(35, LatLngToUtmConverter.getUtmZoneNumber(latLng));
    }

    @ParameterizedTest
    @ValueSource(doubles = {33, 34, 35, 36, 37, 38, 39, 40, 41})
    public void getUtmZoneNumber_shouldReturn37_whenLngIsBetween33And42(double lng) {
        var latLng = new LatLng(72, lng);
        assertEquals(37, LatLngToUtmConverter.getUtmZoneNumber(latLng));
    }

    @ParameterizedTest
    @MethodSource
    public void calculateUtm(LatLng latLng, UTM expectedResult) {
        var result = LatLngToUtmConverter.calculateUtm(latLng);
        assertEquals(expectedResult, result);
    }
}