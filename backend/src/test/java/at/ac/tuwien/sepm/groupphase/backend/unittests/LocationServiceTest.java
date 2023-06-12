package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class LocationServiceTest {
    @Autowired
    LocationService locationService;

    @Autowired
    LocationRepository locationRepository;

    private Location location;
    private Location location1;
    private LocationSearchDto locationSearchDto;
    private LocationSearchDto locationSearchDto1;

    @BeforeEach
    public void beforeAll() {
        location = new Location();
        location.setPostalCode(1111);
        location.setCountry("Austria");
        location.setCity("Vienna");
        location.setStreet("Street 1");
        locationRepository.save(location);

        location1 = new Location();
        location1.setPostalCode(2222);
        location1.setCountry("Austria");
        location1.setCity("Vienna");
        location1.setStreet("Street 2");
        locationRepository.save(location1);

        locationSearchDto = new LocationSearchDto();
        locationSearchDto.setCountry("Aus");

        locationSearchDto1 = new LocationSearchDto();
        locationSearchDto1.setCity("Sofia");

    }

    @Test
    void getAllLocationsWithCityNotMatchingTheParameterThrowsNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> locationService.getAllLocationsWithParameters(locationSearchDto1));
    }

    @Test
    void getAllLocationsWithCountryMatchingTheGivenParametersShouldReturnAllLocations() {
        Assertions.assertDoesNotThrow(() -> locationService.getAllLocationsWithParameters(locationSearchDto));

        List<Location> locations = locationService.getAllLocationsWithParameters(locationSearchDto);
        assertEquals(2, locations.size());
        assertEquals(location.getCountry(), locations.get(0).getCountry());
    }

}
