package org.example.server;

import java.util.HashMap;

public class UsernameManager {
    private HashMap<String, Boolean> usernameAvailability;

    public UsernameManager() {
        usernameAvailability = new HashMap<>();
    }

    public boolean isUsernameAvailable(String username) {
        return !usernameAvailability.containsKey(username) || !usernameAvailability.get(username);
    }


    public String getAvailableUsername(String desiredUsername) {
        if (isUsernameAvailable(desiredUsername)) {
            usernameAvailability.put(desiredUsername, true);
            return desiredUsername;
        } else {
            if (desiredUsername.isEmpty()) {
                desiredUsername = "User";
            }
            int suffix = 1;
            while (true) {
                String altUsername = desiredUsername + suffix;
                if (isUsernameAvailable(altUsername)) {
                    usernameAvailability.put(altUsername, true);
                    return altUsername;
                }
                suffix++;
            }
        }
    }


    public void releaseUsername(String username) {
        usernameAvailability.put(username, false);
    }
}
