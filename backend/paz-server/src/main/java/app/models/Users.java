package app.models;

import app.repositories.Identifiable;
import jakarta.persistence.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Users implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String hashedPassword;
    private String role;
    private String callname;


    public Users(Long id, String email, String hashedPassword, String role) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;

        Pattern regEx = Pattern.compile("^[^@]+");
        Matcher matcher = regEx.matcher(email);
        if(matcher.find()){
            this.callname = matcher.group(0);
        }
    }

    public Users(String email, String hashedPassword, String callname) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.callname = callname;
    }

    public Users() {

    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole( String role) {
        this.role = role;
    }
}
