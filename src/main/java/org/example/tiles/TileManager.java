package org.example.tiles;

import javafx.scene.canvas.GraphicsContext;
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

        // Calculate the top-left corner of the visible screen in the world coordinates
        int offsetX = worldX - gp.getScreenWidth() / 2;
        int offsetY = worldY - gp.getScreenHeight() / 2;

        // Draw the base layer tiles
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                // Calculate the world position of the current tile
                int tileX = col * gp.tileSize + offsetX;
                int tileY = row * gp.tileSize + offsetY;

                // Ensure that the tile exists within the map bounds
                if (tileX >= 0 && tileY >= 0 && tileX < mapData.getMapWidth() * gp.tileSize && tileY < mapData.getMapHeight() * gp.tileSize) {
                    int tileRow = tileY / gp.tileSize;
                    int tileCol = tileX / gp.tileSize;

                    if (tileRow >= 0 && tileRow < cachedBaseTiles.length &&
                            tileCol >= 0 && tileCol < cachedBaseTiles[tileRow].length) {
                        Tile tile = cachedBaseTiles[tileRow][tileCol];

                        // Ensure the tile is not null
                        if (tile != null && tile.getImage() != null) {
                            gc.drawImage(tile.getImage(), tileX - offsetX, tileY - offsetY, gp.tileSize, gp.tileSize);
                        }
                    }
                }
            }
        }


        // Draw the object layer tiles
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                // Calculate the world position of the current tile
                int tileX = col * gp.tileSize + offsetX;
                int tileY = row * gp.tileSize + offsetY;

                // Ensure that the tile exists within the map bounds
                if (tileX >= 0 && tileY >= 0 && tileX < mapData.getMapWidth() * gp.tileSize && tileY < mapData.getMapHeight() * gp.tileSize) {
                    int tileRow = tileY / gp.tileSize;
                    int tileCol = tileX / gp.tileSize;

                    if (tileRow >= 0 && tileRow < cachedObjectTiles.length &&
                            tileCol >= 0 && tileCol < cachedObjectTiles[tileRow].length) {
                        Tile tile = cachedObjectTiles[tileRow][tileCol];
                        gc.drawImage(tile.getImage(), tileX - offsetX, tileY - offsetY, gp.tileSize, gp.tileSize);
                    }
                }
            }
        }
    }

    public MapData getMapData() {
        return mapData;
    }
}
