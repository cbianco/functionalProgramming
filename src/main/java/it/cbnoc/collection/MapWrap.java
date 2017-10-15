package it.cbnoc.collection;

import it.cbnoc.utils.Result;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapWrap<K, V> {

    private final ConcurrentMap<K,V> _map = new ConcurrentHashMap<>();

    public static <K,V> MapWrap<K,V> empty(){
        return new MapWrap<>();
    }

    public static <K,V> MapWrap<K,V> add(MapWrap<K,V> mapWrap, K k, V v) {
        mapWrap._map.put(k,v);
        return mapWrap;
    }

    public Result<V> get(final K k) {
        return this._map.containsKey(k)
                ? Result.success(this._map.get(k))
                : Result.empty();
    }

    public MapWrap<K,V> put(K key, V value) {
        return add(this, key, value);
    }

    public MapWrap<K,V> removeKey(K key) {
        this._map.remove(key);
        return this;
    }

    @Override
    public String toString() {
        return _map.toString();
    }
}
