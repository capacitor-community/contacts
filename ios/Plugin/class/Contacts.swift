//
//  Contacts.swift
//  Plugin
//
//  Created by Jonathan Gerber on 16.02.20.
//  Copyright © 2020 Jonathan Gerber. All rights reserved.
//

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
            ] as [Any]

        //Get all the containers
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
}


