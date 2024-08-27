package org.example.protocol;

import org.example.server.ClientHandler;
import org.example.server.GameState;

public class ReadyCommand implements ServerCommand {
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
        clientHandler.sendMessage("READY " + username);
        System.out.println("factory ready");
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
