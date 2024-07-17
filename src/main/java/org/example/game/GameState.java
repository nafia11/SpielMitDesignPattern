package org.example.game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameState {
    private final ObservableList<String> players = FXCollections.observableArrayList();

    public void addPlayer(String player) {
        players.add(player);
    }

    public void removePlayer(String player) {
        players.remove(player);
    }

    public ObservableList<String> getPlayers() {
        return players;
    }
}
