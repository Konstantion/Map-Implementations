package org.example;

import org.example.dictionaries.*;
import org.example.models.enums.Role;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.lang.Math.*;
import static org.example.models.utils.IntegerUtils.millions;
import static org.example.models.utils.IntegerUtils.thousands;
import static org.example.models.utils.StringUtils.randomString;

class TimeComplexityTest {
    Random random = new Random();
    int maxPow = 16;
    int minPow = 4;

    // On my PC
    // HashDictionary: 4567
    //
    // HashMap: 4423
    @Test
    void hash_dictionary_vs_hash_map_with_strings() {
        long start, end;
        String[] myKeys = new String[]{
                "Lorem ",
                "Adipiscing enim eu turpis egestas pretium aenean pharetra. Sed viverra tellus in hac. Vitae auctor eu augue ut lectus arcu bibendum at varius. Eu augue ut lectus arcu bibendum at varius vel pharetra. Duis convallis convallis tellus id interdum velit laoreet id donec. Auctor urna nunc id cursus metus aliquam eleifend mi. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Quis varius quam quisque id diam vel quam elementum pulvinar. Penatibus et magnis dis parturient montes nascetur. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices eros. Adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna. Venenatis urna cursus eget nunc scelerisque. Scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. Amet justo donec enim diam vulputate ut pharetra sit. ",
                " dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non",
                "proident",
                ","
        };
        IDictionary<String, Integer> hashDictionary = new HashDictionary<>();
        Map<String, Integer> hashMap = new HashMap<>();
        start = System.currentTimeMillis();
        for (String key : myKeys)
            hashDictionary.put(key, key.hashCode());
        for (int i = 0; i < millions(1); i++) {
            hashDictionary.put(
                    randomString(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }

        for (int i = 0; i < millions(4); i++) {
            hashDictionary.get(myKeys[0]);
            hashDictionary.remove(myKeys[2]);
            hashDictionary.get(myKeys[2]);
            hashDictionary.put(myKeys[2], 2 << 2);
            hashDictionary.get(myKeys[3]);
            hashDictionary.computeIfPresent(myKeys[3], (k, v) -> v);
            hashDictionary.containsKey(myKeys[4]);
        }

        end = System.currentTimeMillis();
        System.out.printf("HashDictionary: %s%n%n", end - start);

        start = System.currentTimeMillis();
        for (String key : myKeys)
            hashMap.put(key, key.hashCode());
        for (int i = 0; i < millions(1); i++) {
            hashMap.put(
                    randomString(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }
        for (int i = 0; i < millions(4); i++) {
            hashMap.get(myKeys[0]);
            hashMap.remove(myKeys[2]);
            hashMap.get(myKeys[2]);
            hashMap.put(myKeys[2], 2 << 2);
            hashMap.get(myKeys[3]);
            hashMap.computeIfPresent(myKeys[3], (k, v) -> v);
            hashMap.containsKey(myKeys[4]);
        }

        end = System.currentTimeMillis();
        System.out.printf("HashMap: %s%n%n", end - start);
    }


    // On my PC
    // IdentityHashDictionary: 953
    //
    // HashMap: 17
    @Test
    void identity_hash_dictionary_vs_hash_map_with_strings() {
        long start, end;
        String[] myKeys = new String[]{
                "Lorem ",
                "Adipiscing enim eu turpis egestas pretium aenean pharetra. Sed viverra tellus in hac. Vitae auctor eu augue ut lectus arcu bibendum at varius. Eu augue ut lectus arcu bibendum at varius vel pharetra. Duis convallis convallis tellus id interdum velit laoreet id donec. Auctor urna nunc id cursus metus aliquam eleifend mi. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Quis varius quam quisque id diam vel quam elementum pulvinar. Penatibus et magnis dis parturient montes nascetur. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices eros. Adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna. Venenatis urna cursus eget nunc scelerisque. Scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. Amet justo donec enim diam vulputate ut pharetra sit. ",
                " dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non",
                "proident",
                ","
        };
        IDictionary<String, Integer> hashDictionary = new IdentityHashDictionary<>();
        Map<String, Integer> hashMap = new HashMap<>();
        start = System.currentTimeMillis();
        for (String key : myKeys)
            hashDictionary.put(key, key.hashCode());
        for (int i = 0; i < thousands(10); i++) {
            hashDictionary.put(
                    randomString(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }

        for (int i = 0; i < thousands(0); i++) {
            hashDictionary.get(myKeys[0]);
            hashDictionary.remove(myKeys[2]);
            hashDictionary.get(myKeys[2]);
            hashDictionary.put(myKeys[2], 2 << 2);
            hashDictionary.get(myKeys[3]);
            hashDictionary.computeIfPresent(myKeys[3], (k, v) -> v);
            hashDictionary.containsKey(myKeys[4]);
        }

        end = System.currentTimeMillis();
        System.out.printf("IdentityHashDictionary: %s%n%n", end - start);

        start = System.currentTimeMillis();
        for (String key : myKeys)
            hashMap.put(key, key.hashCode());
        for (int i = 0; i < thousands(10); i++) {
            hashMap.put(
                    randomString(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }
        for (int i = 0; i < thousands(0); i++) {
            hashMap.get(myKeys[0]);
            hashMap.remove(myKeys[2]);
            hashMap.get(myKeys[2]);
            hashMap.put(myKeys[2], 2 << 2);
            hashMap.get(myKeys[3]);
            hashMap.computeIfPresent(myKeys[3], (k, v) -> v);
            hashMap.containsKey(myKeys[4]);
        }

        end = System.currentTimeMillis();
        System.out.printf("HashMap: %s%n%n", end - start);
    }


    // On my PC
    // EnumDictionary: 4211
    //
    // HashMap: 5474
    @Test
    void enum_dictionary_vs_hash_map_with_strings() {
        long start, end;
        String[] myKeys = new String[]{
                "Lorem ",
                "Adipiscing enim eu turpis egestas pretium aenean pharetra. Sed viverra tellus in hac. Vitae auctor eu augue ut lectus arcu bibendum at varius. Eu augue ut lectus arcu bibendum at varius vel pharetra. Duis convallis convallis tellus id interdum velit laoreet id donec. Auctor urna nunc id cursus metus aliquam eleifend mi. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Quis varius quam quisque id diam vel quam elementum pulvinar. Penatibus et magnis dis parturient montes nascetur. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices eros. Adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna. Venenatis urna cursus eget nunc scelerisque. Scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. Amet justo donec enim diam vulputate ut pharetra sit. ",
                " dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non",
                "proident",
                ","
        };
        final Role[] values = Role.values();

        IDictionary<Role, Integer> enumDictionary = new EnumDictionary<>(Role.class);
        Map<Role, Integer> hashMap = new HashMap<>();
        start = System.currentTimeMillis();
        for (Role key : values)
            enumDictionary.put(key, key.hashCode());
        for (int i = 0; i < millions(200); i++) {
            enumDictionary.put(
                    values[random.nextInt(0, values.length)],
                    i
            );
        }

        for (int i = 0; i < millions(1); i++) {
            enumDictionary.get(values[0]);
            enumDictionary.remove(values[2]);
            enumDictionary.get(values[2]);
            enumDictionary.put(values[2], 2 << 2);
            enumDictionary.get(values[3]);
            enumDictionary.computeIfPresent(values[3], (k, v) -> v);
            enumDictionary.containsKey(values[1]);
        }

        end = System.currentTimeMillis();
        System.out.printf("EnumDictionary: %s%n%n", end - start);

        start = System.currentTimeMillis();
        for (Role key : values)
            hashMap.put(key, key.hashCode());
        for (int i = 0; i < millions(200); i++) {
            hashMap.put(
                    values[random.nextInt(0, values.length)],
                    i
            );
        }
        for (int i = 0; i < millions(1); i++) {
            hashMap.get(values[0]);
            hashMap.remove(values[2]);
            hashMap.get(values[2]);
            hashMap.put(values[2], 2 << 2);
            hashMap.get(values[3]);
            hashMap.computeIfPresent(values[3], (k, v) -> v);
            hashMap.containsKey(values[1]);
        }

        end = System.currentTimeMillis();
        System.out.printf("HashMap: %s%n%n", end - start);
    }

    // On my PC
    // TreeDictionary: 6972
    //
    // TreeMap: 5447
    @Test
    void tree_dictionary_vs_tree_map_with_strings() {
        long start, end;
        String[] myKeys = new String[]{
                "Lorem ",
                "Adipiscing enim eu turpis egestas pretium aenean pharetra. Sed viverra tellus in hac. Vitae auctor eu augue ut lectus arcu bibendum at varius. Eu augue ut lectus arcu bibendum at varius vel pharetra. Duis convallis convallis tellus id interdum velit laoreet id donec. Auctor urna nunc id cursus metus aliquam eleifend mi. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Quis varius quam quisque id diam vel quam elementum pulvinar. Penatibus et magnis dis parturient montes nascetur. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices eros. Adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna. Venenatis urna cursus eget nunc scelerisque. Scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. Amet justo donec enim diam vulputate ut pharetra sit. ",
                " dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non",
                "proident",
                ","
        };
        IDictionary<String, Integer> treeDictionary = new TreeDictionary<>();
        Map<String, Integer> treeMap = new TreeMap<>();
        start = System.currentTimeMillis();
        for (String key : myKeys)
            treeDictionary.put(key, key.hashCode());
        for (int i = 0; i < millions(1); i++) {
            treeDictionary.put(
                    randomString(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }

        for (int i = 0; i < millions(1); i++) {
            treeDictionary.get(myKeys[0]);
            treeDictionary.remove(myKeys[2]);
            treeDictionary.get(myKeys[2]);
            treeDictionary.put(myKeys[2], 2 << 2);
            treeDictionary.get(myKeys[3]);
            treeDictionary.computeIfPresent(myKeys[3], (k, v) -> v);
            treeDictionary.containsKey(myKeys[4]);
        }

        end = System.currentTimeMillis();
        System.out.printf("TreeDictionary: %s%n%n", end - start);

        start = System.currentTimeMillis();
        for (String key : myKeys)
            treeMap.put(key, key.hashCode());
        for (int i = 0; i < millions(1); i++) {
            treeMap.put(
                    randomString(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }
        for (int i = 0; i < millions(1); i++) {
            treeMap.get(myKeys[0]);
            treeMap.remove(myKeys[2]);
            treeMap.get(myKeys[2]);
            treeMap.put(myKeys[2], 2 << 2);
            treeMap.get(myKeys[3]);
            treeMap.computeIfPresent(myKeys[3], (k, v) -> v);
            treeMap.containsKey(myKeys[4]);
        }

        end = System.currentTimeMillis();
        System.out.printf("TreeMap: %s%n%n", end - start);
    }

    @Test
    void navigable_dictionary_implementations_should_return_correct_result() {
        INavigableDictionary<Double, Integer> treeDictionary = new TreeDictionary<>();
        INavigableDictionary<Double, Integer> sortedArrayDictionary = new SortedArrayNavigableDictionary<>();
        NavigableMap<Double, Integer> treeMap = new TreeMap<>();

        treeDictionary.put(-1.4, -14);
        treeDictionary.put(1.4, 14);
        treeDictionary.put(1.6, 16);
        treeDictionary.put(2.0, 20);
        treeDictionary.put(2.4, 24);
        treeDictionary.put(2.7, 27);
        treeDictionary.put(2.8, 28);

        sortedArrayDictionary.put(-1.4, -14);
        sortedArrayDictionary.put(1.4, 14);
        sortedArrayDictionary.put(1.6, 16);
        sortedArrayDictionary.put(2.0, 20);
        sortedArrayDictionary.put(2.4, 24);
        sortedArrayDictionary.put(2.7, 27);
        sortedArrayDictionary.put(2.8, 28);

        treeMap.put(-1.4, -14);
        treeMap.put(1.4, 14);
        treeMap.put(1.6, 16);
        treeMap.put(2.0, 20);
        treeMap.put(2.4, 24);
        treeMap.put(2.7, 27);
        treeMap.put(2.8, 28);

        for (double i = 0.0; i < 2.8; i += 0.1) {
            assert treeMap.ceilingKey(i).equals(treeDictionary.ceilingKey(i))
                   && treeMap.ceilingKey(i).equals(sortedArrayDictionary.ceilingKey(i));

            assert treeMap.higherKey(i).equals(treeDictionary.higherKey(i))
                   && treeMap.higherKey(i).equals(sortedArrayDictionary.higherKey(i));

            assert treeMap.lowerKey(i).equals(treeDictionary.lowerKey(i))
                   && treeMap.lowerKey(i).equals(sortedArrayDictionary.lowerKey(i));

            assert treeMap.floorKey(i).equals(treeDictionary.floorKey(i))
                   && treeMap.floorKey(i).equals(sortedArrayDictionary.floorKey(i));
        }
    }
}
