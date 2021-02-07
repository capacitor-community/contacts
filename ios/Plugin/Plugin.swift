//
//  Plugin.swift
//
//
//  Created by Jonathan Gerber on 15.02.20.
//  Copyright © 2020 Byrds & Bytes GmbH. All rights reserved.

import Foundation
import Capacitor
import Contacts


@objc(ContactsPlugin)
public class ContactsPlugin: CAPPlugin {
    
    @objc func getPermissions(_ call: CAPPluginCall) {
        print("checkPermission was triggered in Swift")
        Permissions.contactPermission { granted in
            switch granted {
            case true:
                call.success([
                    "granted": true
                ])
            default:
                call.success([
                    "granted": false
                ])
            }
        }
    }
    
    @objc func getContacts(_ call: CAPPluginCall) {
        var contactsArray : [PluginResultData] = [];
        Permissions.contactPermission { granted in
            if granted {
                do {
                    let contacts = try Contacts.getContactFromCNContact()
                    
                    for contact in contacts {
                        var phoneNumbers: [PluginResultData] = []
                        var emails: [PluginResultData] = []
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
                        let dateFormatter = DateFormatter()
                        // You must set the time zone from your default time zone to UTC +0,
                        // which is what birthdays in Contacts are set to.
                        dateFormatter.timeZone = TimeZone(identifier: "UTC")
                        dateFormatter.dateFormat = "YYYY-MM-dd"
                        
                        var contactResult: PluginResultData = [
                            "contactId": contact.identifier,
                            "displayName": "\(contact.givenName) \(contact.familyName)",
                            "phoneNumbers": phoneNumbers,
                            "emails": emails,
                        ]
                        if let photoThumbnail = contact.thumbnailImageData {
                            contactResult["photoThumbnail"] = "data:image/png;base64,\(photoThumbnail.base64EncodedString())"
                            if let birthday = contact.birthday?.date {
                                contactResult["birthday"] = dateFormatter.string(from: birthday)
                            }
                            if !contact.organizationName.isEmpty {
                                contactResult["organizationName"] = contact.organizationName
                                contactResult["organizationRole"] = contact.jobTitle
                            }
                        }
                        contactsArray.append(contactResult)
                    }
                    call.success([
                        "contacts": contactsArray
                    ])
                } catch let error as NSError {
                    call.error("Generic Error", error)
                }
            } else {
                call.error("User denied access to contacts")
            }
        }
    }
}

