// declare module "@capacitor/core" {
//   interface PluginRegistry {
//     Contacts: ContactsPlugin;
//   }
// }

export interface ContactsPlugin {
  getPermissions(): Promise<PermissionStatus>;
  getContacts(): Promise<{ contacts: Contact[] }>;
  saveContact(contact: NewContact): Promise<void>;
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

export interface UrlAddress {
  label?: string;
  url?: string;
}

export interface SocialProfile {
  label?: string;
  profile?: {
    username?: string;
    service?: string;
    urlString?: string;
  };
}

export interface PostalAddress {
  label?: string;
  address?: {
    street?: string;
    city?: string;
    state?: string;
    postalCode?: string;
    country?: string;
  };
}

export enum ContactType {
  Person,
  Organization,
}

export interface Contact {
  contactId: string;
  displayName?: string;
  phoneNumbers: PhoneNumber[];
  emails: EmailAddress[];
  photoThumbnail?: string;
  organizationName?: string;
  organizationRole?: string;
  birthday?: string;
}

/**
 * New contact schema.
 *
 * @see https://developer.apple.com/documentation/contacts/cnmutablecontact
 * @see android-link...
 */
export interface NewContact {
  contactType?: ContactType;

  // Name information
  namePrefix?: string;
  givenName?: string;
  middleName?: string;
  familyName?: string;
  nameSuffix?: string;
  nickname?: string;

  // Work information
  jobTitle?: string;
  departmentName?: string;
  organizationName?: string;

  // Addresses
  postalAddresses?: PostalAddress[];
  emailAddresses?: EmailAddress[];
  urlAddresses?: UrlAddress[];

  // Other
  phoneNumbers?: PhoneNumber[];
  birthday?: string;
  note?: string;
  socialProfiles?: SocialProfile[];

  /** Base64 image */
  image?: string;
}
