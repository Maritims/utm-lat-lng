# utm-lat-lng

[![Java CI with Maven](https://github.com/Maritims/utm-lat-lng/actions/workflows/maven.yml/badge.svg)](https://github.com/Maritims/utm-lat-lng/actions/workflows/maven.yml)
[![Maven Package](https://github.com/Maritims/utm-lat-lng/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/Maritims/utm-lat-lng/actions/workflows/maven-publish.yml)

Convert UTM to latitude and longitude and vice versa based on the Thompson-Lee series, a variant of Gauss-Krüger.

## Examples
### Convert latitude and longitude to UTM

```java
import no.clueless.utm_lat_lng.LatLng;
import no.clueless.utm_lat_lng.LatLngToUtmConverter;
import no.clueless.utm_lat_lng.UTMCoordinate;

LatLng latLng = new LatLng(59.13958562, 9.94263406);
UTMCoordinate utmCoordinate = LatLngToUtmConverter.calculateUtm(latLng);
```

### Convert UTM to latitude and longitude

```java
import no.clueless.utm_lat_lng.Hemisphere;
import no.clueless.utm_lat_lng.LatLng;
import no.clueless.utm_lat_lng.UTMCoordinate;
import no.clueless.utm_lat_lng.UtmToLatLngConverter;

UTMCoordinate utmCoordinate = new UTMCoordinate(32, Hemisphere.Northern, 553936.0, 6555976.0);
LatLng latLng = UtmToLatLngConverter.calculateLatLng(utmCoordinate);
```