declare module "@capacitor/core" {
  interface PluginRegistry {
    Contacts: ContactsPlugin;
  }
}
export interface PermissionStatus {
  granted: boolean;
}

export interface PhoneNumber {
  label?: string;
  number?: string;
}

export interface EmailAddress {
  label?: string;
  address?: string;
}

export interface Contact {
  contactId: string;
  displayName?: string;
  phoneNumbers: PhoneNumber[];
  emails: EmailAddress[];
  organizationName?: string;
  organizationRole?: string;
  birthday?: string;
}

export interface ContactsPlugin {
  getPermissions(): Promise<PermissionStatus>;
  getContacts(): Promise<{ contacts: Contact[] }>;
}
