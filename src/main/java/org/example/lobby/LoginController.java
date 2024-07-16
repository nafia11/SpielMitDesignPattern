package org.example.lobby;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.client.Client;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    public void handleLogin() {
        String username = usernameField.getText().trim();
        if (!username.isEmpty()) {
            // Create a new client with the entered username
            Client client = new Client(username);

            // Load the main application window
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
}
