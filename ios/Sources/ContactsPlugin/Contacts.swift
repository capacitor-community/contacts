import Foundation
import Contacts

public class Contacts: NSObject {
    public static let phoneTypeMap = BiMap<String, String>([
        // Home / Work
        "home": CNLabelHome,
        "work": CNLabelWork,
        // Other
        "other": CNLabelOther,
        // Custom
        "custom": "custom",
        // Phone specific
        "main": CNLabelPhoneNumberMain,
        "mobile": CNLabelPhoneNumberMobile,
        "pager": CNLabelPhoneNumberPager,
        "fax_home": CNLabelPhoneNumberHomeFax,
        "fax_work": CNLabelPhoneNumberWorkFax,
        "fax_other": CNLabelPhoneNumberOtherFax,
        //
        //
        // iOS only:
        // School
        // "school": CNLabelSchool, // (iOS ^13.x.x only)
        // Phone specific
        // "apple_watch": CNLabelPhoneNumberAppleWatch, // (iOS ^14.3.x only)
        "iphone": CNLabelPhoneNumberiPhone
    ], "custom", "custom")

    public static let emailTypeMap = BiMap<String, String>([
        // Home / Work
        "home": CNLabelHome,
        "work": CNLabelWork,
        // Other
        "other": CNLabelOther,
        // Custom
        "custom": "custom",
        //
        //
        // iOS only:
        // School
        // "school": CNLabelSchool, // (iOS ^13.x.x only)
        // Email specific
        "icloud": CNLabelEmailiCloud
    ], "custom", "custom")

    public static let postalAddressTypeMap = BiMap<String, String>([
        // Home / Work
        "home": CNLabelHome,
        "work": CNLabelWork,
        // Other
        "other": CNLabelOther,
        // Custom
        "custom": "custom"
        //
        //
        // iOS only:
        // School
        // "school": CNLabelSchool, // (iOS ^13.x.x only)
    ], "custom", "custom")

    public func getContact(_ contactId: String, _ projection: GetContactsProjectionInput) -> ContactPayload? {
        let projection = projection.getProjection()

        let cs = CNContactStore()

        do {
            let contactResult = try cs.unifiedContact(withIdentifier: contactId, keysToFetch: projection)

            let contact = ContactPayload(contactId)

            contact.fillData(contactResult)

            return contact
        } catch {
            return nil
        }
    }

    public func getContacts(_ projection: GetContactsProjectionInput) -> [ContactPayload] {
        let projection = projection.getProjection()

        var contacts = [ContactPayload]()

        let cs = CNContactStore()

        do {
            let request = CNContactFetchRequest(keysToFetch: projection)

            try cs.enumerateContacts(with: request) { (contactData, _) in
                let contact = ContactPayload(contactData.identifier)

                contact.fillData(contactData)

                contacts.append(contact)
            }
        } catch {
            // oops
        }

        return contacts
    }

    public func createContact(_ contactInput: CreateContactInput) -> String? {
        let newContact = CNMutableContact()

        // Name
        if let nameGiven = contactInput.nameGiven {
            newContact.givenName = nameGiven
        }
        if let nameMiddle = contactInput.nameMiddle {
            newContact.middleName = nameMiddle
        }
        if let nameFamily = contactInput.nameFamily {
            newContact.familyName = nameFamily
        }
        if let namePrefix = contactInput.namePrefix {
            newContact.namePrefix = namePrefix
        }
        if let nameSuffix = contactInput.nameSuffix {
            newContact.nameSuffix = nameSuffix
        }

        // Organization
        if let organizationName = contactInput.organizationName {
            newContact.organizationName = organizationName
        }
        if let organizationJobTitle = contactInput.organizationJobTitle {
            newContact.jobTitle = organizationJobTitle
        }
        if let organizationDepartment = contactInput.organizationDepartment {
            newContact.departmentName = organizationDepartment
        }

        // Birthday
        if let birthday = contactInput.birthday {
            newContact.birthday = birthday
        }

        // Note
        if let note = contactInput.note {
            newContact.note = note
        }

        // Phones
        newContact.phoneNumbers = contactInput.phones.map { phone in
            return CNLabeledValue(
                label: phone.type,
                value: CNPhoneNumber(stringValue: phone.number ?? "")
            )
        }

        // Email
        newContact.emailAddresses = contactInput.emails.map { email in
            return CNLabeledValue(
                label: email.type,
                value: email.address as? NSString ?? ""
            )
        }

        // URLs
        newContact.urlAddresses = contactInput.urls.map { url in
            return CNLabeledValue(
                label: nil,
                value: url as NSString
            )
        }

        // Postal Addresses
        newContact.postalAddresses = contactInput.postalAddresses.map({ postalAddress in
            let address = CNMutablePostalAddress()
            address.street = postalAddress.street ?? ""
            address.subAdministrativeArea = postalAddress.neighborhood ?? ""
            address.city = postalAddress.city ?? ""
            address.state = postalAddress.region ?? ""
            address.postalCode = postalAddress.postcode ?? ""
            address.country = postalAddress.country ?? ""

            return CNLabeledValue(
                label: postalAddress.type,
                value: address
            )
        })

        // Image
        if let image = contactInput.image {
            if let base64String = image.base64String {
                if let data = Data(base64Encoded: base64String, options: .ignoreUnknownCharacters) {
                    newContact.imageData = data
                }
            }
        }

        do {
            let cs = CNContactStore()
            let saveRequest = CNSaveRequest()
            saveRequest.add(newContact, toContainerWithIdentifier: nil)

            try cs.execute(saveRequest)

            if newContact.isKeyAvailable(CNContactIdentifierKey) {
                return newContact.identifier
            }

            return nil
        } catch {
            return nil
        }
    }

    public func deleteContact(_ contactId: String) -> Bool {
        let cs = CNContactStore()

        do {
            let contactResult = try cs.unifiedContact(withIdentifier: contactId, keysToFetch: [CNContactIdentifierKey] as [CNKeyDescriptor])

            let saveRequest = CNSaveRequest()
            saveRequest.delete(contactResult.mutableCopy() as! CNMutableContact)

            try cs.execute(saveRequest)

            return true
        } catch {
            return false
        }
    }
}
