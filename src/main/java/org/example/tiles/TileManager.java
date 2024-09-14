package org.example.tiles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.client.GamePanel;

public class TileManager {
    private final GamePanel gp;
    private final TileFactory tileFactory;
    private final MapData mapData;
    private Tile[][] cachedBaseTiles;
    private Tile[][] cachedObjectTiles;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        if (this.gp == null) {
            System.out.println("GamePanel is null when passed to TileManager");
        } else {
            System.out.println("GamePanel passed successfully");
        }
        this.tileFactory = new TileFactory();
        this.mapData = new MapData();
        cacheTiles();
    }

    private void cacheTiles() {
        String[] baseMap = mapData.getBaseMapData();
        String[] objectMap = mapData.getObjectMapData();

        cachedBaseTiles = new Tile[baseMap.length][baseMap[0].length()];
        cachedObjectTiles = new Tile[objectMap.length][objectMap[0].length()];

        for (int row = 0; row < baseMap.length; row++) {
            for (int col = 0; col < baseMap[row].length(); col++) {
                cachedBaseTiles[row][col] = TileFactory.createTile(baseMap[row].charAt(col));
            }
        }

        for (int row = 0; row < objectMap.length; row++) {
            for (int col = 0; col < objectMap[row].length(); col++) {
                cachedObjectTiles[row][col] = TileFactory.createTile(objectMap[row].charAt(col));
            }
        }
    }

    public void draw(GraphicsContext gc) {
        int worldX = (int) gp.getLocalPlayer().getX(); // Player's X position in the world
        int worldY = (int) gp.getLocalPlayer().getY(); // Player's Y position in the world

        // Calculate the top-left corner of the visible screen in world coordinates
        int offsetX = worldX - gp.getScreenWidth() / 2;
        int offsetY = worldY - gp.getScreenHeight() / 2;

        // Calculate the boundaries for rendering the visible tiles
        int startRow = Math.max(0, offsetY / gp.tileSize);
        int endRow = Math.min(cachedBaseTiles.length, (offsetY + gp.getScreenHeight()) / gp.tileSize + 1);
        int startCol = Math.max(0, offsetX / gp.tileSize);
        int endCol = Math.min(cachedBaseTiles[0].length, (offsetX + gp.getScreenWidth()) / gp.tileSize + 1);

        // Draw the base layer tiles (ground)
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                Tile tile = cachedBaseTiles[row][col];
                if (tile != null && tile.getImage() != null) {
                    int tileX = col * gp.tileSize - offsetX;
                    int tileY = row * gp.tileSize - offsetY;
                    gc.drawImage(tile.getImage(), tileX, tileY, gp.tileSize, gp.tileSize);
                }
            }
        }

        // Draw the object layer tiles (objects, decorations)
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                Tile tile = cachedObjectTiles[row][col];
                if (tile != null) {
                    // Handle animated tiles
                    if (tile instanceof AnimatedTile animatedTile) {
                        Image currentFrame = animatedTile.getCurrentImage();
                        if (currentFrame != null) {
                            int tileX = col * gp.tileSize - offsetX;
                            int tileY = row * gp.tileSize - offsetY;
                            gc.drawImage(currentFrame, tileX, tileY, gp.tileSize, gp.tileSize);
                        }
                    } else if (tile.getImage() != null) {
                        int tileX = col * gp.tileSize - offsetX;
                        int tileY = row * gp.tileSize - offsetY;
                        gc.drawImage(tile.getImage(), tileX, tileY, gp.tileSize, gp.tileSize);
                    }
                }
            }
        }
    }


    public MapData getMapData() {
        return mapData;
    }
}
