# Retrieving contacts

## Retrieve a list of contacts

Retrieving a list of contacts is quite simple. Just call the `getContacts` method and specify which fields should be returned. For example:

```typescript
import { Contacts } from '@capacitor-community/contacts';

const retrieveListOfContacts = async () => {
  const projection = {
    // Specify which fields should be retrieved.
    name: true,
    phones: true,
    postalAddresses: true,
  };

  const result = await Contacts.getContacts({
    projection,
  });
};
```

## About `projection`

The example above, will return a list of contacts. Because of the `projection` we specified, the contacts will only have four properties: `contactId` (this property will always be returned), `name`, `phones` and `postalAddress`. If we would change the `projection` to, for example:

```typescript
const projection = {
  name: true,
};
```

It would thus only return two properties: `contactId` and `name`.

This is done to keep the performance at its best. Because always retrieving all possible fields, would quickly take up a lot of ram when a large number of contacts is present in the device's phonebook.

All fields available for `projection` can be found in the [API reference](api.md#projection).

!> To be able to request the notes in contract entries in **iOS 13 or later**, your app must have the `com.apple.developer.contacts.notes` entitlement. You can read more about this in the [official Apple documentation](https://developer.apple.com/documentation/bundleresources/entitlements/com_apple_developer_contacts_notes).

## About `contactId`

Both Android and iOS, unfortunately, do not garantuee the `contactId` will always remain the same. In fact, they can change quite regularly, for example when a contact is updated. So always be sure to retrieve a fresh list of contacts before using the `contactId` to, for example, delete a contact.

## About `type` and `label`

`PhonePayload`, `EmailPayload` and `PostalAddressPayload` all have both a `type` and `label` field attached to them. The `PostalAddressPayload.type` can be `PostalAddressType.Home` for example, indicating that this is the contact's home address. `type` can also be `.Custom`. In that case the `label` field will be available and supplied with the custom value. This can be any arbitrary string value, for example `"foobar"`.
