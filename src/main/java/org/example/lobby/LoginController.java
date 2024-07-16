package org.example.lobby;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.example.client.Client;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    public void handleLogin() {
        String username = usernameField.getText().trim();
        if (!username.isEmpty()) {
            Client client = new Client(username);

            client.setUsernameValidationListener(new Client.UsernameValidationListener() {
                @Override
                public void onUsernameValidated(String validUsername) {
                    Platform.runLater(() -> loadMainScene(client, validUsername));
                }

                @Override
                public void onUsernameInvalid(String serverMessage) {
                    Platform.runLater(() -> promptForNewUsername(client, serverMessage));
                }
            });
        }
    }

    private void promptForNewUsername(Client client, String serverMessage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(serverMessage);
        dialog.setContentText("Please enter a new username:");
        dialog.showAndWait().ifPresent(newUsername -> {
            client.setUsername(newUsername.trim());
            client.requestUsername();
        });
    }

    private void loadMainScene(Client client, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setClient(client);
            mainController.setUsername(username);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}