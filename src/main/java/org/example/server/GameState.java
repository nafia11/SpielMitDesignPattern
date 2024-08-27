package org.example.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
    private final List<String> players = new ArrayList<>();
    private final Map<String, Boolean> playerReadyStatus = new HashMap<>();

    public void addPlayer(String username) {
        players.add(username);
        playerReadyStatus.put(username, false); // By default, players are not ready.
    }

    public void removePlayer(String username) {
        players.remove(username);
        playerReadyStatus.remove(username);
    }

    public void setPlayerReady(String username, boolean isReady) {
        if (playerReadyStatus.containsKey(username)) {
            playerReadyStatus.put(username, isReady);
        }
    }

    public List<String> getPlayerList() {
        return new ArrayList<>(players);
    }

    public List<String> getReadyPlayers() {
        List<String> readyPlayers = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : playerReadyStatus.entrySet()) {
            if (entry.getValue()) {
                readyPlayers.add(entry.getKey());
            }
        }
        return readyPlayers;
    }

    public List<String> getNotReadyPlayers() {
        List<String> notReadyPlayers = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : playerReadyStatus.entrySet()) {
            if (!entry.getValue()) {
                notReadyPlayers.add(entry.getKey());
            }
        }
        return notReadyPlayers;
    }
}
