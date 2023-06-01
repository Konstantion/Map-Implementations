package org.example.dictionaries;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TreeDictionary<K extends Comparable<K>, V> extends AbstractDictionary<K,V>
        implements INavigableDictionary<K, V> {
    private Node<K, V> root;
    private final Comparator<? super K> comparator;
    private int size;

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private V prevPut = null;

    private static class Node<K, V> implements IEntry<K, V> {
        private K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;
        private boolean color;
        private int count;

        Node(K key, V value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.count = 1;
        }

        Node(K key, V value, int count, boolean color) {
            this.key = key;
            this.value = value;
            this.count = count;
            this.color = color;
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
        public String toString() {
            return key + "=" + value;
        }
    }

    public TreeDictionary() {
        this(null);
    }

    public TreeDictionary(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node<K, V> node, V value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Null keys are not supported.");
        }
        prevPut = null; // Clear previous value

        root = put(root, key, value);
        root.color = BLACK;
        size++;
        return prevPut;
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) {
            return new Node<>(key, value, RED);
        }

        int cmp = compare(key, node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            // Key already exists, update the value and return the previous value
            prevPut = node.value; //Set previous value
            node.value = value;
            return node;
        }

        //balancing magic
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        node.count = size(node.left) + size(node.right) + 1;
        return node;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = get(root, key);
        return node == null ? null : node.value;
    }

    private Node<K, V> get(Node<K, V> node, K key) {
        while (node != null) {
            int cmp = compare(key, node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node;
            }
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
        if (!containsKey(key))
            return null;

        // Copy the current root to ensure immutability during delete
        Node<K, V> deletedRoot = root;
        root = remove(root, key);
        if (root != null)
            root.color = BLACK;
        size--;
        return deletedRoot.value;
    }

    private Node<K, V> remove(Node<K, V> node, K key) {
        int cmp = compare(key, node.key);
        if (cmp < 0) {
            if (!isRed(node.left) && !isRed(node.left.left))
                node = moveRedLeft(node);
            node.left = remove(node.left, key);
        } else {
            if (isRed(node.left))
                node = rotateRight(node);
            if (compare(key, node.key) == 0 && (node.right == null))
                return null;
            if (!isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node);
            if (compare(key, node.key) == 0) {
                Node<K, V> min = minimum(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = remove(node.right, key);
            }
        }
        return balance(node);
    }

    private Node<K, V> minimum(Node<K, V> node) {
        if (node.left == null)
            return node;
        return minimum(node.left);
    }

    private Node<K, V> deleteMin(Node<K, V> node) {
        if (node.left == null)
            return null;

        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node<K, V> moveRedLeft(Node<K, V> node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node<K, V> moveRedRight(Node<K, V> node) {
        flipColors(node);
        if (!isRed(node.left.left))
            node = rotateRight(node);
        return node;
    }

    private Node<K, V> balance(Node<K, V> node) {
        if (isRed(node.right))
            node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))
            node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
            flipColors(node);

        node.count = size(node.left) + size(node.right) + 1;
        return node;
    }

    private boolean isRed(Node<K, V> node) {
        if (node == null) return false;
        return node.color == RED;
    }

    private Node<K, V> rotateLeft(Node<K, V> node) {
        Node<K, V> temp = node.right;
        node.right = temp.left;
        temp.left = node;
        temp.color = node.color;
        node.color = RED;
        temp.count = node.count;
        node.count = 1 + size(node.left) + size(node.right);
        return temp;
    }

    private Node<K, V> rotateRight(Node<K, V> node) {
        Node<K, V> temp = node.left;
        node.left = temp.right;
        temp.right = node;
        temp.color = node.color;
        node.color = RED;
        temp.count = node.count;
        node.count = 1 + size(node.left) + size(node.right);
        return temp;
    }

    private void flipColors(Node<K, V> node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    private int size(Node<K, V> node) {
        if (node == null) return 0;
        return node.count;
    }

    private int compare(K key1, K key2) {
        if (comparator != null) {
            return comparator.compare(key1, key2);
        } else {
            return key1.compareTo(key2);
        }
    }

    @Override
    public IEntry<K, V> lowerEntry(K key) {
        return lowerNode(root, key);
    }

    private Node<K, V> lowerNode(Node<K, V> node, K key) {
        if (node == null) return null;

        int cmp = compare(key, node.key);

        if (cmp <= 0) {
            return lowerNode(node.left, key);
        } else {
            Node<K, V> rightNode = lowerNode(node.right, key);
            if (rightNode != null) return rightNode;
            return node;
        }
    }

    @Override
    public K lowerKey(K key) {
        IEntry<K, V> entry = lowerEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public IEntry<K, V> floorEntry(K key) {
        return floorNode(root, key);
    }

    private Node<K, V> floorNode(Node<K, V> node, K key) {
        if (node == null) return null;

        int cmp = compare(key, node.key);

        if (cmp < 0) {
            return floorNode(node.left, key);
        } else if (cmp > 0) {
            Node<K, V> rightNode = floorNode(node.right, key);
            if (rightNode != null) return rightNode;
            return node;
        } else {
            return node;
        }
    }

    @Override
    public K floorKey(K key) {
        IEntry<K, V> entry = floorEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public IEntry<K, V> ceilingEntry(K key) {
        return ceilingNode(root, key);
    }

    private Node<K, V> ceilingNode(Node<K, V> node, K key) {
        if (node == null) return null;

        int cmp = compare(key, node.key);

        if (cmp < 0) {
            Node<K, V> leftNode = ceilingNode(node.left, key);
            if (leftNode != null) return leftNode;
            return node;
        } else if (cmp > 0) {
            return ceilingNode(node.right, key);
        } else {
            return node;
        }
    }

    @Override
    public K ceilingKey(K key) {
        IEntry<K, V> entry = ceilingEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public Set<IEntry<K, V>> entrySet() {
        Set<IEntry<K, V>> entrySet = new HashSet<>();
        buildEntrySet(root, entrySet);
        return entrySet;
    }

    private void buildEntrySet(Node<K, V> node, Set<IEntry<K, V>> entrySet) {
        if (node != null) {
            buildEntrySet(node.left, entrySet);
            entrySet.add(node);
            buildEntrySet(node.right, entrySet);
        }
    }

    @Override
    public IEntry<K, V> higherEntry(K key) {
        return higherNode(root, key);
    }

    private Node<K, V> higherNode(Node<K, V> node, K key) {
        if (node == null) return null;

        int cmp = compare(key, node.key);

        if (cmp < 0) {
            Node<K, V> leftNode = higherNode(node.left, key);
            if (leftNode != null) return leftNode;
            return node;
        } else {
            return higherNode(node.right, key);
        }
    }

    @Override
    public K higherKey(K key) {
        IEntry<K, V> entry = higherEntry(key);
        return entry != null ? entry.getKey() : null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendKeyValuePairs(root, sb); // Recursively add all nodes

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }

        sb.append("}");
        return sb.toString();
    }

    private void appendKeyValuePairs(Node<K, V> node, StringBuilder sb) {
        if (node != null) {
            appendKeyValuePairs(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            appendKeyValuePairs(node.right, sb);
        }
    }
}
