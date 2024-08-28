package org.example.protocol;

import org.example.server.ClientHandler;
import org.example.server.UsernameManager;

public class DisconnectCommand implements ServerCommand {
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
        //clientHandler.cleanup();
    }
}
