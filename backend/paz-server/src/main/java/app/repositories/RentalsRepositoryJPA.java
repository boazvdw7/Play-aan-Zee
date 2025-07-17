package app.repositories;

import app.models.Rental;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository("RENTALS.JPA")
public class RentalsRepositoryJPA extends AbstractEntityRepositoryJPA<Rental> {

    public RentalsRepositoryJPA() {
        super(Rental.class);
    }
}
