import java.util.Arrays;

/**
 * Lars Benedetto - 11/19/2015
 * Recursive Maze Solver
 * <p>
 * Extra Features:
 * Shows a graphical representation of the maze
 * User can use the GUI to generate and solve new mazes
 * User should not ask for a maze greater than 50x50,
 * as it won't fit on the screen properly
 */

public class MazeTester {
    public static boolean enableLogging = false;
    public static int[][] maze = {
            {1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
            {1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    public static void main(String[] args) {
        maze = solveMaze(maze);
        new GUI(maze);
    }

    public static int[][] solveMaze(int[][] newMaze) {
        maze = newMaze;
        try {
            if (!mazeRunner(0, 0)) {
                System.out.println("Maze is unsolvable");
            }
            //Print the output to the console
            print2D(maze);
        } catch (StackOverflowError e) {
            System.out.println("Maze too large");
        } catch (NullPointerException e) {
            System.out.println("Maze is null");
        }
        return maze;
    }

    /**
     * Prints a 2D array of ints
     *
     * @param grid 2D array of ints to be printed
     */
    public static void print2D(int[][] grid) {
        for (int[] row : grid) {
            for (int x : row) {
                System.out.print(x);
            }
            System.out.println();
        }
    }

    /**
     * Recursively solves a maze
     *
     * @param x Index coordinate in maze
     * @param y Index coordinate in maze
     * @return boolean - was the end of the maze found?
     */
    public static boolean mazeRunner(int x, int y) {
        if (enableLogging) {
            System.out.println("I'm at: " + x + "," + y);
            print2D(maze);
        }

        try {
            if (maze[y][x] == 0 | maze[y][x] == 3)
                return false;
            maze[y][x] = 3;
            if (y == maze.length - 1 && x == maze[y].length - 1) {
                maze[y][x] = 7;
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        //Move down
        if (mazeRunner(x, y + 1)) {
            maze[y][x] = 7;
            return true;
        }
        //Move right
        if (mazeRunner(x + 1, y)) {
            maze[y][x] = 7;
            return true;
        }
        //Move left
        if (mazeRunner(x - 1, y)) {
            maze[y][x] = 7;
            return true;
        }
        //Move up
        if (mazeRunner(x, y - 1)) {
            maze[y][x] = 7;
            return true;
        }
        return false;
    }

    /**
     * Generates a maze of specified size. 90% chance its solvable
     * Utilizes recursion!
     *
     * @param rows Number of rows (Y)
     * @param cols Number of columns (X)
     * @return Maze in form of int[][]
     */
    public static int[][] generateSolvableMaze(int rows, int cols) {
        int[][] newMaze = generateMaze(rows, cols);
        int[][] oldMazeCopy = cloneMaze(maze);
        maze = cloneMaze(newMaze);
        boolean solvable;
        try {
            solvable = mazeRunner(0, 0);
        }catch(StackOverflowError e){
            System.out.println("Maze too large");
            solvable = false;
            rows=8;
            cols=13;
        }
        maze = oldMazeCopy;
        //A 10% chance to keep an unsolvable maze.
        if (!solvable && getRandom(.1) == 1) {
            newMaze = generateSolvableMaze(rows, cols);
        }
        return newMaze;
    }

    /**
     * Generates a maze of specified size
     *
     * @param rows Number of rows (Y)
     * @param cols Number of columns (X)
     */
    public static int[][] generateMaze(int rows, int cols) {
        int[][] newmaze = new int[rows][cols];
        for (int[] row : newmaze) {
            for (int x = 0; x < row.length; x++) {
                row[x] = getRandom(.3);
            }
        }
        return newmaze;
    }

    public static int getRandom(double odds) {
        double x = Math.random();
        if (x <= odds) {
            return 0;
        }
        return 1;
    }

    /**
     * Clones a 2D array
     *
     * @param oldBody 2D array to be cloned
     * @return A clone of oldBody
     */
    public static int[][] cloneMaze(int[][] oldBody) {
        int[][] newBody = new int[oldBody.length][oldBody[0].length];
        for (int i = 0; i < oldBody.length; i++)
            newBody[i] = Arrays.copyOf(oldBody[i], oldBody[i].length);
        return newBody;
    }
}
