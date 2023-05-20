package org.example.dictionaries;

public interface NavigableDictionary<K, V> extends Dictionary<K, V> {
    Dictionary.Entry<K, V> lowerEntry(K key);

    K lowerKey(K key);

    Dictionary.Entry<K, V> floorEntry(K key);

    K floorKey(K key);

    Dictionary.Entry<K, V> ceilingEntry(K key);

    K ceilingKey(K key);

    Dictionary.Entry<K, V> higherEntry(K key);

    K higherKey(K key);
}
