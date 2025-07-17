export class FetchInterceptorInterface {

    constructor() {

    }
    request(url, options) {}
    requestError (error) {}
    response (response) {}
    responseError(error) {}
}