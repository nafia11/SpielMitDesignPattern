package org.example.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Player;
import org.example.game.KeyHandler;
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
    private KeyHandler keyHandler;

    public ClientHandler(Socket socket, List<ClientHandler> connectedClients, GameState gameState, KeyHandler keyHandler) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.connectedClients = connectedClients;
        this.gameState = gameState;
        this.keyHandler = keyHandler;
        this.username = "default";
        sendMessage("USERNAME_UPDATED " + this.username);
        gameState.addPlayer(username, this, new Player(username, keyHandler));
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                ServerCommandFactory.createCommand(message, gameState, this, keyHandler).execute();
            }
        } catch (IOException e) {
            //logger.error("Error in client communication: ", e);
            logger.error("Bye Bye. Socket closed");
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
        return username != null ? username.trim() : null;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            username = "default";
        }
        this.username = username.trim();
        sendMessage("USERNAME_UPDATED " + this.username);
    }



    public void cleanup() {
        try {
            connectedClients.remove(this);
            gameState.removePlayer(username);
            for (ClientHandler handler : connectedClients) {
                handler.sendMessage("UPDATE_PLAYERS " + String.join(",", gameState.getPlayerList()));
            }
            socket.close();
        } catch (IOException e) {
            logger.error("Error closing connection: ", e);
        }
    }
    @Override
    public String toString() {
        return username;
    }

    public void sendPlayerInitialPositions() {
        for (Player player : gameState.getAllPlayers()) {
            sendMessage("INITIAL_POSITION " + player.getUsername() + "," + player.getX() + "," + player.getY());
            //System.out.println("INITIAL_POSITION " + player.getUsername() + "," + player.getX() + "," + player.getY());
            //System.out.println("Sending updated position from Server side..." + player.getX() + "," + player.getY());
        }
    }

    public void notifyPositionUpdate(String username, double x, double y, String direction, int spriteNum, int interactionCount) {
        GameServer.getInstance().broadcastPositionUpdate(username, x, y, direction, spriteNum, interactionCount);
    }



}
