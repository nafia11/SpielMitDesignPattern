package org.example.server;

import java.util.HashMap;

public class UsernameManager {
    private final HashMap<String, Boolean> usernameAvailability = new HashMap<>();

    // Check if the username is available without converting it to lowercase
    public boolean isUsernameAvailable(String username) {
        return !usernameAvailability.getOrDefault(username.trim(), false);
    }

    // Get an available username while keeping the case sensitivity intact
    public String getAvailableUsername(String desiredUsername) {
        String username = desiredUsername.trim();
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

    // Reserve the username without converting it to lowercase
    public void reserveUsername(String username) {
        usernameAvailability.put(username.trim(), true);
    }

    // Release the username without converting it to lowercase
    public void releaseUsername(String username) {
        usernameAvailability.put(username.trim(), false);
    }
}
