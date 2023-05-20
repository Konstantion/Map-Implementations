package org.example;


import org.example.dictionaries.Dictionary;
import org.example.dictionaries.EnumDictionary;
import org.example.enums.Role;

import static org.example.enums.Role.*;

public class Main {
    public static void main(String[] args) {
        Dictionary<Role, Integer> roles = new EnumDictionary<>(Role.class);

        roles.put(USER, 1);
        roles.put(MODERATOR, 3);
        roles.put(ADMIN, 2);
        roles.put(USER, 4);

        System.out.println(roles.remove(USER));
        System.out.println(roles.entrySet());
    }
}
