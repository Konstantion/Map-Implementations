package org.example.dictionaries;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SortedArrayNavigableDictionary<K extends Comparable<K>, V> extends AbstractDictionary<K, V>
        implements INavigableDictionary<K, V> {
    private final List<IEntry<K, V>> entries;

    public SortedArrayNavigableDictionary() {
        this.entries = new ArrayList<>();
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return binarySearch(key) >= 0;
    }

    @Override
    public boolean containsValue(V value) {
        for (IEntry<K, V> entry : entries) {
            if (Objects.equals(entry.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (IEntry<K, V> entry : entries) {
            values.add(entry.getValue());
        }
        return values;
    }

    @Override
    public Collection<K> keys() {
        List<K> keys = new ArrayList<>();
        for (IEntry<K, V> entry : entries) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    @Override
    public V put(K key, V value) {
        int index = binarySearch(key);
        if (index >= 0) {
            IEntry<K, V> entry = entries.get(index);
            V oldValue = entry.getValue();
            entry.setValue(value);
            return oldValue;
        } else {
            int insertIndex = -(index + 1);
            entries.add(insertIndex, new SimpleEntry<>(key, value));
            return null;
        }
    }

    @Override
    public V get(K key) {
        int index = binarySearch(key);
        if (index >= 0) {
            return entries.get(index).getValue();
        }
        return null;
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public V remove(K key) {
        int index = binarySearch(key);
        if (index >= 0) {
            IEntry<K, V> entry = entries.remove(index);
            return entry.getValue();
        }
        return null;
    }

    @Override
    public boolean remove(K key, V value) {
        int index = binarySearch(key);
        if (index >= 0 && Objects.equals(entries.get(index).getValue(), value)) {
            entries.remove(index);
            return true;
        }
        return false;
    }

    @Override
    public Set<IEntry<K, V>> entrySet() {
        return new HashSet<>(entries);
    }

    @Override
    public V replace(K key, V value) {
        int index = binarySearch(key);
        if (index >= 0) {
            IEntry<K, V> entry = entries.get(index);
            V oldValue = entry.getValue();
            entry.setValue(value);
            return oldValue;
        }
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        int index = binarySearch(key);
        if (index < 0) {
            int insertIndex = -(index + 1);
            entries.add(insertIndex, new SimpleEntry<>(key, value));
            return null;
        } else {
            return entries.get(index).getValue();
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        V value = get(key);
        if (value == null) {
            value = mappingFunction.apply(key);
            put(key, value);
        }
        return value;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        int index = binarySearch(key);
        if (index >= 0) {
            IEntry<K, V> entry = entries.get(index);
            V oldValue = entry.getValue();
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                entry.setValue(newValue);
                return newValue;
            } else {
                entries.remove(index);
                return null;
            }
        }
        return null;
    }

    @Override
    public IEntry<K, V> lowerEntry(K key) {
        int index = binarySearch(key);
        if (index < 0) {
            index = -(index + 1);
        }
        if (index > 0) {
            return entries.get(index - 1);
        }
        return null;
    }

    @Override
    public K lowerKey(K key) {
        IEntry<K, V> entry = lowerEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public IEntry<K, V> floorEntry(K key) {
        int index = binarySearch(key);
        if (index >= 0) {
            return entries.get(index);
        } else {
            index = -(index + 1);
            if (index > 0) {
                return entries.get(index - 1);
            }
        }
        return null;
    }

    @Override
    public K floorKey(K key) {
        IEntry<K, V> entry = floorEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public IEntry<K, V> ceilingEntry(K key) {
        int index = binarySearch(key);
        if (index >= 0) {
            return entries.get(index);
        } else {
            index = -(index + 1);
            if (index < entries.size()) {
                return entries.get(index);
            }
        }
        return null;
    }

    @Override
    public K ceilingKey(K key) {
        IEntry<K, V> entry = ceilingEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public IEntry<K, V> higherEntry(K key) {
        int index = binarySearch(key);
        if (index >= 0) {
            if (index < entries.size() - 1) {
                return entries.get(index + 1);
            }
        } else {
            index = -(index + 1);
            if (index < entries.size()) {
                return entries.get(index);
            }
        }
        return null;
    }

    @Override
    public K higherKey(K key) {
        IEntry<K, V> entry = higherEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    private int binarySearch(K key) {
        return Collections.binarySearch(entries, new SimpleEntry<>(key, null),
                Comparator.comparing(IEntry::getKey));
    }

    private static class SimpleEntry<K, V> implements IEntry<K, V> {
        private final K key;
        private V value;

        public SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
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
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimpleEntry<?, ?> that = (SimpleEntry<?, ?>) o;
            return Objects.equals(key, that.key) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
