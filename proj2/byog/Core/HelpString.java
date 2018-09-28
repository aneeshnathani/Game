package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;



import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;



public class HelpString {
    public static void helpone(long seed, TETile[][] world, String x) {
        Random rando = new Random(seed);
        int[] a;
        a = placeRandomPlayer(world, rando);
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == 'w') {

                if (world[a[0]][a[1] + 1].description().equals("wall")) {
                    continue;
                }
                world[a[0]][a[1]] = Tileset.FLOOR;
                world[a[0]][a[1] + 1] = Tileset.PLAYER;
                a[1]++;
            }
            if (x.charAt(i) == 's') {
                if (world[a[0]][a[1] - 1].description().equals("wall")) {
                    continue;
                }
                world[a[0]][a[1]] = Tileset.FLOOR;
                world[a[0]][a[1] - 1] = Tileset.PLAYER;
                a[1]--;
            }
            if (x.charAt(i) == 'a') {

                if (world[a[0] - 1][a[1]].description().equals("wall")) {
                    continue;
                }
                world[a[0]][a[1]] = Tileset.FLOOR;
                world[a[0] - 1][a[1]] = Tileset.PLAYER;
                a[0]--;
            }
            if (x.charAt(i) == 'd') {

                if (world[a[0] + 1][a[1]].description().equals("wall")) {
                    continue;
                }
                world[a[0]][a[1]] = Tileset.FLOOR;
                world[a[0] + 1][a[1]] = Tileset.PLAYER;
                a[0]++;
            }
            if (x.charAt(i) == ':' && x.charAt(i + 1) == 'q') {
                try {
                    File file = new File("storedMap.txt");
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(world);
                    fos.close();
                    oos.close();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            }

        }
    }

    public static void helptwo(TETile[][] world, String x) {
        int[] a;
        a = findPlayer(world);
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == 'w') {
                if (world[a[0]][a[1] + 1].description().equals("wall")) {
                    continue;
                }
                world[a[0]][a[1]] = Tileset.FLOOR;
                world[a[0]][a[1] + 1] = Tileset.PLAYER;
                a[1]++;
            }
            if (x.charAt(i) == 's') {
                if (world[a[0]][a[1] - 1].description().equals("wall")) {
                    continue;
                }
                world[a[0]][a[1]] = Tileset.FLOOR;
                world[a[0]][a[1] - 1] = Tileset.PLAYER;
                a[1]--;
            }
            if (x.charAt(i) == 'a') {
                if (world[a[0] - 1][a[1]].description().equals("wall")) {
                    continue;
                }
                world[a[0]][a[1]] = Tileset.FLOOR;
                world[a[0] - 1][a[1]] = Tileset.PLAYER;
                a[0]--;
            }
            if (x.charAt(i) == 'd') {
                if (world[a[0] + 1][a[1]].description().equals("wall")) {
                    continue;
                }
                world[a[0]][a[1]] = Tileset.FLOOR;
                world[a[0] + 1][a[1]] = Tileset.PLAYER;
                a[0]++;
            }
            if (x.charAt(i) == ':' && x.charAt(i + 1) == 'q') {
                try {
                    File file = new File("savedMap.txt");
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(world);
                    fos.close();
                    oos.close();
                } catch (FileNotFoundException fnfe) {
                    System.out.println(fnfe.getMessage());
                } catch (IOException fnfe) {
                    System.out.println(fnfe.getMessage());
                }

            }

        }
    }

    private static int[] placeRandomPlayer(TETile[][] world, Random rando) {

        int number1 = RandomUtils.uniform(rando, 0, 80);
        int number2 = RandomUtils.uniform(rando, 0, 50);
        if (world[number1][number2].description().equals("floor")) {
            world[number1][number2] = Tileset.PLAYER;
            int[] tr = new int[]{number1, number2};
            return tr;
        } else {
            return placeRandomPlayer(world, rando);
        }
    }

    private static int[] findPlayer(TETile[][] worldtofind) {
        int[] b = new int[2];
        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 50; j++) {
                if (worldtofind[i][j].description().equals("player")) {
                    b[0] = i;
                    b[1] = j;
                }
            }
        }
        return b;
    }
}
