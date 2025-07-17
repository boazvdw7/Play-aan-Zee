package app.repositories;

import app.models.Cabin;
import app.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class CabinsRepositoryMock extends AbstractEntityRepositoryMock<Cabin> {
    public CabinsRepositoryMock() {
        super();
    }

    @Override
    protected void initializeEntities() {
        getEntities();
    }

    private List<Cabin> getEntities() {
        return super.findAll();
    }

    public static Cabin createSampleCabin(Long pId, Location location) {
        Random random = new Random();
        Cabin.CabinType randomType = Cabin.CabinType.values()[random.nextInt(Cabin.CabinType.values().length)];
        String randomDescription = DESCRIPTIONS[random.nextInt(DESCRIPTIONS.length)];
        Double randomPrice = Math.round(getPriceForType(randomType.toString()) * 100.0) / 100.0;
        Integer randomAvailability = random.nextInt(50) + 1;
        String randomImage = IMAGES[random.nextInt(IMAGES.length)];

        return new Cabin(
                pId,
                randomType,
                randomPrice,
                randomDescription,
                location,
                randomAvailability,
                randomImage
        );
    }

    public static final String[] DESCRIPTIONS = {
            "colourful SmallDayTime cabins, model-5",
            "modern LargeLodge cabins, model-1",
            "spacy SmallLodge cabins, model-4",
            "comfortable LargeLodge cabins, model-2",
            "white FamilyLodge cabins, model-2"
    };

    private static final String[] IMAGES = {
            "cabin-1.jpg",
            "cabin-2.jpg",
            "cabin-3.jpg",
            "cabin-5.jpg",
            "cabin-6.jpg",
            "cabin-7.jpg",
            "cabin-8.jpg",
            "cabin-9.jpg"
    };


    private static Double getPriceForType(String type) {
        return switch (type) {
            case "BeachGear" -> (Math.random() * 100) + 300;
            case "SmallDayTime" -> (Math.random() * 100) + 500;
            case "SmallLodge" -> (Math.random() * 200) + 700;
            case "LargeLodge" -> (Math.random() * 300) + 1000;
            case "FamilyLodge" -> (Math.random() * 300) + 1500;
            default -> 0.0;
        };
    }
}
