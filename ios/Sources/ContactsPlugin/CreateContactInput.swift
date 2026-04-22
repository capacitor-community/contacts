import Foundation
import Capacitor

public class CreateContactInput {

    // Name
    public var nameGiven: String?
    public var nameMiddle: String?
    public var nameFamily: String?
    public var namePrefix: String?
    public var nameSuffix: String?

    // Organization
    public var organizationName: String?
    public var organizationJobTitle: String?
    public var organizationDepartment: String?

    // Birthday
    public var birthday: DateComponents?

    // Note
    public var note: String?

    // Phones
    public var phones: [PhoneInput] = []

    // Emails
    public var emails: [EmailInput] = []

    // URLs
    public var urls: [String] = []

    // Postal Addresses
    public var postalAddresses: [PostalAddressInput] = []

    // Image
    public var image: ImageInput?

    init(_ fromJSONObject: JSObject) {
        // Name
        if let nameObject = fromJSONObject["name"] as? JSObject {
            self.nameGiven = nameObject["given"] as? String
            self.nameMiddle = nameObject["middle"] as? String
            self.nameFamily = nameObject["family"] as? String
            self.namePrefix = nameObject["prefix"] as? String
            self.nameSuffix = nameObject["suffix"] as? String
        }

        // Organization
        if let organizationObject = fromJSONObject["organization"] as? JSObject {
            self.organizationName = organizationObject["company"] as? String
            self.organizationJobTitle = organizationObject["jobTitle"] as? String
            self.organizationDepartment = organizationObject["department"] as? String
        }

        // Birthday
        if let birthdayObject = fromJSONObject["birthday"] as? JSObject {
            let year = birthdayObject["year"] as? Int
            let month = birthdayObject["month"] as? Int
            let day = birthdayObject["day"] as? Int
            let dateComponents = DateComponents(calendar: .current, timeZone: nil, year: year, month: month, day: day)

            if dateComponents.isValidDate {
                self.birthday = dateComponents
            }
        }

        // Note
        self.note = fromJSONObject["note"] as? String

        // Phones
        if let phonesArray = fromJSONObject["phones"] as? JSArray {
            for phone in phonesArray {
                if let phoneObject = phone as? JSObject {
                    self.phones.append(PhoneInput.init(phoneObject))
                }
            }
        }

        // Emails
        if let emailsArray = fromJSONObject["emails"] as? JSArray {
            for email in emailsArray {
                if let emailObject = email as? JSObject {
                    self.emails.append(EmailInput.init(emailObject))
                }
            }
        }

        // URLs
        if let urlsArray = fromJSONObject["urls"] as? JSArray {
            for url in urlsArray {
                if let url = url as? String {
                    self.urls.append(url)
                }
            }
        }

        // Postal Addresses
        if let postalAddressesArray = fromJSONObject["postalAddresses"] as? JSArray {
            for postalAddress in postalAddressesArray {
                if let postalAddressObject = postalAddress as? JSObject {
                    self.postalAddresses.append(PostalAddressInput.init(postalAddressObject))
                }
            }
        }

        // Image
        if let imageObject = fromJSONObject["image"] as? JSObject {
            self.image = ImageInput.init(imageObject)
        }
    }

    static func getType(_ type: String?, _ fromJSONObject: JSObject) -> String? {
        // On Android, a custom label is saved with `type` set to "custom" and a separate `label` attribute.
        // On iOS, on the contrary, the custom value is just saved into the `type` attribute.

        if type == "custom" {
            return fromJSONObject["label"] as? String ?? nil
        }

        return type
    }

    public class PhoneInput {

        public var type: String?

        public var number: String?

        init(_ fromJSONObject: JSObject) {
            let type = Contacts.phoneTypeMap.getValue(fromJSONObject["type"] as? String)

            self.type = CreateContactInput.getType(type, fromJSONObject)

            self.number = fromJSONObject["number"] as? String
        }
    }

    public class EmailInput {

        public var type: String?

        public var address: String?

        init(_ fromJSONObject: JSObject) {
            let type = Contacts.emailTypeMap.getValue(fromJSONObject["type"] as? String)

            self.type = CreateContactInput.getType(type, fromJSONObject)

            self.address = fromJSONObject["address"] as? String
        }
    }

    public class PostalAddressInput {

        public var type: String?

        public var street: String?
        public var neighborhood: String?
        public var city: String?
        public var region: String?
        public var postcode: String?
        public var country: String?

        init(_ fromJSONObject: JSObject) {
            let type = Contacts.postalAddressTypeMap.getValue(fromJSONObject["type"] as? String)

            self.type = CreateContactInput.getType(type, fromJSONObject)

            self.street = fromJSONObject["street"] as? String
            self.neighborhood = fromJSONObject["neighborhood"] as? String
            self.city = fromJSONObject["city"] as? String
            self.region = fromJSONObject["region"] as? String
            self.postcode = fromJSONObject["postcode"] as? String
            self.country = fromJSONObject["country"] as? String
        }
    }

    public class ImageInput {

        public var base64String: String?

        init(_ fromJSONObject: JSObject) {
            self.base64String = fromJSONObject["base64String"] as? String
        }
    }
}
