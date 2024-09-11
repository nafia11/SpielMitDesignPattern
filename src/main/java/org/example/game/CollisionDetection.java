package org.example.game;

import org.example.client.GamePanel;
import org.example.entity.Player;
import org.example.tiles.Tile;
import org.example.tiles.TileFactory;
import org.example.tiles.TileManager;

public class CollisionDetection {

    private final GamePanel gp;
    private final TileManager tileManager;

    public CollisionDetection(GamePanel gp, TileManager tileManager) {
        this.gp = gp;
        this.tileManager = tileManager;
    }

    // Check if the player is colliding with a tile
    public boolean checkTileCollision(Player player, double newX, double newY) {
        int playerTileX = (int) (newX / gp.tileSize);
        int playerTileY = (int) (newY / gp.tileSize);

        // Check if the new position is within the map bounds
        if (playerTileX < 0 || playerTileX >= tileManager.getMapData().getMapWidth() ||
                playerTileY < 0 || playerTileY >= tileManager.getMapData().getMapHeight()) {
            return true; // Prevent moving out of bounds
        }

        // Get the tiles at the new position
        char baseTileChar = tileManager.getMapData().getTileAt(playerTileY, playerTileX, true);
        char objectTileChar = tileManager.getMapData().getTileAt(playerTileY, playerTileX, false);

        // Check for collisions with object tiles (like blocks)
        Tile objectTile = TileFactory.createTile(objectTileChar);
        if (objectTile.hasCollision()) {
            return true;
        }

        // Check for collisions with base tiles if needed
        Tile baseTile = TileFactory.createTile(baseTileChar);
        if (baseTile.hasCollision()) {
            return true;
        }

        return false;
    }

    // Check if the player is colliding with another player
    public boolean checkPlayerCollision(Player player, double newX, double newY) {
        for (Player otherPlayer : gp.getPlayers().values()) {
            if (!otherPlayer.getUsername().equals(player.getUsername())) {
                double otherPlayerX = otherPlayer.getX();
                double otherPlayerY = otherPlayer.getY();

                // Check if the new position overlaps with another player's position
                if (Math.abs(newX - otherPlayerX) < gp.tileSize &&
                        Math.abs(newY - otherPlayerY) < gp.tileSize) {
                    return true;
                }
            }
        }
        return false;
    }

    // Main method to check all collisions before updating the player's position
    public boolean isCollisionDetected(Player player, double newX, double newY) {
        return checkTileCollision(player, newX, newY) || checkPlayerCollision(player, newX, newY);
    }
}
