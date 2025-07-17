package app.repositories;

import app.models.Cabin;
import org.springframework.stereotype.Repository;

@Repository("CABINS.JPA")
public class CabinsRepositoryJPA extends AbstractEntityRepositoryJPA<Cabin> {

    public CabinsRepositoryJPA() {
        super(Cabin.class);
    }
}
