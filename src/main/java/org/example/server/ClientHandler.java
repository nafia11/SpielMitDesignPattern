package org.example.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.game.GameState;
import org.example.protocol.ServerCommandFactory;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final List<ClientHandler> connectedClients;
    private final GameState gameState;
    private String username;

    public ClientHandler(Socket socket, List<ClientHandler> connectedClients, GameState gameState) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.connectedClients = connectedClients;
        this.gameState = gameState;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                ServerCommandFactory.createCommand(message, gameState, this).execute();
            }
        } catch (IOException e) {
            logger.error("Error in client communication: ", e);
        } finally {
            cleanup();
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            logger.error("Error sending message: ", e);
        }
    }

    public List<ClientHandler> getConnectedClients() {
        return connectedClients;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void cleanup() {
        try {
            connectedClients.remove(this);
            socket.close();
        } catch (IOException e) {
            logger.error("Error closing connection: ", e);
        }
    }
}
