package org.example.lobby;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.client.GameClient;

import java.io.IOException;
import java.util.Optional;

public class MainApp extends Application {
    private static String username;

    @Override
    public void start(Stage primaryStage) throws IOException {
        showLoginDialog(primaryStage);
    }

    private void showLoginDialog(Stage primaryStage) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your username to join the game");

        ButtonType loginButtonType = new ButtonType("Login", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        GridPane grid = new GridPane();
        grid.add(usernameField, 0, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return usernameField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            MainApp.username = username;
            try {
                showLobby(primaryStage, username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void showLobby(Stage primaryStage, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lobby.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);

        LobbyController lobbyController = loader.getController();
        GameClient gameClient = new GameClient(username);
        gameClient.setLobbyController(lobbyController);
        lobbyController.setGameClient(gameClient);

        primaryStage.setTitle("Game Lobby");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
