package org.example;


import org.example.dictionaries.*;

import java.security.SecureRandom;
import java.util.Random;

import static org.example.models.utils.StringUtils.randomString;

public class Main {
    private static final Random random = new SecureRandom();

    public static void main(String[] args) {
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

        // Abstract method example,
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
    }

    @SuppressWarnings("unchecked")
    public static <K extends Number, V extends CharSequence> IDictionary<K, V> fillDictionary(
            IDictionary<K, V> dictionary,
            int quantity
    ) {
        for (int i = 0; i < quantity; i++) {
            K key = (K) Double.valueOf(random.nextDouble());
            V value = (V) randomString(4);
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
}
