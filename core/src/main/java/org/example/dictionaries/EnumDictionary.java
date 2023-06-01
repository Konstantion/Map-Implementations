package org.example.dictionaries;

import org.example.dictionaries.entries.KeyValueHolder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EnumDictionary<K extends Enum<K>, V> extends AbstractDictionary<K, V> implements IDictionary<K, V> {
    private final Class<K> keyType;
    private final EnumDictionary.InternalDictionary<K, V> dictionary;

    public EnumDictionary(Class<K> keyType) {
        this.keyType = keyType;
        this.dictionary = new EnumDictionary.InternalDictionary<>(keyType);
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
        return dictionary.containsValue(value);
    }

    @Override
    public V put(K key, V value) {
        return dictionary.put(key, value);
    }

    @Override
    public V get(K key) {
        return dictionary.get(key);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        return dictionary.getOrDefault(key, defaultValue);
    }

    @Override
    public int size() {
        return dictionary.size();
    }

    @Override
    public V remove(K key) {
        return dictionary.remove(key);
    }

    public Set<IEntry<K, V>> entrySet() {
        return dictionary.entrySet();
    }

    @Override
    public V replace(K key, V value) {
        return dictionary.replace(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return dictionary.putIfAbsent(key, value);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return dictionary.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return dictionary.computeIfPresent(key, remappingFunction);
    }

    private static class InternalDictionary<K extends Enum<K>, V> extends AbstractDictionary<K, V> implements Serializable {
        private final Class<K> keyType;
        private final Enum<?>[] keyUniverse;
        private final Object[] values;

        InternalDictionary(Class<K> keyType) {
            this.keyType = keyType;
            this.keyUniverse = keyType.getEnumConstants();
            this.values = new Object[keyUniverse.length];
        }

        public V put(K key, V value) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);
            V oldValue = get(key);
            values[key.ordinal()] = value;
            return oldValue;
        }

        @SuppressWarnings("unchecked")
        @Override
        public V get(K key) {
            if (keyType.isInstance(key)) {
                K enumKey = key;
                return (V) values[enumKey.ordinal()];
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public V getOrDefault(K key, V defaultValue) {
            if (keyType.isInstance(key)) {
                K enumKey = key;
                V value = (V) values[enumKey.ordinal()];
                return (value != null) ? value : defaultValue;
            }
            return defaultValue;
        }

        @Override
        public boolean containsKey(K key) {
            if (keyType.isInstance(key)) {
                K enumKey = key;
                return (values[enumKey.ordinal()] != null);
            }
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            for (Object val : values) {
                if (Objects.equals(value, val)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int size() {
            return keyUniverse.length;
        }

        @Override
        public V remove(K key) {
            if (keyType.isInstance(key)) {
                K enumKey = key;
                V oldValue = get(enumKey);
                values[enumKey.ordinal()] = null;
                return oldValue;
            }
            return null;
        }

        @Override
        public Set<IEntry<K,V>> entrySet() {
            Set<IEntry<K,V>> entrySet = new HashSet<>();
            for(int i = 0; i < keyUniverse.length; i++) {
                if(values[i] != null)
                    entrySet.add(new KeyValueHolder(keyUniverse[i], values[i]));
            }

            return entrySet;
        }
    }
}
