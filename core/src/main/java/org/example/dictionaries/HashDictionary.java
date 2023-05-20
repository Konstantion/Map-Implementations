package org.example.dictionaries;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HashDictionary<K, V> extends AbstractDictionary<K,V>
        implements Dictionary<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] buckets;
    private int size;

    public HashDictionary() {
        this(DEFAULT_CAPACITY);
    }

    public HashDictionary(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive integer.");
        }
        this.buckets = new Node[capacity];
        this.size = 0;
    }

    private int getIndex(K key) {
        int hashCode = Objects.hashCode(key);
        return Math.abs(hashCode) % buckets.length;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        for (Node<K, V> node : buckets) {
            while (node != null) {
                if (Objects.equals(node.value, value)) {
                    return true;
                }
                node = node.next;
            }
        }
        return false;
    }

    @Override
    public V put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;

        if ((float) size / buckets.length > LOAD_FACTOR) {
            resizeBuckets();
        }
        return null;
    }

    @Override
    public V get(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
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
        return size;
    }

    @Override
    public V remove(K key) {
        int index = getIndex(key);
        Node<K, V> prevNode = null;
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                if (prevNode == null) {
                    buckets[index] = node.next;
                } else {
                    prevNode.next = node.next;
                }
                size--;
                return node.value;
            }
            prevNode = node;
            node = node.next;
        }
        return null;
    }

    private void resizeBuckets() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];
        for (Node<K, V> node : buckets) {
            while (node != null) {
                int newIndex = getIndex(node.key);
                Node<K, V> nextNode = node.next;
                node.next = newBuckets[newIndex];
                newBuckets[newIndex] = node;
                node = nextNode;
            }
        }
        buckets = newBuckets;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
        for (Node<K, V> node : buckets) {
            while (node != null) {
                entrySet.add(node);
                node = node.next;
            }
        }
        return entrySet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Entry<K, V> entry : buckets) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashDictionary<K, V> that = (HashDictionary<K, V>) o;
        if (size != that.size) return false;
        for (Entry<K, V> entry : buckets) {
            if (entry != null) {
                K key = entry.getKey();
                V value = entry.getValue();
                V thatValue = that.get(key);
                if (!Objects.equals(value, thatValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Entry<K, V> entry : buckets) {
            if (entry != null) {
                K key = entry.getKey();
                V value = entry.getValue();
                hashCode += Objects.hash(key, value);
            }
        }
        return hashCode;
    }

    private static class Node<K, V> implements Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
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
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) &&
                   Objects.equals(value, node.value);
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
