# Quick start

Before you continue, make sure you have followed all the steps from the [Installation section](getting-started-installation.md).

## Print some data to the console for a list of contacts

```typescript
import { Contacts } from '@capacitor-community/contacts';

const printContactsData = async () => {
  const result = await Contacts.getContacts({
    projection: {
      // Specify which fields should be retrieved.
      name: true,
      phones: true,
      postalAddresses: true,
    },
  });

  for (const contact of result.contacts) {
    const number = contact.phones?.[0]?.number;

    const street = contact.postalAddresses?.[0]?.street;

    console.log(number, street);
  }
};
```

## What's next?

As the previous example shows, it is really easy to use this plugin. But, of course, there are many more possibilities.

- Read the Guides to learn more about the plugin.
- Refer to the [API Reference](api.md#api-reference-🔌) to see all available methods the plugin offers.
<!-- - Take a look at some examples [here](https://github.com/capacitor-community/contacts-examples). -->
