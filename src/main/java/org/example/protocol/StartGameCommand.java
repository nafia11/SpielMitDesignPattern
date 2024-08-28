package org.example.protocol;

import org.example.entity.Player;
import org.example.server.ClientHandler;
import org.example.server.GameState;

import java.util.List;
import java.util.Map;

public class StartGameCommand implements ServerCommand {
    private final GameState gameState;
    private final ClientHandler clientHandler;

    public StartGameCommand(GameState gameState, ClientHandler clientHandler) {
        this.gameState = gameState;
        this.clientHandler = clientHandler;
    }

    @Override
    public void execute() {
        List<String> readyPlayers = gameState.getReadyPlayers();
        Map<ClientHandler, Player> playerMap = gameState.getPlayerMap();
        int initialX = 100, initialY = 100;
        boolean allReady = (playerMap.size() == readyPlayers.size());

        System.out.println("Ready player size: " + readyPlayers.size() + " and all players: " + playerMap.size());

        if (allReady) {
            System.out.println("All players are ready.");
            for (ClientHandler handler : playerMap.keySet()) {
                handler.sendMessage("START_GAME");
                if (readyPlayers.contains(handler.getUsername())) {
                    Player player = gameState.getPlayer(handler);
                    player.setPosition(initialX, initialY);
                    System.out.println("Setting player " + handler.getUsername() + "'s position: " + initialX + "," + initialY);
                    initialX += 50;
                }
            }
            System.out.println("Notifying clients about starting positions.");
            for (ClientHandler handler : playerMap.keySet()) {
            handler.sendPlayerInitialPositions();
            }
        } else {
            for (ClientHandler handler : clientHandler.getConnectedClients()) {
                handler.sendMessage("FORCE_START_GAME");
                System.out.println("I am in command factory force start");
            }

        }
    }
}
