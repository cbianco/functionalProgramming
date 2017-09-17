package it.cbnoc.collection;

import it.cbnoc.utils.Option;

import java.util.concurrent.ConcurrentHashMap;

public class Map<K, V> {

    private final ConcurrentHashMap<K,V> _map = new ConcurrentHashMap<>();

    public static <K,V> Map<K,V> empty(){
        return new Map<>();
    }

    public static <K,V> Map<K,V> add(Map<K,V> map, K k, V v) {
        map._map.put(k,v);
        return map;
    }

    public Option<V> get(final K k) {
        return this._map.contains(k)
                ? Option.some(this._map.get(k))
                : Option.none();
    }

    public Map<K,V> put(K key, V value) {
        return add(this, key, value);
    }

    public Map<K,V> removeKey(K key) {
        this._map.remove(key);
        return this;
    }



}
