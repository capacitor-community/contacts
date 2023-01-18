#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(ContactsPlugin, "Contacts",
           CAP_PLUGIN_METHOD(checkPermissions, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(requestPermissions, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getContact, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getContacts, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(createContact, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(deleteContact, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(pickContact, CAPPluginReturnPromise);
)
