# Installation

## Install package from npm

## For projects using Capacitor v7

```bash
npm install @capacitor-community/contacts@^7.0.0
npx cap sync
```


## For projects using Capacitor v6

```bash
npm install @capacitor-community/contacts@^6.0.0
npx cap sync
```


## For projects using Capacitor v5

```bash
npm install @capacitor-community/contacts@^5.0.0
npx cap sync
```

## For projects using Capacitor v4

```bash
npm install @capacitor-community/contacts@^4.0.0
npx cap sync

```

## For projects using Capacitor v3

```bash
npm install @capacitor-community/contacts@^3.0.0
npx cap sync
```

More info about previous versions of this plugin can be found in the [README](https://github.com/capacitor-community/contacts/blob/main/README.md#versions)

## Setup

### iOS

Apple requires the following usage description to be added and filled out for your app in `Info.plist`:

- `NSContactsUsageDescription` (`Privacy - Contacts Usage Description`)

Read about [Configuring `Info.plist`](https://capacitorjs.com/docs/ios/configuration#configuring-infoplist) in the [iOS Guide](https://capacitorjs.com/docs/ios) for more information on setting iOS permissions in Xcode

#### Accessing the notes in contact entries

To be able to request the notes in contract entries in iOS 13 or later, your app must have the `com.apple.developer.contacts.notes` entitlement. You can read more about this in the [official Apple documentation](https://developer.apple.com/documentation/bundleresources/entitlements/com_apple_developer_contacts_notes).

### Android

This API requires the following permissions to be added to your `AndroidManifest.xml`:

```xml
<!-- Contacts API -->
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.WRITE_CONTACTS" />
```

As the naming itself suggests, the first permission is needed for reading contact entries (e.g. when calling the `getContacts` method) and the second permission is needed for writing contact entries (e.g. when calling the `createContact` method).

Read about [Setting Permissions](https://capacitorjs.com/docs/android/configuration#setting-permissions) in the [Android Guide](https://capacitorjs.com/docs/android) for more information on setting Android permissions.

## What's next?

After you've followed all the installation steps, you can begin implementing this plugin in your code.

- Read the Guides to learn more about specific methods. Such as [retrieving contacts](guide-retrieving-contacts.md) or [creating contacts](guide-creating-contacts.md)
- Refer to the [API Reference](api.md#api-reference-🔌) to see all available methods the plugin offers.
<!-- - Take a look at some examples [here](https://github.com/capacitor-community/contacts-examples). -->
