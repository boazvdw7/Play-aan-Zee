package app.repositories;

import app.models.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository("LOCATIONS.JPA")
public class LocationRepositoryJPA extends AbstractEntityRepositoryJPA<Location> {
    @PersistenceContext
    private EntityManager entityManager;

    protected LocationRepositoryJPA() {
        super(Location.class);
    }
}
