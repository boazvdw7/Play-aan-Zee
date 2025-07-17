
export class RESTAdaptorInterface /* <E extends Entity > */ {
    resourcesUrl; // the full url of the backend resource endpoint
    copyConstructor; // a reference to the copyConstructor of the entity: (E) => E

    constructor(resourcesUrl, copyConstructor) {
        this.resourcesUrl = resourcesUrl;
        this.copyConstructor = copyConstructor;
    }

    // returns all entities that match the optional queryParams
    asyncFindAll(queryParams = null) /* : Promise<E[]> */ {
    }

    // retrieves the entity with given id, and applies optional queryParams
    asyncFindById(id, queryParams = null) /* :Promise<E> */ {
    }

    // saves the given entity and applies optional queryParams
    asyncSave(entity, queryParams = null) /* :Promise<E> */ {
    }

    // deletes the entity with given id and applies optional queryParams
    asyncDelete(id, queryParams = null) {
    }
}