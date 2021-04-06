import { WebPlugin } from "@capacitor/core";
import { ContactsPlugin, PermissionStatus, Contact } from "./definitions";


export class ContactsPluginWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super({
      name: "CapContacts",
      platforms: ["web"],
    });
  }

  async getPermissions(): Promise<PermissionStatus> {
    throw new Error("getPermission not available");
  }

  async getContacts(): Promise<{ contacts: Contact[] }> {
    throw new Error("getContacts not available");
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

const Contacts = new ContactsPluginWeb();

export { Contacts };

