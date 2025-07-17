package app;

import app.models.Cabin;
import app.models.Location;
import app.models.Rental;
import app.models.Users;
import app.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class PazServerApplication implements CommandLineRunner {
	@Autowired
	private EntityRepository<Cabin> cabinsRepository;
	@Autowired
	private EntityRepository<Location> locationsRepository;
	@Autowired
	private EntityRepository<Rental> rentalsRepository;
	@Autowired
	private EntityRepository<Users> usersRepository;


	public static void main(String[] args) {
		SpringApplication.run(PazServerApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {
		System.out.println("Running CommandLine Startup");

		this.createInitialLocations();
		this.createInitialCabins();
		this.createInitialRentals();
		this.createInitialUsers();
	}

	protected void createInitialLocations() {
		// check whether the repo is empty
		List<Location> locations = this.locationsRepository.findAll();

		if (!locations.isEmpty()) return;
		System.out.println("--- Configuring some initial Locations ---");

		for (int i = 0; i < 7; i++) {

			Location location = LocationsRepositoryMock.createSampleLocation(i);

			this.locationsRepository.save(location);

			// TODO maybe some more initial setup later
		}

	}

	protected void createInitialCabins() {
		// check whether the repo is empty
		List<Cabin> cabins = this.cabinsRepository.findAll();
		List<Location> locations = locationsRepository.findAll();

		if (!cabins.isEmpty()) return;
		if (locations.isEmpty()) return;

		System.out.println("--- Configuring some initial cabins ---");

		for (int i = 0; i < 6; i++) {
			Location location = locations.get(i);

			Cabin cabin = CabinsRepositoryMock.createSampleCabin(null, locationsRepository.findAll().get(i));


			cabin.associateLocation(location);
			cabin.setLocation(location);

			this.cabinsRepository.save(cabin);


			// TODO maybe some more initial setup later
		}

	}

	public void createInitialRentals() {
		List<Cabin> cabins = cabinsRepository.findAll();
		List<Rental> rentals = rentalsRepository.findAll();
		Random random = new Random();

		if (!rentals.isEmpty()) return;
		if (cabins.isEmpty()) return;

		System.out.println("--- Configuring some initial Rentals ---");

		for (Cabin cabin : cabins) {
			for (int i = 0; i < random.nextInt(2) + 2; i++) {
				Rental rental = RentalsRepositoryMock.createSampleRental(cabin);
				rentalsRepository.save(rental);
			}
		}
	}

	public void createInitialUsers() {
		List<Users> users = usersRepository.findAll();
		if(!users.isEmpty()) return;

		for (int i = 0; i < UserRepositoryMock.createSampleUsers().toArray().length; i++) {
				usersRepository.save(UserRepositoryMock.createSampleUsers().get(i));
		}
	}
}

