package org.example.lobby;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.client.GameClient;

import java.util.Optional;

public class LobbyController {
    @FXML
    private ListView<String> playerListView;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextArea chatInput;
    @FXML
    private TextArea usernameDisplay;
    @FXML
    private Pane playerFiguresPane;

    private GameClient gameClient;

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
        this.usernameDisplay.setText(gameClient.getUsername());
    }

    @FXML
    private void sendMessage() {
        String message = chatInput.getText();
        if (message != null && !message.trim().isEmpty()) {
            gameClient.sendMessage("CHAT " + message);
            chatInput.clear();
        }
    }

    @FXML
    private void changeUsername() {
        TextInputDialog dialog = new TextInputDialog(gameClient.getUsername());
        dialog.setTitle("Change Username");
        dialog.setHeaderText("Enter a new username:");
        dialog.setContentText("New Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newUsername -> gameClient.sendMessage("UPDATE_USERNAME " + newUsername.trim())); // Trim any extra spaces
    }

    @FXML
    private void disconnect() {
        if (gameClient != null) {
            Stage stage = (Stage) chatArea.getScene().getWindow();
            stage.close(); // Close the GUI window

            new Thread(() -> {
                gameClient.disconnect();
            }).start();
        }
    }

    public void addMessageToChat(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    public void updatePlayerList(String[] players) {
        Platform.runLater(() -> {
            playerListView.setItems(FXCollections.observableArrayList(players));
            updatePlayerFigures(players);
        });
    }

    private void updatePlayerFigures(String[] players) {
        playerFiguresPane.getChildren().clear();
        for (int i = 0; i < players.length; i++) {
            String player = players[i];

            // Create an image view for the player figure
            ImageView playerFigure = new ImageView(new Image(getClass().getResourceAsStream("/images/player_icon.png")));
            playerFigure.setFitWidth(50);
            playerFigure.setFitHeight(50);
            playerFigure.setLayoutX(10 + (i * 60)); // Adjust layout as needed
            playerFigure.setLayoutY(10);

            // Create a text label for the player name
            Text playerName = new Text(player);
            playerName.setX(10 + (i * 60)); // Adjust layout as needed
            playerName.setY(70); // Position below the figure

            playerFiguresPane.getChildren().addAll(playerFigure, playerName);
        }
    }

    public void promptForNewUsername(String suggestedUsername) {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog(suggestedUsername);
            dialog.setTitle("Username Taken");
            dialog.setHeaderText("Username is already taken. Please choose another one or accept the suggested one.");
            dialog.setContentText("New Username:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newUsername -> gameClient.sendMessage("UPDATE_USERNAME " + newUsername));
        });
    }
}
