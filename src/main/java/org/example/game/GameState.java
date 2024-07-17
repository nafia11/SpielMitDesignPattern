package org.example.game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final List<String> players = new ArrayList<>();

    public void addPlayer(String username) {
        players.add(username);
    }

    public void removePlayer(String username) {
        players.remove(username);
    }

    public List<String> getPlayerList() {
        return new ArrayList<>(players);
    }
}
