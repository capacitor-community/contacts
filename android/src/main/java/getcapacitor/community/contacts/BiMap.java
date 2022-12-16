package getcapacitor.community.contacts;

import java.util.HashMap;

// Helper to enable bidirectional mapping.
public class BiMap<K, V> {

    // The following map can be used for retrieving the key belonging to a certain value.
    // This map is essentially the inverted version of `mapForValues`
    private final HashMap<V, K> mapForKeys = new HashMap<>();
    // The following map can be used for retrieving the value belonging to a certain key.
    private final HashMap<K, V> mapForValues;

    private final K defaultKey;
    private final V defaultValue;

    BiMap(HashMap<K, V> map, K defaultKey, V defaultValue) {
        this.mapForValues = map;

        for (HashMap.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            this.mapForKeys.put(value, key);
        }

        this.defaultKey = defaultKey;
        this.defaultValue = defaultValue;
    }

    K getKey(V v) {
        K k = this.mapForKeys.get(v);
        if (k == null) {
            return this.defaultKey;
        }
        return k;
    }

    V getValue(K k) {
        V v = this.mapForValues.get(k);
        if (v == null) {
            return this.defaultValue;
        }
        return v;
    }
}
