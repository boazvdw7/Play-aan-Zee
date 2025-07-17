package app.repositories;

import app.models.Cabin;
import app.models.Rental;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class RentalsRepositoryMock extends AbstractEntityRepositoryMock<Rental> {

    public RentalsRepositoryMock() {
        super();
    }

    @Override
    protected void initializeEntities() {

    }

    /**
     * Creates a sample Rental instance.
     *
     * @param cabin The cabin for which the rental is created.
     * @return A new Rental instance with random details.
     */
    public static Rental createSampleRental(Cabin cabin) {
        Random random = new Random();

        List<Rental.RentalStatus> statuses = List.of(
                Rental.RentalStatus.REQUESTED,
                Rental.RentalStatus.PAID,
                Rental.RentalStatus.CANCELLED,
                Rental.RentalStatus.FULFILLED
        );

        LocalDate startDate = LocalDate.of(2021, random.nextInt(12) + 1, random.nextInt(28) + 1);
        long daysBetween = random.nextInt(10) + 2;
        LocalDate endDate = startDate.plusDays(daysBetween);

        Rental rental = new Rental();
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setStatus(statuses.get(random.nextInt(statuses.size())));
        rental.setCost(random.nextDouble() * 150 + 50);
        rental.setCabin(cabin);

        return rental;
    }
}
