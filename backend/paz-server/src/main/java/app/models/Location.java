package app.models;

import app.repositories.Identifiable;
import app.serializers.CustomJson;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Location implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonView(CustomJson.Shallow.class)
    private Long id;

    @JsonView(CustomJson.Shallow.class)
    private String name;

    @JsonView(CustomJson.Shallow.class)
    private String country;

    public Location() {
    }

    /**
     * Creates an instance of Location.
     *
     * @param id      The unique identifier for the location.
     * @param name    The name of the location.
     * @param country The country where the location is situated.
     */
    public Location(Long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    // Getters & Setters
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static final String[] countries = {
            "Nederland", "Greece", "Letland", "Portugal"
    };

    public static final String[] names = {
            "Wijk aan Zee", "Renesse", "Noordwijk", "De Panne",
            "Egmond aan Zee", "Oostende", "Cadzand"
    };
}
