<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Contacts</h3>
<p align="center"><strong><code>@capacitor-community/contacts</code></strong></p>
<p align="center">
  Capacitor community plugin for fetching contacts.
</p>

<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2020?style=flat-square" />
  <a href="https://github.com/capacitor-community/contacts/actions?query=workflow%3A%22CI%22"><img src="https://img.shields.io/github/workflow/status/capacitor-community/contacts/CI?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/contacts"><img src="https://img.shields.io/npm/l/@capacitor-community/contacts?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/contacts"><img src="https://img.shields.io/npm/dw/@capacitor-community/contacts?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/contacts"><img src="https://img.shields.io/npm/v/@capacitor-community/contacts?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
<a href="#contributors-"><img src="https://img.shields.io/badge/all%20contributors-0-orange?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:END -->
</p>

# Capacitor Contacts Plugin

<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->

<!-- ALL-CONTRIBUTORS-BADGE:END -->

Capacitor community plugin for native Contact fetching.

<!-- Badges -->
<a href="https://npmjs.com/package/@capacitor-community/contacts">
  <img src="https://img.shields.io/npm/v/@capacitor-community/contacts.svg">
</a>
<a href="https://npmjs.com/package/@capacitor-community/contacts">
  <img src="https://img.shields.io/npm/l/@capacitor-community/contacts.svg">
</a>

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

Initalize Capacitor

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

For Android you have to add the permisions in your AndroidManifest.xml. Add the following permissions before the closing of the "manifest" tag.

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

If you're considering to use this plugin you most likely want to retrive contacts a users contacts:

Import the Plugin in your TS file:

```
import { Plugins } from "@capacitor/core";
const  { Contacts } = Plugins;
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

That's it. Do Whatever you want with the retrived contacts.

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
  phoneNumbers: string[];
  emails: string[];
  organizationName?: string;
  organizationRole?: string;
  birthday?: string;
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
