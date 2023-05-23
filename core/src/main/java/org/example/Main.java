package org.example;


import org.example.dictionaries.Dictionary;
import org.example.dictionaries.TreeDictionary;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static java.lang.Math.*;
import static org.example.model.utils.IntegerUtils.millions;
import static org.example.model.utils.StringUtils.generateRandom;

public class Main {
    static final Random random = new Random();
    static final int maxPow = 16;
    static final int minPow = 4;

    public static void main(String[] args) {
        long start, end;
        String[] myKeys = new String[]{
                "Lorem ",
                "Adipiscing enim eu turpis egestas pretium aenean pharetra. Sed viverra tellus in hac. Vitae auctor eu augue ut lectus arcu bibendum at varius. Eu augue ut lectus arcu bibendum at varius vel pharetra. Duis convallis convallis tellus id interdum velit laoreet id donec. Auctor urna nunc id cursus metus aliquam eleifend mi. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Quis varius quam quisque id diam vel quam elementum pulvinar. Penatibus et magnis dis parturient montes nascetur. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices eros. Adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna. Venenatis urna cursus eget nunc scelerisque. Scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. Amet justo donec enim diam vulputate ut pharetra sit. ",
                " dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non",
                "proident",
                ","
        };
        Dictionary<String, Integer> treeDictionary = new TreeDictionary<>();
        Map<String, Integer> treeMap = new TreeMap<>();
        start = System.currentTimeMillis();
        for (String key : myKeys)
            treeDictionary.put(key, key.hashCode());
        for (int i = 0; i < millions(5); i++) {
            treeDictionary.put(
                    generateRandom(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }

        for (int i = 0; i < millions(0); i++) {
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
        for (int i = 0; i < millions(5); i++) {
            treeMap.put(
                    generateRandom(random.nextInt(1, (int) sqrt(min(max(i, 2 << minPow), 2 << maxPow)))),
                    i
            );
        }
        for (int i = 0; i < millions(0); i++) {
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
}
