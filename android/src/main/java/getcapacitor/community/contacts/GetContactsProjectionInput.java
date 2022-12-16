package getcapacitor.community.contacts;

import android.provider.ContactsContract;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import org.json.JSONObject;

public class GetContactsProjectionInput {

    // Name
    private final boolean name;

    // Organization
    private final boolean organization;

    // Birthday
    private final boolean birthday;

    // Note
    private final boolean note;

    // Phones
    private final boolean phones;

    // Emails
    private final boolean emails;

    // URLs
    private final boolean urls;

    // Postal Addresses
    private final boolean postalAddresses;

    // Image
    private final boolean image;

    GetContactsProjectionInput(JSONObject fromJSONObject) {
        // Name
        this.name = fromJSONObject.optBoolean("name");

        // Organization
        this.organization = fromJSONObject.optBoolean("organization");

        // Birthday
        this.birthday = fromJSONObject.optBoolean("birthday");

        // Note
        this.note = fromJSONObject.optBoolean("note");

        // Phones
        this.phones = fromJSONObject.optBoolean("phones");

        // Emails
        this.emails = fromJSONObject.optBoolean("emails");

        // URLs
        this.urls = fromJSONObject.optBoolean("urls");

        // Postal Addresses
        this.postalAddresses = fromJSONObject.optBoolean("postalAddresses");

        // Image
        this.image = fromJSONObject.optBoolean("image");
    }

    public String[] getProjection() {
        ArrayList<String> projection = new ArrayList<>();

        projection.add(ContactsContract.Data.MIMETYPE);
        projection.add(ContactsContract.Data._ID);
        projection.add(ContactsContract.Data.CONTACT_ID);

        // Name
        if (this.name) {
            projection.add(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);
            projection.add(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
            projection.add(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME);
            projection.add(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
            projection.add(ContactsContract.CommonDataKinds.StructuredName.PREFIX);
            projection.add(ContactsContract.CommonDataKinds.StructuredName.SUFFIX);
        }

        // Organization
        if (this.organization) {
            projection.add(ContactsContract.CommonDataKinds.Organization.COMPANY);
            projection.add(ContactsContract.CommonDataKinds.Organization.TITLE);
            projection.add(ContactsContract.CommonDataKinds.Organization.DEPARTMENT);
        }

        // Birthday
        if (this.birthday) {
            projection.add(ContactsContract.CommonDataKinds.Event.START_DATE);
            projection.add(ContactsContract.CommonDataKinds.Event.TYPE);
        }

        // Note
        if (this.note) {
            projection.add(ContactsContract.CommonDataKinds.Note.NOTE);
        }

        // Phones
        if (this.phones) {
            projection.add(ContactsContract.CommonDataKinds.Phone.TYPE);
            projection.add(ContactsContract.CommonDataKinds.Phone.LABEL);
            projection.add(ContactsContract.CommonDataKinds.Phone.IS_PRIMARY);
            projection.add(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // projection.add(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
        }

        // Emails
        if (this.emails) {
            projection.add(ContactsContract.CommonDataKinds.Email.TYPE);
            projection.add(ContactsContract.CommonDataKinds.Email.LABEL);
            projection.add(ContactsContract.CommonDataKinds.Email.IS_PRIMARY);
            projection.add(ContactsContract.CommonDataKinds.Email.ADDRESS);
        }

        // URLs
        if (this.urls) {
            projection.add(ContactsContract.CommonDataKinds.Website.URL);
        }

        // Postal Addresses
        if (this.postalAddresses) {
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.TYPE);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.LABEL);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.IS_PRIMARY);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.STREET);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.CITY);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.REGION);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE);
            projection.add(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY);
        }

        // Image
        if (this.image) {
            projection.add(ContactsContract.Contacts.Photo.PHOTO);
        }

        return projection.toArray(new String[projection.size()]);
    }

    public String[] getSelectionArgs() {
        ArrayList<String> selectionArgs = new ArrayList<>();

        // Name
        if (this.name) {
            selectionArgs.add(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        }

        // Organization
        if (this.organization) {
            selectionArgs.add(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
        }

        // Birthday
        if (this.birthday) {
            selectionArgs.add(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE);
        }

        // Note
        if (this.note) {
            selectionArgs.add(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
        }

        // Phones
        if (this.phones) {
            selectionArgs.add(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        }

        // Emails
        if (this.emails) {
            selectionArgs.add(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        }

        // URLs
        if (this.urls) {
            selectionArgs.add(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
        }

        // Postal Addresses
        if (this.postalAddresses) {
            selectionArgs.add(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
        }

        // Image
        if (this.image) {
            selectionArgs.add(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
        }

        return selectionArgs.toArray(new String[selectionArgs.size()]);
    }

    public static String getSelection(String[] selectionArgs) {
        String selection = "";

        if (selectionArgs.length > 0) {
            selection = selection.concat(ContactsContract.Data.MIMETYPE + " in (");
            for (int i = 0; i < selectionArgs.length; i++) {
                if (i == 0) {
                    selection = selection.concat("?");
                } else {
                    selection = selection.concat(", ?");
                }
            }
            selection = selection.concat(")");
        }

        return selection;
    }
}
