package org.example.server;

import org.example.entity.Player;

import java.util.*;

public class GameState {
    private final Map<ClientHandler, Player> playerMap = new LinkedHashMap<>();
    private final Map<String, Boolean> playerReadyStatus = new HashMap<>();

    public void addPlayer(String username, ClientHandler clientHandler, Player player) {
        playerMap.put(clientHandler, player);
        playerReadyStatus.put(username, false); // Players are not ready by default
        System.out.println("Added player: " + username + " to map.");
        System.out.println("Current players in map: " + playerMap.keySet());
    }

    public Player getPlayer(ClientHandler clientHandler) {
        System.out.println("Current players in map: " + playerMap.keySet());
        return playerMap.get(clientHandler);
    }
    public Collection<Player> getAllPlayers() {
        System.out.println("Current players in map: " + playerMap.keySet());
        return playerMap.values();
    }
    public Map<ClientHandler, Player> getPlayerMap() {
        return playerMap;
    }

    public void removePlayer(String username) {
        playerReadyStatus.remove(username);
        playerMap.entrySet().removeIf(entry -> entry.getKey().getUsername().equals(username));
        System.out.println("Removed player: " + username + " from map.");
        System.out.println("Current players in map: " + playerMap.keySet());
    }

    // Sets a player's ready status
    public void setPlayerReady(String username, boolean isReady) {
        if (playerReadyStatus.containsKey(username)) {
            playerReadyStatus.put(username, isReady);
        }
    }

    // Retrieves a list of all player usernames
    public List<String> getPlayerList() {
        List<String> playerList = new ArrayList<>();
        for (ClientHandler handler : playerMap.keySet()) {
            playerList.add(handler.getUsername());
        }
        System.out.println("Current players in map: " + playerList);
        return playerList;
    }

    // Retrieves a list of usernames of ready players
    public List<String> getReadyPlayers() {
        List<String> readyPlayers = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : playerReadyStatus.entrySet()) {
            if (entry.getValue()) {
                readyPlayers.add(entry.getKey());
            }
        }
        return readyPlayers;
    }

    // Retrieves a list of usernames of players who are not ready
    public List<String> getNotReadyPlayers() {
        List<String> notReadyPlayers = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : playerReadyStatus.entrySet()) {
            if (!entry.getValue()) {
                notReadyPlayers.add(entry.getKey());
            }
        }
        return notReadyPlayers;
    }

    // Handles player username changes
    public void updatePlayerUsername(String oldUsername, String newUsername) {
        if (playerReadyStatus.containsKey(oldUsername)) {
            boolean isReady = playerReadyStatus.remove(oldUsername); // Remove old entry
            playerReadyStatus.put(newUsername, isReady); // Add new entry with same ready status
            System.out.println("Updated username from " + oldUsername + " to " + newUsername);
        }
    }
}

