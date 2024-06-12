package getcapacitor.community.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Objects;

public class ContactPayload {

    // Id
    private String contactId;

    // Name
    private JSObject name = new JSObject();

    // Organization
    private JSObject organization = new JSObject();

    // Birthday
    private JSObject birthday = new JSObject();

    // Note
    private String note;

    // Phones
    private JSArray phones = new JSArray();

    // Emails
    private JSArray emails = new JSArray();

    // URLs
    private JSArray urls = new JSArray();

    // Postal Addresses
    private JSArray postalAddresses = new JSArray();

    // Image
    private JSObject image = new JSObject();

    ContactPayload(String contactId) {
        this.contactId = contactId;
    }

    private static @NonNull String getMimetype(byte[] blob) {
        try {
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(blob));
            String mimeType = URLConnection.guessContentTypeFromStream(is);
            if (mimeType == null) {
                return "image/png";
            }
            return mimeType;
        } catch (Exception e) {
            return "image/png";
        }
    }

    private Integer parseStrToIntSafe(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean getBooleanByColumnName(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index >= 0) {
            int result = cursor.getInt(index);
            return result == 1;
        }
        return null;
    }

    private String getStringByColumnName(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index >= 0) {
            return cursor.getString(index);
        }
        return null;
    }

    private Integer getIntByColumnName(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index >= 0) {
            return cursor.getInt(index);
        }
        return null;
    }

    private String getBase64ByColumnName(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index >= 0) {
            byte[] blob = cursor.getBlob(index);
            if (blob != null) {
                String mimeType = getMimetype(blob);
                String encodedImage = Base64.encodeToString(blob, Base64.NO_WRAP);
                return "data:" + mimeType + ";base64," + encodedImage;
            }
        }
        return null;
    }

    public void fillDataByCursor(ContentResolver cr, @NonNull Cursor cursor, GetContactsProjectionInput projectionInput)
        throws IOException {
        String mimeType = getStringByColumnName(cursor, ContactsContract.Data.MIMETYPE);

        if (mimeType == null) {
            return;
        }

        switch (mimeType) {
            // Name
            case StructuredName.CONTENT_ITEM_TYPE:
                name.put("display", getStringByColumnName(cursor, StructuredName.DISPLAY_NAME));
                name.put("given", getStringByColumnName(cursor, StructuredName.GIVEN_NAME));
                name.put("middle", getStringByColumnName(cursor, StructuredName.MIDDLE_NAME));
                name.put("family", getStringByColumnName(cursor, StructuredName.FAMILY_NAME));
                name.put("prefix", getStringByColumnName(cursor, StructuredName.PREFIX));
                name.put("suffix", getStringByColumnName(cursor, StructuredName.SUFFIX));
                break;
            // Organization
            case Organization.CONTENT_ITEM_TYPE:
                organization.put("company", getStringByColumnName(cursor, Organization.COMPANY));
                organization.put("jobTitle", getStringByColumnName(cursor, Organization.TITLE));
                organization.put("department", getStringByColumnName(cursor, Organization.DEPARTMENT));
                break;
            // Birthday
            case Event.CONTENT_ITEM_TYPE:
                Integer eventType = getIntByColumnName(cursor, Event.TYPE);
                if (eventType != null && eventType == Event.TYPE_BIRTHDAY) {
                    String birthdayString = getStringByColumnName(cursor, Event.START_DATE);

                    if (birthdayString != null) {
                        // Normally the date is formatted as "yyyy-mm-dd"
                        // But it's possible to not have a year saved to the birthday,
                        // then it's formatted like "--mm-dd".
                        // So we'll have to rectify that:
                        birthdayString = birthdayString.replace("--", "");

                        // Next we'll split the string into pieces:
                        String[] splittedBirthdayString = birthdayString.split("-");

                        if (splittedBirthdayString.length == 2) {
                            // Birthday is formatted like "mm-dd"
                            birthday.put("month", parseStrToIntSafe(splittedBirthdayString[0]));
                            birthday.put("day", parseStrToIntSafe(splittedBirthdayString[1]));
                        } else if (splittedBirthdayString.length == 3) {
                            // Birthday is formatted like "yyyy-mm-dd"
                            birthday.put("year", parseStrToIntSafe(splittedBirthdayString[0]));
                            birthday.put("month", parseStrToIntSafe(splittedBirthdayString[1]));
                            birthday.put("day", parseStrToIntSafe(splittedBirthdayString[2]));
                        }
                    }
                }
                break;
            // Note
            case Note.CONTENT_ITEM_TYPE:
                note = getStringByColumnName(cursor, Note.NOTE);
                break;
            // Phones
            case Phone.CONTENT_ITEM_TYPE:
                String number = getStringByColumnName(cursor, Phone.NUMBER);
                Integer phoneType = getIntByColumnName(cursor, Phone.TYPE);
                if (number != null && phoneType != null) {
                    JSObject phoneObject = new JSObject();
                    phoneObject.put("type", Contacts.phoneTypeMap.getKey(phoneType));
                    phoneObject.put("label", getStringByColumnName(cursor, Phone.LABEL));
                    phoneObject.put("isPrimary", getBooleanByColumnName(cursor, Phone.IS_PRIMARY));
                    phoneObject.put("number", number);
                    phones.put(phoneObject);
                }
                break;
            // Emails
            case Email.CONTENT_ITEM_TYPE:
                String address = getStringByColumnName(cursor, Email.ADDRESS);
                Integer emailType = getIntByColumnName(cursor, Email.TYPE);
                if (address != null && emailType != null) {
                    JSObject emailObject = new JSObject();
                    emailObject.put("type", Contacts.emailTypeMap.getKey(emailType));
                    emailObject.put("label", getStringByColumnName(cursor, Email.LABEL));
                    emailObject.put("isPrimary", getBooleanByColumnName(cursor, Email.IS_PRIMARY));
                    emailObject.put("address", address);
                    emails.put(emailObject);
                }
                break;
            // URLs
            case Website.CONTENT_ITEM_TYPE:
                String url = getStringByColumnName(cursor, Website.URL);
                urls.put(url);
                break;
            // Postal Addresses
            case StructuredPostal.CONTENT_ITEM_TYPE:
                Integer postalAddressType = getIntByColumnName(cursor, StructuredPostal.TYPE);
                if (postalAddressType != null) {
                    JSObject postalAddressObject = new JSObject();
                    postalAddressObject.put("type", Contacts.postalAddressTypeMap.getKey(postalAddressType));
                    postalAddressObject.put("label", getStringByColumnName(cursor, StructuredPostal.LABEL));
                    postalAddressObject.put("isPrimary", getBooleanByColumnName(cursor, StructuredPostal.IS_PRIMARY));
                    postalAddressObject.put("formatted", getStringByColumnName(cursor, StructuredPostal.FORMATTED_ADDRESS));
                    postalAddressObject.put("street", getStringByColumnName(cursor, StructuredPostal.STREET));
                    postalAddressObject.put("neighborhood", getStringByColumnName(cursor, StructuredPostal.NEIGHBORHOOD));
                    postalAddressObject.put("city", getStringByColumnName(cursor, StructuredPostal.CITY));
                    postalAddressObject.put("region", getStringByColumnName(cursor, StructuredPostal.REGION));
                    postalAddressObject.put("postcode", getStringByColumnName(cursor, StructuredPostal.POSTCODE));
                    postalAddressObject.put("country", getStringByColumnName(cursor, StructuredPostal.COUNTRY));
                    postalAddresses.put(postalAddressObject);
                }
                break;
            // Image
            case Photo.CONTENT_ITEM_TYPE:
                if (projectionInput.image) {
                    String base64String = getBase64ByColumnName(cursor, Photo.PHOTO);
                    if (base64String != null) {
                        image.put("base64String", base64String);
                    }
                }

                if (projectionInput.image) {
                    long contactIdLong = Long.parseLong(contactId);

                    Uri contact = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactIdLong);

//                    // Option 1:
//                    Uri imageUrl = Uri.withAppendedPath(contact, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//                    Bitmap bitmap;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(cr, imageUrl));
//                    } else {
//                        bitmap = MediaStore.Images.Media.getBitmap(cr, imageUrl);
//                    }
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    byte[] b = baos.toByteArray();
//                    String base64String = "data:" + "image/png" + ";base64," + Base64.encodeToString(b, Base64.NO_WRAP);

//                    // Option 2:
//                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(cr, contact, true);
//                    inputStream.reset();
//                    byte[] blob = new byte[inputStream.available()];
//                    DataInputStream dataInputStream = new DataInputStream(inputStream);
//                    dataInputStream.readFully(blob);
//
//                    String blobMimeType = getMimetype(blob);
//                    String encodedImage = Base64.encodeToString(blob, Base64.NO_WRAP);
//                    String base64String = "data:" + blobMimeType + ";base64," + encodedImage;

                    // Option 3:
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(cr, contact, true);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);

                    if (bitmap != null) {
                        Bitmap.CompressFormat compressFormat;
                        String streamMimeType;

                        if (Objects.equals(options.outMimeType, "image/png")) {
                            compressFormat = Bitmap.CompressFormat.PNG;
                            streamMimeType = "image/png";
                        } else {
                            compressFormat = Bitmap.CompressFormat.JPEG;
                            streamMimeType = "image/jpeg";
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(compressFormat, 100, baos);
                        byte[] blob = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(blob, Base64.NO_WRAP);
                        String base64String = "data:" + streamMimeType + ";base64," + encodedImage;
                    }
                }

                break;
        }
    }

    public JSObject getJSObject() {
        JSObject contact = new JSObject();

        // Id
        contact.put("contactId", contactId);

        // Name
        if (name.length() > 0) {
            contact.put("name", name);
        }

        // Organization
        if (organization.length() > 0) {
            contact.put("organization", organization);
        }

        // Birthday
        if (birthday.length() > 0) {
            contact.put("birthday", birthday);
        }

        // Note
        if (note != null) {
            contact.put("note", note);
        }

        // Phones
        if (phones.length() > 0) {
            contact.put("phones", phones);
        }

        // Emails
        if (emails.length() > 0) {
            contact.put("emails", emails);
        }

        // URLs
        if (urls.length() > 0) {
            contact.put("urls", urls);
        }

        // Postal Addresses
        if (postalAddresses.length() > 0) {
            contact.put("postalAddresses", postalAddresses);
        }

        // Image
        if (image.length() > 0) {
            contact.put("image", image);
        }

        return contact;
    }
}
