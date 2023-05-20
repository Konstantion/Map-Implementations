package org.example.dictionaries;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IdentityHashDictionary<K, V> extends AbstractDictionary<K, V> implements Dictionary<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;

    public IdentityHashDictionary() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public IdentityHashDictionary(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive number");
        }
        int tableSize = calculateTableSize(capacity);
        this.table = (Entry<K, V>[]) new Entry[tableSize];
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        int index = findEntryIndex(key);
        return index != -1;
    }

    @Override
    public boolean containsValue(V value) {
        for (Entry<K, V> entry : table) {
            if (entry != null && Objects.equals(entry.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V put(K key, V value) {
        int index = findEntryIndex(key);
        if (index != -1) {
            Entry<K, V> entry = table[index];
            V oldValue = entry.getValue();
            entry.setValue(value);
            return oldValue;
        }

        ensureCapacity(size + 1);
        index = findEmptyIndex(key);
        Entry<K, V> newEntry = new Entry<>(key, value);
        table[index] = newEntry;
        size++;
        return null;
    }

    @Override
    public V get(K key) {
        int index = findEntryIndex(key);
        if (index != -1) {
            Entry<K, V> entry = table[index];
            return entry.getValue();
        }
        return null;
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V remove(K key) {
        int index = findEntryIndex(key);
        if (index != -1) {
            Entry<K, V> entry = table[index];
            table[index] = null;
            size--;
            return entry.getValue();
        }
        return null;
    }

    @Override
    public Set<Dictionary.Entry<K, V>> entrySet() {
        Set<Dictionary.Entry<K, V>> entrySet = new HashSet<>();
        for (Dictionary.Entry<K, V> entry : table) {
            if (entry != null) {
                entrySet.add(entry);
            }
        }
        return entrySet;
    }

    @Override
    public V replace(K key, V value) {
        int index = findEntryIndex(key);
        if (index != -1) {
            Entry<K, V> entry = table[index];
            V oldValue = entry.getValue();
            entry.setValue(value);
            return oldValue;
        }
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        int index = findEntryIndex(key);
        if (index != -1) {
            Entry<K, V> entry = table[index];
            return entry.getValue();
        }
        return put(key, value);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        int index = findEntryIndex(key);
        if (index != -1) {
            Entry<K, V> entry = table[index];
            return entry.getValue();
        }
        V value = mappingFunction.apply(key);
        put(key, value);
        return value;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        int index = findEntryIndex(key);
        if (index != -1) {
            Entry<K, V> entry = table[index];
            V oldValue = entry.getValue();
            V newValue = remappingFunction.apply(key, oldValue);
            entry.setValue(newValue);
            return newValue;
        }
        return null;
    }

    private int calculateTableSize(int capacity) {
        int tableSize = 1;
        while (tableSize < capacity) {
            tableSize <<= 1;
        }
        return tableSize;
    }

    private int findEntryIndex(K key) {
        int index = getIndex(key);
        int startIndex = index;
        do {
            Entry<K, V> entry = table[index];
            if (entry != null && entry.getKey() == key) {
                return index;
            }
            index = (index + 1) % table.length;
        } while (index != startIndex);
        return -1;
    }

    private int findEmptyIndex(K key) {
        int index = getIndex(key);
        int startIndex = index;
        do {
            if (table[index] == null) {
                return index;
            }
            index = (index + 1) % table.length;
        } while (index != startIndex);
        throw new IllegalStateException("Hash table is full.");
    }

    private int getIndex(K key) {
        return System.identityHashCode(key) & (table.length - 1);
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int requiredCapacity) {
        int capacity = table.length;
        if (requiredCapacity > (int) (capacity * LOAD_FACTOR)) {
            Entry<K, V>[] oldTable = table;
            int newCapacity = capacity << 1;
            Entry<K, V>[] newTable = new Entry[newCapacity];
            transferEntries(oldTable, newTable);
            table = newTable;
        }
    }

    private void transferEntries(Entry<K, V>[] source, Entry<K, V>[] destination) {
        for (Entry<K, V> entry : source) {
            if (entry != null) {
                int index = findEmptyIndex(entry.getKey());
                destination[index] = entry;
            }
        }
    }

    private static class Entry<K, V> implements Dictionary.Entry<K, V> {
        private final K key;
        private V value;

        Entry(K key, V value) {
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
            if (this == o) {
                return true;
            }
            if (o instanceof Entry) {
                Entry<?, ?> entry = (Entry<?, ?>) o;
                return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
            }
            return false;
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
