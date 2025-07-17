import Location from "@/models/Location";
import Rental from "@/models/Rental";

export default class Cabin {
    static cabins = [];
    static selectedCabin = null;
    static lastId = 10000;

    /**
     * Creates an instance of Cabin.
     *
     * @param {number} id - The unique identifier for the cabin.
     * @param {string} type - The type of the cabin.
     * @param {number} pricePerWeek - The price per week for renting the cabin.
     * @param {string} description - A description of the cabin.
     * @param {Location} location - The location object representing where the cabin is situated.
     * @param {number} numAvailable - The number of available cabins.
     * @param {string} image - The filename of the cabin image.
     * @param {Rental} rentals - Array of all rentals.
     */
    constructor(id, type, pricePerWeek, description, location, numAvailable, image, rentals) {
        this.setType(type);
        this.description = description;
        this.pricePerWeek = pricePerWeek;
        this.location = location;
        this.numAvailable = numAvailable;
        this.image = image;
        this.id = id;
        this.rentals = rentals;
    }
    
    /**
     * Enumeration of cabin types.
     * @enum {string}
     */
    static Type = {
        BeachGear: "BeachGear",
        SmallDayTime: "SmallDayTime",
        SmallLodge: "SmallLodge",
        LargeLodge: "LargeLodge",
        FamilyLodge: "FamilyLodge"
    };

    /**
     * Static array of sample cabin descriptions.
     * @type {string[]}
     */
    static descriptions = [
        "colourful SmallDayTime cabins, model-5",
        "modern LargeLodge cabins, model-1",
        "spacy SmallLodge cabins, model-4",
        "comfortable LargeLodge cabins, model-2",
        "white FamilyLodge cabins, model-2"
    ];

    /**
     * Static array of sample cabin images.
     * @type {string[]}
     */
    static images = [
        "cabin-1.jpg",
        "cabin-2.jpg",
        "cabin-3.jpg",
        "cabin-5.jpg",
        "cabin-6.jpg",
        "cabin-7.jpg",
        "cabin-8.jpg",
        "cabin-9.jpg"
    ];

    /**
     * Creates a copy of a cabin instance.
     *
     * @param {Cabin|null} cabin - The cabin to copy.
     * @returns {Cabin|null} A new Cabin instance or null if the input is null.
     */
    static copyConstructor(cabin) {
        if (!cabin) return null;

        return new Cabin(
            cabin.id,
            cabin.type,
            cabin.pricePerWeek,
            cabin.description,
            Location.copyConstructor(cabin.location),
            cabin.numAvailable,
            cabin.image,
            cabin.rentals ? cabin.rentals.map(Rental.copyConstructor) : []
        );
    }

    /**
     * Checks if two cabins are equal based on their properties.
     *
     * @param {Cabin} otherCabin - The cabin to compare against.
     * @returns {boolean} True if the cabins are equal, false otherwise.
     */
    equals(otherCabin) {
        return this.id === otherCabin.id &&
            this.type === otherCabin.type &&
            this.description === otherCabin.description &&
            this.image === otherCabin.image &&
            this.pricePerWeek === otherCabin.pricePerWeek &&
            this.numAvailable === otherCabin.numAvailable &&
            this.location.id === otherCabin.location.id;
    }

    /**
     * Sets the type of the cabin after validation.
     *
     * @param {string} type - The type of the cabin to set.
     * @throws {Error} Throws an error if the type is invalid.
     */
    setType(type) {
        if (Object.values(Cabin.Type).includes(type)) {
            this.type = type;
        } else {
            throw new Error(`Invalid cabin type: ${type}. Must be one of ${Object.values(Cabin.Type).join(", ")}.`);
        }
    }

    /**
     * Generates and adds a new cabin to the cabins array.
     *
     * @returns {Cabin} The created cabin instance.
     */
    static generateSampleCabins() {
        let nextId = this.lastId += Math.floor(Math.random() * 3) + 1;

        let newCabin = Cabin.createRandomCabin(nextId, Location.random());

        this.lastId = nextId;

        this.cabins.push(newCabin);

        return newCabin;
    }

    /**
     * Creates a cabin with random properties.
     *
     * @param {number} pId - The ID for the new cabin.
     * @param randomLocation
     * @returns {Cabin} A new cabin instance with random properties.
     */
    static createRandomCabin(pId, randomLocation) {
        const randomType = Object.values(Cabin.Type)[Math.floor(Math.random() * Object.values(Cabin.Type).length)];
        const randomDescription = Cabin.descriptions[Math.floor(Math.random() * Cabin.descriptions.length)];
        // const randomLocation = Location.random(pId);
        const randomPrice = Cabin.getPriceForType(randomType);
        const randomAvailability = Math.floor(Math.random() * 50) + 1;
        const randomImage = Cabin.images[Math.floor(Math.random() * Cabin.images.length)];

        return new Cabin(
            pId,
            randomType,
            randomPrice,
            randomDescription,
            randomLocation,
            randomAvailability,
            randomImage,
        );
    }

    /**
     * Gets the price for a cabin type.
     *
     * @param {string} type - The type of the cabin.
     * @returns {number} The price for the specified cabin type.
     */
    static getPriceForType(type) {
        switch (type) {
            case Cabin.Type.BeachGear:
                return Math.floor(Math.random() * 100) + 300;
            case Cabin.Type.SmallDayTime:
                return Math.floor(Math.random() * 100) + 500;
            case Cabin.Type.SmallLodge:
                return Math.floor(Math.random() * 200) + 700;
            case Cabin.Type.LargeLodge:
                return Math.floor(Math.random() * 300) + 1000;
            case Cabin.Type.FamilyLodge:
                return Math.floor(Math.random() * 300) + 1500;
            default:
                return 0;
        }
    }

    /**
     * Returns a string representation of the cabin.
     *
     * @returns {string} A string detailing the cabin's properties.
     */
    toString() {
        return `
      Cabin ID: ${this.id}
      Image: ${this.image}
      Type: ${this.type}
      Description: ${this.description}
      Location: ${this.location}
      Price per Week: ${this.pricePerWeek}
      Available: ${this.numAvailable}
      Rentals: ${this.rentals}
    `;
    }
}
