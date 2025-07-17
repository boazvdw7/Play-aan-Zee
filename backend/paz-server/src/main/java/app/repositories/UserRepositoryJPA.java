package app.repositories;

import app.models.Users;
import org.springframework.stereotype.Repository;

@Repository("USERS.JPA")
public class UserRepositoryJPA extends AbstractEntityRepositoryJPA<Users> {

    public UserRepositoryJPA() {
        super(Users.class);
    }
}
