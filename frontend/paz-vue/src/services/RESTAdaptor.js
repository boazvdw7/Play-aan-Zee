import {RESTAdaptorInterface} from "@/interfaces/RestAdaptorInterface";

export class RESTAdaptorWithFetch extends RESTAdaptorInterface {
    constructor(resourcesUrl, copyConstructor) {
        super(resourcesUrl, copyConstructor);
    }

    static serializeQueryParams(queryParams) {
        if (!queryParams) return '';
        return '?' + new URLSearchParams(queryParams).toString();
    }

    async asyncFindAll(queryParams = null) {
        const url = this.fullURL("/summary", queryParams);
        const response = await fetch(url)

        if (!response.ok) {
            throw new Error('Failed to fetch entities');
        }

        const data = await response.json();
        return data.map(this.copyConstructor);
    }

    async asyncFindById(id, queryParams = null) {
        const url = this.fullURL(`${this.resourcesUrl}/${id}`, queryParams);
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Failed to fetch entity with ID ${id}, Status: ${response.status}`);
        }

        const data = await response.json();

        return this.copyConstructor(data);
    }


    async asyncSave(entity, queryParams = null) {
        const method = entity.id ? 'PUT' : 'POST';

        const url = entity.id ? `${this.resourcesUrl}/${entity.id}` : `${this.resourcesUrl}`;

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: entity.id,
                type: entity.type,
                description: entity.description,
                numAvailable: entity.numAvailable,
                image: entity.image,
                pricePerWeek: entity.pricePerWeek,
                location: entity.location
            }),
        });

        if (!response.ok) {
            throw new Error('Failed to save entity');
        }

        const data = await response.json();

        return this.copyConstructor(data);
    }

    async asyncDeleteById(id, queryParams = null) {
        const response = await fetch(`${this.resourcesUrl}/${id}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error(`Failed to delete entity with ID ${id}`);
        }
    }

    fullURL(target, queryParams) {
        let url = target.startsWith('http') ? target : this.resourcesUrl + target;

        if (queryParams != null) {
            let newUrl = new URL(url);
            newUrl.search = new URLSearchParams(queryParams).toString();
            url = newUrl.toString();
        }

        return url;
    }
}
