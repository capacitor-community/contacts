import { registerPlugin } from '@capacitor/core';
const Contacts = registerPlugin('Contacts', {
    web: () => import('./web').then((m) => new m.ContactsWeb()),
});
export * from './definitions';
export { Contacts };
//# sourceMappingURL=index.js.map