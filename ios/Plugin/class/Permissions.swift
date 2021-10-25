//
//  Permission.swift
//  Plugin
//
//  Created by Jonathan Gerber on 15.02.20.
//  Copyright © 2020 Byrds & Bytes GmbH. All rights reserved.
//

import Foundation
import Contacts

class Permissions {

    class func contactPermission(completionHandler: @escaping (_ accessGranted: Bool) -> Void) {
        let contactStore = CNContactStore()
        switch CNContactStore.authorizationStatus(for: .contacts) {
        case .authorized:
            completionHandler(true)
        case .denied:
            completionHandler(false)
        case .restricted, .notDetermined:
            contactStore.requestAccess(for: .contacts) { granted, _ in
                if granted {
                    completionHandler(true)
                } else {
                    DispatchQueue.main.async {
                        completionHandler(false)
                    }
                }
            }
        }
    }
}
