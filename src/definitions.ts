declare module "@capacitor/core" {
  interface PluginRegistry {
    Contacts: ContactsPlugin;
  }
}
export interface PermissionStatus {
  granted: boolean;
}

export interface Contact {
  contactId: string;
  displayName?: string;
  photoThumbnail?: string;
  phoneNumbers: string[];
  emails: string[];
  organizationName?: string;
  organizationRole?: string;
  birthday?: string;
}

export interface ContactsPlugin {
  getPermissions(): Promise<PermissionStatus>;
  getContacts(): Promise<{ contacts: Contact[] }>;
}
