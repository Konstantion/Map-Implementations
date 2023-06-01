package org.example.dictionaries;

public interface INavigableDictionary<K, V> extends IDictionary<K, V> {
    IEntry<K, V> lowerEntry(K key);

    K lowerKey(K key);

    IEntry<K, V> floorEntry(K key);

    K floorKey(K key);

    IEntry<K, V> ceilingEntry(K key);

    K ceilingKey(K key);

    IEntry<K, V> higherEntry(K key);

    K higherKey(K key);
}
