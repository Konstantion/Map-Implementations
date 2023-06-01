package org.example.dictionaries.entries;

import org.example.dictionaries.IDictionary;

import java.util.Objects;

public class KeyValueHolder<K, V> implements IDictionary.IEntry<K, V> {

    final K key;

    final V value;

    public KeyValueHolder(K k, V v) {
        key = Objects.requireNonNull(k);
        value = Objects.requireNonNull(v);
    }


    @Override
    public K getKey() {
        return key;
    }


    @Override
    public V getValue() {
        return value;
    }


    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException("not supported");
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof IDictionary.IEntry<?, ?> e
               && key.equals(e.getKey())
               && value.equals(e.getValue());
    }


    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }


    @Override
    public String toString() {
        return key + "=" + value;
    }
}
