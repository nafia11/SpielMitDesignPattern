package org.example.protocol;

import org.example.game.GameState;
import org.example.server.ClientHandler;

public class ServerCommandFactory {
    public static ServerCommand createCommand(String message, GameState gameState, ClientHandler clientHandler) {
        if (message.startsWith("CHAT")) {
            return new ChatCommand(message.substring(5), clientHandler);
        } else if (message.startsWith("JOIN")) {
            return new JoinCommand(message.substring(5), gameState, clientHandler);
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
        System.out.println("Server got a Chat Command from " +clientHandler.getUsername() + ": " + message);
        for (ClientHandler handler : clientHandler.getConnectedClients()) {
            handler.sendMessage("CHAT " + clientHandler.getUsername() + ": " + message);
        }
    }
}

class JoinCommand implements ServerCommand {
    private final String username;
    private final GameState gameState;
    private final ClientHandler clientHandler;

    public JoinCommand(String username, GameState gameState, ClientHandler clientHandler) {
        this.username = username;
        this.gameState = gameState;
        this.clientHandler = clientHandler;
    }

    @Override
    public void execute() {
        gameState.addPlayer(username);
        clientHandler.setUsername(username);
        System.out.println("CHAT "+" Server got a Join Command from " + username );
        clientHandler.sendMessage("CHAT "+ "Welcome, " + username + "!"); // Welcome message to the new user
        for (ClientHandler handler : clientHandler.getConnectedClients()) {
            handler.sendMessage("CHAT "+ "User " + username + " has joined."); // Notification to all users
        }
    }
}

class UnknownCommand implements ServerCommand {
    @Override
    public void execute() {
        System.out.println("Server got an unknown command");
        // Handle unknown commands
    }
}
