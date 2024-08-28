package org.example.protocol;

import org.example.server.ClientHandler;
import org.example.server.GameState;

public class PositionUpdateCommand implements ServerCommand {
    private final String message;
    private final GameState gameState;
    private final ClientHandler clientHandler;

    public PositionUpdateCommand(String message, GameState gameState, ClientHandler clientHandler) {
        this.message = message;
        this.gameState = gameState;
        this.clientHandler = clientHandler;
    }

    @Override
    public void execute() {
        System.out.println("request command");
        String[] parts = message.split(",");
        if (parts.length >= 3) {
            String username = parts[0].trim();
            try {
                double x = Double.parseDouble(parts[1].trim());
                double y = Double.parseDouble(parts[2].trim());
                // Validate and update the position in the game state
                gameState.updatePlayerPosition(username, x, y);
                // Notify the GameServer to broadcast the updated position
                clientHandler.notifyPositionUpdate(username, x, y);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing position update: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid POSITION_UPDATE message format: " + message);
        }
    }
}
