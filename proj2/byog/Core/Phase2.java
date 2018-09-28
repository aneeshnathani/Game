package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;


public class Phase2 {
    long seed;
    private Random rando;
    private TETile[][] world;
    private int[] tyr = new int[2];
    private int countflower;
    private TERenderer ter = new TERenderer();
    private Random randonew;
    private int[] tyrnew = new int[2];
    private Boolean checkQuit = false;

    public Phase2() {
        this.menu();
        //this.placeRandomFlowers();
        //tyr = placeRandomPlayer();
        //ter.renderFrame(world);
        //movePlayer(tyr, world);
    }

    private void gen(String s) {
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(40, 25, s);
        StdDraw.show();
    }

    private void menu() {
        StdDraw.setCanvasSize(80 * 16, 50 * 16);
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setXscale(0, 80);
        StdDraw.setYscale(0, 50);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        gen("New Game (N)");
        StdDraw.text(40, 20, "Load Game (L)");
        StdDraw.text(40, 30, "Quit Game (Q)");
        StdDraw.text(40, 40, "Aneesh and Nain present the CS61B game");
        StdDraw.show();
        Boolean goOn = true;
        while (goOn) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == 'n') {
                goOn = false;
                this.seed = Long.parseLong(drawSeed());
                startGame();

            }
            if (key == 'q') {
                goOn = false;
                gen("");
            }
            if (key == 'l') {
                try {
                    FileInputStream fis = new FileInputStream("storedMap.txt");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    this.world = (TETile[][]) ois.readObject();
                    fis.close();
                    ois.close();
                    PrintWriter pw = new PrintWriter("storedMap.txt");
                    pw.close();
                    ter.initialize(80, 50);
                    ter.renderFrame(this.world);
                    this.findPlayer(this.world);
                    this.findPlayernew(this.world);
                    movePlayer(tyr, tyrnew, world);
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                /*for (int i = 0; i < 80; i++) {
                    for (int j = 0; j < 50; j++) {
                        if (this.world[i][j] == Tileset.PLAYER) {
                            this.tyr[0] = i;
                            this.tyr[1] = j;
                            System.out.println("reached here");
                        }
                    }
                }
                ter.initialize(80, 50);
                ter.renderFrame(this.world);
                this.movePlayer(this.tyr, this.world);*/

            }
        }
    }

    private void findPlayer(TETile[][] worldtofind) {
        for (int a = 0; a < 80; a++) {
            for (int b = 0; b < 50; b++) {
                if (worldtofind[a][b].description().equals("player")) {
                    this.tyr[0] = a;
                    this.tyr[1] = b;
                }
            }
        }
    }

    private void findPlayernew(TETile[][] worldtofind) {
        for (int a = 0; a < 80; a++) {
            for (int b = 0; b < 50; b++) {
                if (worldtofind[a][b].description().equals("flower")) {
                    this.tyrnew[0] = a;
                    this.tyrnew[1] = b;
                }
            }
        }
    }

    private String drawSeed() {
        gen("Enter a seed ");
        String input = "";
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == 's') {
                return input;
            }
            input += String.valueOf(key);
            gen(input);

        }
    }

    private void startGame() {
        TETile[][] finalWorldFrame = new TETile[80][50];
        WorldGenerator worldgen = new WorldGenerator(this.seed, finalWorldFrame);
        this.rando = new Random(this.seed);
        this.randonew = new Random(this.seed + 1);
        this.world = finalWorldFrame;
        ter.initialize(80, 50);
        tyr = placeRandomPlayer();
        tyrnew = placeRandomPlayernew();
        ter.renderFrame(world);
        movePlayer(tyr, tyrnew, world);


    }

    private int[] placeRandomPlayer() {

        int number1 = RandomUtils.uniform(rando, 0, 80);
        int number2 = RandomUtils.uniform(rando, 0, 50);
        if (world[number1][number2].description().equals("floor")) {
            world[number1][number2] = Tileset.PLAYER;
            int[] tr = new int[]{number1, number2};
            return tr;
        } else {
            return placeRandomPlayer();
        }
    }

    private int[] placeRandomPlayernew() {

        int number1 = RandomUtils.uniform(randonew, 0, 80);
        int number2 = RandomUtils.uniform(randonew, 0, 50);
        if (world[number1][number2].description().equals("floor")) {
            world[number1][number2] = Tileset.FLOWER;
            int[] tr = new int[]{number1, number2};
            return tr;
        } else {
            return placeRandomPlayernew();
        }
    }


    private void movePlayer(int[] a, int[] b, TETile[][] world2) {
        while (true) {
            StdDraw.clear(Color.black); ter.renderFrame(world2); displayHUD(world2);
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == ':') {
                checkQuit = true; continue;
            }
            if (key != 'q') {
                checkQuit = false;
            }
            if (key == 'w') {
                if (hack(a, world2)) {
                    continue;
                }
                world2[a[0]][a[1]] = Tileset.FLOOR; world2[a[0]][a[1] + 1] = Tileset.PLAYER; a[1]++;
            }
            if (key == 'y') {
                if (hackmit(b, world2)) {
                    continue;
                }
                world2[b[0]][b[1]] = Tileset.FLOOR; world2[b[0]][b[1] + 1] = Tileset.FLOWER; b[1]++;
            }
            if (key == 's') {
                if (world2[a[0]][a[1] - 1].description().equals("wall")
                        || world2[a[0]][a[1] - 1].description().equals("flower")) {
                    continue;
                }
                world2[a[0]][a[1]] = Tileset.FLOOR; world2[a[0]][a[1] - 1] = Tileset.PLAYER; a[1]--;
            }
            if (key == 'h') {
                if (world2[b[0]][b[1] - 1].description().equals("wall")
                        || world2[b[0]][b[1] - 1].description().equals("player")) {
                    continue;
                }
                world2[b[0]][b[1]] = Tileset.FLOOR; world2[b[0]][b[1] - 1] = Tileset.FLOWER; b[1]--;
            }
            if (key == 'a') {
                if (world2[a[0] - 1][a[1]].description().equals("wall")
                        || world2[a[0] - 1][a[1]].description().equals("flower")) {
                    continue;
                }
                world2[a[0]][a[1]] = Tileset.FLOOR; world2[a[0] - 1][a[1]] = Tileset.PLAYER; a[0]--;
            }
            if (key == 'g') {
                if (world2[b[0] - 1][b[1]].description().equals("wall")
                        || world2[b[0] - 1][b[1]].description().equals("player")) {
                    continue;
                }
                world2[b[0]][b[1]] = Tileset.FLOOR; world2[b[0] - 1][b[1]] = Tileset.FLOWER; b[0]--;
            }
            if (key == 'd') {
                if (world2[a[0] + 1][a[1]].description().equals("wall")
                        || world2[a[0] + 1][a[1]].description().equals("flower")) {
                    continue;
                }
                world2[a[0]][a[1]] = Tileset.FLOOR; world2[a[0] + 1][a[1]] = Tileset.PLAYER; a[0]++;
            }
            if (key == 'j') {
                if (world2[b[0] + 1][b[1]].description().equals("wall")
                        || world2[b[0] + 1][b[1]].description().equals("player")) {
                    continue;
                }
                world2[b[0]][b[1]] = Tileset.FLOOR; world2[b[0] + 1][b[1]] = Tileset.FLOWER; b[0]++;
            }
            if (key == 'q' && checkQuit) {
                try {
                    File file = new File("storedMap.txt");
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                    oos.writeObject(this.world); oos.close(); break;
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    private boolean hack(int[] p, TETile[][] worldty) {
        if (worldty[p[0]][p[1] + 1].description().equals("wall")
                || worldty[p[0]][p[1] + 1].description().equals("flower")) {
            return true;
        }
        return false;
    }
    private boolean hackmit(int[] p, TETile[][] worldty) {
        if (worldty[p[0]][p[1] + 1].description().equals("wall")
                || worldty[p[0]][p[1] + 1].description().equals("player")) {
            return true;
        }
        return false;
    }


    private void displayHUD(TETile[][] grid) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        String description;
        if (mouseY < 49) {
            description = grid[mouseX][mouseY].description();
            if (description.equalsIgnoreCase("wall")) {
                description = "That is a YUUUUUGE Wall";
            } else if (description.equalsIgnoreCase("floor")) {
                description = "It is a floor, my dude";
            } else if (description.equalsIgnoreCase("player")) {
                description = "That is you!";
            }
        } else {
            description = "Out of bounds";
        }
        StdDraw.setPenColor(Color.white);
        java.util.Date date = new java.util.Date();
        StdDraw.textLeft(0, 49, description + " and for your reference, it is " + date);
        StdDraw.show();

    }

}
