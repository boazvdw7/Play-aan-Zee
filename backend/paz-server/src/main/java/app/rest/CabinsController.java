package app.rest;

import app.exceptions.PreConditionFailedException;
import app.exceptions.ResourceNotFoundException;
import app.models.Cabin;
import app.models.Location;
import app.models.Rental;
import app.repositories.CabinsRepositoryMock;
import app.repositories.EntityRepository;
import app.serializers.CustomJson;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;

import java.net.URI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cabins")
public class CabinsController {

    private final EntityRepository<Cabin> cabinsRepository;
    private final EntityRepository<Rental> rentalsRepository;

    @Autowired
    public CabinsController(EntityRepository<Cabin> cabinsRepository, EntityRepository<Rental> rentalsRepository, CabinsRepositoryMock cabinsRepositoryMock) {
        this.cabinsRepository = cabinsRepository;
        this.rentalsRepository = rentalsRepository;
    }

    /**
     * Retrieves a cabin by a given ID.
     * URL: GET http://localhost:8086/cabins/{cabinId}
     *
     * @param id The ID of the cabin to retrieve.
     * @return Response containing the cabin if found, or else a Resource Not Found error.
     */
    @GetMapping("/{id}")
    @JsonView(CustomJson.Summary.class)
    public ResponseEntity<Cabin> getCabinById(
            @PathVariable Long id,
            @RequestParam Optional<String> availabilitiesFrom) {
        Cabin cabin = cabinsRepository.findById(id);

        if (cabin == null) {
            throw new ResourceNotFoundException("Cabin with id " + id + " not found");
        }

        int n = 12; // TODO weet niet hoe/waar je n moet meegeven schijnbaar niet als query parameter (zie plaatjes 4.5)

        try {
            if (availabilitiesFrom.isPresent()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(availabilitiesFrom.get(), formatter);

                cabin.calculateAvailabilities(n, startDate);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd");
        }

        System.out.println(Arrays.toString(cabin.getAvailabilities()));

        return ResponseEntity.ok(cabin);
    }

    /**
     * Creates a new cabin and saves it to the database.
     * URL: POST http://localhost:8086/cabins
     * Final URI: http://localhost:8086/cabins/{cabinId}
     *
     * @param cabin The cabin object to create.
     * @return Response containing the created cabin and a location header with the new cabin.
     */
    @PostMapping
    public ResponseEntity<Cabin> createCabin(@RequestBody Cabin cabin) {
        Cabin createdCabin = cabinsRepository.save(cabin);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{cabinId}")
                .buildAndExpand(createdCabin.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdCabin);
    }

    /**
     * Updates an existing cabin with the given cabin object.
     * URL: PUT http://localhost:8086/cabins/{cabinId}
     *
     * @param id The ID of the cabin to update.
     * @param cabin The cabin object with updated properties.
     * @return Response containing the updated cabin.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cabin> updateCabin(@PathVariable Long id, @RequestBody Cabin cabin) {
        if (!id.equals(cabin.getId())) {
            throw new PreConditionFailedException("ID in path and body must match");
        }

        Cabin existingCabin = cabinsRepository.findById(id);

        cabin.setRentals(existingCabin.getRentals());

        Cabin updatedCabin = cabinsRepository.save(cabin);
        return ResponseEntity.ok(updatedCabin);
    }

    /**
     * Deletes a cabin by its ID.
     * URL: DELETE http://localhost:8086/cabins/{cabinId}
     *
     * @param id The ID of the cabin to delete.
     * @return Response with a 204 No Content status if successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCabin(@PathVariable Integer id) {
        Cabin cabin = cabinsRepository.findById(id);
        if (cabin == null) {
            throw new ResourceNotFoundException("Cabin with id " + id + " not found");
        }
        cabinsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a rental to a cabin.
     * URL: POST http://localhost:8086/cabins/{cabinId}/rentals
     * Final URI: http://localhost:8086/cabins/{cabinId}/rentals/{rentalId}
     *
     * @param cabinId The ID of the cabin which the rental should be added to.
     * @param rental The rental object to add to the cabin.
     * @return Response containing the created rental and a location header with the new rental.
     */
    @PostMapping("/{cabinId}/rentals")
    public ResponseEntity<Rental> addRentalToCabin(@PathVariable Long cabinId, @RequestBody Rental rental) {
        if (rental.getEndDate() == null) {
            rental.setEndDate(rental.getStartDate().plusWeeks(1));
        }

        if (!rental.getEndDate().isAfter(rental.getStartDate())) {
            throw new PreConditionFailedException("End date must be after start date.");
        }

        long durationInDays = java.time.temporal.ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
        if (durationInDays % 7 != 0) {
            throw new PreConditionFailedException(String.format("Period %s to %s is not a valid rental period of 1 to 6 weeks.", rental.getStartDate(), rental.getEndDate()));
        }

        Cabin cabin = cabinsRepository.findById(cabinId);
        if (cabin == null) {
            throw new ResourceNotFoundException("Cabin with id " + cabinId + " not found.");
        }

        rental.setCabin(cabin);
        rental = rentalsRepository.save(rental);

        URI cabinRental = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{cabinId}")
                .buildAndExpand(rental.getId())
                .toUri();

        return ResponseEntity.created(cabinRental).body(rental);
    }

    /**
     * Retrieves a list of cabins based on optional filters for type and location.
     * URL: GET http://localhost:8086/cabins(?location={locationObj}&type={cabinType})
     *
     * @param type Cabin type filter.
     * @param location Location filter.
     * @return Response containing a list of cabins.
     * @throws BadRequestException If the type or location parameters are invalid.
     */
    @GetMapping
    @JsonView(CustomJson.Shallow.class)
    public ResponseEntity<List<Cabin>> getCabins(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location) throws BadRequestException {

        List<Cabin> cabins;

        if (type != null && validateType(type)) return null;
        if (location != null && validateLocation(location)) return null;

        Cabin.CabinType cabinType = null;
        if(type != null) {
            cabinType = Cabin.CabinType.valueOf(type);
        }


        if (type != null && location != null) {
            cabins = findCabinsByTypeAndLocation(cabinType, location);
        } else if (type != null) {
            cabins = cabinsRepository.findByQuery("Cabin_find_by_type", cabinType);
        } else if (location != null) {
            cabins = cabinsRepository.findByQuery("Cabin_find_by_locationName", location);
        } else {
            cabins = cabinsRepository.findAll();
        }

        // Als hij een lege array terug geeft bestaan de parameters wel,
        // maar staat de opgevraagde data niet in de database.
        return ResponseEntity.ok(cabins);
    }

    /**
     * Finds rentals attached to a cabin based on from and to dates.
     * If no from and to params are given, will return all rentals for
     * given cabinId.
     * URL: GET http://localhost:8086/cabins/{cabinId}/rentals
     *
     * @param cabinId The cabin for which to find the rentals.
     * @param from (Optional!) The starting date.
     * @param to (Optional!) The end date.
     * @return Rentals located between the dates.
     */
    @GetMapping("{cabinId}/rentals")
    @JsonView({CustomJson.Unrestricted.class})
    public ResponseEntity<List<Rental>> getRentals(
            @PathVariable long cabinId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {

        List<Rental> rentals = rentalsRepository.findAll();

        if (from != null && to != null) {
            rentals = rentalsRepository.findByQuery("Rental_find_by_cabinId_and_period", cabinId, from, to);
        } else {
            // TODO: dit kan beter maar het werkt.
            rentals = rentalsRepository.findByQuery("Rental_find_by_cabinId_and_period",
                    cabinId, LocalDate.of(1, 1, 1),
                    LocalDate.of(9999, 12, 31));
        }

        return ResponseEntity.ok(rentals);
    }

    /**
     * Retrieves a summary list of all cabins.
     * URL: GET http://localhost:8086/cabins/summary
     *
     * @return A list of all cabins with a summary view.
     */
    @GetMapping("/summary")
    @JsonView(CustomJson.Summary.class)
    public List<Cabin> getCabinsSummary() {
        return cabinsRepository.findAll();
    }

    ///////////////////////////////
    //      Helper Methods      //
    /////////////////////////////
    private boolean validateType(String type) throws BadRequestException {
        if (type.isEmpty()) {
            throw new BadRequestException("type cannot be empty");
        }

        try {
            Cabin.CabinType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(String.format("type=%s is not a valid cabin type", type));
        }

        return false;
    }

    private boolean validateLocation(String location) throws BadRequestException {
        if (location.isEmpty()) {
            throw new BadRequestException("location cannot be empty");
        }

        if (!Arrays.asList(Location.names).contains(location)) {
            throw new BadRequestException(String.format("location=%s is not a valid location", location));
        }

        return false;
    }

    private List<Cabin> findCabinsByTypeAndLocation(Cabin.CabinType cabinType, String location) {
        List<Cabin> cabins = cabinsRepository.findByQuery("Cabin_find_by_type", cabinType);
        return cabins.stream()
                .filter(cabin -> cabin.getLocation().getName().equals(location))
                .collect(Collectors.toList());
    }
}
