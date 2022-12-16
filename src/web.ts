import { WebPlugin } from '@capacitor/core';

import type { ContactsPlugin } from './definitions';

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
