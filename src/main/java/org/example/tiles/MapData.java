package org.example.tiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapData {
    private final String[] baseMapData;
    private final String[] objectMapData;

    public MapData() {
        this.baseMapData = loadMapData("/map/map_data.txt");
        this.objectMapData = loadMapData("/map/object_data.txt");

    }

    private String[] loadMapData(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            return br.lines().toArray(String[]::new);

        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

    public String[] getBaseMapData() {
        return baseMapData;
    }

    public String[] getObjectMapData() {
        return objectMapData;
    }

    public void printRoadLocations() {
        System.out.println("Floors locations:");
        for (int row = 0; row < baseMapData.length; row++) {
            for (int col = 0; col < baseMapData[row].length(); col++) {
                char tileChar = baseMapData[row].charAt(col);
                if (tileChar == 'F' || tileChar == 'f'|| tileChar == 'E') {
                    System.out.println("Road found at: Row " + row + ", Col " + col);
                }
            }
        }
    }
    // Method to print house locations
    public void printChestLocations() {
        System.out.println("Chest locations:");
        for (int row = 0; row < objectMapData.length; row++) {
            for (int col = 0; col < objectMapData[row].length(); col++) {
                char tileChar = objectMapData[row].charAt(col);
                if (tileChar == 'C') {
                    System.out.println("Chest found at: Row " + row + ", Col " + col);
                }
            }
        }
    }

    // Method to print spawn locations
    public void printBlockLocations() {
        System.out.println("Block locations:");
        for (int row = 0; row < objectMapData.length; row++) {
            for (int col = 0; col < objectMapData[row].length(); col++) {
                char tileChar = objectMapData[row].charAt(col);
                if (tileChar == 'B'|| tileChar == 'b'|| tileChar == 'w') {  // Assuming 'S' represents a spawn point
                    System.out.println("Blocks found at: Row " + row + ", Col " + col);
                }
            }
        }
    }

    public int getMapWidth() {
        return baseMapData[0].length();
    }

    public int getMapHeight() {
        return baseMapData.length;
    }

    public char getTileAt(int row, int col, boolean isBaseMap) {
        String[] map = isBaseMap ? baseMapData : objectMapData;
        // Check bounds to avoid ArrayIndexOutOfBoundsException
        if (row >= 0 && row < map.length && col >= 0 && col < map[0].length()) {
            return map[row].charAt(col);
        }
        return ' '; // Return an empty space for out-of-bounds access
    }

    // New method to get a tile specifically from the object map
    public Tile getTileAtt(int row, int col, boolean isBaseMap) {
        String[] map = isBaseMap ? baseMapData : objectMapData;
        // Check bounds to avoid ArrayIndexOutOfBoundsException
        if (row >= 0 && row < map.length && col >= 0 && col < map[0].length()) {
            char tileChar = map[row].charAt(col);
            return TileFactory.createTile(tileChar);
        }
        return null; // Return null for out-of-bounds access
    }


    // New method for the object map
    public Tile getTileAtObjectMap(int row, int col) {
        return getTileAtt(row, col, false); // Use object map
    }

}
