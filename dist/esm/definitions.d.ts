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
export declare enum PhoneType {
    Home = "home",
    Work = "work",
    Other = "other",
    Custom = "custom",
    Mobile = "mobile",
    FaxWork = "fax_work",
    FaxHome = "fax_home",
    Pager = "pager",
    Callback = "callback",
    Car = "car",
    CompanyMain = "company_main",
    Isdn = "isdn",
    Main = "main",
    OtherFax = "other_fax",
    Radio = "radio",
    Telex = "telex",
    TtyTdd = "tty_tdd",
    WorkMobile = "work_mobile",
    WorkPager = "work_pager",
    Assistant = "assistant",
    Mms = "mms"
}
export declare enum EmailType {
    Home = "home",
    Work = "work",
    Other = "other",
    Custom = "custom",
    Mobile = "mobile"
}
export declare enum PostalAddressType {
    Home = "home",
    Work = "work",
    Other = "other",
    Custom = "custom"
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
export interface ImageInput {
    /**
     * Base64 encoded image.
     *
     * @example "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII="
     */
    base64String?: string | null;
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
    image?: ImageInput | null;
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
