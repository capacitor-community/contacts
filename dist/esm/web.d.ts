import { WebPlugin } from '@capacitor/core';
import type * as Definitions from './definitions';
export declare class ContactsWeb extends WebPlugin implements Definitions.ContactsPlugin {
    checkPermissions(): Promise<Definitions.PermissionStatus>;
    requestPermissions(): Promise<Definitions.PermissionStatus>;
    getContact(): Promise<Definitions.GetContactResult>;
    getContacts(): Promise<Definitions.GetContactsResult>;
    createContact(): Promise<Definitions.CreateContactResult>;
    deleteContact(): Promise<void>;
    pickContact(): Promise<Definitions.PickContactResult>;
}
