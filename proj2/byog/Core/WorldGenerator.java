package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {
    private static final double MAXSPACE = 0.30;
    private static final double MINSPACE = 0.15;
    ArrayList<Room> listOfRooms = new ArrayList<>();
    private TETile[][] world;
    private int WIDTH;
    private int HEIGHT;
    private long SEED;
    private TERenderer ter;
    private double actualratio;
    private int actualtilesinrooms;
    private Random RANDOM;
    private Random rANDOMtwo;

    public WorldGenerator(long seed, TETile[][] world) {
        SEED = seed;
        this.world = world;
        this.WIDTH = world.length;
        this.HEIGHT = world[0].length;
        RANDOM = new Random(SEED);
        rANDOMtwo = new Random(SEED + 1);
        actualratio = RandomUtils.uniform(RANDOM, MINSPACE, MAXSPACE);
        actualtilesinrooms = ((int) (actualratio * WIDTH * HEIGHT));
        // this.renderWorld();
        this.initializeWorld();
        this.drawAllRooms();
        this.connectAllRooms();
        this.printWalls();
        //this.imageWorld();

    }

    /* Checks if a room of width width and height height can be placed in xcord,ycord */
    private boolean checkvalidplacement(int xcord, int ycord, int width, int height) {
        try {
            for (int x = xcord; x < xcord + width; x++) {
                for (int y = ycord; y < ycord + height; y++) {
                    if (world[x][y] == Tileset.FLOOR || world[x][y] == Tileset.WALL
                            || x < 2 || x > WIDTH - 3 || y < 2 || y > HEIGHT - 3) {
                        return false;
                    }
                }
            }

            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private int[] genrandomcoordinates(int width, int height) {
        int number1 = RandomUtils.uniform(RANDOM, 0, WIDTH);
        int number2 = RandomUtils.uniform(rANDOMtwo, 0, HEIGHT);
        if (!checkvalidplacement(number1, number2, width, height)) {
            return genrandomcoordinates(width, height);
        }
        return new int[]{number1, number2};
    }

    private void drawAllRooms() {
        while (actualtilesinrooms > 13) {
            DimensionStorer g = generateRoomDimensions();
            int[] coordinates = genrandomcoordinates(g.width, g.height);
            Position p = new Position(coordinates[0], coordinates[1]);
            Room finalRoom = new Room(p, g.width, g.height);
            listOfRooms.add(finalRoom);
            fillRoom(finalRoom);
        }
    }

    private void connectAllRooms() {
        for (int i = 0; i < listOfRooms.size() - 1; i++) {
            connectTwoRooms(listOfRooms.get(i), listOfRooms.get(i + 1));
        }
    }

    private void connectTwoRooms(Room one, Room two) {

        if (one.pos3.ycord < two.pos1.ycord) {
            if (corresponddownup(one, two)) {
                return;
            }
        }
        if (two.pos3.ycord < one.pos1.ycord) {
            if (corresponddownup(two, one)) {
                return;
            }
        }
        if (one.pos2.xcord < two.pos1.xcord) {
            if (correspondleftright(one, two)) {
                return;
            }
        }
        if (two.pos2.xcord < one.pos1.xcord) {
            if (correspondleftright(two, one)) {
                return;
            }

        }
        if ((one.pos2.xcord < two.pos1.xcord && one.pos3.ycord > two.pos1.ycord)
                || (two.pos2.xcord < one.pos1.xcord && two.pos3.ycord > one.pos1.ycord)) {
            connectNonCorrespondingRoomsone(one, two);
        }
        if ((one.pos2.xcord < two.pos1.xcord && one.pos3.ycord < two.pos1.ycord)
                || (two.pos2.xcord < one.pos1.xcord && two.pos3.ycord < one.pos1.ycord)) {
            connectNonCorrespondingRoomstwo(one, two);
        }
    }

    private void connectNonCorrespondingRoomsone(Room a, Room b) {
        if (a.pos2.xcord < b.pos1.xcord && a.pos3.ycord > b.pos1.ycord) {

            int randomend = RandomUtils.uniform(RANDOM, a.pos2.xcord, b.pos1.xcord);
            int astart = RandomUtils.uniform(RANDOM, a.pos2.ycord, a.pos3.ycord);
            int bstart = RandomUtils.uniform(RANDOM, b.pos1.ycord, b.pos4.ycord);
            makeHorizontalHallway(randomend - a.pos2.xcord, a.pos2.xcord, astart);
            makeHorizontalHallway(b.pos1.xcord - randomend, randomend, bstart);
            makeVerticalHallway(astart - bstart + 1, randomend, bstart);
        }
        if (b.pos2.xcord < a.pos1.xcord && b.pos3.ycord > a.pos1.ycord) {
            connectNonCorrespondingRoomsone(b, a);
        }
    }

    private void connectNonCorrespondingRoomstwo(Room a, Room b) {
        if (a.pos2.xcord < b.pos1.xcord && a.pos3.ycord < b.pos1.ycord) {

            int randomend = RandomUtils.uniform(RANDOM, a.pos2.xcord, b.pos1.xcord);
            int astart = RandomUtils.uniform(RANDOM, a.pos2.ycord, a.pos3.ycord);
            int bstart = RandomUtils.uniform(RANDOM, b.pos1.ycord, b.pos4.ycord);
            makeHorizontalHallway(randomend - a.pos2.xcord, a.pos2.xcord, astart);
            makeHorizontalHallway(b.pos1.xcord - randomend, randomend, bstart);
            makeVerticalHallway(bstart - astart + 1, randomend, astart);
        }
        if (b.pos2.xcord < a.pos1.xcord && b.pos3.ycord < a.pos1.ycord) {
            connectNonCorrespondingRoomstwo(b, a);
        }
    }

    private boolean corresponddownup(Room one, Room two) {
        ArrayList<int[]> checking = new ArrayList<>();
        for (int check = one.pos1.xcord; check < one.pos2.xcord; check++) {
            for (int check2 = two.pos1.xcord; check2 < two.pos2.xcord; check2++) {
                if (check == check2) {
                    int[] a = new int[]{check, one.pos3.ycord};
                    checking.add(a);
                }
            }
        }
        if (checking.size() != 0) {
            int b = RandomUtils.uniform(RANDOM, 0, checking.size());

            makeVerticalHallway(two.pos2.ycord - one.pos3.ycord + 1,
                    checking.get(b)[0], checking.get(b)[1]);
            return true;
        } else {
            return false;
        }

    }

    private boolean correspondleftright(Room one, Room two) {
        ArrayList<int[]> checking = new ArrayList<>();
        for (int check = one.pos1.ycord; check < one.pos3.ycord; check++) {
            for (int check2 = two.pos1.ycord; check2 < two.pos3.ycord; check2++) {
                if (check == check2) {
                    int[] a = new int[]{one.pos2.xcord, check};
                    checking.add(a);
                }
            }
        }
        if (checking.size() != 0) {
            int b = RandomUtils.uniform(RANDOM, 0, checking.size());

            makeHorizontalHallway(two.pos1.xcord - one.pos2.xcord,
                    checking.get(b)[0], checking.get(b)[1]);
            return true;
        } else {
            return false;
        }

    }

    private void initializeWorld() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void makeHorizontalHallway(int length, int xval, int yval) {
        for (int i = 0; i < length; i++) {
            world[xval][yval] = Tileset.FLOOR;
            xval++;
        }
    }

    private void makeVerticalHallway(int length, int xval, int yval) {
        for (int i = 0; i < length; i++) {
            world[xval][yval] = Tileset.FLOOR;
            yval++;
        }
    }

    private DimensionStorer generateRoomDimensions() {
        int roomArea = (int) ((RandomUtils.uniform(RANDOM, 0.1, 0.3)) * actualtilesinrooms);
        return dimensionHelper(roomArea);
    }

    private DimensionStorer dimensionHelper(int roomArea) {
        ArrayList<DimensionStorer> list = new ArrayList<>();
        if (roomArea <= 2) {
            int init = roomArea;
            roomArea = 4;
            actualtilesinrooms -= (4 - init);
        }
        int midpoint = roomArea / 2;
        int numMods = 0;
        for (int i = 1; i <= roomArea; i++) {
            if (roomArea % i == 0) {
                numMods++;
            }
        }
        if (numMods == 2) {
            roomArea += 1;
            midpoint = roomArea / 2;
            actualtilesinrooms -= 1;
        }

        for (int i = 2; i <= midpoint; i++) {
            if (roomArea % i == 0) {
                if ((Math.abs((roomArea / i) - i) <= 5)) {
                    DimensionStorer g = new DimensionStorer(i, roomArea / i);
                    list.add(g);
                }
            }
        }
        if (list.size() == 0) {
            return generateRoomDimensions();
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            DimensionStorer result = list.get(RandomUtils.uniform(RANDOM, 0, list.size()));
            actualtilesinrooms -= roomArea;
            return result;
        }
    }

    private void fillRoom(Room r) {

        for (int x = r.pos1.xcord; x < r.pos2.xcord; x++) {
            for (int y = r.pos1.ycord; y < r.pos3.ycord; y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }


    }

    private void printWalls() {
        for (int x = 1; x < WIDTH - 1; x++) {
            for (int y = 1; y < HEIGHT - 1; y++) {
                if (checkForEdgeFloors(x, y)) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    private boolean checkForEdgeFloors(int x, int y) {
        if (world[x][y].description().equals("nothing")
                && world[x + 1][y].description().equals("floor")) {
            return true;
        } else if (world[x][y].description().equals("nothing")
                && world[x - 1][y].description().equals("floor")) {
            return true;
        } else if (world[x][y].description().equals("nothing")
                && world[x][y - 1].description().equals("floor")) {
            return true;
        } else if (world[x][y].description().equals("nothing")
                && world[x][y + 1].description().equals("floor")) {
            return true;
        } else if (world[x][y].description().equals("nothing")
                && world[x - 1][y - 1].description().equals("floor")) {
            return true;
        } else if (world[x][y].description().equals("nothing")
                && world[x - 1][y + 1].description().equals("floor")) {
            return true;
        } else if (world[x][y].description().equals("nothing")
                && world[x + 1][y + 1].description().equals("floor")) {
            return true;
        } else if (world[x][y].description().equals("nothing")
                && world[x + 1][y - 1].description().equals("floor")) {
            return true;
        }
        return false;
    }

    private class DimensionStorer {
        private int width;
        private int height;

        private DimensionStorer(int x, int y) {
            width = x;
            height = y;
        }
    }

    private class Position {
        private int xcord;
        private int ycord;

        private Position(int x, int y) {
            xcord = x;
            ycord = y;
        }
    }

    private class Room {
        private Position pos1;
        private Position pos2;
        private Position pos3;
        private Position pos4;
        private int ww;
        private int hh;


        private Room(Position x, int wid, int ht) {
            pos1 = x;
            pos2 = new Position(x.xcord + wid, x.ycord);
            pos3 = new Position(pos2.xcord, pos1.ycord + ht);
            pos4 = new Position(pos1.xcord, pos1.ycord + ht);
            ww = wid;
            hh = ht;

        }
    }

}
