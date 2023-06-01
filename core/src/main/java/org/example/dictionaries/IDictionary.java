package org.example.dictionaries;

import org.example.dictionaries.entries.KeyValueHolder;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IDictionary<K, V> {
    boolean isEmpty();

    boolean containsKey(K key);

    boolean containsValue(V value);

    Collection<V> values();

    Collection<K> keys();


    /**
     * @param key   key for value to put
     * @param value value to put
     * @return previous value associated to key or null if there was no mapping for key
     */
    V put(K key, V value);

    V get(K key);

    V getOrDefault(K key, V defaultValue);

    int size();

    V remove(K key);

    boolean remove(K key, V value);

    Set<IEntry<K, V>> entrySet();


    /**
     * @param key   key for value to replace
     * @param value value to set
     * @return previous value associated to key or null if there was no mapping for key
     */
    V replace(K key, V value);

    static <K, V> IEntry<K, V> entry(K k, V v) {
        return new KeyValueHolder<>(k, v);
    }

    interface IEntry<K, V> {
        K getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCode();
    }

    V putIfAbsent(K key, V value);

    V computeIfAbsent(K key,
                      Function<? super K, ? extends V> mappingFunction);

    V computeIfPresent(K key,
                       BiFunction<? super K, ? super V, ? extends V> remappingFunction);

    static <K, V> IDictionary<K, V> ofEntries(IEntry<? extends K, ? extends V>... entries) {
        if (entries.length == 0) {
            @SuppressWarnings("unchecked")
            var map = (IDictionary<K, V>) AbstractImmutableDictionary.EMPTY_DICTIONARY;
            return map;
        } else {
            Object[] kva = new Object[entries.length << 1];
            int a = 0;
            for (IEntry<? extends K, ? extends V> entry : entries) {
                kva[a++] = entry.getKey();
                kva[a++] = entry.getValue();
            }
            return new AbstractImmutableDictionary.DictionaryN<>(kva);
        }
    }
}
