package ch.byrds.capacitor.contacts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Base64;
import android.util.Log;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(
    name = "Contacts",
    //requestCodes is labeled as legacy in bridge
    requestCodes = Contacts.REQUEST_CODE,
    permissions = { @Permission(strings = { Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS }, alias = "contacts") }
)
public class Contacts extends Plugin {

    public static final String LOG_TAG = "Contacts";

    /**
     * Unique request code
     */
    public static final int REQUEST_CODE = 0x1651;

    private static final String CONTACT_ID = "contactId";
    private static final String EMAILS = "emails";
    private static final String EMAIL_LABEL = "label";
    private static final String EMAIL_ADDRESS = "address";
    private static final String PHONE_NUMBERS = "phoneNumbers";
    private static final String PHONE_LABEL = "label";
    private static final String PHONE_NUMBER = "number";
    private static final String DISPLAY_NAME = "displayName";
    private static final String PHOTO_THUMBNAIL = "photoThumbnail";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_ROLE = "organizationRole";
    private static final String BIRTHDAY = "birthday";

    @PluginMethod
    public void getPermissions(PluginCall call) {
        if (!hasRequiredPermissions()) {
            requestPermissions(call);
        } else {
            JSObject result = new JSObject();
            result.put("granted", true);
            call.resolve(result);
        }
    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        PluginCall savedCall = getSavedCall();
        JSObject result = new JSObject();

        if (!hasRequiredPermissions()) {
            result.put("granted", false);
            savedCall.resolve(result);
        } else {
            result.put("granted", true);
            savedCall.resolve(result);
        }
    }

    @PluginMethod
    public void getContacts(PluginCall call) {
        JSArray jsContacts = new JSArray();

        ContentResolver contentResolver = getContext().getContentResolver();

        String[] projection = new String[] {
            ContactsContract.Data.MIMETYPE,
            Organization.TITLE,
            ContactsContract.Contacts._ID,
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.Photo.PHOTO,
            ContactsContract.CommonDataKinds.Contactables.DATA,
            ContactsContract.CommonDataKinds.Contactables.TYPE,
            ContactsContract.CommonDataKinds.Contactables.LABEL
        };
        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?, ?, ?, ?)";
        String[] selectionArgs = new String[] {
            Email.CONTENT_ITEM_TYPE,
            Phone.CONTENT_ITEM_TYPE,
            Event.CONTENT_ITEM_TYPE,
            Organization.CONTENT_ITEM_TYPE,
            Photo.CONTENT_ITEM_TYPE
        };

        Cursor contactsCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null);

        if (contactsCursor != null && contactsCursor.getCount() > 0) {
            HashMap<Object, JSObject> contactsById = new HashMap<>();

            while (contactsCursor.moveToNext()) {
                String _id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));

                JSObject jsContact = new JSObject();

                if (!contactsById.containsKey(contactId)) {
                    // this contact does not yet exist in HashMap,
                    // so put it to the HashMap

                    jsContact.put(CONTACT_ID, contactId);
                    String displayName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    jsContact.put(DISPLAY_NAME, displayName);
                    JSArray jsPhoneNumbers = new JSArray();
                    jsContact.put(PHONE_NUMBERS, jsPhoneNumbers);
                    JSArray jsEmailAddresses = new JSArray();
                    jsContact.put(EMAILS, jsEmailAddresses);

                    jsContacts.put(jsContact);
                } else {
                    // this contact already exists,
                    // retrieve it
                    jsContact = contactsById.get(contactId);
                }

                if (jsContact != null) {
                    String mimeType = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                    String data = contactsCursor.getString(
                        contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA)
                    );
                    int type = contactsCursor.getInt(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.TYPE));
                    String label = contactsCursor.getString(
                        contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.LABEL)
                    );

                    // email
                    switch (mimeType) {
                        case Email.CONTENT_ITEM_TYPE:
                            try {
                                // add this email to the list
                                JSArray emailAddresses = (JSArray) jsContact.get(EMAILS);
                                JSObject jsEmail = new JSObject();
                                jsEmail.put(EMAIL_LABEL, mapEmailTypeToLabel(type, label));
                                jsEmail.put(EMAIL_ADDRESS, data);
                                emailAddresses.put(jsEmail);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        // phone
                        case Phone.CONTENT_ITEM_TYPE:
                            try {
                                // add this phone to the list
                                JSArray jsPhoneNumbers = (JSArray) jsContact.get(PHONE_NUMBERS);
                                JSObject jsPhone = new JSObject();
                                jsPhone.put(PHONE_LABEL, mapPhoneTypeToLabel(type, label));
                                jsPhone.put(PHONE_NUMBER, data);
                                jsPhoneNumbers.put(jsPhone);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        // birthday
                        case Event.CONTENT_ITEM_TYPE:
                            int eventType = contactsCursor.getInt(
                                contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.TYPE)
                            );
                            if (eventType == Event.TYPE_BIRTHDAY) {
                                jsContact.put(BIRTHDAY, data);
                            }
                            break;
                        // organization
                        case Organization.CONTENT_ITEM_TYPE:
                            jsContact.put(ORGANIZATION_NAME, data);
                            String organizationRole = contactsCursor.getString(contactsCursor.getColumnIndex(Organization.TITLE));
                            if (organizationRole != null) {
                                jsContact.put(ORGANIZATION_ROLE, organizationRole);
                            }
                            break;
                        // photo
                        case Photo.CONTENT_ITEM_TYPE:
                            byte[] thumbnailPhoto = contactsCursor.getBlob(
                                contactsCursor.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO)
                            );
                            if (thumbnailPhoto != null) {
                                String encodedThumbnailPhoto = Base64.encodeToString(thumbnailPhoto, Base64.NO_WRAP);
                                jsContact.put(PHOTO_THUMBNAIL, "data:image/png;base64," + encodedThumbnailPhoto);
                            }
                            break;
                    }

                    contactsById.put(contactId, jsContact);
                }
            }
        }
        if (contactsCursor != null) {
            contactsCursor.close();
        }

        JSObject result = new JSObject();
        result.put("contacts", jsContacts);
        call.resolve(result);
    }

    @PluginMethod
    public void getGroups(PluginCall call) {
        JSObject result = new JSObject();
        JSArray jsGroups = new JSArray();
        Cursor dataCursor = getContext().getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, null, null, null);

        while (dataCursor.moveToNext()) {
            JSObject jsGroup = new JSObject();
            String groupId = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Groups._ID));
            jsGroup.put("groupId", groupId);
            jsGroup.put("accountType", dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_TYPE)));
            jsGroup.put("accountName", dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME)));
            jsGroup.put("title", dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Groups.TITLE)));
            jsGroups.put(jsGroup);
        }
        dataCursor.close();

        result.put("groups", jsGroups);
        call.resolve(result);
    }

    @PluginMethod
    public void getContactGroups(PluginCall call) {
        Cursor dataCursor = getContext()
            .getContentResolver()
            .query(
                ContactsContract.Data.CONTENT_URI,
                new String[] { ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DATA1 },
                ContactsContract.Data.MIMETYPE + "=?",
                new String[] { ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE },
                null
            );

        Map<String, Set<String>> contact2GroupMap = new HashMap<>();
        while (dataCursor.moveToNext()) {
            String contact_id = dataCursor.getString(0);
            String group_id = dataCursor.getString(1);

            Set<String> groups = new HashSet<>();
            if (contact2GroupMap.containsKey(contact_id)) {
                groups = contact2GroupMap.get(contact_id);
            }
            groups.add(group_id);
            contact2GroupMap.put(contact_id, groups);
        }
        dataCursor.close();

        JSObject result = new JSObject();
        for (Map.Entry<String, Set<String>> entry : contact2GroupMap.entrySet()) {
            JSArray jsGroups = new JSArray();
            Set<String> groups = entry.getValue();
            for (String group : groups) {
                jsGroups.put(group);
            }
            result.put(entry.getKey(), jsGroups);
        }

        call.resolve(result);
    }

    @PluginMethod
    public void deleteContact(PluginCall call) {
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, call.getString(CONTACT_ID));
        getContext().getContentResolver().delete(uri, null, null);

        JSObject result = new JSObject();
        call.resolve(result);
    }

    @PluginMethod
    public void saveContact(PluginCall call) throws JSONException {
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        // TODO: add missing: birthday, social profiles, nickname, name prefix, url addresses

        // Name

        String givenName = call.getString("givenName", "");
        String middleName = call.getString("middleName", "");
        String familyName = call.getString("familyName", "");

        String name = givenName;
        if (middleName != null) name += " " + middleName;
        name += " " + familyName;
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);

        // Other

        intent.putExtra(ContactsContract.Intents.Insert.NOTES, call.getString("note", ""));

        // Email addresses

        JSArray emailAddressesArray = call.getArray("emailAddresses", new JSArray());
        List<Object> emailAddresses = emailAddressesArray.toList();

        // TODO: allow more than these three email types
        if (emailAddresses.size() > 0) {
            JSObject primaryEmail = JSObject.fromJSONObject((JSONObject) emailAddresses.get(0));
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, primaryEmail.getString("address", ""));
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, primaryEmail.getString("label", ""));
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_ISPRIMARY, true);

            if (emailAddresses.size() > 1) {
                JSObject secondaryEmail = JSObject.fromJSONObject((JSONObject) emailAddresses.get(1));
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, secondaryEmail.getString("address", ""));
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE, secondaryEmail.getString("label", ""));

                if (emailAddresses.size() > 2) {
                    JSObject tertiaryEmail = JSObject.fromJSONObject((JSONObject) emailAddresses.get(2));
                    intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL, tertiaryEmail.getString("address", ""));
                    intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE, tertiaryEmail.getString("label", ""));
                }
            }
        }

        // Phone numbers

        JSArray phoneNumbersArray = call.getArray("phoneNumbers", new JSArray());
        List<Object> phoneNumbers = phoneNumbersArray.toList();

        // TODO: allow more than these three phone number types
        if (phoneNumbers.size() > 0) {
            JSObject primaryNumber = JSObject.fromJSONObject((JSONObject) phoneNumbers.get(0));
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, primaryNumber.getString("number", ""));
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, primaryNumber.getString("label", ""));
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_ISPRIMARY, true);

            if (phoneNumbers.size() > 1) {
                JSObject secondaryNumber = JSObject.fromJSONObject((JSONObject) phoneNumbers.get(1));
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, secondaryNumber.getString("number", ""));
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, secondaryNumber.getString("label", ""));

                if (phoneNumbers.size() > 2) {
                    JSObject tertiaryNumber = JSObject.fromJSONObject((JSONObject) phoneNumbers.get(2));
                    intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, tertiaryNumber.getString("number", ""));
                    intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, tertiaryNumber.getString("label", ""));
                }
            }
        }

        // Company

        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, call.getString("jobTitle", ""));
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, call.getString("organizationName", ""));
        // TODO: why can department name not be added?
        //intent.putExtra(ContactsContract.Intents.Insert.DEPARTMENT, call.getString("departmentName", ""));

        // Addresses

        JSArray addressesArray = call.getArray("postalAddresses", new JSArray());
        List<Object> addresses = addressesArray.toList();

        // TODO: allow more than one address
        if (addresses.size() > 0) {
            JSObject address = JSObject.fromJSONObject((JSONObject) addresses.get(0));
            JSObject postalObject = address.getJSObject("address", new JSObject());
            String postal =
                postalObject.getString("street", "") +
                ", " +
                postalObject.getString("postalCode", "") +
                " " +
                postalObject.getString("city", "") +
                ", " +
                postalObject.getString("country", "");
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, postal);
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL_TYPE, address.getString("label", ""));
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL_ISPRIMARY, true);
        }

        // --- Save

        getContext().startActivity(intent);

        Log.w("", intent.toString());

        call.resolve();
    }

    private String mapPhoneTypeToLabel(int type, String defaultLabel) {
        switch (type) {
            case Phone.TYPE_MOBILE:
                return "mobile";
            case Phone.TYPE_HOME:
                return "home";
            case Phone.TYPE_WORK:
                return "work";
            case Phone.TYPE_FAX_WORK:
                return "fax work";
            case Phone.TYPE_FAX_HOME:
                return "fax home";
            case Phone.TYPE_PAGER:
                return "pager";
            case Phone.TYPE_OTHER:
                return "other";
            case Phone.TYPE_CALLBACK:
                return "callback";
            case Phone.TYPE_CAR:
                return "car";
            case Phone.TYPE_COMPANY_MAIN:
                return "company main";
            case Phone.TYPE_ISDN:
                return "isdn";
            case Phone.TYPE_MAIN:
                return "main";
            case Phone.TYPE_OTHER_FAX:
                return "other fax";
            case Phone.TYPE_RADIO:
                return "radio";
            case Phone.TYPE_TELEX:
                return "telex";
            case Phone.TYPE_TTY_TDD:
                return "tty";
            case Phone.TYPE_WORK_MOBILE:
                return "work mobile";
            case Phone.TYPE_WORK_PAGER:
                return "work pager";
            case Phone.TYPE_ASSISTANT:
                return "assistant";
            case Phone.TYPE_MMS:
                return "mms";
            default:
                return defaultLabel;
        }
    }

    private String mapEmailTypeToLabel(int type, String defaultLabel) {
        switch (type) {
            case Email.TYPE_HOME:
                return "home";
            case Email.TYPE_WORK:
                return "work";
            case Email.TYPE_OTHER:
                return "other";
            case Email.TYPE_MOBILE:
                return "mobile";
            default:
                return defaultLabel;
        }
    }
}
