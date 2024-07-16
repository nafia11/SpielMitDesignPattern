package org.example.client;

import javafx.application.Application;
import javafx.application.Platform;
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

    public interface UsernameValidationListener {
        void onUsernameValidated(String validUsername);
        void onUsernameInvalid(String serverMessage);
    }

    private UsernameValidationListener usernameValidationListener;

    public Client(String username) {
        this.username = username;
        new Thread(this::connectToServer).start();
    }

    public void setUsernameValidationListener(UsernameValidationListener listener) {
        this.usernameValidationListener = listener;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void setUsername(String newUsername) {
        sendMessage("CHANGE_USERNAME " + newUsername);
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

    public void requestUsername() {
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();

            String serverResponse = reader.readLine();
            if (serverResponse.startsWith("The username")) {
                if (usernameValidationListener != null) {
                    usernameValidationListener.onUsernameInvalid(serverResponse);
                }
            } else {
                this.username = serverResponse; // Server approved username
                if (usernameValidationListener != null) {
                    usernameValidationListener.onUsernameValidated(this.username);
                }
                if (controller != null) {
                    Platform.runLater(() -> controller.setUsername(this.username));
                }
            }
        } catch (IOException e) {
            logger.error("Failed to request username: ", e);
            shutdown();
        }
    }

    public void sendMessage(String message) {
        new Thread(() -> {
            try {
                writer.write(message);
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
                if (message.startsWith("CHANGE_USERNAME")) {
                    handleUsernameChange(message.substring(15));
                } else {
                    String finalMessage = message;
                    Platform.runLater(() -> {
                        if (controller != null) {
                            controller.receiveMessage(finalMessage);
                        }
                    });
                }
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

    private void handleUsernameChange(String newUsername) {
        this.username = newUsername;
        if (controller != null) {
            Platform.runLater(() -> controller.setUsername(this.username));
        }
    }

    public void shutdown() {
        try {
            running = false;
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            logger.error("Error closing resources: ", e);
        }
    }

    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
