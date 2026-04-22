import Foundation
import Capacitor
import Contacts

public class GetContactsProjectionInput {
    // Name
    private var name: Bool?

    // Organization
    private var organization: Bool?

    // Birthday
    private var birthday: Bool?

    // Note
    private var note: Bool?

    // Phones
    private var phones: Bool?

    // Emails
    private var emails: Bool?

    // URLs
    private var urls: Bool?

    // Postal Addresses
    private var postalAddresses: Bool?

    // Image
    private var image: Bool?

    init(_ fromJSONObject: JSObject) {
        // Name
        self.name = fromJSONObject["name"] as? Bool

        // Organization
        self.organization = fromJSONObject["organization"] as? Bool

        // Birthday
        self.birthday = fromJSONObject["birthday"] as? Bool

        // Note
        self.note = fromJSONObject["note"] as? Bool

        // Phones
        self.phones = fromJSONObject["phones"] as? Bool

        // Emails
        self.emails = fromJSONObject["emails"] as? Bool

        // URLs
        self.urls = fromJSONObject["urls"] as? Bool

        // Postal Addresses
        self.postalAddresses = fromJSONObject["postalAddresses"] as? Bool

        // Image
        self.image = fromJSONObject["image"] as? Bool
    }

    public func getProjection() -> [CNKeyDescriptor] {
        var projection: [CNKeyDescriptor] = []

        // Name
        if self.name == true {
            projection.append(CNContactFormatter.descriptorForRequiredKeys(for: .fullName))
            projection.append(CNContactGivenNameKey as CNKeyDescriptor)
            projection.append(CNContactMiddleNameKey as CNKeyDescriptor)
            projection.append(CNContactFamilyNameKey as CNKeyDescriptor)
            projection.append(CNContactNamePrefixKey as CNKeyDescriptor)
            projection.append(CNContactNameSuffixKey as CNKeyDescriptor)
        }

        // Organization
        if self.organization == true {
            projection.append(CNContactOrganizationNameKey as CNKeyDescriptor)
            projection.append(CNContactJobTitleKey as CNKeyDescriptor)
            projection.append(CNContactDepartmentNameKey as CNKeyDescriptor)
        }

        // Birthday
        if self.birthday == true {
            projection.append(CNContactBirthdayKey as CNKeyDescriptor)
        }

        // Note
        if self.note == true {
            projection.append(CNContactNoteKey as CNKeyDescriptor)
        }

        // Phones
        if self.phones == true {
            projection.append(CNContactPhoneNumbersKey as CNKeyDescriptor)
        }

        // Emails
        if self.emails == true {
            projection.append(CNContactEmailAddressesKey as CNKeyDescriptor)
        }

        // URLs
        if self.urls == true {
            projection.append(CNContactUrlAddressesKey as CNKeyDescriptor)
        }

        // Postal Addresses
        if self.postalAddresses == true {
            projection.append(CNContactPostalAddressesKey as CNKeyDescriptor)
        }

        // Image
        if self.image == true {
            projection.append(CNContactImageDataKey as CNKeyDescriptor)
        }

        return projection
    }
}
