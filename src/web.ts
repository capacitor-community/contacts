import { WebPlugin } from "@capacitor/core";
import { ContactsPlugin, PermissionStatus, Contact } from "./definitions";


export class ContactsPluginWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super();
  }

  async getPermissions(): Promise<PermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getContacts(): Promise<{ contacts: Contact[] }> {
    throw this.unimplemented('Not implemented on web.');
  }
}

const Contacts = new ContactsPluginWeb();

export { Contacts };

