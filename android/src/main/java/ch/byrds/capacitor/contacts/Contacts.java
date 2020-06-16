package ch.byrds.capacitor.contacts;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;

@NativePlugin(
  permissionRequestCode = 1,
  permissions = {
    Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
  }
)
public class Contacts extends Plugin {
  public static final String CONTACT_ID = "contactId";
  public static final String EMAILS = "emails";
  public static final String PHONE_NUMBERS = "phoneNumbers";
  public static final String DISPLAY_NAME = "displayName";
  public static final String ORGANIZATION_NAME = "organizationName";
  public static final String ORGANIZATION_ROLE = "organizationRole";
  public static final String BIRTHDAY = "birthday";

  @PluginMethod
  public void getPermissions(PluginCall call) {
    if (!hasRequiredPermissions()) {
      requestPermissions(call);
    } else {
      JSObject result = new JSObject();
      result.put("granted", true);
      call.success(result);
    }
  }

  @Override
  protected void handleRequestPermissionsResult(
    int requestCode,
    String[] permissions,
    int[] grantResults
  ) {
    super.handleRequestPermissionsResult(
      requestCode,
      permissions,
      grantResults
    );

    PluginCall savedCall = getSavedCall();
    JSObject result = new JSObject();

    if (!hasRequiredPermissions()) {
      result.put("granted", false);
      savedCall.success(result);
    } else {
      result.put("granted", true);
      savedCall.success(result);
    }
  }

  @PluginMethod
  public void getContacts(PluginCall call) {
    JSObject result = new JSObject();
    JSArray jsContacts = new JSArray();
    Cursor dataCursor = getContext()
      .getContentResolver()
      .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    while (dataCursor.moveToNext()) {
      JSObject jsContact = new JSObject();
      String contactId = dataCursor.getString(
        dataCursor.getColumnIndex(ContactsContract.Contacts._ID)
      );
      jsContact.put(CONTACT_ID, contactId);
      jsContact.put(
        DISPLAY_NAME,
        dataCursor.getString(
          dataCursor.getColumnIndex(
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
          )
        )
      );

      addOrganization(jsContact);
      addPhoneNumbers(jsContact);
      addEmails(jsContact);
      addBirthday(jsContact);
      jsContacts.put(jsContact);
    }
    dataCursor.close();

    result.put("contacts", jsContacts);
    call.success(result);
  }

  private void addBirthday(JSObject jsContact) {
    try {
      String contactId = (String) jsContact.get(CONTACT_ID);
      String orgWhere =
        ContactsContract.Data.CONTACT_ID +
        " = ? AND " +
        ContactsContract.Data.MIMETYPE +
        " = ? AND " +
        ContactsContract.CommonDataKinds.Event.TYPE +
        "=" +
        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;
      String[] orgWhereParams = new String[] {
        contactId,
        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
      };
      Cursor cur = getContext()
        .getContentResolver()
        .query(
          ContactsContract.Data.CONTENT_URI,
          null,
          orgWhere,
          orgWhereParams,
          null
        );
      while (cur.moveToNext()) {
        String birthday = cur.getString(
          cur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
        );
        jsContact.put(BIRTHDAY, birthday);
      }
      cur.close();
    } catch (JSONException e) {
      Log.e("Contacts", "JSONException addBirthday");
    }
  }

  private void addOrganization(JSObject jsContact) {
    try {
      String contactId = (String) jsContact.get(CONTACT_ID);
      String orgWhere =
        ContactsContract.Data.CONTACT_ID +
        " = ? AND " +
        ContactsContract.Data.MIMETYPE +
        " = ?";
      String[] orgWhereParams = new String[] {
        contactId,
        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
      };
      Cursor orgCur = getContext()
        .getContentResolver()
        .query(
          ContactsContract.Data.CONTENT_URI,
          null,
          orgWhere,
          orgWhereParams,
          null
        );
      while (orgCur.moveToNext()) {
        String orgName = orgCur.getString(
          orgCur.getColumnIndex(
            ContactsContract.CommonDataKinds.Organization.DATA
          )
        );
        jsContact.put(ORGANIZATION_NAME, orgName);
        String role = orgCur.getString(
          orgCur.getColumnIndex(
            ContactsContract.CommonDataKinds.Organization.TITLE
          )
        );
        jsContact.put(ORGANIZATION_ROLE, role);
      }
      orgCur.close();
    } catch (JSONException e) {
      Log.e("Contacts", "JSONException addOrganization");
    }
  }

  @PluginMethod
  public void getGroups(PluginCall call) {
    JSObject result = new JSObject();
    JSArray jsGroups = new JSArray();
    Cursor dataCursor = getContext()
      .getContentResolver()
      .query(ContactsContract.Groups.CONTENT_URI, null, null, null, null);

    while (dataCursor.moveToNext()) {
      JSObject jsGroup = new JSObject();
      String groupId = dataCursor.getString(
        dataCursor.getColumnIndex(ContactsContract.Groups._ID)
      );
      jsGroup.put("groupId", groupId);
      jsGroup.put(
        "accountType",
        dataCursor.getString(
          dataCursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_TYPE)
        )
      );
      jsGroup.put(
        "accountName",
        dataCursor.getString(
          dataCursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME)
        )
      );
      jsGroup.put(
        "title",
        dataCursor.getString(
          dataCursor.getColumnIndex(ContactsContract.Groups.TITLE)
        )
      );
      jsGroups.put(jsGroup);
    }
    dataCursor.close();

    result.put("groups", jsGroups);
    call.success(result);
  }

  @PluginMethod
  public void getContactGroups(PluginCall call) {
    Cursor dataCursor = getContext()
      .getContentResolver()
      .query(
        ContactsContract.Data.CONTENT_URI,
        new String[] {
          ContactsContract.Data.CONTACT_ID,
          ContactsContract.Data.DATA1,
        },
        ContactsContract.Data.MIMETYPE + "=?",
        new String[] {
          ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
        },
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

    call.success(result);
  }

  private void addEmails(JSObject jsContact) {
    try {
      JSArray emails = new JSArray();
      String contactId = (String) jsContact.get(CONTACT_ID);
      Cursor cur1 = getContext()
        .getContentResolver()
        .query(
          ContactsContract.CommonDataKinds.Email.CONTENT_URI,
          null,
          ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
          new String[] { contactId },
          null
        );
      while (cur1.moveToNext()) {
        String email = cur1.getString(
          cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
        );
        emails.put(email);
      }
      cur1.close();

      jsContact.put(EMAILS, emails);
    } catch (JSONException e) {
      Log.e("Contacts", "JSONException addEmails");
    }
  }

  private void addPhoneNumbers(JSObject jsContact) {
    try {
      JSArray phoneNumbers = new JSArray();
      String contactId = (String) jsContact.get(CONTACT_ID);
      Cursor cur1 = getContext()
        .getContentResolver()
        .query(
          ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
          null,
          ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
          new String[] { contactId },
          null
        );
      while (cur1.moveToNext()) {
        String phoneNumber = cur1.getString(
          cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        );
        phoneNumbers.put(phoneNumber);
      }
      cur1.close();

      jsContact.put(PHONE_NUMBERS, phoneNumbers);
    } catch (JSONException e) {
      Log.e("Contacts", "JSONException addPhoneNumbers");
    }
  }

  @PluginMethod
  public void deleteContact(PluginCall call) {
    Uri uri = Uri.withAppendedPath(
      ContactsContract.Contacts.CONTENT_LOOKUP_URI,
      call.getString(CONTACT_ID)
    );
    getContext().getContentResolver().delete(uri, null, null);

    JSObject result = new JSObject();
    call.success(result);
  }
}
