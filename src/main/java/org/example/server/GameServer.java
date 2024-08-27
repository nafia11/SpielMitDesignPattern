package org.example.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private static final int PORT = 111;
    private static GameServer instance;
    private static final Logger logger = LogManager.getLogger(GameServer.class);
    private final List<ClientHandler> connectedClients = new ArrayList<>();
    private final GameState gameState = new GameState();

    private GameServer() {}

    public static synchronized GameServer getInstance() {
        if (instance == null) {
            instance = new GameServer();
        }
        return instance;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server is ready. Waiting for connection...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("New client connected.");
                ClientHandler clientHandler = new ClientHandler(clientSocket, connectedClients, gameState);
                connectedClients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            logger.error("Server encountered an error: ", e);
        }
    }

    public void stop() {
        for (ClientHandler client : connectedClients) {
            client.cleanup();
        }
        connectedClients.clear();
        logger.info("Server stopped.");
    }

    public static void main(String[] args) {
        GameServer.getInstance().start();
    }
}
