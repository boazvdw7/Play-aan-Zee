export class SessionSbService {
    BROWSER_STORAGE_ITEM_NAME;
    RESOURCES_URL;

    constructor(resourcesUrl, browserStorageItemName) {
        this.BROWSER_STORAGE_ITEM_NAME = browserStorageItemName;
        this.RESOURCES_URL = resourcesUrl;
        this.getTokenFromBrowserStorage();
        //console. log("SessionSbService recovered token: ", this ._ currentToken);
    }

    /**
     * Logs in the user, retrieves the JWT token and user details from the backend.
     * @param {string} email - The email.
     * @param {string} password - The password.
     * @returns {Promise<Object>} - The logged-in user details.
     */
    async asyncSignIn(email, password) /* :Promise<User> */ {
        const body = JSON.stringify({ email: email, hashedPassword: password });
        let response = await fetch(this.RESOURCES_URL + "/login",
            {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: body,
                credentials: 'include',
            });

        if (response.ok) {
            let user = await response.json();
            this.saveTokenIntoBrowserStorage(
                response.headers.get('Authorization'),
                user);
            return user;
        } else {
            throw response.body;
        }
    }

    /**
     * Create a new user, retrieves the JWT token and user details from the backend.
     * @param {string} email - The email.
     * @param {string} password - The password.
     * @param callname - The callname.
     * @returns {Promise<Object>} - The logged-in user details.
     */
    async asyncSignUp(email, password, callname) /* :Promise<User> */ {
        const body = JSON.stringify({ email: email, hashedPassword: password, callname: callname});
        let response = await fetch(this.RESOURCES_URL + "/sign-up",
            {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: body,
                credentials: 'include',
            });

        if (response.ok) {
            let user = await response.json();
            this.saveTokenIntoBrowserStorage(
                response.headers.get('Authorization'),
                user);
            return user;
        } else {
            throw response.body;
        }
    }

    /**
     * Logs out the user and clears the session data.
     */
    signOut() {
        this._currentToken = null;
        this._currentUser = null;
        window.sessionStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME);
    }

    /**
     * Checks if the user is authenticated by verifying if a valid token exists.
     * @returns {boolean} - true if the user is authenticated, if not false.
     */
    isAuthenticated() {
        return this._currentToken;
    }

    /**
     * Saves the token and user details into session storage.
     * @param {string} token - The JWT token
     * @param {Object} user - The user details.
     */
    saveTokenIntoBrowserStorage(token, user) {
        this._currentToken = token;
        this._currentUser = user;

        const sessionData = JSON.stringify({ token, user });
        window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME, sessionData);
    }

    /**
     * Retrieves the token and user details from session storage.
     */
    getTokenFromBrowserStorage() {
        const sessionData = window.sessionStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME);

        if (sessionData) {
            try {
                const { token, user } = JSON.parse(sessionData);
                this._currentToken = token;
                this._currentUser = user;
            } catch (error) {
                console.error("Failed to parse session storage data:", error);
            }
        }
    }

    /**
     * Returns the current JWT token.
     * @returns {string|null} - The JWT token or null if not logged in.
     */
    getToken() {
        return this._currentToken;
    }

    /**
     * Returns the current logged-in user's details.
     * @returns {Object|null} - The user details or null if not logged in.
     */
    getCurrentUser() {
        return this._currentUser;
    }
}