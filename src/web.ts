import { WebPlugin } from '@capacitor/core';

import type { NewContact } from '.';
import type { ContactsPlugin, PermissionStatus, Contact } from './definitions';

export class ContactsPluginWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super({
      name: 'CapContacts',
      platforms: ['web'],
    });
  }

  async getPermissions(): Promise<PermissionStatus> {
    throw new Error('getPermission not available');
  }

  async getContacts(): Promise<{ contacts: Contact[] }> {
    throw new Error('getContacts not available');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  async saveContact(_: NewContact): Promise<void> {
    throw new Error('getContacts not available');
  }
}

const Contacts = new ContactsPluginWeb();

export { Contacts };
