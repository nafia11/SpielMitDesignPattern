package org.example.protocol;

import org.example.server.ClientHandler;
import org.example.server.GameState;
import org.example.server.UsernameManager;
public class ServerCommandFactory {
    private static final UsernameManager usernameManager = new UsernameManager();

    public static ServerCommand createCommand(String message, GameState gameState, ClientHandler clientHandler) {
        if (message.startsWith("CHAT")) {
            return new ChatCommand(message.substring(5), clientHandler);
        } else if (message.startsWith("JOIN")) {
            return new JoinCommand(message.substring(5), gameState, clientHandler, usernameManager);
        } else if (message.startsWith("UPDATE_USERNAME")) {
            return new UpdateUsernameCommand(message.substring(15), gameState, clientHandler, usernameManager);
        } else if (message.startsWith("DISCONNECT")) {
            return new DisconnectCommand(clientHandler, usernameManager);
        } else if (message.startsWith("START_GAME")) {
            return new StartGameCommand(gameState, clientHandler);
        } else if (message.startsWith("READY")) {
            return new ReadyCommand(gameState, clientHandler);
        } else {

            return new UnknownCommand();
        }
    }
}

