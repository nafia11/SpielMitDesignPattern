package org.example.lobby;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.client.Client;

import java.util.Arrays;

public class MainController {
    @FXML
    private ListView<String> playersListView;
    @FXML
    private TextField messageField;
    @FXML
    private VBox chatBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField newUsernameField;

    private Model model;
    private Client client;

    public void initialize() {
        model = new Model();
        model.getPlayers().clear();
        playersListView.setItems(model.getPlayers());
    }



    public void setClient(Client client) {
        this.client = client;
        this.client.setController(this);
        this.client.setUsernameValidationListener(new Client.UsernameValidationListener() {
            @Override
            public void onUsernameValidated(String validUsername) {
                Platform.runLater(() -> usernameLabel.setText("Username: " + validUsername));
            }

            @Override
            public void onUsernameInvalid(String serverMessage) {
                Platform.runLater(() -> {
                    // Show an alert or prompt for a new username
                    // For simplicity, set a label with server message
                    usernameLabel.setText(serverMessage);
                });
            }
        });
    }

    public void setUsername(String username) {
        Platform.runLater(() -> {
            usernameLabel.setText("Username: " + username);
        });
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

    @FXML
    private void showPlayers() {
        client.sendMessage("SHOW_PLAYERS"); // Command to server to send the list of players
    }

    public void addPlayer(String player) {
        Platform.runLater(() -> model.addPlayer(player));
    }

    public void removePlayer(String player) {
        Platform.runLater(() -> model.removePlayer(player));
    }

    public void receiveMessage(String message) {
        Platform.runLater(() -> {
            if (message.startsWith("New player: ")) {
                String newPlayer = message.substring(12);
                model.addPlayer(newPlayer);
            } else if (message.startsWith("Player left: ")) {
                String playerLeft = message.substring(13);
                model.removePlayer(playerLeft);
            } else if (message.startsWith("Player ") && message.contains("changed username to")) {
                String[] parts = message.split(" ");
                String oldUsername = parts[1];
                String newUsername = parts[5];

                model.removePlayer(oldUsername);
                model.addPlayer(newUsername);
            } else {
                chatArea.appendText(message + "\n");
            }
        });
    }

    @FXML
    private void changeUsername() {
        String newUsername = newUsernameField.getText().trim();
        if (!newUsername.isEmpty()) {
            client.setUsername(newUsername);
        }
    }
    public void updatePlayersList(String playersList) {
        String[] players = playersList.split(",");
        model.getPlayers().clear();
        model.getPlayers().addAll(Arrays.asList(players));
    }
}


