<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>

<h3 align="center">Contacts</h3>
<p align="center"><strong><code>@capacitor-community/contacts</code></strong></p>
<p align="center">
  Capacitor community plugin for fetching contacts.
</p>

<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2021?style=flat-square" />
  <a href="https://github.com/capacitor-community/contacts/actions?query=workflow%3A%22Test+and+Build+Plugin%22"><img src="https://img.shields.io/github/workflow/status/capacitor-community/contacts/Test%20and%20Build%20Plugin?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/contacts"><img src="https://img.shields.io/npm/l/@capacitor-community/contacts?style=flat-square" /></a>
  <br>
  <a href="https://www.npmjs.com/package/@capacitor-community/contacts"><img src="https://img.shields.io/npm/dw/@capacitor-community/contacts?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/contacts"><img src="https://img.shields.io/npm/v/@capacitor-community/contacts?style=flat-square" /></a>
</p>

## Maintainers

| Maintainer                           | GitHub                                                                                       | Social                       | Sponsoring Company |
| ------------------------------------ | -------------------------------------------------------------------------------------------- | ---------------------------- | ------------------ |
| Jonathan Gerber / Byrds & Bytes GmbH | [idrimi](https://github.com/idrimi) / [Byrds & Bytes GmbH](https://github.com/byrdsandbytes) | [byrds.ch](https://byrds.ch) | Byrds & Bytes GmbH |

Maintenance Status: Actively Maintained

## Demo

You can find a working Ionic App using the Byrds' Capacitor Contacts plugin here:
https://github.com/byrdsandbytes/capContactsDemo/tree/capacitor-community

## Prerequisites

Setup your project with Capacitor. For details check here: https://capacitorjs.com

```
cd my-app
npm install --save @capacitor/core @capacitor/cli
```

Initialize Capacitor

```
npx cap init
```

Add the platforms you want to use.

```
npx cap add android
npx cap add ios
npx cap add electron
```

## Installation

Install:

```
npm i --save @capacitor-community/contacts
```

Sync:

```
npx cap sync
```

### iOS

For iOS you need to set a usage description in your info.plist file. (Privacy Setting)
Open xCode search for your info.plist file and press the tiny "+". Add the following entry:

```
Privacy - Contacts Usage Description
```

Give it a value like:

```
"We need access to your contacts in order to do something."
```

### Android Notes

For Android you have to add the permissions in your AndroidManifest.xml. Add the following permissions before the closing of the "manifest" tag.

```
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.WRITE_CONTACTS"/>
```

Next import the capContacts class to your MainActivity

```
// Initializes the Bridge
    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
      // Additional plugins you've installed go here
      // Ex: add(TotallyAwesomePlugin.class);
      add(Contacts.class);
    }});
```

Make sure to import it properly as well.

```
import ch.byrds.capacitor.contacts.Contacts;
```

**NOTE**: On Android you have to ask for permission first, before you can fetch the contacts. Use the `getPermissions()` method before you try to fetch contacts using `getContacts()`.

## Usage / Examples

You have the following Methods available:

```
export interface ContactsPlugin {
  getPermissions(): Promise<PermissionStatus>;
  getContacts(): Promise<{contacts: Contact[]}>;
}
```

If you're considering to use this plugin you most likely want to retrieve contacts a users contacts:

Import the Plugin in your TS file:

```
import { Contacts } from '@capacitor-community/contacts'
```

Next use it and console log the result:

```
Contacts.getContacts().then(result => {
    console.log(result);
    for (const contact of result.contacts) {
        console.log(contact);
    }
});

```

That's it. Do Whatever you want with the retrieved contacts.

If you're trying to build something like "contacts matching" based on phone numbers i recommend using google libphonenumber: https://www.npmjs.com/package/google-libphonenumber

In order to match them properly you need to format them before you can match or store them properly.

### Interfaces

```
export interface PermissionStatus {
  granted: boolean;
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

export interface PhoneNumber {
  label?: string;
  number?: string;
}

export interface EmailAddress {
  label?: string;
  address?: string;
}
```

## Built With

- Swift 5
- Java
- Angular
- Capacitor

## Authors

- Jonathan Gerber ([idrimi](https://github.com/idrimi))

## License

MIT

## Contributors

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/Idrimi"><img src="https://avatars0.githubusercontent.com/u/24573405?v=4?s=100" width="100px;" alt=""/><br /><sub><b>idrimi</b></sub></a><br /><a href="https://github.com/capacitor-community/contacts/commits?author=Idrimi" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/tafelnl"><img src="https://avatars2.githubusercontent.com/u/35837839?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Tafel</b></sub></a><br /><a href="https://github.com/capacitor-community/contacts/commits?author=tafelnl" title="Code">💻</a></td>
    <td align="center"><a href="http://ionicframework.com/"><img src="https://avatars3.githubusercontent.com/u/11214?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Max Lynch</b></sub></a><br /><a href="https://github.com/capacitor-community/contacts/commits?author=mlynch" title="Documentation">📖</a> <a href="#eventOrganizing-mlynch" title="Event Organizing">📋</a></td>
    <td align="center"><a href="https://github.com/david-garzon-adl"><img src="https://avatars0.githubusercontent.com/u/45822796?v=4?s=100" width="100px;" alt=""/><br /><sub><b>David Javier Garzon Carrillo</b></sub></a><br /><a href="https://github.com/capacitor-community/contacts/commits?author=david-garzon-adl" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/vhinic"><img src="https://avatars.githubusercontent.com/u/244439?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Vladimir Hinić</b></sub></a><br /><a href="https://github.com/capacitor-community/contacts/commits?author=vhinic" title="Code">💻</a></td>
    <td align="center"><a href="https://t.me/reslear"><img src="https://avatars.githubusercontent.com/u/12596485?v=4?s=100" width="100px;" alt=""/><br /><sub><b>reslear</b></sub></a><br /><a href="https://github.com/capacitor-community/contacts/commits?author=reslear" title="Documentation">📖</a></td>
    <td align="center"><a href="https://marvin.digital/"><img src="https://avatars.githubusercontent.com/u/11534760?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Marvin Heilemann</b></sub></a><br /><a href="https://github.com/capacitor-community/contacts/commits?author=muuvmuuv" title="Code">💻</a> <a href="https://github.com/capacitor-community/contacts/commits?author=muuvmuuv" title="Documentation">📖</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

## API

<docgen-index>

* [`getPermissions()`](#getpermissions)
* [`getContacts()`](#getcontacts)
* [`saveContact(...)`](#savecontact)
* [Interfaces](#interfaces)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getPermissions()

```typescript
getPermissions() => any
```

**Returns:** <code>any</code>

--------------------


### getContacts()

```typescript
getContacts() => any
```

**Returns:** <code>any</code>

--------------------


### saveContact(...)

```typescript
saveContact(contact: NewContact) => any
```

| Param         | Type                                              |
| ------------- | ------------------------------------------------- |
| **`contact`** | <code><a href="#newcontact">NewContact</a></code> |

**Returns:** <code>any</code>

--------------------


### Interfaces


#### PermissionStatus

| Prop          | Type                 |
| ------------- | -------------------- |
| **`granted`** | <code>boolean</code> |


#### Contact

| Prop                   | Type                |
| ---------------------- | ------------------- |
| **`contactId`**        | <code>string</code> |
| **`displayName`**      | <code>string</code> |
| **`phoneNumbers`**     | <code>{}</code>     |
| **`emails`**           | <code>{}</code>     |
| **`photoThumbnail`**   | <code>string</code> |
| **`organizationName`** | <code>string</code> |
| **`organizationRole`** | <code>string</code> |
| **`birthday`**         | <code>string</code> |


#### PhoneNumber

| Prop         | Type                |
| ------------ | ------------------- |
| **`label`**  | <code>string</code> |
| **`number`** | <code>string</code> |


#### EmailAddress

| Prop          | Type                |
| ------------- | ------------------- |
| **`label`**   | <code>string</code> |
| **`address`** | <code>string</code> |


#### NewContact

New contact schema.

| Prop                   | Type                                                | Description  |
| ---------------------- | --------------------------------------------------- | ------------ |
| **`contactType`**      | <code><a href="#contacttype">ContactType</a></code> |              |
| **`namePrefix`**       | <code>string</code>                                 |              |
| **`givenName`**        | <code>string</code>                                 |              |
| **`middleName`**       | <code>string</code>                                 |              |
| **`familyName`**       | <code>string</code>                                 |              |
| **`nameSuffix`**       | <code>string</code>                                 |              |
| **`nickname`**         | <code>string</code>                                 |              |
| **`jobTitle`**         | <code>string</code>                                 |              |
| **`departmentName`**   | <code>string</code>                                 |              |
| **`organizationName`** | <code>string</code>                                 |              |
| **`postalAddresses`**  | <code>{}</code>                                     |              |
| **`emailAddresses`**   | <code>{}</code>                                     |              |
| **`urlAddresses`**     | <code>{}</code>                                     |              |
| **`phoneNumbers`**     | <code>{}</code>                                     |              |
| **`birthday`**         | <code>string</code>                                 |              |
| **`note`**             | <code>string</code>                                 |              |
| **`socialProfiles`**   | <code>{}</code>                                     |              |
| **`image`**            | <code>string</code>                                 | Base64 image |


#### PostalAddress

| Prop          | Type                                                                                                    |
| ------------- | ------------------------------------------------------------------------------------------------------- |
| **`label`**   | <code>string</code>                                                                                     |
| **`address`** | <code>{ street?: string; city?: string; state?: string; postalCode?: string; country?: string; }</code> |


#### UrlAddress

| Prop        | Type                |
| ----------- | ------------------- |
| **`label`** | <code>string</code> |
| **`url`**   | <code>string</code> |


#### SocialProfile

| Prop          | Type                                                                      |
| ------------- | ------------------------------------------------------------------------- |
| **`label`**   | <code>string</code>                                                       |
| **`profile`** | <code>{ username?: string; service?: string; urlString?: string; }</code> |


### Enums


#### ContactType

| Members            |
| ------------------ |
| **`Person`**       |
| **`Organization`** |

</docgen-api>
