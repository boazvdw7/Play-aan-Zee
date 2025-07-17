package app.rest;

import app.models.Rental;
import app.repositories.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cabins/{cabinId}/rentals")
public class RentalsController {

    private final EntityRepository<Rental> rentalsRepository;

    @Autowired
    public RentalsController(EntityRepository<Rental> rentalsRepository) {
        this.rentalsRepository = rentalsRepository;
    }

//    @GetMapping
//    public ResponseEntity<List<Rental>> getRentals(
//            @PathVariable long cabinId,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
//
//        List<Rental> rentals = new ArrayList<>();
//
//        if (from != null && to != null) {
//            rentals = rentalsRepository.findByQuery("Rental_find_by_cabinId_and_period", cabinId, from, to);
//        } else {
//            rentals = rentalsRepository.findByQuery("Rental_find_by_cabinId_and_period", cabinId, LocalDate.MIN, LocalDate.MAX);
//        }
//
//        return ResponseEntity.ok(rentals);
//    }
}

