import type { PermissionState } from '@capacitor/core';

export interface PermissionStatus {
  contacts: PermissionState;
}

export interface ContactsPlugin {
  checkPermissions(): Promise<PermissionStatus>;
  requestPermissions(): Promise<PermissionStatus>;
  getContact(options: GetContactOptions): Promise<GetContactResult>;
  getContacts(options: GetContactsOptions): Promise<GetContactsResult>;
  createContact(options: CreateContactOptions): Promise<CreateContactResult>;
  deleteContact(options: DeleteContactOptions): Promise<void>;
  pickContact(options: PickContactOptions): Promise<PickContactResult>;
}

export enum PhoneType {
  // Home / Work
  Home = 'home',
  Work = 'work',
  // Other / Custom
  Other = 'other',
  Custom = 'custom',
  // Phone specific
  Mobile = 'mobile',
  FaxWork = 'fax_work',
  FaxHome = 'fax_home',
  Pager = 'pager',
  Callback = 'callback',
  Car = 'car',
  CompanyMain = 'company_main',
  Isdn = 'isdn',
  Main = 'main',
  OtherFax = 'other_fax',
  Radio = 'radio',
  Telex = 'telex',
  TtyTdd = 'tty_tdd',
  WorkMobile = 'work_mobile',
  WorkPager = 'work_pager',
  Assistant = 'assistant',
  Mms = 'mms',
}

export enum EmailType {
  // Home / Work
  Home = 'home',
  Work = 'work',
  // Other / Custom
  Other = 'other',
  Custom = 'custom',
  // Email specific
  Mobile = 'mobile',
}

export enum PostalAddressType {
  // Home / Work
  Home = 'home',
  Work = 'work',
  // Other / Custom
  Other = 'other',
  Custom = 'custom',
}

export interface Projection {
  /**
   * @default false
   */
  name?: boolean;

  /**
   * @default false
   */
  organization?: boolean;

  /**
   * @default false
   */
  birthday?: boolean;

  /**
   * @default false
   */
  note?: boolean;

  /**
   * @default false
   */
  phones?: boolean;

  /**
   * @default false
   */
  emails?: boolean;

  /**
   * @default false
   */
  urls?: boolean;

  /**
   * @default false
   */
  postalAddresses?: boolean;

  /**
   * Be careful! This can potentially slow down your query by a large factor.
   *
   * @default false
   */
  image?: boolean;
}

export interface GetContactOptions {
  contactId: string;
  projection: Projection;
}

export interface GetContactResult {
  contact: ContactPayload;
}

export interface GetContactsOptions {
  projection: Projection;
}

export interface GetContactsResult {
  contacts: ContactPayload[];
}

export interface NamePayload {
  display: string | null;
  given: string | null;
  middle: string | null;
  family: string | null;
  prefix: string | null;
  suffix: string | null;
}

export interface OrganizationPayload {
  company: string | null;
  jobTitle: string | null;
  department: string | null;
}

export interface BirthdayPayload {
  day?: number | null;
  month?: number | null;
  year?: number | null;
}

export interface PhonePayload {
  type: PhoneType;
  label?: string | null;
  isPrimary?: boolean | null;

  number: string | null;
}

export interface EmailPayload {
  type: EmailType;
  label?: string | null;
  isPrimary?: boolean | null;

  address: string | null;
}

export interface PostalAddressPayload {
  type: PostalAddressType;
  label?: string | null;
  isPrimary?: boolean | null;

  street?: string | null;
  neighborhood?: string | null;
  city?: string | null;
  region?: string | null;
  postcode?: string | null;
  country?: string | null;
}

export interface ImagePayload {
  base64String?: string | null;
}

export interface ContactPayload {
  contactId: string;

  /**
   * Object holding the name data
   */
  name?: NamePayload;

  /**
   * Object holding the organization data
   */
  organization?: OrganizationPayload;

  /**
   * Birthday
   */
  birthday?: BirthdayPayload | null;

  /**
   * Note
   */
  note?: string | null;

  /**
   * Phones
   */
  phones?: PhonePayload[];

  /**
   * Emails
   */
  emails?: EmailPayload[];

  /**
   * URLs
   */
  urls?: (string | null)[];

  /**
   * Postal Addresses
   */
  postalAddresses?: PostalAddressPayload[];

  /**
   * Image
   */
  image?: ImagePayload;
}

export interface CreateContactOptions {
  contact: ContactInput;
}

export interface NameInput {
  given?: string | null;
  middle?: string | null;
  family?: string | null;
  prefix?: string | null;
  suffix?: string | null;
}

export interface OrganizationInput {
  company?: string | null;
  jobTitle?: string | null;
  department?: string | null;
}

export interface BirthdayInput {
  day: number;
  month: number;
  year?: number;
}

export interface PhoneInput {
  type: PhoneType;
  label?: string | null;
  isPrimary?: boolean;

  number: string | null;
}

export interface EmailInput {
  type: EmailType;
  label?: string | null;
  isPrimary?: boolean;

  address: string | null;
}

export interface PostalAddressInput {
  type: PostalAddressType;
  label?: string | null;
  isPrimary?: boolean;

  street?: string | null;
  neighborhood?: string | null;
  city?: string | null;
  region?: string | null;
  postcode?: string | null;
  country?: string | null;
}

export interface ContactInput {
  /**
   * Object holding the name data
   */
  name?: NameInput;

  /**
   * Object holding the organization data
   */
  organization?: OrganizationInput;

  /**
   * Birthday
   */
  birthday?: BirthdayInput | null;

  /**
   * Note
   */
  note?: string | null;

  /**
   * Phones
   */
  phones?: PhoneInput[];

  /**
   * Emails
   */
  emails?: EmailInput[];

  /**
   * URLs
   */
  urls?: string[];

  /**
   * Postal Addresses
   */
  postalAddresses?: PostalAddressInput[];

  /**
   * Image
   */
  // @TODO:
  // image?: ImageInput | null;
}

export interface CreateContactResult {
  contactId: string;
}

export interface DeleteContactOptions {
  contactId: string;
}

export interface PickContactOptions {
  projection: Projection;
}

export interface PickContactResult {
  contact: ContactPayload;
}
