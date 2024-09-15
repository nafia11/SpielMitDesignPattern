package org.example.lobby;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.client.GameClient;

import java.util.Optional;

public class LobbyController {

    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatInput;
    @FXML
    private TextField usernameDisplay;
    @FXML
    private Pane playerFiguresPane;

    private GameClient gameClient;


    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
        this.usernameDisplay.setText(gameClient.getUsername());
    }

    public void updatePlayerList(String[] players) {
        Platform.runLater(() -> updatePlayerFigures(players));
    }

    private void updatePlayerFigures(String[] players) {
        playerFiguresPane.getChildren().clear();
        for (int i = 0; i < players.length; i++) {
            String player = players[i];

            // Create an image view for the player figure
            ImageView playerFigure = new ImageView(new Image(getClass().getResourceAsStream("/images/player_icon.png")));
            playerFigure.setFitWidth(100);
            playerFigure.setFitHeight(100);
            playerFigure.setLayoutX(135 + (i * 100)); // Adjust layout as needed
            playerFigure.setLayoutY(15);

            // Create a text label for the player name
            Text playerName = new Text(player);
            playerName.setX(165 + (i * 100)); // Adjust layout as needed
            playerName.setY(105); // Position below the figure
            playerName.setFill(Color.WHITE); // Set the text fill color
            playerName.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // Set font size and style

            // Set the stroke (border) for the text
            playerName.setStroke(Color.BLACK); // Set border color to black
            playerName.setStrokeWidth(1); // Set the thickness of the border

            playerFiguresPane.getChildren().addAll(playerFigure, playerName);
        }
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
    private void startGame() {
        gameClient.sendMessage("START_GAME");
        System.out.println("I am in lobby controller startgame");
    }


    @FXML
    private void changeUsername() {
        TextInputDialog dialog = new TextInputDialog(gameClient.getUsername());
        dialog.setTitle("Change Username");
        dialog.setHeaderText("Enter a new username:");
        dialog.setContentText("New Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newUsername -> {
            String trimmedNewUsername = newUsername.trim();

            if (trimmedNewUsername.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Username");
                alert.setHeaderText("Username cannot be empty or just spaces.");
                alert.setContentText("Please enter a valid username.");
                alert.showAndWait();
            } else {
                gameClient.sendMessage("UPDATE_USERNAME " + trimmedNewUsername);
                updateUsernameDisplay(trimmedNewUsername);
            }
        });
    }



    @FXML
    private void disconnect() {
        if (gameClient != null) {
            Stage stage = (Stage) chatArea.getScene().getWindow();
            stage.close();

            new Thread(() -> {
                gameClient.disconnect();
            }).start();
        }
    }

    public void addMessageToChat(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    public void promptForNewUsername(String suggestedUsername) {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog(suggestedUsername);
            dialog.setTitle("Username Taken");
            dialog.setHeaderText("Username is already taken. Please choose another one or accept the suggested one.");
            dialog.setContentText("New Username:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newUsername -> {
                String trimmedNewUsername = newUsername.trim();
                gameClient.sendMessage("UPDATE_USERNAME " + trimmedNewUsername);
                updateUsernameDisplay(trimmedNewUsername);
            });
        });
    }


    private void updateUsernameDisplay(String newUsername) {
        Platform.runLater(() -> {
            // Check if newUsername is null or empty; if so, fallback to "Guest" without a leading space
            String displayName = (newUsername == null || newUsername.trim().isEmpty()) ? "Guest" : newUsername.trim();
            usernameDisplay.setText(displayName);
        });
    }



    public void ready() {
        String username = gameClient.getUsername();
        gameClient.sendMessage("READY " + username);
    }

    public void ForceStartRequest() {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Start Game");
            alert.setHeaderText("Not all players are ready");
            alert.setContentText("Do you want to start the game without waiting for all players to be ready?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                Platform.runLater(MainApp::showGameWindow);
            }

    }
}
