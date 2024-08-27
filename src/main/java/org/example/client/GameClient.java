package org.example.client;

import javafx.application.Application;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.lobby.LobbyController;
import org.example.lobby.MainApp;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class GameClient {
    private static final Logger logger = LogManager.getLogger(GameClient.class);
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private static String username;
    private LobbyController lobbyController;

    public GameClient(String username) {
        GameClient.username = username.trim();
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("127.0.0.1", 111);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Automatically send join request
            sendMessage("JOIN " + username);
            Thread receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();
        } catch (IOException e) {
            logger.error("Failed to initialize client: ", e);
            shutdown();
        }
    }

    public void sendMessage(String message) {
        new Thread(() -> {
            try {
                writer.write(message); // Assuming message is formatted correctly before calling sendMessage
                writer.newLine();
                writer.flush();
            } catch (SocketException se) {
                logger.error("Connection to the server lost: ", se);
                shutdown();
            } catch (IOException e) {
                logger.error("Failed to send message: ", e);
            }
        }).start();
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {

                String finalMessage = message;
                Platform.runLater(() -> handleServerMessage(finalMessage));
            }
        } catch (SocketException se) {
            logger.error("Connection to the server lost: ", se);
            shutdown();
        } catch (IOException e) {
            logger.error("Error receiving message: ", e);
            shutdown();
        } finally {
            shutdown();
        }
    }

    private void handleServerMessage(String message) {
        if (message.startsWith("CHAT")) {
            String chatMessage = message.substring(5);
            if (lobbyController != null) {
                lobbyController.addMessageToChat(chatMessage);
            } else {
                System.out.println(chatMessage);
            }
        } else if (message.startsWith("UPDATE_PLAYERS")) {
            String[] players = message.substring(15).split(",");
            if (lobbyController != null) {
                lobbyController.updatePlayerList(players);
            }
        } else if (message.startsWith("USERNAME_TAKEN")) {
            String suggestedUsername = message.substring(14);
            username= suggestedUsername;
            if (lobbyController != null) {
                lobbyController.promptForNewUsername(suggestedUsername);
            }
        } else if (message.startsWith("START_GAME")) {
            MainApp.showGameWindow();
        } else if (message.startsWith("FORCE_START_GAME")) {
            if (lobbyController != null) {
                lobbyController.ForceStartRequest();
            }
        } else if (message.startsWith("USERNAME_UPDATED")) {
            String updatedUsername = message.substring(17).trim();
            username = updatedUsername;
        }
        else if (message.startsWith("READY")) {
            String username = message.substring(6).trim();
            if (lobbyController != null) {
                lobbyController.addMessageToChat(username + " is ready");
            }
        } else if (message.startsWith("READY_STATUS")) {
            String statusMessage = message.substring(13).trim();
            if (lobbyController != null) {
                lobbyController.addMessageToChat(statusMessage);
            }
        }
    }

    public void disconnect() {
        sendMessage("DISCONNECT"); // Optionally inform the server that the client is disconnecting
        shutdown();
        Platform.exit(); // Close the application after disconnecting
    }

    public void shutdown() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            logger.error("Error closing resources: ", e);
        }
    }

    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public String getUsername() {
        return username;
    }

    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
        System.out.println(username);
    }
}
