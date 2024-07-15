package org.example.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.lobby.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private static final List<ClientHandler> connectedClients = new ArrayList<>();
    private static final UsernameManager usernameManager = new UsernameManager();
    static Logger logger = LogManager.getLogger(Server.class);
    private static final int PORT = 111;
    private static Model model;

    public static void sendServerMessage(String message) {
        for (ClientHandler client : connectedClients) {
            client.sendMessage("SERVER: " + message);
        }
    }

    public static void start(Model model) {
        Server.model = model;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("Server is ready. Waiting for connection...");

            Thread serverMessageThread = new Thread(() -> {
                try (Scanner scanner = new Scanner(System.in)) {
                    while (true) {
                        String message = scanner.nextLine();
                        sendServerMessage(message);
                    }
                }
            });
            serverMessageThread.start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("New client connected.");

                ClientHandler clientHandler = new ClientHandler(clientSocket, connectedClients, usernameManager, model);
                connectedClients.add(clientHandler);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            logger.error("Server encountered an error: ", e);
        } finally {
            notifyClientsServerShutdown();
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    logger.error("Error closing server socket: ", e);
                }
            }
        }
    }

    private static void notifyClientsServerShutdown() {
        for (ClientHandler client : connectedClients) {
            client.sendMessage("SERVER: Server is shutting down.");
        }
        logger.info("All clients notified of server shutdown.");
    }

    public static void main(String[] args) {
        Model model = new Model();
        start(model);
    }
}
