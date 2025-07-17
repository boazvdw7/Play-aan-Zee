package app.repositories;

import app.models.Users;

import java.util.ArrayList;
import java.util.List;

public  class UserRepositoryMock {

    public static List<Users> createSampleUsers() {
        List<Users> testUsers = new ArrayList<>();

        Users users1 = new Users(1L,"test@test.nl", "1234", "user");
        Users users2 = new Users(2L,"boaz@test.nl", "1234", "admin");
        Users users3 = new Users(3L,"zineddine@test.nl", "1234", "admin");

        testUsers.add(users1);
        testUsers.add(users2);
        testUsers.add(users3);

        return testUsers;
    }
}
