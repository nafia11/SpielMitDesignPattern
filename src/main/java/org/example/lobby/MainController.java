package org.example.lobby;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.client.Client;

public class MainController {
    @FXML
    private ListView<String> playersListView;
    @FXML
    private TextField messageField;
    @FXML
    private VBox chatBox;
    @FXML
    private Label usernameLabel;

    private Model model;
    private Client client;

    public void initialize() {
        model = new Model();
        playersListView.setItems(model.getPlayers());
    }

    public void setClient(Client client) {
        this.client = client;
        this.client.setController(this);
    }

    public void setUsername(String username) {
        Platform.runLater(() -> usernameLabel.setText("Username: " + username));
    }

    @FXML
    private void showChat() {
        chatBox.setVisible(true);
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(message);
            messageField.clear();
        }
    }

    public void addPlayer(String player) {
        Platform.runLater(() -> model.addPlayer(player));
    }

    public void removePlayer(String player) {
        Platform.runLater(() -> model.removePlayer(player));
    }

    public void receiveMessage(String message) {
        Platform.runLater(() -> {
            // Handle message reception in GUI
        });
    }
}
