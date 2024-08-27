package org.example.protocol;

import org.example.server.ClientHandler;

public class ChatCommand implements ServerCommand {
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

