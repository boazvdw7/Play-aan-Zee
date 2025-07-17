import { RESTAdaptorWithFetch } from "@/services/RESTAdaptor";

export class CachedRESTAdaptorWithFetch extends RESTAdaptorWithFetch {
    entities;

    constructor(resourcesUrl, copyConstructor) {
        super(resourcesUrl, copyConstructor);
        this.entities = [];
    }

    async asyncFindAll(queryParams= null) {
        this.entities = await super.asyncFindAll(queryParams);
        return this.entities;
    }

    async asyncFindById(id, queryParams= null) {
        const cachedEntity = this.entities.find(entity => entity.id === id);

        if (cachedEntity) {
            return cachedEntity;
        }

        const entity = await super.asyncFindById(id, queryParams);
        this.entities.push(entity);
        return entity;
    }

    async asyncSave(entity, queryParams= null) {
        const savedEntity = await super.asyncSave(entity, queryParams);

        if (entity.id) {
            const index = this.entities.findIndex(e => e.id === entity.id);
            if (index !== -1) {
                this.entities[index] = savedEntity;
            }
        } else {
            this.entities.push(savedEntity);
        }

        return savedEntity;
    }


    async asyncDeleteById(id, queryParams= null) {
        await super.asyncDeleteById(id, queryParams);

        this.entities = this.entities.filter(entity => entity.id !== id);
    }
}
