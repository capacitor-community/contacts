package getcapacitor.community.contacts;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.RawContacts;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

public class Contacts {

    private final Activity mActivity;

    public static BiMap<String, Integer> phoneTypeMap = new BiMap<String, Integer>(
        new HashMap<String, Integer>() {
            {
                // Home / Work
                put("home", Phone.TYPE_HOME);
                put("work", Phone.TYPE_WORK);
                // Other
                put("other", Phone.TYPE_OTHER);
                // Custom
                put("custom", Phone.TYPE_CUSTOM);
                // Phone specific
                put("main", Phone.TYPE_MAIN);
                put("mobile", Phone.TYPE_MOBILE);
                put("pager", Phone.TYPE_PAGER);
                put("fax_work", Phone.TYPE_FAX_WORK);
                put("fax_home", Phone.TYPE_FAX_HOME);
                put("fax_other", Phone.TYPE_OTHER_FAX);
                //
                //
                // Android only:
                // Phone specific
                put("callback", Phone.TYPE_CALLBACK);
                put("car", Phone.TYPE_CAR);
                put("company_main", Phone.TYPE_COMPANY_MAIN);
                put("isdn", Phone.TYPE_ISDN);
                put("radio", Phone.TYPE_RADIO);
                put("telex", Phone.TYPE_TELEX);
                put("tty_tdd", Phone.TYPE_TTY_TDD);
                put("work_mobile", Phone.TYPE_WORK_MOBILE);
                put("work_pager", Phone.TYPE_WORK_PAGER);
                put("assistant", Phone.TYPE_ASSISTANT);
                put("mms", Phone.TYPE_MMS);
            }
        },
        "custom",
        Phone.TYPE_CUSTOM
    );

    public static BiMap<String, Integer> emailTypeMap = new BiMap<String, Integer>(
        new HashMap<String, Integer>() {
            {
                // Home / Work
                put("home", Email.TYPE_HOME);
                put("work", Email.TYPE_WORK);
                // Other
                put("other", Email.TYPE_OTHER);
                // Custom
                put("custom", Email.TYPE_CUSTOM);
                //
                //
                // Android only:
                // Email specific
                put("mobile", Email.TYPE_MOBILE);
            }
        },
        "custom",
        Email.TYPE_CUSTOM
    );

    public static BiMap<String, Integer> postalAddressTypeMap = new BiMap<String, Integer>(
        new HashMap<String, Integer>() {
            {
                // Home / Work
                put("home", StructuredPostal.TYPE_HOME);
                put("work", StructuredPostal.TYPE_WORK);
                // Other
                put("other", StructuredPostal.TYPE_OTHER);
                // Custom
                put("custom", StructuredPostal.TYPE_CUSTOM);
            }
        },
        "custom",
        StructuredPostal.TYPE_CUSTOM
    );

    Contacts(Activity activity) {
        this.mActivity = activity;
    }

    public ContactPayload getContact(@NonNull String contactId, GetContactsProjectionInput projectionInput) {
        String[] projection = projectionInput.getProjection();

        String selection = ContactsContract.RawContacts.CONTACT_ID + " = ?";
        String[] selectionArgs = new String[] { contactId };

        ContentResolver cr = this.mActivity.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null && cursor.getCount() > 0) {
            ContactPayload contact = new ContactPayload(contactId);

            while (cursor.moveToNext()) {
                contact.fillDataByCursor(cursor);
            }
            cursor.close();

            return contact;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public HashMap<String, ContactPayload> getContacts(GetContactsProjectionInput projectionInput) {
        String[] projection = projectionInput.getProjection();

        // String[] selectionArgs = projectionInput.getSelectionArgs();

        // String selection = GetContactsProjectionInput.getSelection(selectionArgs);

        HashMap<String, ContactPayload> contacts = new HashMap<>();

        ContentResolver cr = this.mActivity.getContentResolver();
        // Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selectionArgs.length > 0 ? selection : null, selectionArgs.length > 0 ? selectionArgs : null, null);
        Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, projection, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // String _id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));

                ContactPayload contact;

                if (!contacts.containsKey(contactId)) {
                    contact = new ContactPayload(contactId);
                    contacts.put(contactId, contact);
                } else {
                    contact = contacts.get(contactId);
                }

                if (contact != null) {
                    contact.fillDataByCursor(cursor);
                }
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return contacts;
    }

    public String createContact(CreateContactInput contactInput) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
            .withValue(RawContacts.ACCOUNT_TYPE, null)
            .withValue(RawContacts.ACCOUNT_NAME, null);
        ops.add(op.build());

        // Name
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            // Add to this key
            .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
            // Data
            // .withValue(StructuredName.DISPLAY_NAME, name)
            .withValue(StructuredName.GIVEN_NAME, contactInput.nameGiven)
            .withValue(StructuredName.MIDDLE_NAME, contactInput.nameMiddle)
            .withValue(StructuredName.FAMILY_NAME, contactInput.nameFamily)
            .withValue(StructuredName.PREFIX, contactInput.namePrefix)
            .withValue(StructuredName.SUFFIX, contactInput.nameSuffix);
        ops.add(op.build());

        // Organization
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            // Add to this key
            .withValue(ContactsContract.Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE)
            // Data
            .withValue(Organization.COMPANY, contactInput.organizationName)
            .withValue(Organization.TITLE, contactInput.organizationJobTitle)
            .withValue(Organization.DEPARTMENT, contactInput.organizationDepartment);
        ops.add(op.build());

        // Birthday
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            // Add to this key
            .withValue(ContactsContract.Data.MIMETYPE, Event.CONTENT_ITEM_TYPE)
            // Data
            .withValue(Event.START_DATE, contactInput.birthday)
            .withValue(Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY);
        ops.add(op.build());

        // Note
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            // Add to this key
            .withValue(ContactsContract.Data.MIMETYPE, Note.CONTENT_ITEM_TYPE)
            // Data
            .withValue(Note.NOTE, contactInput.note);
        ops.add(op.build());

        // @TODO not sure where to allow yields
        // https://www.grokkingandroid.com/androids-contentprovideroperation-withyieldallowed-explained/
        op.withYieldAllowed(true);

        // Phones
        for (int i = 0; i < contactInput.phones.size(); i++) {
            CreateContactInput.PhoneInput phone = contactInput.phones.get(i);

            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                // Add to this key
                .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                // Data
                .withValue(Phone.TYPE, phone.type)
                .withValue(Phone.LABEL, phone.label)
                .withValue(Phone.NUMBER, phone.number);
            if (phone.isPrimary) {
                op.withValue(Phone.IS_PRIMARY, true);
            }
            ops.add(op.build());
        }

        // Emails
        for (int i = 0; i < contactInput.emails.size(); i++) {
            CreateContactInput.EmailInput email = contactInput.emails.get(i);

            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                // Add to this key
                .withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
                // Data
                .withValue(Email.TYPE, email.type)
                .withValue(Email.LABEL, email.label)
                .withValue(Email.ADDRESS, email.address);
            if (email.isPrimary) {
                op.withValue(Email.IS_PRIMARY, true);
            }
            ops.add(op.build());
        }

        // URLs
        for (int i = 0; i < contactInput.urls.size(); i++) {
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                // Add to this key
                .withValue(ContactsContract.Data.MIMETYPE, Website.CONTENT_ITEM_TYPE)
                // Data
                .withValue(Website.URL, contactInput.urls.get(i));
            ops.add(op.build());
        }

        // Postal Addresses
        for (int i = 0; i < contactInput.postalAddresses.size(); i++) {
            CreateContactInput.PostalAddressInput address = contactInput.postalAddresses.get(i);

            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                // Add to this key
                .withValue(ContactsContract.Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
                // Data
                .withValue(StructuredPostal.TYPE, address.type)
                .withValue(StructuredPostal.LABEL, address.label)
                .withValue(StructuredPostal.STREET, address.street)
                .withValue(StructuredPostal.NEIGHBORHOOD, address.neighborhood)
                .withValue(StructuredPostal.CITY, address.city)
                .withValue(StructuredPostal.REGION, address.region)
                .withValue(StructuredPostal.POSTCODE, address.postcode)
                .withValue(StructuredPostal.COUNTRY, address.country);
            if (address.isPrimary) {
                op.withValue(StructuredPostal.IS_PRIMARY, true);
            }
            ops.add(op.build());
        }

        // Image
        if (contactInput.image != null && contactInput.image.base64String != null) {
            byte[] photoData = Base64.decode(contactInput.image.base64String, Base64.DEFAULT);

            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                // Add to this key
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                // Data
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photoData);
            ops.add(op.build());
        }

        try {
            ContentResolver cr = this.mActivity.getContentResolver();
            ContentProviderResult[] result = cr.applyBatch(ContactsContract.AUTHORITY, ops);

            if (result != null && result.length > 0 && result[0] != null) {
                // This will return a URI for retrieving the contact, e.g.: "content://com.android.contacts/raw_contacts/1234"
                Uri uri = result[0].uri;
                // Parse the rawId from this URI.
                String rawId = getIdFromUri(uri);
                if (rawId != null) {
                    // Get the contactId from the rawId and return it.
                    return getContactIdByRawId(rawId);
                }
            }
        } catch (Exception e) {
            // @TODO: we might want to throw an error somehow
        }

        return null;
    }

    public boolean deleteContact(@NonNull String contactId) {
        try {
            // This will compose a URI, used for deleting that specific contact,
            // e.g.: "content://com.android.contacts/contacts/1234"
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId);
            ContentResolver cr = this.mActivity.getContentResolver();
            int deleted = cr.delete(uri, null, null);
            return deleted > 0;
        } catch (Exception e) {
            // Something went wrong
            return false;
        }
    }

    public static @Nullable String getIdFromUri(@Nullable Uri uri) {
        if (uri != null) {
            return uri.getLastPathSegment();
        }
        return null;
        // If we want to support long, we can do something like this instead:
        // long id = ContentUris.parseId(uri);
        // if (id == -1) {
        //    return null;
        // }
        // return id;
    }

    private String getContactIdByRawId(@NonNull String contactRawId) {
        String[] projection = new String[] { ContactsContract.RawContacts.CONTACT_ID };

        String selection = ContactsContract.RawContacts._ID + "= ?";
        String[] selectionArgs = new String[] { contactRawId };

        ContentResolver cr = this.mActivity.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);

        String contactId = null;

        if (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);

            if (index >= 0) {
                contactId = cursor.getString(index);
            }
        }

        cursor.close();

        return contactId;
    }
}
