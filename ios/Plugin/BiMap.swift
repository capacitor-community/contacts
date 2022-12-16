import Foundation

// Helper to enable bidirectional mapping.
public class BiMap<K: Hashable, V: Hashable> {
    // The following map can be used for retrieving the key belonging to a certain value.
    // This map is essentially the inverted version of `mapForValues`
    private var mapForKeys: [V: K] = [:]
    // The following map can be used for retrieving the value belonging to a certain key.
    private var mapForValues: [K: V] = [:]

    private var defaultKey: K
    private var defaultValue: V

    init(_ map: [K: V], _ defaultKey: K, _ defaultValue: V) {
        self.mapForValues = map

        for entry in map {
            let key: K = entry.key
            let value: V = entry.value
            self.mapForKeys[value] = key
        }

        self.defaultKey = defaultKey
        self.defaultValue = defaultValue
    }

    func getKey(_ v: V?) -> K {
        let k: K? = self.mapForKeys[v ?? "" as! V]
        guard let k = k else {
            return self.defaultKey
        }
        return k
    }

    func getValue(_ k: K?) -> V {
        let v: V? = self.mapForValues[k ?? "" as! K]
        guard let v = v else {
            return self.defaultValue
        }
        return v
    }
}
