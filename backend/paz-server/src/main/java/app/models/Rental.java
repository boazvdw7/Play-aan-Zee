package app.models;

import app.repositories.Identifiable;
import app.serializers.CustomJson;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@NamedQuery(name = "Rental_find_by_cabinId_and_period",
        query = "SELECT r FROM Rental r WHERE r.cabin.id = ?1 AND r.startDate >= ?2 AND r.endDate <= ?3")
public class Rental implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(CustomJson.Summary.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cabin_id")
    @JsonBackReference
    @JsonView(CustomJson.Summary.class)
    private Cabin cabin;

    @JsonView(CustomJson.Summary.class)
    private LocalDate startDate;

    @JsonView(CustomJson.Unrestricted.class)
    private LocalDate endDate;

    @JsonView(CustomJson.Summary.class)
    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @JsonView(CustomJson.Unrestricted.class)
    private double cost;

    public Rental(Cabin cabin, LocalDate startDate, LocalDate endDate, RentalStatus status, double cost) {
        this.cabin = cabin;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.cost = cost;
    }

    public Rental() {

    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cabin getCabin() {
        return cabin;
    }

    public void setCabin(Cabin cabin) {
        this.cabin = cabin;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public enum RentalStatus {
        REQUESTED, APPROVED, DECLINED, PAID, FULFILLED, CANCELLED, BLOCKED
    }

// LOGIC
    /**
     * Associates the given cabin with this rental, if not yet associated.
     * If the provided cabin is null, dissociate the current cabin.
     *
     * @param cabin The cabin to associate with the rental, or null to dissociate.
     * @return true if the association has changed, false if no change was made.
     */
    public boolean associateCabin(Cabin cabin) {
        if (cabin == null) {
            if (this.cabin != null) {
                this.cabin = null;
                return true;
            }
            return false;
        } else {
            if (this.cabin != null && this.cabin.equals(cabin)) {
                return false;
            }
            this.cabin = cabin;
            return true;
        }
    }

}

