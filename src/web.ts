import { WebPlugin } from "@capacitor/core";
import { ContactsPlugin, PermissionStatus, Contact } from "./definitions";


export class ContactsPluginWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super();
  }

  async getPermissions(): Promise<PermissionStatus> {
    throw new Error("getPermission not available");
  }

  async getContacts(): Promise<{ contacts: Contact[] }> {
    throw new Error("getContacts not available");
  }
}

const Contacts = new ContactsPluginWeb();

export { Contacts };

