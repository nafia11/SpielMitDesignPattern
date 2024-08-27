package org.example.protocol;

import org.example.server.ClientHandler;
import org.example.server.GameState;
import org.example.server.UsernameManager;

public class UpdateUsernameCommand implements ServerCommand {
    private final String newUsername;
    private final GameState gameState;
    private final ClientHandler clientHandler;
    private final UsernameManager usernameManager;

    public UpdateUsernameCommand(String newUsername, GameState gameState, ClientHandler clientHandler, UsernameManager usernameManager) {
        this.newUsername = newUsername.trim(); // Trim any extra spaces
        this.gameState = gameState;
        this.clientHandler = clientHandler;
        this.usernameManager = usernameManager;
    }

    @Override
    public void execute() {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            clientHandler.sendMessage("ERROR: Username cannot be empty or just spaces.");
            return;
        }

        String currentUsername = clientHandler.getUsername();
        if (currentUsername == null) {
            clientHandler.sendMessage("ERROR: Username not set.");
            return;
        }
        currentUsername = currentUsername.trim();

        String assignedUsername = usernameManager.getAvailableUsername(newUsername);
        if (!assignedUsername.equals(newUsername)) {
            clientHandler.sendMessage("USERNAME_TAKEN " + assignedUsername);
        } else {
            usernameManager.releaseUsername(currentUsername);
            usernameManager.reserveUsername(newUsername);
            gameState.removePlayer(currentUsername);
            gameState.addPlayer(newUsername);
            clientHandler.setUsername(newUsername);
            for (ClientHandler handler : clientHandler.getConnectedClients()) {
                handler.sendMessage("CHAT User " + currentUsername + " has changed their username to " + newUsername);
                handler.sendMessage("UPDATE_PLAYERS " + String.join(",", gameState.getPlayerList()));
            }
        }
    }
}
