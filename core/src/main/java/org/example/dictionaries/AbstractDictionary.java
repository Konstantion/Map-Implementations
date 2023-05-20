package org.example.dictionaries;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractDictionary<K, V> implements Dictionary<K, V> {

    @Override
    public Collection<V> values() {
        return entrySet().stream()
                .map(Entry::getValue)
                .toList();
    }

    @Override
    public Collection<K> keys() {
        return entrySet().stream()
                .map(Entry::getKey)
                .toList();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(K key) {
        Iterator<Dictionary.Entry<K, V>> i = entrySet().iterator();
        if (key == null) {
            while (i.hasNext()) {
                Dictionary.Entry<K, V> e = i.next();
                if (e.getKey() == null)
                    return true;
            }
        } else {
            while (i.hasNext()) {
                Dictionary.Entry<K, V> e = i.next();
                if (key.equals(e.getKey()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(K key, V value) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, value) ||
            (curValue == null && !containsKey(key))) {
            return false;
        }
        remove(key);
        return true;
    }

    @Override
    public boolean containsValue(V value) {
        Iterator<Dictionary.Entry<K, V>> i = entrySet().iterator();
        if (value == null) {
            while (i.hasNext()) {
                Dictionary.Entry<K, V> e = i.next();
                if (e.getValue() == null)
                    return true;
            }
        } else {
            while (i.hasNext()) {
                Dictionary.Entry<K, V> e = i.next();
                if (value.equals(e.getValue()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(K key) {
        Iterator<Dictionary.Entry<K, V>> i = entrySet().iterator();
        if (key == null) {
            while (i.hasNext()) {
                Dictionary.Entry<K, V> e = i.next();
                if (e.getKey() == null)
                    return e.getValue();
            }
        } else {
            while (i.hasNext()) {
                Dictionary.Entry<K, V> e = i.next();
                if (key.equals(e.getKey()))
                    return e.getValue();
            }
        }
        return null;
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key))
                ? v
                : defaultValue;
    }

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public V remove(K key) {
        Iterator<Dictionary.Entry<K, V>> i = entrySet().iterator();
        Dictionary.Entry<K, V> correctEntry = null;
        if (key == null) {
            while (correctEntry == null && i.hasNext()) {
                Dictionary.Entry<K, V> e = i.next();
                if (e.getKey() == null)
                    correctEntry = e;
            }
        } else {
            while (correctEntry == null && i.hasNext()) {
                Dictionary.Entry<K, V> e = i.next();
                if (key.equals(e.getKey()))
                    correctEntry = e;
            }
        }

        V oldValue = null;
        if (correctEntry != null) {
            oldValue = correctEntry.getValue();
            i.remove();
        }
        return oldValue;
    }

    @Override
    public V replace(K key, V value) {
        V curValue;
        if (((curValue = get(key)) != null) || containsKey(key)) {
            curValue = put(key, value);
        }
        return curValue;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V v = get(key);
        if (v == null) {
            v = put(key, value);
        }

        return v;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v;
        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }

    @Override
    public V computeIfPresent(K key,
                              BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue;
        if ((oldValue = get(key)) != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                put(key, newValue);
                return newValue;
            } else {
                remove(key);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Entry<K, V> entry : entrySet()) {
            if (entry != null) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("}");
        return sb.toString();
    }
}
