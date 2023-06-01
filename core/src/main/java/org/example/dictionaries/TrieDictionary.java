package org.example.dictionaries;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TrieDictionary<K, V> extends AbstractDictionary<K, V>
        implements IDictionary<K, V> {
    private final TrieNode root;

    public TrieDictionary() {
        this.root = new TrieNode();
    }

    @Override
    public V put(K key, V value) {
        validateKey(key);
        return root.put(key.toString(), value);
    }

    @Override
    public V get(K key) {
        validateKey(key);
        return root.get(key.toString());
    }

    @Override
    public V remove(K key) {
        validateKey(key);
        return root.remove(key.toString());
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof TrieDictionary)) {
            return false;
        }

        TrieDictionary<?, ?> other = (TrieDictionary<?, ?>) obj;
        return entrySet().equals(other.entrySet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entrySet());
    }

    @Override
    public Set<IEntry<K, V>> entrySet() {
        Set<IEntry<K, V>> entrySet = new HashSet<>();
        root.collectEntries("", entrySet);
        return entrySet;
    }

    private void validateKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
    }

    private class TrieNode implements IEntry<String, V> {
        private final IDictionary<Character, TrieNode> children;
        private V value;

        public TrieNode() {
            this.children = new HashDictionary<>();
            this.value = null;
        }

        public V put(String key, V value) {
            if (key.isEmpty()) {
                V oldValue = this.value;
                this.value = value;
                return oldValue;
            }

            char firstChar = key.charAt(0);
            TrieNode child = children.get(firstChar);
            if (child == null) {
                child = new TrieNode();
                children.put(firstChar, child);
            }

            return child.put(key.substring(1), value);
        }

        public V get(String key) {
            if (key.isEmpty()) {
                return this.value;
            }

            char firstChar = key.charAt(0);
            TrieNode child = children.get(firstChar);
            if (child == null) {
                return null;
            }

            return child.get(key.substring(1));
        }

        public V remove(String key) {
            if (key.isEmpty()) {
                V oldValue = this.value;
                this.value = null;
                return oldValue;
            }

            char firstChar = key.charAt(0);
            TrieNode child = children.get(firstChar);
            if (child == null) {
                return null;
            }

            V removedValue = child.remove(key.substring(1));
            if (child.isEmpty()) {
                children.remove(firstChar);
            }

            return removedValue;
        }

        public boolean isEmpty() {
            return children.isEmpty() && value == null;
        }

        public void collectEntries(String prefix, Set<IEntry<K, V>> entrySet) {
            if (value != null) {
                entrySet.add(IDictionary.entry((K) prefix, value));
            }

            for (IEntry<Character, TrieNode> entry : children.entrySet()) {
                entry.getValue().collectEntries(prefix + entry.getKey(), entrySet);
            }
        }


        @Override
        public String getKey() {
            return root.toString();
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
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }
}
