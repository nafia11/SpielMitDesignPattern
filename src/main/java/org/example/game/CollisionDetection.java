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
        // Player dimensions
        int playerWidth = (int) player.getWidth();
        int playerHeight = (int) player.getHeight();

        // Convert player position to tile coordinates (top-left corner)
        int topLeftTileX = (int) (newX / gp.tileSize);
        int topLeftTileY = (int) (newY / gp.tileSize);

        // Calculate other corners (top-right, bottom-left, bottom-right)
        int topRightTileX = (int) ((newX + playerWidth) / gp.tileSize);
        int bottomLeftTileY = (int) ((newY + playerHeight) / gp.tileSize);
        int bottomRightTileX = (int) ((newX + playerWidth) / gp.tileSize);
        int bottomRightTileY = (int) ((newY + playerHeight) / gp.tileSize);

        // Check if the new position is within the map bounds
        if (topLeftTileX < 0 || topRightTileX >= tileManager.getMapData().getMapWidth() ||
                topLeftTileY < 0 || bottomRightTileY >= tileManager.getMapData().getMapHeight()) {
            return true; // Prevent moving out of bounds
        }

        // Check for collisions with all corners of the player's bounding box
        if (checkTileCollisionAt(topLeftTileX, topLeftTileY) ||
                checkTileCollisionAt(topRightTileX, topLeftTileY) ||
                checkTileCollisionAt(topLeftTileX, bottomLeftTileY) ||
                checkTileCollisionAt(bottomRightTileX, bottomRightTileY)) {
            return true; // Collision detected
        }

        return false; // No collision
    }

    // Helper method to check tile collision at specific tile coordinates
    private boolean checkTileCollisionAt(int tileX, int tileY) {
        // Get the tiles at the new position
        char baseTileChar = tileManager.getMapData().getTileAt(tileY, tileX, true);
        char objectTileChar = tileManager.getMapData().getTileAt(tileY, tileX, false);

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

        return false; // No collision at this tile
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
