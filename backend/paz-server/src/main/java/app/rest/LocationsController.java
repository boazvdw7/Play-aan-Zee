package app.rest;

import app.models.Cabin;
import app.models.Location;
import app.repositories.EntityRepository;
import app.serializers.CustomJson;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationsController {
    private final EntityRepository<Location> locationRepository;

    public LocationsController(EntityRepository<Location> locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Retrieves a summary list of all locations.
     *
     * @return A list of all locations with a summary view.
     */
    @GetMapping("/summary")
    @JsonView(CustomJson.Summary.class)
    public List<Location> getLocationsSummary() {
        return locationRepository.findAll();
    }
}
