# Creating contacts

Creating a new contact is quite simple. Just call the `createContact` method and specify the data for the new contact. For example:

```typescript
import { Contacts, PhoneType, EmailType } from '@capacitor-community/contacts';

const createNewContact = async () => {
  const res = await Contacts.createContact({
    contact: {
      name: {
        given: 'John',
        family: 'Doe',
      },
      birthday: {
        year: 1990,
        month: 1,
        day: 1,
      },
      phones: [
        {
          type: PhoneType.Mobile,
          label: 'mobile',
          number: '+1-212-456-7890',
        },
        {
          type: PhoneType.Work,
          label: 'work',
          number: '212-456-7890',
        },
      ],
      emails: [
        {
          type: EmailType.Work,
          label: 'work',
          address: 'example@example.com',
        },
      ],
      urls: ['example.com'],
    },
  });

  console.log(res.contactId);
};
```
