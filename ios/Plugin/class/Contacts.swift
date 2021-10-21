//
//  Contacts.swift
//  Plugin
//
//  Created by Jonathan Gerber on 16.02.20.
//  Copyright © 2020 Byrds & Bytes GmbH. All rights reserved.
//

import Capacitor
import Foundation
import Contacts

class Contacts {

    class func getContactFromCNContact() throws -> [CNContact] {
        let contactStore = CNContactStore()

        let keysToFetch = [
            CNContactFormatter.descriptorForRequiredKeys(for: .fullName),
            CNContactPhoneNumbersKey,
            CNContactGivenNameKey,
            CNContactMiddleNameKey,
            CNContactFamilyNameKey,
            CNContactEmailAddressesKey,
            CNContactThumbnailImageDataKey,
            CNContactBirthdayKey,
            CNContactOrganizationNameKey,
            CNContactJobTitleKey
        ] as [Any]

        // Get all the containers
        var allContainers: [CNContainer] = []
        allContainers = try contactStore.containers(matching: nil)

        var results: [CNContact] = []

        // Iterate all containers and append their contacts to our results array
        for container in allContainers {
            let fetchPredicate = CNContact.predicateForContactsInContainer(withIdentifier: container.identifier)
            let containerResults = try contactStore.unifiedContacts(matching: fetchPredicate, keysToFetch: keysToFetch as! [CNKeyDescriptor])
            results.append(contentsOf: containerResults)
        }

        return results
    }

    class func findContacts(withName: String) throws -> [CNContact] {
        var contacts = [CNContact]()
        let predicate: NSPredicate = CNContact.predicateForContacts(matchingName: withName)
        let contactStore = CNContactStore()
        contacts = try contactStore.unifiedContacts(matching: predicate, keysToFetch: [CNContactVCardSerialization.descriptorForRequiredKeys()])
        return contacts
    }

    class func getPostalAddressFromAddress(jsAddress: JSObject) -> CNMutablePostalAddress {
        let address = CNMutablePostalAddress()

        address.street = jsAddress["street"] as? String ?? ""
        address.state = jsAddress["state"] as? String ?? ""
        address.city = jsAddress["city"] as? String ?? ""
        address.country = jsAddress["country"] as? String ?? ""
        address.postalCode = jsAddress["postalCode"] as? String ?? ""

        return address
    }

}
