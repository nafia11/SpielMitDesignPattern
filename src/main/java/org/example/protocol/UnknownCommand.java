package org.example.protocol;

public class UnknownCommand implements ServerCommand {
    @Override
    public void execute() {
        System.out.println("Server got an unknown command");
        // Handle unknown commands
    }
}
