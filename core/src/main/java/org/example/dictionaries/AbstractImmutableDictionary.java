package org.example.dictionaries;

import org.example.dictionaries.entries.KeyValueHolder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

abstract class AbstractImmutableDictionary<K, V> extends AbstractDictionary<K, V> {
    static UnsupportedOperationException uoe() {
        return new UnsupportedOperationException();
    }

    static final int EXPAND_FACTOR = 2;
    @SuppressWarnings("unchcked")
    public static final AbstractImmutableDictionary<Object, Object> EMPTY_DICTIONARY = DictionaryN.empty();


    @Override
    public V put(K key, V value) {
        throw uoe();
    }

    @Override
    public V remove(K key) {
        throw uoe();
    }

    @Override
    public V replace(K key, V value) {
        throw uoe();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        throw uoe();
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        throw uoe();
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw uoe();
    }

    static final class DictionaryN<K, V> extends AbstractImmutableDictionary<K, V> {
        final Object[] table;

        final int size;

        private static DictionaryN<Object, Object> legendaryProgrammingAntiPattern = null;

        public static DictionaryN<Object, Object> empty() {
            if (legendaryProgrammingAntiPattern == null) {
                legendaryProgrammingAntiPattern = new DictionaryN<>();
            }
            return legendaryProgrammingAntiPattern;
        }

        /**
         * create immutable dictionary from key/value pairs from array going one by one
         * {@code new DictionaryN(k1, v1, k2, v2, k3, v3...kn,vn)};
         */
        DictionaryN(Object... input) {
            //in binary system all odd numbers contains the first bit
            //that in binary equals 1 (0001): 3 = 001(1), 9 = 100(1)
            //that's why bit & checks is value odd or not
            //9 & 1 => 1001     6 & 1 => 0110
            //        &0001             &0001
            //         0001 = 1          0000 = 0
            if ((input.length & 1) != 0) {
                throw new InternalError("length is odd");
            }

            size = input.length >> 1; //half of all key value pairs

            int len = EXPAND_FACTOR * input.length;
            // additional check, put 1 to lower bit on len
            // than do bitwise & operator with bitwise flipped 1
            // bitwise flipped 1 guarantees 0 in 1st bit 00..01 => 11..10
            // with bitwise & number is guarantied to be even
            len = (len + 1) & ~1;
            table = new Object[len];

            for (int i = 0; i < input.length; i += 2) {
                @SuppressWarnings("unchecked")
                K k = Objects.requireNonNull((K) input[i]);
                @SuppressWarnings("unchecked")
                V v = Objects.requireNonNull((V) input[i + 1]);
                int idx = probe(k);
                if (idx >= 0) {
                    throw new IllegalArgumentException("duplicate key: " + k);
                } else {
                    int dest = -(idx + 1);
                    table[dest] = k;
                    table[dest + 1] = v;
                }
            }
        }

        @Override
        public boolean containsKey(Object o) {
            Objects.requireNonNull(o);
            return size > 0 && probe(o) >= 0;
        }

        @Override
        public boolean containsValue(Object o) {
            Objects.requireNonNull(o);
            for (int i = 1; i < table.length; i += 2) {
                Object v = table[i];
                if (v != null && o.equals(v)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 0;
            for (int i = 0; i < table.length; i += 2) {
                Object k = table[i];
                if (k != null) {
                    hash += k.hashCode() ^ table[i + 1].hashCode();
                }
            }
            return hash;
        }

        @Override
        @SuppressWarnings("unchecked")
        public V get(Object o) {
            if (size == 0) {
                Objects.requireNonNull(o);
                return null;
            }
            int i = probe(o);
            if (i >= 0) {
                return (V) table[i + 1];
            } else {
                return null;
            }
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<IEntry<K, V>> entrySet() {
            Set<IEntry<K, V>> entrySet = new HashSet<>();
            if (table.length == 0)
                return entrySet;

            for (int i = 0; i < table.length; i += 2) {
                if (table[i] != null)
                    entrySet.add(new KeyValueHolder<>((K) table[i], (V) table[i + 1]));
            }
            return entrySet;
        }

        private int probe(Object pk) {
            // guarantee that index would be positive number
            int idx = Math.floorMod(pk.hashCode(), table.length >> 1) << 1;
            while (true) {
                @SuppressWarnings("unchecked")
                K ek = (K) table[idx];
                if (ek == null) {
                    return -idx - 1;
                } else if (pk.equals(ek)) {
                    return idx;
                } else if ((idx += 2) == table.length) {
                    idx = 0;
                }
            }
        }
    }
}
