import Cabin from "@/models/Cabin";

export class CabinsAdaptor {
    resourcesUrl;

    constructor (resourcesUrl) {
        this.resourcesUrl = resourcesUrl;
        console.log("Created CabinsAdaptor for " + resourcesUrl);
    }

    async fetchJson(url, options = null) {
        let response = await fetch(url, options)
        if (response.ok) {
            return await response.json();
        } else {
            console.log(response, !response.bodyUsed ? await response.text() : "");
            return null;
        }
    }

    async asyncFindAll() /* :Promise<Cabin[]> */ {
        console.log('CabinsAdaptor.asyncFindAll()');
        const cabins = await this.fetchJson(`${this.resourcesUrl}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        });
        return cabins?.map(s => Cabin.copyConstructor(s));
    }

    async asyncFindById(id) /* :Promise<Cabin> */
    {
        return await this.fetchJson(`${this.resourcesUrl}/${id}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        });
    }

    async asyncSave(cabin) /* :Promise<Cabin> */
    {
        return await this.fetchJson(`${this.resourcesUrl}/${cabin.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: cabin.id,
                type: cabin.type,
                description: cabin.description,
                numAvailable: cabin.numAvailable,
                image: cabin.image,
                pricePerWeek: cabin.pricePerWeek,
                location: cabin.location
            }),
        });
    }

    async asyncDeleteById(id)
    {
        try {
            const response = await fetch(`${this.resourcesUrl}/${id}`, {
                method: 'DELETE'
            });

            if (response.status === 204) {
                return;
            }

            return await response.json();

        } catch (error) {
            console.error("Fetch error:", error);
            throw error;
        }
    }

}