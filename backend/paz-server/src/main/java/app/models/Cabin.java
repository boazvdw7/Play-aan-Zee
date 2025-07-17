package app.models;

import app.repositories.Identifiable;
import app.serializers.CustomJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name = "Cabin_find_by_type", query = "SELECT c FROM Cabin c WHERE c.type = ?1"),
        @NamedQuery(name = "Cabin_find_by_locationName", query = "SELECT c FROM Cabin c WHERE c.location.name = ?1")
})
public class Cabin implements Identifiable {
    @Id
    @SequenceGenerator(name = "customer_seq", initialValue = 30001, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "customer_seq")
    @JsonView({CustomJson.Shallow.class})
    private Long id;

    @Enumerated(EnumType.STRING)
    @JsonView({CustomJson.Shallow.class})
    private CabinType type;

    @JsonView(CustomJson.Summary.class)
    private String description;

    @JsonView({CustomJson.Shallow.class})
    private Integer numAvailable;

    @JsonView({CustomJson.Shallow.class})
    private String image;

    @JsonView(CustomJson.Summary.class)
    private Double pricePerWeek;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    @JsonView({CustomJson.Summary.class, CustomJson.Shallow.class})
    private Location location;

    @OneToMany(mappedBy = "cabin", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonSerialize(using = CustomJson.SummarySerializer.class)
    @JsonView(CustomJson.Summary.class)
    private Set<Rental> rentals = new HashSet<>();

    @Transient
    @JsonProperty("availabilities")
    private int[] availabilities;

    public int[] getAvailabilities() {
        return availabilities;
    }

    public Cabin(Long id, CabinType type, Double pricePerWeek, String description, Location location, Integer numAvailable, String image) {
        this.type = type;
        this.id = id;
        this.description = description;
        this.pricePerWeek = pricePerWeek;
        this.location = location;
        this.numAvailable = numAvailable;
        this.image = image;
    }

    public Cabin() {}

    ///////////////////////////////////
    //      Getters and Setters     //
    /////////////////////////////////
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public CabinType getType() {
        return type;
    }

    public void setType(CabinType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricePerWeek() {
        return pricePerWeek;
    }

    public void setPricePerWeek(Double pricePerWeek) {
        this.pricePerWeek = pricePerWeek;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getNumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(Integer numAvailable) {
        this.numAvailable = numAvailable;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    public enum CabinType {
        BeachGear("BeachGear"),
        SmallDayTime("SmallDayTime"),
        SmallLodge("SmallLodge"),
        LargeLodge("LargeLodge"),
        FamilyLodge("FamilyLodge");

        private final String type;

        CabinType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
    ////////////////////////////////
    //           LOGIC           //
    //////////////////////////////
    public void calculateAvailabilities(int n, LocalDate startDate) {
        if (startDate == null) return;
        this.availabilities = new int[n];

        Arrays.fill(this.availabilities, this.numAvailable);

        for (Rental r : this.getRentals()) {
            LocalDate rentalStart = r.getStartDate();
            LocalDate rentalEnd = r.getEndDate();

            for (int i = 0; i < n; i++) {
                LocalDate weekStart = startDate.plusWeeks(i);
                LocalDate weekEnd = weekStart.plusDays(7);

                if ((rentalStart.isBefore(weekEnd) && rentalEnd.isAfter(weekStart))) {
                    this.availabilities[i]--;
                }
            }
        }
    }


    /**
     * Associates the given rental with this cabin, if not yet associated.
     * @param rental The rental to associate with this cabin.
     * @return true if a new association has been added, false if the rental is already associated.
     */
    public boolean associateRental(Rental rental) {
        if (rental != null && rental.getCabin() == null) {
            rental.setCabin(this);
            return true;
        }

        return false;
    }


    /**
     * Dissociates the given rental from this cabin, if associated.
     * @param rental The rental to dissociate from this cabin.
     * @return true if an existing association has been removed, false if the rental was not associated.
     */
    public boolean dissociateRental(Rental rental) {
        if (rental != null && rental.getCabin() != null && rental.getCabin().equals(this)) {
            rental.setCabin(null);
            return true;
        }

        return false;
    }


    /**
     * Associates the given location with this cabin, if not already associated.
     * @param location The location to associate with this cabin.
     * @return true if a new association has been added, false if the cabin is already associated with the location.
     */
    public boolean associateLocation(Location location) {
        if (location != null && this.getLocation() == null) {
            this.setLocation(location);
            return true;
        }
        return false;
    }

    /**
     * Dissociates the given location from this cabin, if associated.
     * @param location The location to dissociate from this cabin.
     * @return true if an existing association has been removed, false if the cabin was not associated with the location.
     */
    public boolean dissociateLocation(Location location) {
        if (this.getLocation() != null && this.getLocation().equals(location)) {
            this.setLocation(null);
            return true;
        }
        return false;
    }
}
