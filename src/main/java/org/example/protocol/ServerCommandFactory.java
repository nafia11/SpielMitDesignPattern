package org.example.protocol;

import org.example.game.GameState;
import org.example.server.ClientHandler;
import org.example.server.UsernameManager;

import java.util.List;

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
        } else if (message.startsWith("START_GAME")) {
                return new StartGameCommand(gameState, clientHandler);
        } else if (message.startsWith("READY")) {
            return new ReadyCommand(gameState, clientHandler);
        } else {

            return new UnknownCommand();
        }
    }
}

class ReadyCommand implements ServerCommand {
    private final GameState gameState;
    private final ClientHandler clientHandler;

    public ReadyCommand(GameState gameState, ClientHandler clientHandler) {
        this.gameState = gameState;
        this.clientHandler = clientHandler;
    }

    @Override
    public void execute() {
        String username = clientHandler.getUsername();
        gameState.setPlayerReady(username, true); // Mark player as ready

        // Notify all clients about the updated readiness status
        /*for (ClientHandler handler : clientHandler.getConnectedClients()) {
            handler.sendMessage("READY " + username);
            handler.sendMessage("READY_STATUS " + getReadyStatusMessage());
        }*/
    }

    private String getReadyStatusMessage() {
        return "Ready: " + String.join(", ", gameState.getReadyPlayers()) +
                " |Not Ready: " +
                String.join(", ", gameState.getNotReadyPlayers());
    }
}

class StartGameCommand implements ServerCommand {
    private final GameState gameState;
    private final ClientHandler clientHandler;

    public StartGameCommand(GameState gameState, ClientHandler clientHandler) {
        this.gameState = gameState;
        this.clientHandler = clientHandler;
    }

    @Override
    public void execute() {
        List<String> allPlayers = gameState.getPlayerList();
        List<String> readyPlayers = gameState.getReadyPlayers();

        if (allPlayers.size() == readyPlayers.size()) {
            for (ClientHandler handler : clientHandler.getConnectedClients()) {

                handler.sendMessage("START_GAME");
                System.out.println("I am in command factory normal start");
            }
        } else {

            for (ClientHandler handler : clientHandler.getConnectedClients()) {
                handler.sendMessage("FORCE_START_GAME");
                System.out.println("I am in command factory force start");
            }

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
            usernameManager.reserveUsername(assignedUsername); // Reserve the username after confirming
            clientHandler.setUsername(assignedUsername);
            for (ClientHandler handler : clientHandler.getConnectedClients()) {
                handler.sendMessage("CHAT User " + assignedUsername + " has joined.");
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
            usernameManager.releaseUsername(currentUsername); // Release the old username
            usernameManager.reserveUsername(newUsername); // Reserve the new username
            gameState.removePlayer(currentUsername);
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
