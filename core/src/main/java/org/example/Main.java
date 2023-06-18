package org.example;


import org.example.dictionaries.*;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.max;
import static org.example.models.utils.IntegerUtils.*;
import static org.example.models.utils.StringUtils.randomAlphabeticString;
import static org.example.models.utils.StringUtils.randomString;

public class Main {
    private static final Random random = new SecureRandom();

    public static void main(String[] args) {
        test();
        //Dictionary initialization
        HashDictionary<Number, String> hashDictionary = new HashDictionary<>();
        IdentityHashDictionary<Number, String> identityHashDictionary = new IdentityHashDictionary<>();
        MultiDictionary<Number, String> multiDictionary = new MultiDictionary<>();
        TrieDictionary<Number, String> trieDictionary = new TrieDictionary<>();
        TreeDictionary<Double, String> treeDictionary = new TreeDictionary<>();
        SortedArrayNavigableDictionary<Double, String> sortedArrayNavigableDictionary = new SortedArrayNavigableDictionary<>();

        //IDictionary polymorphism
        fillDictionary(hashDictionary, 10);
        fillDictionary(identityHashDictionary, 10);
        fillDictionary(multiDictionary, 10);
        fillDictionary(trieDictionary, 10);
        fillDictionary(treeDictionary, 10);
        fillDictionary(sortedArrayNavigableDictionary, 10);

        System.out.println("Hash Dictionary:" + hashDictionary);
        System.out.println("Identity Hash Dictionary:" + identityHashDictionary);
        System.out.println("Multi Dictionary:" + multiDictionary);
        System.out.println("Trie Dictionary:" + trieDictionary);
        System.out.println("Tree Dictionary:" + treeDictionary);
        System.out.println("Sorted Array Navigable Dictionary:" + sortedArrayNavigableDictionary);

        //INavigableDictionary polymorphism
        double treeLowerKey = lowerKey(treeDictionary, 0.5);
        double arrayLowerKey = lowerKey(sortedArrayNavigableDictionary, 0.5);

        double treeHigherKey = higherKey(treeDictionary, 0.5);
        double arrayHigherKey = higherKey(sortedArrayNavigableDictionary, 0.5);

        System.out.printf("Lower: tree = %s, array = %s%n", treeLowerKey, arrayLowerKey);
        System.out.printf("Higher: tree = %s, array = %s%n", treeHigherKey, arrayHigherKey);

        // Abstract methods example,
        // note that the methods are not implemented in the inheritor
        IDictionary<Integer, String> dictionary = new TreeDictionary<>();
        final int ten = 10;
        final int five = 5;
        //put 10="ten" | abstract method call
        dictionary.putIfAbsent(ten, String.valueOf(ten));
        //change 10="ten" to 10="11" | abstract method call
        dictionary.computeIfPresent(ten, (key, value) -> String.valueOf(++key));
        //put 5="5" | abstract method call
        dictionary.computeIfAbsent(five, Object::toString);
        //set 5="10" if value for 5 is absent | abstract method call | fail
        dictionary.computeIfAbsent(five, key -> String.valueOf(ten));
        //remove 5="10"
        dictionary.remove(five);
        //set 5="10" if value for 5 is absent | abstract method call | complete
        dictionary.computeIfAbsent(five, key -> String.valueOf(ten));

        dictionary.put(1, "one");
        //replace 1="one" to 1="1" | abstract method call
        dictionary.replace(1, "1");

        System.out.println(dictionary);

        //Immutable dictionary, not all methods are in example
        //but result would be similar
        @SuppressWarnings("unchecked")
        IDictionary<String, String> immutable = IDictionary.ofEntries(
                IDictionary.entry("hello", "hello"),
                IDictionary.entry(",", ","),
                IDictionary.entry("world", "world")
        );

        try {
            immutable.put("who", "who");
        } catch (UnsupportedOperationException e) {
            System.out.println("You can't put in immutable dictionary");
        }

        try {
            immutable.remove("who");
        } catch (UnsupportedOperationException e) {
            System.out.println("You can't remove in immutable dictionary");
        }

        try {
            immutable.computeIfAbsent("who", null);
        } catch (UnsupportedOperationException e) {
            System.out.println("You can't computeIfAbsent in immutable dictionary");
        }

        try {
            immutable.computeIfPresent("who", null);
        } catch (UnsupportedOperationException e) {
            System.out.println("You can't computeIfPresent in immutable dictionary");
        }

        try {
            immutable.remove("who", null);
        } catch (UnsupportedOperationException e) {
            System.out.println("You can't remove in immutable dictionary");
        }
    }

    @SuppressWarnings("unchecked")
    public static <K extends Number, V extends CharSequence> IDictionary<K, V> fillDictionary(
            IDictionary<K, V> dictionary,
            int quantity
    ) {
        for (int i = 0; i < quantity; i++) {
            K key = (K) Double.valueOf(random.nextDouble());
            V value = (V) randomAlphabeticString(4);
            dictionary.put(key, value);
        }

        return dictionary;
    }

    public static <K, V> K lowerKey(
            INavigableDictionary<K, V> dictionary,
            K key
    ) {
        return dictionary.lowerKey(key);
    }

    public static <K, V> K higherKey(
            INavigableDictionary<K, V> dictionary,
            K key
    ) {
        return dictionary.higherKey(key);
    }

    public static void test() {
        final int minPow = 4;
        final int maxPow = 16;
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
        for (int i = 0; i < thousands(10); i++) {
            hashDictionary.put(
                    randomString(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }

        for (int i = 0; i < millions(1); i++) {
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
        for (int i = 0; i < thousands(10); i++) {
            hashMap.put(
                    randomString(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }
        for (int i = 0; i < millions(1); i++) {
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
}
