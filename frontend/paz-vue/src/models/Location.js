import Cabin from "@/models/Cabin";

export default class Location {
    /**
     * Creates an instance of Location.
     *
     * @param {number} id - The unique identifier for the location.
     * @param {string} name - The name of the location.
     * @param {string} country - The country where the location is situated.
     */
    constructor(id, name, country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    /**
     * Checks for duplicate locations and generates a random location if there are none.
     *
     * @param {number} pId - The ID to be used for generating a new location if needed.
     * @returns {Location|null} A random location or null if duplicates are found.
     * @todo Implement a proper duplicate checking mechanism.
     */
    static noDupes(pId) {
        Cabin.cabins.forEach(cabin => {
            if (!cabin.includes(Location.samples)) {
                return this.random(pId);
            }
        });
        return null;
    }

    /**
     * An array of predefined location samples.
     * @type {Location[]}
     */
    static samples = [
        new Location(1, "Wijk aan Zee", "Nederland"),
        new Location(2, "Renesse", "Nederland"),
        new Location(3, "Noordwijk", "Nederland"),
        new Location(4, "De Panne", "Nederland"),
        new Location(5, "Egmond aan Zee", "Greece"),
        new Location(6, "Oostende", "Letland"),
        new Location(7, "Cadzand", "Portugal"),
    ];

    /**
     * Selects a random location from the samples.
     *
     * @param {number} pId - The ID used to reference the location (not used in this method).
     * @returns {Location} A random Location instance from the samples.
     */
    static random() {
        return this.samples[Math.floor(Math.random() * this.samples.length)];
    }

    /**
     * Creates a copy of a given location instance.
     *
     * @param {Location} location - The location to copy.
     * @returns {Location} A new Location instance that is a copy of the input location.
     */
    static copyConstructor(location) {
        if (!location) return null;

        return new Location(location.id, location.name, location.country);
    }
}
