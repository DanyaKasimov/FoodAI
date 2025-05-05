export class ApiClientException extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'ApiClientException';

        Object.setPrototypeOf(this, new.target.prototype);
    }
}
