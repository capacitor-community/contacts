import { WebPlugin } from '@capacitor/core';
export class ContactsWeb extends WebPlugin {
    async checkPermissions() {
        throw this.unimplemented('Not implemented on web.');
    }
    async requestPermissions() {
        throw this.unimplemented('Not implemented on web.');
    }
    async getContact() {
        throw this.unimplemented('Not implemented on web.');
    }
    async getContacts() {
        throw this.unimplemented('Not implemented on web.');
    }
    async createContact() {
        throw this.unimplemented('Not implemented on web.');
    }
    async deleteContact() {
        throw this.unimplemented('Not implemented on web.');
    }
    async pickContact() {
        throw this.unimplemented('Not implemented on web.');
    }
}
//# sourceMappingURL=web.js.map