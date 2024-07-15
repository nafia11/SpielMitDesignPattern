package org.example.client;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.lobby.Controller;
import org.example.lobby.MainApp;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean running = true;
    private Controller controller;
    static Logger logger = LogManager.getLogger(Client.class);

    public Client(String username) {
        try {
            socket = new Socket("127.0.0.1", 111);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write(username);
            writer.newLine();
            writer.flush();

            Thread receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();
        } catch (IOException e) {
            logger.error("Failed to initialize client: ", e);
            shutdown();
        }
    }

    public void setController(Controller controller) {
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
