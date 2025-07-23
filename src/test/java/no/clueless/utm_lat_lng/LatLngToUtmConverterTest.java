package no.clueless.utm_lat_lng;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LatLngToUtmConverterTest {
    public static Stream<Arguments> calculateUtm() {
        return Stream.of(
                Arguments.of(new LatLng(59.139586, 9.942634), new UTMCoordinate(32, Hemisphere.Northern, 553935.9960721927, 6555976.042645933)),
                Arguments.of(new LatLng(43.642567, -79.387139), new UTMCoordinate(17, Hemisphere.Northern, 630084.3008325039, 4833438.585627057))
        );
    }

    @ParameterizedTest
    @MethodSource
    public void calculateUtm(LatLng latLng, UTMCoordinate expectedResult) {
        var result = LatLngToUtmConverter.calculateUtm(latLng);
        assertEquals(expectedResult, result);
    }
}