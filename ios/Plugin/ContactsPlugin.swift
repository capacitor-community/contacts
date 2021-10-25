//
//  Plugin.swift
//
//
//  Created by Jonathan Gerber on 15.02.20.
//  Copyright © 2020 Byrds & Bytes GmbH. All rights reserved.
//

import Foundation
import Capacitor
import Contacts

@objc(ContactsPlugin)
public class ContactsPlugin: CAPPlugin {

    private let birthdayFormatter = DateFormatter()

    override public func load() {
        // You must set the time zone from your default time zone to UTC +0,
        // which is what birthdays in Contacts are set to.
        birthdayFormatter.timeZone = TimeZone(identifier: "UTC")
        birthdayFormatter.dateFormat = "YYYY-MM-dd"
    }

    @objc func getPermissions(_ call: CAPPluginCall) {
        print("checkPermission was triggered in Swift")
        Permissions.contactPermission { granted in
            switch granted {
            case true:
                call.resolve([
                    "granted": true
                ])
            default:
                call.resolve([
                    "granted": false
                ])
            }
        }
    }

    @objc func getContacts(_ call: CAPPluginCall) {
        var contactsArray: [PluginCallResultData] = []
        Permissions.contactPermission { granted in
            if granted {
                do {
                    let contacts = try Contacts.getContactFromCNContact()

                    for contact in contacts {
                        var phoneNumbers: [PluginCallResultData] = []
                        var emails: [PluginCallResultData] = []
                        for number in contact.phoneNumbers {
                            let numberToAppend = number.value.stringValue
                            let label = number.label ?? ""
                            let labelToAppend = CNLabeledValue<CNPhoneNumber>.localizedString(forLabel: label)
                            phoneNumbers.append([
                                "label": labelToAppend,
                                "number": numberToAppend
                            ])
                            print(phoneNumbers)
                        }
                        for email in contact.emailAddresses {
                            let emailToAppend = email.value as String
                            let label = email.label ?? ""
                            let labelToAppend = CNLabeledValue<NSString>.localizedString(forLabel: label)
                            emails.append([
                                "label": labelToAppend,
                                "address": emailToAppend
                            ])
                        }

                        var contactResult: PluginCallResultData = [
                            "contactId": contact.identifier,
                            "displayName": "\(contact.givenName) \(contact.familyName)",
                            "phoneNumbers": phoneNumbers,
                            "emails": emails
                        ]
                        if let photoThumbnail = contact.thumbnailImageData {
                            contactResult["photoThumbnail"] = "data:image/png;base64,\(photoThumbnail.base64EncodedString())"
                            if let birthday = contact.birthday?.date {
                                contactResult["birthday"] = self.birthdayFormatter.string(from: birthday)
                            }
                            if !contact.organizationName.isEmpty {
                                contactResult["organizationName"] = contact.organizationName
                                contactResult["organizationRole"] = contact.jobTitle
                            }
                        }
                        contactsArray.append(contactResult)
                    }
                    call.resolve([
                        "contacts": contactsArray
                    ])
                } catch let error as NSError {
                    call.reject("Generic Error", error as? String)
                }
            } else {
                call.reject("User denied access to contacts")
            }
        }
    }

    /**
     * [WIP]
     *
     * Find a contact by name or fallback to return all contacts if no search string is given.
     */
    @objc func findContacts(_ call: CAPPluginCall) {
        Permissions.contactPermission { granted in
            if !granted {
                call.reject("User denied access to contacts")
                return
            }
        }

        let searchString = call.getString("searchString", "")
        if searchString == "" {
            return getContacts(call)
        }

        do {
            let contacts = try Contacts.findContacts(withName: searchString)

            call.resolve([
                "contacts": contacts
            ])
        } catch let error as NSError {
            call.reject("Error during find contacts", error as? String)
        }
    }

    @objc func saveContact(_ call: CAPPluginCall) {
        Permissions.contactPermission { granted in
            if !granted {
                call.reject("User denied access to contacts")
                return
            }
        }

        let contact = CNMutableContact()

        contact.contactType = CNContactType(rawValue: call.getInt("contactType", 0))!

        // Name information

        contact.namePrefix = call.getString("namePrefix", "")
        contact.givenName = call.getString("givenName", "")
        contact.middleName = call.getString("middleName", "")
        contact.familyName = call.getString("familyName", "")
        contact.nameSuffix = call.getString("nameSuffix", "")
        contact.nickname = call.getString("nickname", "")

        // Work information

        contact.jobTitle = call.getString("jobTitle", "")
        contact.departmentName = call.getString("departmentName", "")
        contact.organizationName = call.getString("organizationName", "")

        // Addresses

        for value in call.getArray("emailAddresses", JSObject.self) ?? [] {
            if let address = value["address"] as? NSString {
                contact.emailAddresses.append(CNLabeledValue(
                    label: value["label"] as? String,
                    value: address
                ))
            }
        }

        for value in call.getArray("urlAddresses", JSObject.self) ?? [] {
            if let url = value["url"] as? NSString {
                contact.urlAddresses.append(CNLabeledValue(
                    label: value["label"] as? String,
                    value: url
                ))
            }
        }

        for value in call.getArray("postalAddresses", JSObject.self) ?? [] {
            if let address = value["address"] as? JSObject {
                contact.postalAddresses.append(CNLabeledValue(
                    label: value["label"] as? String,
                    value: Contacts.getPostalAddressFromAddress(jsAddress: address)
                ))
            }
        }

        // Other

        for value in call.getArray("phoneNumbers", JSObject.self) ?? [] {
            if let number = value["number"] as? String {
                contact.phoneNumbers.append(CNLabeledValue(
                    label: value["label"] as? String,
                    value: CNPhoneNumber(stringValue: number)
                ))
            }
        }

        contact.note = call.getString("note", "")

        if let birthday = self.birthdayFormatter.date(from: call.getString("birthday", "")) {
            contact.birthday = Calendar.current.dateComponents([.day, .month, .year], from: birthday)
        }

        for value in call.getArray("socialProfiles", JSObject.self) ?? [] {
            if let profile = value["profile"] as? JSObject {
                contact.socialProfiles.append(CNLabeledValue(
                    label: value["label"] as? String,
                    value: CNSocialProfile(
                        urlString: profile["urlString"] as? String,
                        username: profile["username"] as? String,
                        userIdentifier: "", // TODO: what is this?
                        service: profile["service"] as? String
                    )
                ))
            }
        }

        // --- Save

        do {
            let saveRequest = CNSaveRequest()
            saveRequest.add(contact, toContainerWithIdentifier: nil)
            try CNContactStore().execute(saveRequest)
            call.resolve()
        } catch let error as NSError {
            call.reject("Error during saving new contact", error as? String)
        }
    }

}
