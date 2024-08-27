package org.example.server;

import java.util.HashMap;

public class UsernameManager {
    private final HashMap<String, Boolean> usernameAvailability = new HashMap<>();

    public boolean isUsernameAvailable(String username) {
        return !usernameAvailability.getOrDefault(username.trim().toLowerCase(), false);
    }

    public String getAvailableUsername(String desiredUsername) {
        String username = desiredUsername.trim().toLowerCase();
        if (isUsernameAvailable(username)) {
            return username;
        } else {
            int suffix = 1;
            while (true) {
                String altUsername = username + suffix;
                if (isUsernameAvailable(altUsername)) {
                    return altUsername;
                }
                suffix++;
            }
        }
    }

    public void reserveUsername(String username) {
        usernameAvailability.put(username.trim().toLowerCase(), true);
    }

    public void releaseUsername(String username) {
        usernameAvailability.put(username.trim().toLowerCase(), false);
    }
}
