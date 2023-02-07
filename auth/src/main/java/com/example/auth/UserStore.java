package com.example.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UserStore {

    private static final List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
    }

    public static Optional<User> getUserByEmailAndPassword(
            String email,
            String password
    ) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email)
                    && user.getPassword().equalsIgnoreCase(password))
                .findFirst();
    }

}
