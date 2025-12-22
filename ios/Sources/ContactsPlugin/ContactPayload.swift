import Foundation
import Contacts
import Capacitor

public class ContactPayload {

    // Id
    private var contactId: String

    // Name
    private var name: JSObject = JSObject()

    // Organization
    private var organization: JSObject = JSObject()

    // Birthday
    private var birthday: JSObject = JSObject()

    // Note
    private var note: String?

    // Phones
    private var phones: JSArray = JSArray()

    // Emails
    private var emails: JSArray = JSArray()

    // URLs
    private var urls: JSArray = JSArray()

    // Postal Addresses
    private var postalAddresses: JSArray = JSArray()

    // Image
    private var image: JSObject = JSObject()

    init(_ contactId: String) {
        self.contactId = contactId
    }

    static func getMimetype(_ data: Data) -> String {
        var b: UInt8 = 0
        data.copyBytes(to: &b, count: 1)
        switch b {
        case 0xFF:
            return "image/jpeg"
        default:
            return "image/png"
        }
    }

    static func getLabel(_ type: String?, _ rawType: String?) -> String? {
        // On Android, a custom label is saved with `type` set to "custom" and a separate `label` attribute.
        // On iOS, on the contrary, the custom value is just saved into the `type` attribute.

        if type == "custom" {
            return rawType
        }

        return nil
    }

    public func fillData(_ contact: CNContact) {
        // Name
        if contact.isKeyAvailable(CNContactGivenNameKey) {
            let displayName = CNContactFormatter.string(from: contact, style: .fullName)
            self.name["display"] = displayName
            self.name["given"] = contact.givenName
            self.name["middle"] = contact.middleName
            self.name["family"] = contact.familyName
            self.name["prefix"] = contact.namePrefix
            self.name["suffix"] = contact.nameSuffix
        }

        // Organization
        if contact.isKeyAvailable(CNContactJobTitleKey) {
            self.organization["company"] = contact.organizationName
            self.organization["jobTitle"] = contact.jobTitle
            self.organization["department"] = contact.departmentName
        }

        // Birthday
        if contact.isKeyAvailable(CNContactBirthdayKey) {
            if let dateComponents = contact.birthday {
                self.birthday["year"] = dateComponents.year
                self.birthday["month"] = dateComponents.month
                self.birthday["day"] = dateComponents.day
            }
        }

        // Note
        if contact.isKeyAvailable(CNContactNoteKey) {
            self.note = contact.note
        }

        // Phones
        if contact.isKeyAvailable(CNContactPhoneNumbersKey) {
            self.phones = contact.phoneNumbers.map { phone in
                let type = Contacts.phoneTypeMap.getKey(phone.label)

                var phoneObject: JSObject = [
                    "type": type,
                    "number": phone.value.stringValue
                ]

                if let label = ContactPayload.getLabel(type, phone.label) {
                    phoneObject["label"] = label
                }

                return phoneObject
            }
        }

        // Emails
        if contact.isKeyAvailable(CNContactEmailAddressesKey) {
            self.emails = contact.emailAddresses.map { email in
                let type = Contacts.emailTypeMap.getKey(email.label)

                var emailObject: JSObject = [
                    "type": type,
                    "address": email.value as String
                ]

                if let label = ContactPayload.getLabel(type, email.label) {
                    emailObject["label"] = label
                }

                return emailObject
            }
        }

        // URLs
        if contact.isKeyAvailable(CNContactUrlAddressesKey) {
            self.urls = contact.urlAddresses.map { url in
                return url.value as String
            }
        }

        // Postal Addresses
        if contact.isKeyAvailable(CNContactPostalAddressesKey) {
            self.postalAddresses = contact.postalAddresses.map { postalAddress in
                let type = Contacts.postalAddressTypeMap.getKey(postalAddress.label)
                let formatted = CNPostalAddressFormatter.string(from: postalAddress.value, style: .mailingAddress)

                var postalAddressObject: JSObject = [
                    "type": type,
                    "formatted": formatted,
                    "street": postalAddress.value.street,
                    "neighborhood": postalAddress.value.subAdministrativeArea,
                    "city": postalAddress.value.city,
                    "region": postalAddress.value.state,
                    "postcode": postalAddress.value.postalCode,
                    "country": postalAddress.value.country
                ]

                if let label = ContactPayload.getLabel(type, postalAddress.label) {
                    postalAddressObject["label"] = label
                }

                return postalAddressObject
            }
        }

        // Image
        if contact.isKeyAvailable(CNContactImageDataKey) {
            if let image = contact.imageData {
                let mimeType = ContactPayload.getMimetype(image)
                let encodedImage = image.base64EncodedString()
                self.image["base64String"] = "data:\(mimeType);base64,\(encodedImage)"
            }
        }
    }

    public func getJSObject() -> JSObject {
        var contact = JSObject()

        // Id
        contact["contactId"] = self.contactId

        // Name
        if self.name.count > 0 {
            contact["name"] = self.name
        }

        // Organization
        if self.organization.count > 0 {
            contact["organization"] = self.organization
        }

        // Birthday
        if self.birthday.count > 0 {
            contact["birthday"] = self.birthday
        }

        // Note
        if self.note != nil {
            contact["note"] = self.note
        }

        // Phones
        if self.phones.count > 0 {
            contact["phones"] = self.phones
        }

        // Emails
        if self.emails.count > 0 {
            contact["emails"] = self.emails
        }

        // URLs
        if self.urls.count > 0 {
            contact["urls"] = self.urls
        }

        // Postal Addresses
        if self.postalAddresses.count > 0 {
            contact["postalAddresses"] = self.postalAddresses
        }

        // Image
        if self.image.count > 0 {
            contact["image"] = self.image
        }

        return contact
    }
}
