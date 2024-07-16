package org.example.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.lobby.MainApp;
import org.example.lobby.MainController;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean running = true;
    private MainController controller;
    static Logger logger = LogManager.getLogger(Client.class);
    private String username;

    public Client(String username) {
        this.username = username;
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("127.0.0.1", 111);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            requestUsername();

            Thread receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();
        } catch (IOException e) {
            logger.error("Failed to initialize client: ", e);
            shutdown();
        }
    }

    private void requestUsername() throws IOException {
        writer.write(username);
        writer.newLine();
        writer.flush();

        String serverResponse = reader.readLine();
        if (serverResponse.startsWith("The username")) {
            Platform.runLater(() -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setHeaderText(serverResponse);
                dialog.setContentText("Please enter a new username:");
                dialog.showAndWait().ifPresent(newUsername -> {
                    try {
                        username = newUsername.trim();
                        requestUsername();
                    } catch (IOException e) {
                        logger.error("Failed to request new username: ", e);
                    }
                });
            });
        } else {
            this.username = serverResponse; // Server approved username
        }
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (SocketException se) {
            logger.error("Connection to the server lost: ", se);
            running = false;
            shutdown();
        } catch (IOException e) {
            logger.error("Failed to send message: ", e);
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                if (message.startsWith("New player: ")) {
                    String newPlayer = message.substring(12);
                    if (controller != null) {
                        controller.addPlayer(newPlayer);
                    }
                } else if (message.startsWith("Player left: ")) {
                    String playerLeft = message.substring(13);
                    if (controller != null) {
                        controller.removePlayer(playerLeft);
                    }
                } else {
                    // Handle regular messages
                    if (controller != null) {
                        controller.receiveMessage(message);
                    }
                }
            }
        } catch (IOException e) {
            logger.trace("Error receiving message: ", e);
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            logger.error("Error closing resources: ", e);
        }
        running = false;
    }

    public static void main(String[] args) {
        Application.launch(MainApp.class, args); // Use if extending Application
    }
}
