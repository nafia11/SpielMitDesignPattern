package org.example;

import org.example.client.GameClient;
import org.example.server.GameServer;
import org.example.server.UsernameManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementTest {
    private static GameServer server;

    @BeforeAll
    public static void setUp() {
        // Start the server in a separate thread before running tests
        server = GameServer.getInstance();
        Thread serverThread = new Thread(() -> server.start());
        serverThread.start();

        // Wait for the server to start
        try {
            Thread.sleep(2000); // Adjust as needed for server start-up
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown() {
        server.stop();
    }

    @Test
    public void testUsernameReuseAfterRelease() {
        UsernameManager usernameManager = new UsernameManager();
        String username = "Player1";
        String normalizedUsername = "player1"; // Adjust to your actual normalization logic

        // Reserve the username
        usernameManager.reserveUsername(username);
        assertFalse(usernameManager.isUsernameAvailable(username), "Username should not be available after being reserved");

        // Suggest a different username when the requested one is taken
        String differentUsername = usernameManager.getAvailableUsername(username);
        assertNotEquals(username, differentUsername, "System should suggest a different username if the requested one is taken");

        // Release the username
        usernameManager.releaseUsername(username);
        assertTrue(usernameManager.isUsernameAvailable(username), "Username should be available after being released");

        // Ensure the released username is available for reuse
        String reusedUsername = usernameManager.getAvailableUsername(username);
        assertEquals(normalizedUsername, reusedUsername, "Username should be available for reuse after release");
    }

    @Test
    public void testEmptyUsernameAssignment() throws Exception {
        // Create a client with a valid username
        GameClient client = new GameClient("default");
        //client.sendMessage("JOIN ");

        // Send an empty username to the server
        //client.sendMessage("UPDATE_USERNAME "); // Ensure this is in the correct format

        // Allow some time for the server to process
        Thread.sleep(1000);

        // Verify that the username was set to "default"
        assertEquals("default", client.getUsername(), "Client with empty username should receive a 'default' username");
        client.disconnect();
    }


}
