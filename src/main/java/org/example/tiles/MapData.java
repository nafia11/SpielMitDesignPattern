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
        System.out.println("Roads and Streets locations:");
        for (int row = 0; row < baseMapData.length; row++) {
            for (int col = 0; col < baseMapData[row].length(); col++) {
                char tileChar = baseMapData[row].charAt(col);
                if (tileChar == 'R' || tileChar == 'V') {
                    System.out.println("Road found at: Row " + row + ", Col " + col);
                }
            }
        }
    }
    // Method to print house locations
    public void printHouseLocations() {
        System.out.println("House locations:");
        for (int row = 0; row < baseMapData.length; row++) {
            for (int col = 0; col < baseMapData[row].length(); col++) {
                char tileChar = baseMapData[row].charAt(col);
                if (tileChar == 'H' || tileChar == 'h') {
                    System.out.println("House found at: Row " + row + ", Col " + col);
                }
            }
        }
    }

    // Method to print spawn locations
    public void printSpawnLocations() {
        System.out.println("Spawn locations:");
        for (int row = 0; row < baseMapData.length; row++) {
            for (int col = 0; col < baseMapData[row].length(); col++) {
                char tileChar = baseMapData[row].charAt(col);
                if (tileChar == 'S') {  // Assuming 'S' represents a spawn point
                    System.out.println("Spawn point found at: Row " + row + ", Col " + col);
                }
            }
        }
    }

}
