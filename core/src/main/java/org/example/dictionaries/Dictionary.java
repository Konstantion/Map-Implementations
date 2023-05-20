package org.example.dictionaries;

import org.example.dictionaries.entries.KeyValueHolder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Dictionary<K, V> {
    boolean isEmpty();

    boolean containsKey(K key);

    boolean containsValue(V value);

    Collection<V> values();
    Collection<K> keys();


    /**
     * @param key key for value to put
     * @param value value to put
     * @return previous value associated to key or null if there was no mapping for key
     */
    V put(K key, V value);

    V get(K key);

    V getOrDefault(K key, V defaultValue);

    int size();

    V remove(K key);
    boolean remove(K key, V value);

    Set<Entry<K, V>> entrySet();


    /**
     * @param key key for value to replace
     * @param value value to set
     * @return previous value associated to key or null if there was no mapping for key
     */
    V replace(K key, V value);

    static <K, V> Dictionary.Entry<K, V> entry(K k, V v) {
        return new KeyValueHolder<>(k, v);
    }

    interface Entry<K, V> {
        K getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCode();

        static <K extends Comparable<? super K>, V> Comparator<Dictionary.Entry<K, V>> comparingByKey() {
            return (Comparator<Dictionary.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        static <K, V extends Comparable<? super V>> Comparator<Dictionary.Entry<K, V>> comparingByValue() {
            return (Comparator<Dictionary.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }

        static <K, V> Comparator<Dictionary.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Dictionary.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
        }

        static <K, V> Comparator<Dictionary.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Dictionary.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
        }

        @SuppressWarnings("unchecked")
        static <K, V> Dictionary.Entry<K, V> copyOf(Dictionary.Entry<? extends K, ? extends V> e) {
            Objects.requireNonNull(e);
            if (e instanceof KeyValueHolder) {
                return (Dictionary.Entry<K, V>) e;
            } else {
                return Dictionary.entry(e.getKey(), e.getValue());
            }
        }
    }

    V putIfAbsent(K key, V value);

    V computeIfAbsent(K key,
                      Function<? super K, ? extends V> mappingFunction);

    V computeIfPresent(K key,
                       BiFunction<? super K, ? super V, ? extends V> remappingFunction);

    static <K, V> Dictionary<K, V> ofEntries(Dictionary.Entry<? extends K, ? extends V>... entries) {
        if (entries.length == 0) {
            @SuppressWarnings("unchecked")
            var map = (Dictionary<K, V>) AbstractImmutableDictionary.EMPTY_DICTIONARY;
            return map;
        } else {
            Object[] kva = new Object[entries.length << 1];
            int a = 0;
            for (Dictionary.Entry<? extends K, ? extends V> entry : entries) {
                kva[a++] = entry.getKey();
                kva[a++] = entry.getValue();
            }
            return new AbstractImmutableDictionary.DictionaryN<>(kva);
        }
    }
}
