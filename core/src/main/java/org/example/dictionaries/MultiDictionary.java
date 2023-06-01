package org.example.dictionaries;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MultiDictionary<K, V> extends AbstractDictionary<K, V> implements IDictionary<K, V> {
    private final IDictionary<K, Set<V>> dictionary;

    public MultiDictionary() {
        dictionary = new HashDictionary<>();
    }

    @Override
    public boolean isEmpty() {
        return dictionary.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return dictionary.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        for (Set<V> valueSet : dictionary.values()) {
            if (valueSet.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V put(K key, V value) {
        Set<V> values = dictionary.get(key);
        if (values == null) {
            values = new HashSet<>();
            dictionary.put(key, values);
        }
        values.add(value);
        return null;
    }

    @Override
    public V get(K key) {
        Set<V> values = dictionary.get(key);
        return (values != null) ? values.iterator().next() : null;
    }

    @Override
    public boolean remove(K key, V value) {
        Set<V> values;

        return (values = dictionary.get(key)) != null
               && values.remove(value);
    }

    public Set<V> getAll(K key) {
        return dictionary.get(key);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        Set<V> values = dictionary.get(key);
        return (values != null) ? values.iterator().next() : defaultValue;
    }

    @Override
    public int size() {
        int size = 0;
        for (Set<V> values : dictionary.values()) {
            size += values.size();
        }
        return size;
    }

    @Override
    public V remove(K key) {
        Set<V> values = dictionary.remove(key);
        return (values != null && !values.isEmpty()) ? values.iterator().next() : null;
    }

    public Set<IEntry<K, V>> entrySet() {
        Set<IEntry<K, V>> entries = new HashSet<>();
        for (IEntry<K, Set<V>> entry : dictionary.entrySet()) {
            K key = entry.getKey();
            Set<V> values = entry.getValue();
            for (V value : values) {
                entries.add(new KeyValueHolder<>(key, value));
            }
        }
        return entries;
    }

    @Override
    public V replace(K key, V value) {
        Set<V> values = dictionary.get(key);
        if (values != null && !values.isEmpty()) {
            V oldValue = values.iterator().next();
            values.clear();
            values.add(value);
            return oldValue;
        }
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        Set<V> values = dictionary.get(key);
        if (values == null) {
            values = new HashSet<>();
            values.add(value);
            dictionary.put(key, values);
        } else if (values.isEmpty()) {
            values.add(value);
        }
        return null;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        Set<V> values = dictionary.get(key);
        if (values == null) {
            V newValue = mappingFunction.apply(key);
            if (newValue != null) {
                values = new HashSet<>();
                values.add(newValue);
                dictionary.put(key, values);
                return newValue;
            }
        } else if (values.isEmpty()) {
            V newValue = mappingFunction.apply(key);
            if (newValue != null) {
                values.add(newValue);
                return newValue;
            }
        }
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Set<V> values = dictionary.get(key);
        if (values != null && !values.isEmpty()) {
            V oldValue = values.iterator().next();
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                values.clear();
                values.add(newValue);
                return newValue;
            } else {
                values.clear();
                dictionary.remove(key);
            }
        }
        return null;
    }

    private static class KeyValueHolder<K, V> implements IEntry<K, V> {
        private final K key;
        private V value;

        public KeyValueHolder(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IDictionary.IEntry)) {
                return false;
            }
            IEntry<?, ?> entry = (IEntry<?, ?>) o;
            return Objects.equals(key, entry.getKey()) &&
                   Objects.equals(value, entry.getValue());
        }

        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}