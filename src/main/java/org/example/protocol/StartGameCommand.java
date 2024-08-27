package org.example.protocol;

import org.example.server.ClientHandler;
import org.example.server.GameState;

import java.util.List;

public class StartGameCommand implements ServerCommand {
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
