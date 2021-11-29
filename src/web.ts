import { WebPlugin } from '@capacitor/core';

import type { GrantStatus, NewContact } from '.';
import type { ContactsPlugin, PermissionStatus, Contact } from './definitions';

export class ContactsPluginWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super();
  }

  async getPermissions(): Promise<PermissionStatus> {
    throw this.unimplemented('getPermissions - Not implemented on web.');
  }

  async hasPermission(): Promise<GrantStatus> {
    throw this.unimplemented('hasPermissions - Not implemented on web.');
  }

  async getContacts(): Promise<{ contacts: Contact[] }> {
    throw this.unimplemented('getContacts - Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  async saveContact(_: NewContact): Promise<void> {
    throw this.unimplemented('saveContact - Not implemented on web.');
  }
}

const Contacts = new ContactsPluginWeb();

export { Contacts };
