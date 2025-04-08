import { WebPlugin } from '@capacitor/core';

import type * as Definitions from './definitions';

export class ContactsWeb extends WebPlugin implements Definitions.ContactsPlugin {
  async checkPermissions(): Promise<Definitions.PermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }

  async requestPermissions(): Promise<Definitions.PermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getContact(): Promise<Definitions.GetContactResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getContacts(): Promise<Definitions.GetContactsResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async createContact(): Promise<Definitions.CreateContactResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async deleteContact(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async pickContact(): Promise<Definitions.PickContactResult> {
    throw this.unimplemented('Not implemented on web.');
  }
}
