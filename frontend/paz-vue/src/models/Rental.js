
export default class Rental {
    static rentals = [];

    // constructor(id, cost, end_date, start_date, status, cabin_id) {
    //     this.id = id;
    //     this.cost = cost;
    //     this.endDate = end_date;
    //     this.startDate = start_date;
    //     this.cabin_id = cabin_id;
    // }

    constructor(id, startDate, status) {
        this.id = id;
        this.startDate = startDate;
        this.status = status;
    }

    static copyConstructor(rental) {
        if(!rental) return null;

        return new Rental(rental.id, rental.startDate, rental.status);
    }
}