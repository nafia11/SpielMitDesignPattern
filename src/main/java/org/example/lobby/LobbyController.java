package org.example.lobby;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.example.client.GameClient;

import java.util.Arrays;

public class LobbyController {
    @FXML
    private ListView<String> playerListView;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextArea chatInput;

    private GameClient gameClient;

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @FXML
    private void sendMessage() {
        String message = chatInput.getText();
        if (message != null && !message.trim().isEmpty()) {
            gameClient.sendMessage("CHAT " + message);
            chatInput.clear();
        }
    }

    public void addMessageToChat(String message) {
        Platform.runLater(() -> {
            chatArea.appendText(message + "\n");
            System.out.println("Added message to chat: " + message); // Add this line for debugging
        });
    }

    public void updatePlayerList(String[] players) {
        Platform.runLater(() -> {
            ObservableList<String> playerList = FXCollections.observableArrayList(players);
            playerListView.setItems(playerList);
            System.out.println("Chat Updated player list with: " + Arrays.toString(players)); // Add this line for debugging
        });
    }




}
