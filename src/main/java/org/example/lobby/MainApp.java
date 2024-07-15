package org.example.lobby;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.client.Client;
import org.example.server.Server;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Your JavaFX application initialization code
        Model model = new Model();
        Server.start(model);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lobby.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        Client client = new Client("defaultUsername");
        controller.setClient(client);

        primaryStage.setTitle("Chat Application");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setOnCloseRequest(event -> {
            client.sendMessage("/quit"); // Inform server when client closes
            client.shutdown();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch your JavaFX application from here
        launch(args);
    }
}