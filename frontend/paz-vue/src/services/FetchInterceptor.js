import fetchIntercept from "fetch-intercept";

export class FetchInterceptor {
    static theInstance; // the singleton instance that has been registered
    session;
    router;
    unregister; // callback function to unregister this instance at shutdown
    static responseStatus = null;
    static href = null;

    constructor(session, router) {
        this.session = session;
        this.router = router;
        // fetchIntercept does not register the object closure, only the methods as functions
        // so we need an additional static reference to the singleton's attributes
        FetchInterceptor.theInstance = this;
        this.unregister = fetchIntercept.register(this);
        FetchInterceptor.href = window.location.href;

        console.log(
            "FetchInterceptor has been registered; Current token = ",
            this.session.getToken()
        );
    }

    request (url, options) {
    let token = FetchInterceptor.theInstance.session.getToken();

    if (token == null) {
        // no change
        return [url, options];
    } else if (options == null) {
        // the authorisation header is the only custom option
        return [url, {headers: {Authorization: token}}]
    } else {
        // add authorization header to other options
        let newOptions = { ...options };
        newOptions.headers = {
            // TODO combine new Authorization header with existing headers
        }

        if(token) {
            newOptions.headers.Authorization = token;
        }

        if (!newOptions.headers['Content-Type']) {
            newOptions.headers['Content-Type'] = 'application/json';
        }
            return [url, newOptions];
        }
    }

    response(response) {
        FetchInterceptor.href = window.location.href;
        FetchInterceptor.responseStatus = response.status;
        if (response.status === 401) {
            console.warn("Unauthorized response intercepted. Redirecting to sign-out.");
            FetchInterceptor.theInstance.router.push({ name: "ERROR" });
        }
        return response;
    }

    unregister() {
        if (this.unregister) {
            this.unregister();
            console.log("FetchInterceptor unregistered.");
        }
    }
}