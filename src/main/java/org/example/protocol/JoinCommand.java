package org.example.protocol;

import org.example.server.ClientHandler;
import org.example.server.GameState;
import org.example.server.UsernameManager;

public class JoinCommand implements ServerCommand {
    private static int defaultUsernameCounter = 1; // Counter for default usernames

    private final String username;
    private final GameState gameState;
    private final ClientHandler clientHandler;
    private final UsernameManager usernameManager;

    public JoinCommand(String username, GameState gameState, ClientHandler clientHandler, UsernameManager usernameManager) {
        // Validate and trim the username
        this.username = (username == null || username.trim().isEmpty()) ? generateDefaultUsername() : username.trim();
        this.gameState = gameState;
        this.clientHandler = clientHandler;
        this.usernameManager = usernameManager;
    }

    @Override
    public void execute() {
        String assignedUsername = usernameManager.getAvailableUsername(username);
        if (!assignedUsername.equals(username)) {
            clientHandler.sendMessage("USERNAME_TAKEN " + assignedUsername);
        } else {
            gameState.addPlayer(assignedUsername);
            usernameManager.reserveUsername(assignedUsername); // Reserve the username after confirming
            clientHandler.setUsername(assignedUsername);
            for (ClientHandler handler : clientHandler.getConnectedClients()) {
                handler.sendMessage("USERNAME_UPDATED " + this.username);
                handler.sendMessage("CHAT User " + assignedUsername + " has joined.");
                handler.sendMessage("UPDATE_PLAYERS " + String.join(",", gameState.getPlayerList()));
            }
        }
    }

    // Generates a simple default username with an incrementing counter
    private String generateDefaultUsername() {
        return "Guest" + defaultUsernameCounter++;
    }

}
