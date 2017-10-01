package it.cbnoc.collection;

import it.cbnoc.utils.Result;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Map<K, V> {

    private final ConcurrentMap<K,V> _map = new ConcurrentHashMap<>();

    public static <K,V> Map<K,V> empty(){
        return new Map<>();
    }

    public static <K,V> Map<K,V> add(Map<K,V> map, K k, V v) {
        map._map.put(k,v);
        return map;
    }

    public Result<V> get(final K k) {
        return this._map.containsKey(k)
                ? Result.success(this._map.get(k))
                : Result.empty();
    }

    public Map<K,V> put(K key, V value) {
        return add(this, key, value);
    }

    public Map<K,V> removeKey(K key) {
        this._map.remove(key);
        return this;
    }

    @Override
    public String toString() {
        return _map.toString();
    }
}
