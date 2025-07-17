package app.repositories;

import app.models.Location;
import org.springframework.stereotype.Component;

import java.util.Random;

import static app.models.Location.countries;
import static app.models.Location.names;

@Component
public class LocationsRepositoryMock extends AbstractEntityRepositoryMock<Location> {

    public LocationsRepositoryMock() {
        super();
    }

    @Override
    protected void initializeEntities() {
    }

    /**
     * Creates a sample Location instance using predefined arrays of countries and names.
     *
     * @param pId The unique identifier for the location (nullable).
     * @return A new Location instance with random country and name.
     */
    public static Location createSampleLocation(int pId) {
        Random random = new Random();
        String randomCountry;

        // Out-Of-Bounds fix, kan netter, maar geen zin
        if (pId < 4) {
            randomCountry = countries[pId];
        } else {
            randomCountry = countries[0];
        }

        String randomName = names[pId];

        return new Location(null, randomName, randomCountry);
    }
}
