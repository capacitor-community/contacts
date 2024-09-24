package getcapacitor.community.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CapacitorPlugin(
    name = "Contacts",
    permissions = { @Permission(strings = { Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS }, alias = "contacts") }
)
public class ContactsPlugin extends Plugin {

    public static final String TAG = "Contacts";

    private Contacts implementation;

    @Override
    public void load() {
        implementation = new Contacts(getActivity());
    }

    private void requestContactsPermission(PluginCall call) {
        requestPermissionForAlias("contacts", call, "permissionCallback");
    }

    /**
     * Checks the the given permission is granted or not
     * @return Returns true if the permission is granted and false if it is denied.
     */
    private boolean isContactsPermissionGranted() {
        return getPermissionState("contacts") == PermissionState.GRANTED;
    }

    @PermissionCallback
    private void permissionCallback(PluginCall call) {
        if (!isContactsPermissionGranted()) {
            call.reject("Permission is required to access contacts.");
            return;
        }

        switch (call.getMethodName()) {
            case "getContact":
                getContact(call);
                break;
            case "getContacts":
                getContacts(call);
                break;
            case "createContact":
                createContact(call);
                break;
            case "deleteContact":
                deleteContact(call);
                break;
            case "pickContact":
                pickContact(call);
                break;
        }
    }

    @PluginMethod
    public void getContact(PluginCall call) {
        try {
            if (!isContactsPermissionGranted()) {
                requestContactsPermission(call);
            } else {
                String contactId = call.getString("contactId");

                if (contactId == null) {
                    call.reject("Parameter `contactId` not provided.");
                    return;
                }

                GetContactsProjectionInput projectionInput = new GetContactsProjectionInput(call.getObject("projection"));

                ContactPayload contact = implementation.getContact(contactId, projectionInput);

                if (contact == null) {
                    call.reject("Contact not found.");
                    return;
                }

                JSObject result = new JSObject();
                result.put("contact", contact.getJSObject());
                call.resolve(result);
            }
        } catch (Exception exception) {
            rejectCall(call, exception);
        }
    }

    @PluginMethod
    public void getContacts(PluginCall call) {
        try {
            if (!isContactsPermissionGranted()) {
                requestContactsPermission(call);
            } else {
                ExecutorService executor = Executors.newSingleThreadExecutor();

                executor.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HashMap<String, ContactPayload> contacts = implementation.getContacts(
                                    new GetContactsProjectionInput(call.getObject("projection"))
                                );

                                JSArray contactsJSArray = new JSArray();
                                for (Map.Entry<String, ContactPayload> entry : contacts.entrySet()) {
                                    ContactPayload value = entry.getValue();
                                    contactsJSArray.put(value.getJSObject());
                                }

                                JSObject result = new JSObject();
                                result.put("contacts", contactsJSArray);

                                bridge
                                    .getActivity()
                                    .runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                call.resolve(result);
                                            }
                                        }
                                    );
                            } catch (Exception exception) {
                                rejectCall(call, exception);
                            }
                        }
                    }
                );

                executor.shutdown();
            }
        } catch (Exception exception) {
            rejectCall(call, exception);
        }
    }

    @PluginMethod
    public void createContact(PluginCall call) {
        try {
            if (!isContactsPermissionGranted()) {
                requestContactsPermission(call);
            } else {
                String contactId = implementation.createContact(new CreateContactInput(call.getObject("contact")));

                if (contactId == null) {
                    call.reject("Something went wrong.");
                    return;
                }

                JSObject result = new JSObject();
                result.put("contactId", contactId);

                call.resolve(result);
            }
        } catch (Exception exception) {
            rejectCall(call, exception);
        }
    }

    @PluginMethod
    public void deleteContact(PluginCall call) {
        try {
            if (!isContactsPermissionGranted()) {
                requestContactsPermission(call);
            } else {
                String contactId = call.getString("contactId");

                if (contactId == null) {
                    call.reject("Parameter `contactId` not provided.");
                    return;
                }

                if (!implementation.deleteContact(contactId)) {
                    call.reject("Something went wrong.");
                    return;
                }

                call.resolve();
            }
        } catch (Exception exception) {
            rejectCall(call, exception);
        }
    }

    @PluginMethod
    public void pickContact(PluginCall call) {
        try {
            if (!isContactsPermissionGranted()) {
                requestContactsPermission(call);
            } else {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(call, contactPickerIntent, "pickContactResult");
            }
        } catch (Exception exception) {
            rejectCall(call, exception);
        }
    }

    @ActivityCallback
    private void pickContactResult(PluginCall call, ActivityResult activityResult) {
        if (call != null && activityResult.getResultCode() == Activity.RESULT_OK && activityResult.getData() != null) {
            // This will return a URI for retrieving the contact, e.g.: "content://com.android.contacts/contacts/1234"
            Uri uri = activityResult.getData().getData();
            // Parse the contactId from this URI.
            String contactId = Contacts.getIdFromUri(uri);

            if (contactId == null) {
                call.reject("Parameter `contactId` not returned from pick. Please raise an issue in GitHub if this problem persists.");
                return;
            }

            GetContactsProjectionInput projectionInput = new GetContactsProjectionInput(call.getObject("projection"));

            ContactPayload contact = implementation.getContact(contactId, projectionInput);

            if (contact == null) {
                call.reject("Contact not found.");
                return;
            }

            JSObject result = new JSObject();
            result.put("contact", contact.getJSObject());
            call.resolve(result);
        }
    }

    private void rejectCall(PluginCall call, Exception exception) {
        String message = exception.getMessage();
        message = (message != null) ? message : "An error occurred.";
        Logger.error(TAG, message, exception);
        call.reject(message);
    }
}
