import Foundation
import Capacitor
import Contacts
import ContactsUI

enum CallingMethod {
    case GetContact
    case GetContacts
    case CreateContact
    case DeleteContact
    case PickContact
}

@objc(ContactsPlugin)
public class ContactsPlugin: CAPPlugin, CNContactPickerDelegate {
    private let implementation = Contacts()

    private var callingMethod: CallingMethod?

    private var pickContactCallbackId: String?

    @objc override public func checkPermissions(_ call: CAPPluginCall) {
        let permissionState: String

        switch CNContactStore.authorizationStatus(for: .contacts) {
        case .notDetermined:
            permissionState = "prompt"
        case .restricted, .denied:
            permissionState = "denied"
        case .authorized:
            permissionState = "granted"
        case .limited:
            permissionState = "limited"
        @unknown default:
            permissionState = "prompt"
        }

        call.resolve([
            "contacts": permissionState
        ])
    }

    @objc override public func requestPermissions(_ call: CAPPluginCall) {
        CNContactStore().requestAccess(for: .contacts) { [weak self] _, _  in
            self?.checkPermissions(call)
        }
    }

    private func requestContactsPermission(_ call: CAPPluginCall, _ callingMethod: CallingMethod) {
        self.callingMethod = callingMethod
        if isContactsPermissionGranted() {
            permissionCallback(call)
        } else {
            CNContactStore().requestAccess(for: .contacts) { [weak self] _, _  in
                self?.permissionCallback(call)
            }
        }
    }

    private func isContactsPermissionGranted() -> Bool {
        switch CNContactStore.authorizationStatus(for: .contacts) {
        case .notDetermined, .restricted, .denied:
            return false
        case .authorized, .limited:
            return true
        @unknown default:
            return false
        }
    }

    private func permissionCallback(_ call: CAPPluginCall) {
        let method = self.callingMethod

        self.callingMethod = nil

        if !isContactsPermissionGranted() {
            call.reject("Permission is required to access contacts.")
            return
        }

        switch method {
        case .GetContact:
            getContact(call)
        case .GetContacts:
            getContacts(call)
        case .CreateContact:
            createContact(call)
        case .DeleteContact:
            deleteContact(call)
        case .PickContact:
            pickContact(call)
        default:
            // No method was being called,
            // so nothing has to be done here.
            break
        }
    }

    @objc func getContact(_ call: CAPPluginCall) {
        if !isContactsPermissionGranted() {
            requestContactsPermission(call, CallingMethod.GetContact)
        } else {
            let contactId = call.getString("contactId")

            guard let contactId = contactId else {
                call.reject("Parameter `contactId` not provided.")
                return
            }

            let projectionInput = GetContactsProjectionInput(call.getObject("projection") ?? JSObject())

            let contact = implementation.getContact(contactId, projectionInput)

            guard let contact = contact else {
                call.reject("Contact not found.")
                return
            }

            call.resolve([
                "contact": contact.getJSObject()
            ])
        }
    }

    @objc func getContacts(_ call: CAPPluginCall) {
        if !isContactsPermissionGranted() {
            requestContactsPermission(call, CallingMethod.GetContacts)
        } else {
            let projectionInput = GetContactsProjectionInput(call.getObject("projection") ?? JSObject())

            let contacts = implementation.getContacts(projectionInput)

            var contactsJSArray: JSArray = JSArray()

            for contact in contacts {
                contactsJSArray.append(contact.getJSObject())
            }

            call.resolve([
                "contacts": contactsJSArray
            ])
        }
    }

    @objc func createContact(_ call: CAPPluginCall) {
        if !isContactsPermissionGranted() {
            requestContactsPermission(call, CallingMethod.CreateContact)
        } else {
            let contactInput = CreateContactInput.init(call.getObject("contact", JSObject()))

            let contactId = implementation.createContact(contactInput)

            guard let contactId = contactId else {
                call.reject("Something went wrong.")
                return
            }

            call.resolve([
                "contactId": contactId
            ])
        }
    }

    @objc func deleteContact(_ call: CAPPluginCall) {
        if !isContactsPermissionGranted() {
            requestContactsPermission(call, CallingMethod.DeleteContact)
        } else {
            let contactId = call.getString("contactId")

            guard let contactId = contactId else {
                call.reject("Parameter `contactId` not provided.")
                return
            }

            if !implementation.deleteContact(contactId) {
                call.reject("Something went wrong.")
                return
            }

            call.resolve()
        }
    }

    @objc func pickContact(_ call: CAPPluginCall) {
        if !isContactsPermissionGranted() {
            requestContactsPermission(call, CallingMethod.PickContact)
        } else {
            DispatchQueue.main.async {
                // Save the call and its callback id
                self.bridge?.saveCall(call)
                self.pickContactCallbackId = call.callbackId

                // Initialize the contact picker
                let contactPicker = CNContactPickerViewController()
                // Mark current class as the delegate class,
                // this will make the callback `contactPicker` actually work.
                contactPicker.delegate = self
                // Present (open) the native contact picker.
                self.bridge?.viewController?.present(contactPicker, animated: true)
            }
        }
    }

    public func contactPicker(_ picker: CNContactPickerViewController, didSelect selectedContact: CNContact) {
        let call = self.bridge?.savedCall(withID: self.pickContactCallbackId ?? "")

        guard let call = call else {
            return
        }

        let contact = ContactPayload(selectedContact.identifier)

        contact.fillData(selectedContact)

        call.resolve([
            "contact": contact.getJSObject()
        ])

        self.bridge?.releaseCall(call)
    }
}
