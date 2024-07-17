package org.example.protocol;

import org.example.game.GameState;
import org.example.server.ClientHandler;
import org.example.server.UsernameManager;

public class ServerCommandFactory {
    private static final UsernameManager usernameManager = new UsernameManager();

    public static ServerCommand createCommand(String message, GameState gameState, ClientHandler clientHandler) {
        if (message.startsWith("CHAT")) {
            return new ChatCommand(message.substring(5), clientHandler);
        } else if (message.startsWith("JOIN")) {
            return new JoinCommand(message.substring(5), gameState, clientHandler, usernameManager);
        } else if (message.startsWith("UPDATE_USERNAME")) {
            return new UpdateUsernameCommand(message.substring(15), gameState, clientHandler, usernameManager);
        } else if (message.startsWith("DISCONNECT")) {
            return new DisconnectCommand(clientHandler, usernameManager);
        } else {
            return new UnknownCommand();
        }
    }
}


class ChatCommand implements ServerCommand {
    private final String message;
    private final ClientHandler clientHandler;

    public ChatCommand(String message, ClientHandler clientHandler) {
        this.message = message;
        this.clientHandler = clientHandler;
    }

    @Override
    public void execute() {
        for (ClientHandler handler : clientHandler.getConnectedClients()) {
            handler.sendMessage("CHAT " + clientHandler.getUsername() + ": " + message);
        }
    }
}

class JoinCommand implements ServerCommand {
    private final String username;
    private final GameState gameState;
    private final ClientHandler clientHandler;
    private final UsernameManager usernameManager;

    public JoinCommand(String username, GameState gameState, ClientHandler clientHandler, UsernameManager usernameManager) {
        this.username = username.trim();
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
            clientHandler.setUsername(assignedUsername);
            for (ClientHandler handler : clientHandler.getConnectedClients()) {
                handler.sendMessage("CHAT " + "User " + assignedUsername + " has joined.");
                handler.sendMessage("UPDATE_PLAYERS " + String.join(",", gameState.getPlayerList()));
            }
        }
    }
}

class UpdateUsernameCommand implements ServerCommand {
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
        String currentUsername = clientHandler.getUsername().trim(); // Trim any extra spaces
        String assignedUsername = usernameManager.getAvailableUsername(newUsername);
        if (!assignedUsername.equals(newUsername)) {
            clientHandler.sendMessage("USERNAME_TAKEN " + assignedUsername);
        } else {
            gameState.removePlayer(currentUsername);
            usernameManager.releaseUsername(currentUsername);
            gameState.addPlayer(newUsername);
            clientHandler.setUsername(newUsername);
            for (ClientHandler handler : clientHandler.getConnectedClients()) {
                handler.sendMessage("CHAT User " + currentUsername + " has changed their username to " + newUsername); // Ensure message has no extra spaces
                handler.sendMessage("UPDATE_PLAYERS " + String.join(",", gameState.getPlayerList()));
            }
        }
    }
}

class DisconnectCommand implements ServerCommand {
    private final ClientHandler clientHandler;
    private final UsernameManager usernameManager;

    public DisconnectCommand(ClientHandler clientHandler, UsernameManager usernameManager) {
        this.clientHandler = clientHandler;
        this.usernameManager = usernameManager;
    }

    @Override
    public void execute() {
        String username = clientHandler.getUsername();

        for (ClientHandler handler : clientHandler.getConnectedClients()) {
            handler.sendMessage("CHAT User " + username + " has disconnected.");
        }

        usernameManager.releaseUsername(username);
        clientHandler.cleanup();
    }
}
class UnknownCommand implements ServerCommand {
    @Override
    public void execute() {
        System.out.println("Server got an unknown command");
        // Handle unknown commands
    }
}
