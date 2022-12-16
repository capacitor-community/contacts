import Foundation

@objc public class Contacts: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
