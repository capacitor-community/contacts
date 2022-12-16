import { registerPlugin } from '@capacitor/core';

import type { ContactsPlugin } from './definitions';

const Contacts = registerPlugin<ContactsPlugin>('Contacts', {
  web: () => import('./web').then(m => new m.ContactsWeb()),
});

export * from './definitions';
export { Contacts };
