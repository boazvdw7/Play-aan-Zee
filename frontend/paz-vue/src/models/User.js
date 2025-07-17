export default class User {
    username;
    email;
    password;
    token;

    constructor(id, username, email, password, token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.token = token;
    }
}