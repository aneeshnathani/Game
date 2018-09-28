package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;


public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    TERenderer ter = new TERenderer();

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        Phase2 x = new Phase2();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        TETile[][] finalWorldFrame = new TETile[80][50];

        input = input.toLowerCase();
        int firstMoveIndex = 0;
        String strSeed = "";
        String moves = "'";
        long seed = 0;
        if (input.charAt(0) == 'n') {
            for (int i = 1; i < input.length(); i++) {
                if (input.charAt(i) == 's') {
                    firstMoveIndex = i + 1;
                    i = input.length();

                } else {
                    strSeed = strSeed + Character.toString(input.charAt(i));
                }
            }

        }
        if (input.charAt(0) != 'l') {
            seed = Long.parseLong(strSeed);
            WorldGenerator worldgen = new WorldGenerator(seed, finalWorldFrame);
        }
        if (firstMoveIndex < input.length() && firstMoveIndex != 0) {
            moves = input.substring(firstMoveIndex, input.length());
            HelpString.helpone(seed, finalWorldFrame, moves);
            //start game from this point

        }
        if (input.charAt(0) == 'l') {
            try {
                FileInputStream fis = new FileInputStream("storedMap.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
                finalWorldFrame = (TETile[][]) ois.readObject();
                if (input.length() > 1) {
                    moves = input.substring(1, input.length());
                    System.out.println(moves);
                    HelpString.helptwo(finalWorldFrame, moves);
                }
                //start game from this point
            } catch (FileNotFoundException fnfe) {
                System.out.println(fnfe.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.out.println(cnfe.getMessage());
            }

        }

        return finalWorldFrame;
    }
}

