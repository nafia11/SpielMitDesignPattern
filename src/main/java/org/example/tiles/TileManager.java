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
        int tileSize = gp.tileSize;
        String[] baseMap = mapData.getBaseMapData();
        String[] objectMap = mapData.getObjectMapData();

        // Draw base layer
        for (int row = 0; row < cachedBaseTiles.length; row++) {
            for (int col = 0; col < cachedBaseTiles[row].length; col++) {
                Tile tile = cachedBaseTiles[row][col];
                gc.drawImage(tile.image, col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }

    public MapData getMapData() {
        return mapData;
    }
}
