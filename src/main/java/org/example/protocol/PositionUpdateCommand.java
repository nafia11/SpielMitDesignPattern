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
        System.out.println("Received message: " + message);
        String[] parts = message.split(",");

        if (parts.length >= 6) { // Adjust to 6 parts to include interaction count
            String username = parts[0].trim();
            try {
                double x = Double.parseDouble(parts[1].trim());
                double y = Double.parseDouble(parts[2].trim());
                String direction = parts[3].trim();
                int spriteNum = Integer.parseInt(parts[4].trim());
                int interactionCount = Integer.parseInt(parts[5].trim()); // Parse interaction count
                System.out.println("Parsed: " + username + " " + x + " " + y + " " + direction + " " + spriteNum + " " + interactionCount);

                // Update the position and interaction count in the game state
                gameState.updatePlayerPosition(username, x, y, direction, spriteNum, interactionCount);

                // Notify the GameServer to broadcast the updated position and interaction count
                clientHandler.notifyPositionUpdate(username, x, y, direction, spriteNum, interactionCount);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing position update: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid POSITION_UPDATE message format: " + message);
        }
    }

}